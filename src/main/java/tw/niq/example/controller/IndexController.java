package tw.niq.example.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

	@GetMapping("/")
	public String index() {
		return "index";
	}
	
	@GetMapping("/authenticated")
	public String authenticated() {
		return "authenticated";
	}
	
//	@Secured("ROLE_ADMIN")
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/authenticatedAsAdminRole")
	public String authenticatedAsAdminRole() {
		return "authenticatedAsAdminRole";
	}
	
//	@Secured("ROLE_USER")
	@PreAuthorize("hasRole('USER')")
	@GetMapping("/authenticatedAsUserRole")
	public String authenticatedAsUserRole() {
		return "authenticatedAsUserRole";
	}
	
//	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	@GetMapping("/authenticatedAsAnyRole")
	public String authenticatedAsAnyRole() {
		return "authenticatedAsAnyRole";
	}
	
}
