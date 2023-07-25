package tw.niq.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.niq.example.entity.LoginSuccess;

public interface LoginSuccessRepository extends JpaRepository<LoginSuccess, Long> {

}
