package tw.niq.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.niq.example.entity.RoleEntity;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

	Optional<RoleEntity> findByRole(String role);
	
}
