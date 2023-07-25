package tw.niq.example.beans;

import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorConfig;

import tw.niq.example.security.GoogleCredentialRepository;

@Configuration
public class SecurityBeans {
	
	@Bean
	public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
		return new SecurityEvaluationContextExtension();
	}
	
	@Bean
	public PersistentTokenRepository persistentTokenRepository(DataSource dataSource) {
		JdbcTokenRepositoryImpl jdbcTokenRepositoryImpl = new JdbcTokenRepositoryImpl();
		jdbcTokenRepositoryImpl.setDataSource(dataSource);
		return jdbcTokenRepositoryImpl;
	}
	
	@Bean
	public AuthenticationEventPublisher authenticationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		return new DefaultAuthenticationEventPublisher(applicationEventPublisher);
	}
	
	@Bean
	public GoogleAuthenticator googleAuthenticator(GoogleCredentialRepository googleCredentialRepository) {
		
		GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder googleAuthenticatorConfigBuilder = 
				new GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder();
		
		googleAuthenticatorConfigBuilder
			.setTimeStepSizeInMillis(TimeUnit.SECONDS.toMillis(30))
			.setWindowSize(10)
			.setNumberOfScratchCodes(0);
		
		GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator(googleAuthenticatorConfigBuilder.build());
		
		googleAuthenticator.setCredentialRepository(googleCredentialRepository);
		
		return googleAuthenticator;		
	}

}
