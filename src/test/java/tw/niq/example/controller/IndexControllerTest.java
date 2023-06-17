package tw.niq.example.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

//@WebMvcTest
@SpringBootTest
class IndexControllerTest {
	
	@Autowired
	WebApplicationContext webApplicationContext;
	
	MockMvc mockMvc;

	@BeforeEach
	void setUp() throws Exception {
		mockMvc = MockMvcBuilders
				.webAppContextSetup(webApplicationContext)
				.apply(springSecurity())
				.build();
	}
	
	@Test
	void testIndex() throws Exception {
		mockMvc.perform(get("/"))
			.andExpect(status().isOk())
			.andExpect(view().name("index"));
	}

	@WithMockUser("admin")
	@Test
	void testAuthenticated_withAdminUser() throws Exception {
		mockMvc.perform(get("/authenticated"))
			.andExpect(status().isOk())
			.andExpect(view().name("authenticated"));
	}
	
	@Test
	void testAuthenticated_withAdminCredentials() throws Exception {
		mockMvc.perform(get("/authenticated").with(httpBasic("admin", "admin")))
			.andExpect(status().isOk())
			.andExpect(view().name("authenticated"));
	}
	
	@Test
	void testAuthenticated_withBadAdminCredentials() throws Exception {
		mockMvc.perform(get("/authenticated").with(httpBasic("admin", "wrong_password")))
			.andExpect(status().isUnauthorized());
	}
	
	@Test
	void testAuthenticatedAsAdminRole_withoutAuthorized() throws Exception {
		mockMvc.perform(get("/authenticatedAsAdminRole"))
			.andExpect(status().isUnauthorized());
	}
	
	@Test
	void testAuthenticatedAsAdminRole_withAdminRole() throws Exception {
		mockMvc.perform(get("/authenticatedAsAdminRole").with(httpBasic("admin", "admin")))
			.andExpect(status().isOk())
			.andExpect(view().name("authenticatedAsAdminRole"));
	}
	
	@Test
	void testAuthenticatedAsAdminRole_withUserRole() throws Exception {
		mockMvc.perform(get("/authenticatedAsAdminRole").with(httpBasic("user", "user")))
			.andExpect(status().isForbidden());
	}
	
	@Test
	void testAuthenticatedAsUserRole_withoutAuthorized() throws Exception {
		mockMvc.perform(get("/authenticatedAsUserRole"))
			.andExpect(status().isUnauthorized());
	}
	
	@Test
	void testAuthenticatedAsUserRole_withAdminRoled() throws Exception {	
		mockMvc.perform(get("/authenticatedAsUserRole").with(httpBasic("admin", "admin")))
			.andExpect(status().isForbidden());
	}
	
	@Test
	void testAuthenticatedAsUserRole_withUserRole() throws Exception {
		mockMvc.perform(get("/authenticatedAsUserRole").with(httpBasic("user", "user")))
			.andExpect(status().isOk())
			.andExpect(view().name("authenticatedAsUserRole"));
	}
	
	@Test
	void testAuthenticatedAsAnyRole_withoutAuthorized() throws Exception {
		mockMvc.perform(get("/authenticatedAsAnyRole"))
			.andExpect(status().isUnauthorized());
	}
	
	@Test
	void testAuthenticatedAsAnyRole_withAdminRoled() throws Exception {	
		mockMvc.perform(get("/authenticatedAsAnyRole").with(httpBasic("admin", "admin")))
			.andExpect(status().isOk())
			.andExpect(view().name("authenticatedAsAnyRole"));
	}
	
	@Test
	void testAuthenticatedAsAnyRole_withUserRole() throws Exception {
		mockMvc.perform(get("/authenticatedAsAnyRole").with(httpBasic("user", "user")))
			.andExpect(status().isOk())
			.andExpect(view().name("authenticatedAsAnyRole"));
	}

}
