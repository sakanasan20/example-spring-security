package tw.niq.example.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import tw.niq.example.model.UserModel;
import tw.niq.example.permission.UserReadPermission;
import tw.niq.example.service.UserService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
	
	private final UserService userService;

	@PreAuthorize("hasAuthority('user.create')")
	@PostMapping
	public String createUser(@RequestBody UserModel userModel) {
		return "creating user...";
	}
	
	@UserReadPermission
	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public List<UserModel> readAllUsers() {
		
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		List<UserModel> users = userService.readAllUsers().stream()
				.map(userEntity -> modelMapper.map(userEntity, UserModel.class))
				.collect(Collectors.toList());

		return users;
	}
	
	@UserReadPermission
	@GetMapping("/{username}")
	public String readUserByUsername(@PathVariable("username") String username) {
		return "reading user by username...";
	}
	
	@PreAuthorize("hasAuthority('user.update')")
	@PutMapping("/{username}")
	public String updateUserByUsername(@PathVariable("username") String username, @RequestBody UserModel userModel) {
		return "updating user by username...";
	}
	
	@PreAuthorize("hasAuthority('user.delete')")
	@DeleteMapping("/{username}")
	public String deleteUserByUsername(@PathVariable("username") String username) {
		return "deleting user by username...";
	}
	
}
