package tw.niq.example.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import lombok.RequiredArgsConstructor;
import tw.niq.example.security.CustomPasswordEncoderFactories;
import tw.niq.example.security.Google2faFilter;
import tw.niq.example.service.UserService;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfig {
	
	private final UserService userService;
	private final PersistentTokenRepository persistentTokenRepository;
	private final Google2faFilter google2faFilter;

	@Bean
	protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
		
		AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
		
//		authenticationManagerBuilder.inMemoryAuthentication()
//			.withUser("admin")
//			.password("admin")																						// NoOp
//			.password("{SSHA}o2frpTdobhx/FCJfD8jU/3tRonncD9HrWg2TtA==")												// LDAP
//			.password("5925049ef3cb27b4d6ff39d0a979cad9c2930153cc648e18492e364e47e92982461a13d9083e9af9")			// Standard (SHA)
//			.password("$2a$10$3n3NtjZehpTSGEjq/Z2cs.F9ZCiXZ0R3x0peE9GpyUbONrbs9KNKe")								// BCrypt
//			.password("{noop}admin")																				// Delegate NoOp
//			.password("{ldap}{SSHA}o2frpTdobhx/FCJfD8jU/3tRonncD9HrWg2TtA==")										// Delegate LDAP
//			.password("{sha256}5925049ef3cb27b4d6ff39d0a979cad9c2930153cc648e18492e364e47e92982461a13d9083e9af9")	// Delegate Standard (SHA)
//			.password("{bcrypt}$2a$10$3n3NtjZehpTSGEjq/Z2cs.F9ZCiXZ0R3x0peE9GpyUbONrbs9KNKe")						// Delegate BCrypt
//			.roles("ADMIN");
		
		AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
		
		http.addFilterBefore(google2faFilter, SessionManagementFilter.class);
		
		http.authorizeHttpRequests((authorize) -> authorize
				.requestMatchers(PathRequest.toH2Console()).permitAll()
				.requestMatchers("/", "/webjars/**", "/login", "/resources/**").permitAll()
//				.requestMatchers("/authenticatedAsAdminRole").hasRole("ADMIN")
//				.requestMatchers("/authenticatedAsUserRole").hasRole("USER")
//				.requestMatchers("/authenticatedAsAnyRole").hasAnyRole("ADMIN", "USER")
				.anyRequest().authenticated())
			.authenticationManager(authenticationManager)
			.csrf((csrf) ->
				csrf.disable()
//				csrf.ignoringRequestMatchers("/", "/webjars/**", "/login", "/resources/**", "/h2-console/**")
				)
			.headers((headers) -> headers.frameOptions((frameOptions) -> frameOptions.disable()))
			.formLogin(formLoginCustomizer -> 
				formLoginCustomizer
					.loginProcessingUrl("/login")
					.loginPage("/").permitAll()
					.successForwardUrl("/")
					.defaultSuccessUrl("/")
					.failureUrl("/?error"))
			.logout(logoutCustomizer -> 
				logoutCustomizer
					.logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
					.logoutSuccessUrl("/?logout").permitAll())
			.httpBasic(Customizer.withDefaults())
			.rememberMe(rememberMeCustomizer -> 
//				rememberMeCustomizer
//					.key("mykey")
//					.userDetailsService(userService)
				rememberMeCustomizer
					.tokenRepository(persistentTokenRepository)
					.userDetailsService(userService));
		
		return http.build();
	}
	
	@Bean
	protected PasswordEncoder passwordEncoder() {
//		return NoOpPasswordEncoder.getInstance();
//		return new LdapShaPasswordEncoder();
//		return new StandardPasswordEncoder();
//		return new BCryptPasswordEncoder();
//		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
		return CustomPasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	
//	@Bean
//	protected UserDetailsService inMemoryuUserDetailsService() {
//	
//		UserDetails admin = User.withDefaultPasswordEncoder()
//				.username("admin")
//				.password("admin")
//				.roles("ADMIN")
//				.build();
//		
//		return new InMemoryUserDetailsManager(admin);
//	}
	
}
