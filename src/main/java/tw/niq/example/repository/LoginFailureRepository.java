package tw.niq.example.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.niq.example.entity.LoginFailure;
import tw.niq.example.entity.UserEntity;

public interface LoginFailureRepository extends JpaRepository<LoginFailure, Long> {
	
	List<LoginFailure> findAllByUserAndCreatedDateIsAfter(UserEntity user, Timestamp timestamp);

}
