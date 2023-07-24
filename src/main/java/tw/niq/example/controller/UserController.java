package tw.niq.example.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tw.niq.example.dto.UserDto;
import tw.niq.example.entity.UserEntity;
import tw.niq.example.model.UserModel;
import tw.niq.example.permission.UserReadPermission;
import tw.niq.example.service.RoleService;
import tw.niq.example.service.UserService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
	
	private final UserService userService;
	private final RoleService roleService;

	@PreAuthorize("hasAuthority('user.create')")
	@PostMapping
	public String createUser(@RequestBody UserModel userModel) {
		return "creating user...";
	}
	
	@UserReadPermission
	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public List<UserModel> readAllUsers(@AuthenticationPrincipal UserEntity authenticatedUser) {
		
		authenticatedUser.getRoles().forEach(role -> log.debug(role.getRole()));
		
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		List<UserModel> users = userService.readAllUsers().stream()
				.map(userEntity -> modelMapper.map(userEntity, UserModel.class))
				.collect(Collectors.toList());

		if (authenticatedUser.getRoles().contains(roleService.findRoleByRole("ADMIN"))) {
			return users;
		} else {
			return Arrays.asList(readUserByUserId(authenticatedUser.getId()));
		}
	}
	
	@PreAuthorize("(hasAuthority('user.read') AND @userAuthenticationManager.userIdMatches(authentication, #userId)) OR hasRole('ADMIN')")
//	@UserReadPermission
	@GetMapping("/{userId}")
	public UserModel readUserByUserId(@PathVariable("userId") Long userId) {
		
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		UserDto userDto = userService.readUserByUserId(userId);
		
		UserModel userModel = modelMapper.map(userDto, UserModel.class);

		return userModel;
	}
	
	@UserReadPermission
	@GetMapping("/name/{username}")
	public UserModel readUserByUsername(@PathVariable("username") String username) {
		
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		UserDto userDto = userService.readUserByUserName(username);
		
		UserModel userModel = modelMapper.map(userDto, UserModel.class);

		return userModel;
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
