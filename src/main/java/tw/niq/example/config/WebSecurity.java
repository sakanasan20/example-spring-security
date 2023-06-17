package tw.niq.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurity {

	@Bean
	protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
		
		http.authorizeHttpRequests((authorize) -> authorize
				.requestMatchers("/", "/webjars/**", "/login", "/resources/**").permitAll()
				.anyRequest().authenticated())
			.formLogin(Customizer.withDefaults())
			.httpBasic(Customizer.withDefaults());
		
		return http.build();
	}
	
}
