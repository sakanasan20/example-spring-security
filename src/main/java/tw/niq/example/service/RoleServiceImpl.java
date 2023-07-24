package tw.niq.example.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tw.niq.example.entity.RoleEntity;
import tw.niq.example.repository.RoleRepository;

@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {
	
	private final RoleRepository roleRepository;

	@Override
	public RoleEntity findRoleByRole(String role) {
		
		return roleRepository.findByRole(role).orElseThrow(RuntimeException::new);
	}

}
