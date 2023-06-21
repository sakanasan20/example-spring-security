package tw.niq.example.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import tw.niq.example.model.UserModel;

@SpringBootTest
class UserControllerTest {

	@Autowired
	WebApplicationContext webApplicationContext;
	
	MockMvc mockMvc;
	
	ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void setUp() throws Exception {
		mockMvc = MockMvcBuilders
				.webAppContextSetup(webApplicationContext)
				.apply(springSecurity())
				.build();
	}
	
	@Test
	void testCreateUser_withAdmin() throws Exception {
		mockMvc.perform(post("/api/v1/users")
				.content(objectMapper.writeValueAsString(new UserModel()))
				.contentType(MediaType.APPLICATION_JSON)
				.with(httpBasic("admin", "admin")))
			.andExpect(status().isOk())
			.andExpect(content().string("creating user..."));
	}
	
	@Test
	void testCreateUser_withUser() throws Exception {
		mockMvc.perform(post("/api/v1/users")
				.content(objectMapper.writeValueAsString(new UserModel()))
				.contentType(MediaType.APPLICATION_JSON)
				.with(httpBasic("user", "user")))
			.andExpect(status().isForbidden());
	}
	
	@Test
	void testCreateUser_withGuest() throws Exception {
		mockMvc.perform(post("/api/v1/users")
				.content(objectMapper.writeValueAsString(new UserModel()))
				.contentType(MediaType.APPLICATION_JSON)
				.with(httpBasic("guest", "guest")))
			.andExpect(status().isForbidden());
	}

	@Test
	void testReadAllUsers_withAdmin() throws Exception {
		mockMvc.perform(get("/api/v1/users").with(httpBasic("admin", "admin")))
			.andExpect(status().isOk())
			.andExpect(content().string("reading all users..."));
	}
	
	@Test
	void testReadAllUsers_withUser() throws Exception {
		mockMvc.perform(get("/api/v1/users").with(httpBasic("user", "user")))
			.andExpect(status().isOk())
			.andExpect(content().string("reading all users..."));
	}
	
	@Test
	void testReadAllUsers_withAGuest() throws Exception {
		mockMvc.perform(get("/api/v1/users").with(httpBasic("guest", "guest")))
			.andExpect(status().isOk())
			.andExpect(content().string("reading all users..."));
	}
	
	@Test
	void testReadUserByUsername_withAdmin() throws Exception {
		mockMvc.perform(get("/api/v1/users/test").with(httpBasic("admin", "admin")))
			.andExpect(status().isOk())
			.andExpect(content().string("reading user by username..."));
	}
	
	@Test
	void testReadUserByUsername_withUser() throws Exception {
		mockMvc.perform(get("/api/v1/users/test").with(httpBasic("user", "user")))
			.andExpect(status().isOk())
			.andExpect(content().string("reading user by username..."));
	}
	
	@Test
	void testReadUserByUsername_withAGuest() throws Exception {
		mockMvc.perform(get("/api/v1/users/test").with(httpBasic("guest", "guest")))
			.andExpect(status().isOk())
			.andExpect(content().string("reading user by username..."));
	}

	@Test
	void testUpdateUserByUsername_withAdmin() throws Exception {
		mockMvc.perform(put("/api/v1/users/test")
				.content(objectMapper.writeValueAsString(new UserModel()))
				.contentType(MediaType.APPLICATION_JSON)
				.with(httpBasic("admin", "admin")))
			.andExpect(status().isOk())
			.andExpect(content().string("updating user by username..."));
	}
	
	void testUpdateUserByUsername_withUser() throws Exception {
		mockMvc.perform(put("/api/v1/users/test")
				.content(objectMapper.writeValueAsString(new UserModel()))
				.contentType(MediaType.APPLICATION_JSON)
				.with(httpBasic("user", "user")))
			.andExpect(status().isOk())
			.andExpect(content().string("updating user by username..."));
	}
	
	void testUpdateUserByUsername_withAGuest() throws Exception {
		mockMvc.perform(put("/api/v1/users/test")
				.content(objectMapper.writeValueAsString(new UserModel()))
				.contentType(MediaType.APPLICATION_JSON)
				.with(httpBasic("guest", "guest")))
			.andExpect(status().isForbidden());
	}

	@Test
	void testDeleteUserByUsername_withAdmin() throws Exception {
		mockMvc.perform(delete("/api/v1/users/test").with(httpBasic("admin", "admin")))
			.andExpect(status().isOk())
			.andExpect(content().string("deleting user by username..."));
	}
	
	@Test
	void testDeleteUserByUsername_withUser() throws Exception {
		mockMvc.perform(delete("/api/v1/users/test").with(httpBasic("user", "user")))
			.andExpect(status().isOk())
			.andExpect(content().string("deleting user by username..."));
	}
	
	@Test
	void testDeleteUserByUsername_withAGuest() throws Exception {
		mockMvc.perform(delete("/api/v1/users/test").with(httpBasic("guest", "guest")))
			.andExpect(status().isForbidden());
	}

}
