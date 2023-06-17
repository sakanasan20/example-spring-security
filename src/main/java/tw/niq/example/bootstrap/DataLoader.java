package tw.niq.example.bootstrap;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tw.niq.example.entity.AuthorityEntity;
import tw.niq.example.entity.UserEntity;
import tw.niq.example.repository.AuthorityRepository;
import tw.niq.example.repository.UserRepository;

@Slf4j
@RequiredArgsConstructor
@Component
public class DataLoader implements CommandLineRunner {

	private final UserRepository userRepository;
	private final AuthorityRepository authorityRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	@Override
	public void run(String... args) throws Exception {
		loadDefaultUser();
	}

	private void loadDefaultUser() {

		AuthorityEntity adminAuthority = authorityRepository.save(
				AuthorityEntity.builder()
					.role("ROLE_ADMIN")
					.build());
		
		AuthorityEntity userAuthority = authorityRepository.save(
				AuthorityEntity.builder()
					.role("ROLE_USER")
					.build());

		log.debug("Authority loaded: " + authorityRepository.count());
		
		userRepository.saveAll(Arrays.asList(
				UserEntity.builder()
					.username("admin")
					.password(passwordEncoder.encode("admin"))
					.authority(adminAuthority)
					.build(), 
				UserEntity.builder()
					.username("user")
					.password(passwordEncoder.encode("user"))
					.authority(userAuthority)
					.build()
		));
		
		log.debug("User loaded: " + userRepository.count());
	}

}
