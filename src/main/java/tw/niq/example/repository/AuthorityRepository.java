package tw.niq.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.niq.example.entity.AuthorityEntity;

public interface AuthorityRepository extends JpaRepository<AuthorityEntity, Long> {

}
