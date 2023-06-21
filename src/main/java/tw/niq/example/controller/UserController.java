package tw.niq.example.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tw.niq.example.model.UserModel;
import tw.niq.example.permission.UserReadPermission;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

	@PreAuthorize("hasAuthority('user.create')")
	@PostMapping
	public String createUser(@RequestBody UserModel userModel) {
		return "creating user...";
	}
	
	@UserReadPermission
	@GetMapping
	public String readAllUsers() {
		return "reading all users...";
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
