package co.edu.unbosque.parcialfinal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.google.gson.JsonParser;

@SpringBootTest
@AutoConfigureMockMvc
class ProductoControllerTests {

	@Autowired
	private MockMvc mockMvc;

	private String login(String username, String password) throws Exception {
		String body = mockMvc
				.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
						.content("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}"))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		return JsonParser.parseString(body).getAsJsonObject().get("token").getAsString();
	}

	@ParameterizedTest
	@ValueSource(strings = { "lacteo", "carne" })
	void crudYCompra(String tipo) throws Exception {
		String admin = "Bearer " + login("admin", "1234567890");
		String cliente = "Bearer " + login("normaluser", "1234567890");
		String producto = "{\"nombre\":\"Producto " + tipo
				+ "\",\"cantidad\":10,\"precio\":2500,\"urlImagen\":\"https://example.com/img.png\"}";

		mockMvc.perform(post("/" + tipo + "/createjson").header("Authorization", cliente)
				.contentType(MediaType.APPLICATION_JSON).content(producto)).andExpect(status().isForbidden());
		mockMvc.perform(post("/" + tipo + "/createjson").header("Authorization", admin)
				.contentType(MediaType.APPLICATION_JSON).content(producto)).andExpect(status().isCreated());
		mockMvc.perform(post("/" + tipo + "/createjson").header("Authorization", admin)
				.contentType(MediaType.APPLICATION_JSON).content(producto)).andExpect(status().isNotAcceptable());
		mockMvc.perform(post("/" + tipo + "/createjson").header("Authorization", admin)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"nombre\":\"x\",\"cantidad\":-1,\"precio\":0,\"urlImagen\":\"nada\"}"))
				.andExpect(status().isBadRequest());

		String lista = mockMvc.perform(get("/" + tipo + "/getall").header("Authorization", cliente))
				.andExpect(status().isAccepted()).andExpect(jsonPath("$[0].urlImagen").value("https://example.com/img.png"))
				.andReturn().getResponse().getContentAsString();
		long id = JsonParser.parseString(lista).getAsJsonArray().get(0).getAsJsonObject().get("id").getAsLong();

		mockMvc.perform(put("/" + tipo + "/comprar").param("id", String.valueOf(id)).param("cantidad", "3")
				.header("Authorization", admin)).andExpect(status().isForbidden());
		mockMvc.perform(put("/" + tipo + "/comprar").param("id", String.valueOf(id)).param("cantidad", "3")
				.header("Authorization", cliente)).andExpect(status().isAccepted());
		mockMvc.perform(put("/" + tipo + "/comprar").param("id", String.valueOf(id)).param("cantidad", "50")
				.header("Authorization", cliente)).andExpect(status().isConflict());
		mockMvc.perform(put("/" + tipo + "/comprar").param("id", String.valueOf(id)).param("cantidad", "0")
				.header("Authorization", cliente)).andExpect(status().isBadRequest());
		mockMvc.perform(get("/" + tipo + "/getbyid/" + id).header("Authorization", cliente))
				.andExpect(status().isAccepted()).andExpect(jsonPath("$.cantidad").value(7));

		mockMvc.perform(put("/" + tipo + "/updatejson").param("id", String.valueOf(id)).header("Authorization", admin)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"nombre\":\"Editado " + tipo
						+ "\",\"cantidad\":20,\"precio\":3000,\"urlImagen\":\"https://example.com/b.png\"}"))
				.andExpect(status().isAccepted());
		mockMvc.perform(delete("/" + tipo + "/deletebyid/" + id).header("Authorization", cliente))
				.andExpect(status().isForbidden());
		mockMvc.perform(delete("/" + tipo + "/deletebyid/" + id).header("Authorization", admin))
				.andExpect(status().isAccepted());
		mockMvc.perform(get("/" + tipo + "/getall")).andExpect(status().isUnauthorized());
	}

}
