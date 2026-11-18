package co.edu.unbosque.parcialfinal;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.google.gson.JsonParser;

@SpringBootTest
@AutoConfigureMockMvc
class ParcialfinalApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	private String login(String username, String password) throws Exception {
		String body = mockMvc
				.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
						.content("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}"))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		return JsonParser.parseString(body).getAsJsonObject().get("token").getAsString();
	}

	@Test
	void contextLoads() {
		assertNotNull(mockMvc);
	}

	@Test
	void flujoCompletoJwt() throws Exception {
		mockMvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON)
				.content("{\"username\":\"pepito\",\"password\":\"clave123\",\"role\":\"ADMIN\"}"))
				.andExpect(status().isCreated());
		mockMvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON)
				.content("{\"username\":\"pepito\",\"password\":\"otra\"}")).andExpect(status().isConflict());
		mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
				.content("{\"username\":\"pepito\",\"password\":\"mala\"}")).andExpect(status().isUnauthorized());

		String userToken = login("pepito", "clave123");
		mockMvc.perform(get("/user/me").header("Authorization", "Bearer " + userToken))
				.andExpect(status().isAccepted()).andExpect(jsonPath("$.role").value("USER"))
				.andExpect(jsonPath("$.password").doesNotExist());
		mockMvc.perform(get("/user/getall").header("Authorization", "Bearer " + userToken))
				.andExpect(status().isAccepted());
		mockMvc.perform(delete("/user/deletebyname").param("name", "normaluser").header("Authorization",
				"Bearer " + userToken)).andExpect(status().isForbidden());
		mockMvc.perform(get("/user/getall")).andExpect(status().isUnauthorized());
		mockMvc.perform(get("/user/getall").header("Authorization", "Bearer token.falso.x"))
				.andExpect(status().isUnauthorized());

		String adminToken = login("admin", "1234567890");
		mockMvc.perform(delete("/user/deletebyname").param("name", "pepito").header("Authorization",
				"Bearer " + adminToken)).andExpect(status().isAccepted());
	}

	@Test
	void corsPermiteAngular() throws Exception {
		mockMvc.perform(options("/user/getall").header("Origin", "http://localhost:4200")
				.header("Access-Control-Request-Method", "GET")
				.header("Access-Control-Request-Headers", "authorization,content-type"))
				.andExpect(status().isOk())
				.andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:4200"));
	}

}
