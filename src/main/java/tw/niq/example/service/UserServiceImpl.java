package tw.niq.example.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import tw.niq.example.entity.AuthorityEntity;
import tw.niq.example.entity.UserEntity;
import tw.niq.example.repository.UserRepository;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	
	@Transactional
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException(username));
		
		return new User(
				userEntity.getUsername(), 
				userEntity.getPassword(), 
				userEntity.getEnabled(), 
				userEntity.getAccountNonExpired(), 
				userEntity.getCredentialsNonExpired(), 
				userEntity.getAccountNonLocked(), 
				convertToGrandAuthority(userEntity.getAuthorities()));
	}

	private Collection<? extends GrantedAuthority> convertToGrandAuthority(Set<AuthorityEntity> authorities) {
		
		if (authorities != null && authorities.size() > 0) {
			return authorities.stream()
					.map(AuthorityEntity::getRole)
					.map(SimpleGrantedAuthority::new)
					.collect(Collectors.toSet());
		}
		
		return new HashSet<>();
	}

}
