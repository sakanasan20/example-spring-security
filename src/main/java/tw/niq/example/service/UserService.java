package tw.niq.example.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import tw.niq.example.dto.UserDto;

public interface UserService extends UserDetailsService {

	List<UserDto> readAllUsers();

}
