package tw.niq.example.bootstrap;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tw.niq.example.entity.AuthorityEntity;
import tw.niq.example.entity.RoleEntity;
import tw.niq.example.entity.UserEntity;
import tw.niq.example.repository.AuthorityRepository;
import tw.niq.example.repository.RoleRepository;
import tw.niq.example.repository.UserRepository;

@Slf4j
@RequiredArgsConstructor
@Component
public class DataLoader implements CommandLineRunner {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final AuthorityRepository authorityRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	@Override
	public void run(String... args) throws Exception {
		loadDefaultUser();
	}

	private void loadDefaultUser() {

		authorityRepository.saveAll(Arrays.asList(
				AuthorityEntity.builder().permission("user.create").build(),
				AuthorityEntity.builder().permission("user.read").build(),
				AuthorityEntity.builder().permission("user.update").build(),
				AuthorityEntity.builder().permission("user.delete").build()
			)
		);
		
		log.debug("Authority loaded: " + authorityRepository.count());
		
		AuthorityEntity authorityUserCreate = authorityRepository.findByPermission("user.create").orElseThrow(RuntimeException::new);
		AuthorityEntity authorityUserRead = authorityRepository.findByPermission("user.read").orElseThrow(RuntimeException::new);
		AuthorityEntity authorityUserUpdate = authorityRepository.findByPermission("user.update").orElseThrow(RuntimeException::new);
		AuthorityEntity authorityUserDelete = authorityRepository.findByPermission("user.delete").orElseThrow(RuntimeException::new);
		
		roleRepository.saveAll(Arrays.asList(
				RoleEntity.builder().role("ADMIN")
					.authority(authorityUserCreate)
					.authority(authorityUserRead)
					.authority(authorityUserUpdate)
					.authority(authorityUserDelete).build(),
				RoleEntity.builder().role("USER")
					.authority(authorityUserRead)
					.authority(authorityUserUpdate)
					.authority(authorityUserDelete).build(),
				RoleEntity.builder().role("GUEST")
					.authority(authorityUserRead).build()
			)
		);

		log.debug("Role loaded: " + roleRepository.count());
		
		RoleEntity roleAdmin = roleRepository.findByRole("ADMIN").orElseThrow(RuntimeException::new);
		RoleEntity roleUser = roleRepository.findByRole("USER").orElseThrow(RuntimeException::new);
		RoleEntity roleGuest = roleRepository.findByRole("GUEST").orElseThrow(RuntimeException::new);
		
		userRepository.saveAll(Arrays.asList(
				UserEntity.builder()
					.username("admin")
					.password(passwordEncoder.encode("admin"))
					.role(roleAdmin)
					.build(), 
				UserEntity.builder()
					.username("user")
					.password(passwordEncoder.encode("user"))
					.role(roleUser)
					.build(), 
				UserEntity.builder()
					.username("guest")
					.password(passwordEncoder.encode("guest"))
					.role(roleGuest)
					.build()
		));
		
		log.debug("User loaded: " + userRepository.count());
	}

}
