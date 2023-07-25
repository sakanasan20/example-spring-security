package tw.niq.example.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tw.niq.example.entity.UserEntity;
import tw.niq.example.repository.UserRepository;

@Slf4j
@RequiredArgsConstructor
@Controller
public class IndexController {
	
	private final UserRepository userRepository;
	
	private final GoogleAuthenticator googleAuthenticator;

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
	
	@GetMapping("/register2fa")
	public String register2fa(Model model) {
		
		UserEntity user = getUser();
		
		String googleUrl = GoogleAuthenticatorQRGenerator.getOtpAuthURL(
				"example", 
				user.getUsername(), 
				googleAuthenticator.createCredentials(user.getUsername()));
		
		log.debug("googleUrl: " + googleUrl);
		
		model.addAttribute("googleUrl", googleUrl);
		
		return "register2fa";
	}

	UserEntity getUser() {
		return (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	
	@PostMapping("/register2fa")
	public String confirmRegister2fa(@RequestParam Integer verifyCode) {
		
		UserEntity user = getUser();
		
		log.debug("verifyCode: " + verifyCode);
		
		Boolean verificationResult = googleAuthenticator.authorizeUser(user.getUsername(), verifyCode);
		
		log.debug("verificationResult: " + verificationResult);
		
		if (verificationResult) {
			
			log.debug("verifyCode: " + verifyCode);
			
			UserEntity userSaved = userRepository.findById(user.getId()).orElseThrow();
			userSaved.setGoogle2faEnabled(true);
			userRepository.save(userSaved);
			
			return "index";
			
		} else {
			// failed
			return "register2fa";
		}
	}
	
	@GetMapping("/verify2fa")
	public String verify2fa() {
		
		return "verify2fa";
	}
	
	@PostMapping("/verify2fa")
	public String confirmVerify2fa(@RequestParam Integer verifyCode) {
		
		UserEntity user = getUser();
		
		log.debug("verifyCode: " + verifyCode);
		
		if (googleAuthenticator.authorizeUser(user.getUsername(), verifyCode)) {
			
			((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).setGoogle2faNonVerified(false);
			
			return "index";
			
		} else {
			// failed
			return "verify2fa";
		}
	}
	
}
