package tw.niq.example.security;

import java.util.List;

import org.springframework.stereotype.Component;

import com.warrenstrange.googleauth.ICredentialRepository;

import lombok.RequiredArgsConstructor;
import tw.niq.example.entity.UserEntity;
import tw.niq.example.repository.UserRepository;

@RequiredArgsConstructor
@Component
public class GoogleCredentialRepository implements ICredentialRepository {
	
	private final UserRepository userRepository;

	@Override
	public String getSecretKey(String userName) {
		
		UserEntity user = userRepository.findByUsername(userName).orElseThrow();

		return user.getGoogle2faSecret();
	}

	@Override
	public void saveUserCredentials(String userName, String secretKey, int validationCode, List<Integer> scratchCodes) {

		UserEntity user = userRepository.findByUsername(userName).orElseThrow();
		
		user.setGoogle2faSecret(secretKey);
		user.setGoogle2faEnabled(true);
		userRepository.save(user);		
	}

}
