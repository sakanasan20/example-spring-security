package tw.niq.example;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.util.DigestUtils;

@SuppressWarnings("deprecation")
public class PasswordEncodingTests {

	final static String PASSWORD = "admin";
	
	@Test
	void testMd5Digest() {
		System.out.println("MD5 1st: " + DigestUtils.md5DigestAsHex(PASSWORD.getBytes()));
		System.out.println("MD5 2nd: " + DigestUtils.md5DigestAsHex(PASSWORD.getBytes()));
		String saltedPassword = PASSWORD + "salt";
		System.out.println("MD5 Salted: " + DigestUtils.md5DigestAsHex(saltedPassword.getBytes()));
	}

	@Test
	void testNoOpEncode() {
		PasswordEncoder passwordEncoder = NoOpPasswordEncoder.getInstance();
		System.out.println("NoOp: " + passwordEncoder.encode(PASSWORD));
	}
	
	@Test
	void testLdapShaEncode() {
		PasswordEncoder passwordEncoder = new LdapShaPasswordEncoder();
		System.out.println("LDAP 1st : " + passwordEncoder.encode(PASSWORD));
		System.out.println("LDAP 2nd : " + passwordEncoder.encode(PASSWORD));
		System.out.println("LDAP Matches : " + passwordEncoder.matches(PASSWORD, passwordEncoder.encode(PASSWORD)));
	}
	
	@Test
	void testStandardEncode() {
		PasswordEncoder passwordEncoder = new StandardPasswordEncoder();
		System.out.println("Standard 1st : " + passwordEncoder.encode(PASSWORD));
		System.out.println("Standard 2nd : " + passwordEncoder.encode(PASSWORD));
		System.out.println("Standard Matches : " + passwordEncoder.matches(PASSWORD, passwordEncoder.encode(PASSWORD)));
	}
	
	@Test
	void testBCryptEncode() {
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		System.out.println("BCrypt 1st : " + passwordEncoder.encode(PASSWORD));
		System.out.println("BCrypt 2nd : " + passwordEncoder.encode(PASSWORD));
		System.out.println("BCrypt Matches : " + passwordEncoder.matches(PASSWORD, passwordEncoder.encode(PASSWORD)));
	}
	
}
