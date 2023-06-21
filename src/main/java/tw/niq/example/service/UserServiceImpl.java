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
import tw.niq.example.entity.RoleEntity;
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
				convertToGrandAuthority(userEntity));
	}

	private Collection<? extends GrantedAuthority> convertToGrandAuthority(UserEntity userEntity) {
		
		Set<RoleEntity> roles = userEntity.getRoles();
		
		Set<AuthorityEntity> authorities = userEntity.getAuthorities();
		
		Set<SimpleGrantedAuthority> simpleGrantedAuthorities = new HashSet<>();
		
		if (roles != null && roles.size() > 0) {
			simpleGrantedAuthorities.addAll(roles.stream()
					.map(RoleEntity::getRole)
					.map(role -> "ROLE_" + role)
					.map(SimpleGrantedAuthority::new)
					.collect(Collectors.toSet()));
		}
		
		if (authorities != null && authorities.size() > 0) {
			simpleGrantedAuthorities.addAll(authorities.stream()
					.map(AuthorityEntity::getPermission)
					.map(SimpleGrantedAuthority::new)
					.collect(Collectors.toSet()));
		}
		
		return simpleGrantedAuthorities;
	}

}
