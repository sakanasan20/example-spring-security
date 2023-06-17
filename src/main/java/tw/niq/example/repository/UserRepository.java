package tw.niq.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.niq.example.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

	Optional<UserEntity> findByUsername(String username);
	
}
