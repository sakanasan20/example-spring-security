package tw.niq.example.repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import tw.niq.example.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

	Optional<UserEntity> findByUsername(String username);
	
	@Query("SELECT ue "
		+ "FROM UserEntity ue "
		+ "WHERE ue.username = ?1 "
		+ "AND true = :#{hasAuthority('user.read')} "
		+ "AND ue.id = ?#{principal?.id} ")
	Optional<UserEntity> findByUsernameSecured(String username);
	
	List<UserEntity> findAllByAccountNonLockedAndLastModifiedDateIsBefore(Boolean accountNonLocked, Timestamp timestamp);
	
}
