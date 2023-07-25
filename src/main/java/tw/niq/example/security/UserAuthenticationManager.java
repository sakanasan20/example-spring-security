package tw.niq.example.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import tw.niq.example.entity.UserEntity;

@Slf4j
@Component
public class UserAuthenticationManager {
	
	public Boolean userIdMatches(Authentication authentication, Long userId) {
		
		UserEntity authenticatedUser = (UserEntity) authentication.getPrincipal();
		
		log.debug("Authenticated User ID:" + authenticatedUser.getId());
		
		return authenticatedUser.getId().equals(userId);
	}

}
