package tw.niq.example.config;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tw.niq.example.entity.LoginSuccess;
import tw.niq.example.entity.LoginSuccess.LoginSuccessBuilder;
import tw.niq.example.entity.UserEntity;
import tw.niq.example.repository.LoginSuccessRepository;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthenticationSuccessListener {
	
	private final LoginSuccessRepository loginSuccessRepository;
	
	@EventListener
	public void listen(AuthenticationSuccessEvent event) {
		
		LoginSuccessBuilder loginSuccessBuilder = LoginSuccess.builder();

		if (event.getSource() instanceof UsernamePasswordAuthenticationToken) {
			
			UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) event.getSource();
			
			if (token.getPrincipal() instanceof UserEntity) {
				UserEntity userEntity = (UserEntity) token.getPrincipal();
				loginSuccessBuilder.user(userEntity);
				log.debug("User logged in: " + userEntity.getUsername());
			}
			
			if (token.getDetails() instanceof WebAuthenticationDetails) {
				WebAuthenticationDetails details = (WebAuthenticationDetails) token.getDetails();
				loginSuccessBuilder.sourceIp(details.getRemoteAddress());
				log.debug("Remote Address: " + details.getRemoteAddress());
			}
			
			LoginSuccess loginSuccessSaved = loginSuccessRepository.save(loginSuccessBuilder.build());
			
			log.debug("LoginSuccess: " + loginSuccessSaved.getId());
		}
	}

}
