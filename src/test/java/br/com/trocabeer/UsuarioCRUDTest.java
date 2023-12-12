package br.com.trocabeer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import br.com.trocabeer.controller.UsuarioController;
import br.com.trocabeer.domain.model.Cidade;
import br.com.trocabeer.domain.model.Endereco;
import br.com.trocabeer.domain.model.Usuario;
import br.com.trocabeer.domain.model.enums.TipoPessoa;
import br.com.trocabeer.domain.repository.UsuarioRepository;
import br.com.trocabeer.domain.service.UsuarioService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;


@SpringBootTest
@AutoConfigureMockMvc	
public class UsuarioCRUDTest {

	@MockBean
	private UsuarioService usuarioService;

	@MockBean
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private UsuarioController usuarioController;

	@Autowired
    private MockMvc mockMvc;	
	
	Usuario novoUsuario;

	@BeforeEach
	public void setup() {
		RestAssuredMockMvc.standaloneSetup(this.usuarioController);
	}

	@Test
	public void deveRetornarSucessoNaRotinaCadastrar() throws Exception {	
		this.mockMvc.perform(get("/registro")).andDo(print())
	      .andExpect(view().name("registro"));
	}
	
	/*@Test
	public void deveRetornarSucessoNaRotinaIndex() throws Exception {	
		this.mockMvc.perform(get("/")).andDo(print())
	      .andExpect(view().name("index"));
	}*/
	
	
	@Test
	public void contextLoads() throws Exception {
		assertThat(usuarioService).isNotNull();
	}

	private void preparaDados() {
		novoUsuario = new Usuario();

		novoUsuario.setNome("Usuario Teste");
		novoUsuario.setAtivo(true);
		novoUsuario.setEmail("usuario.teste@teste.com");
		novoUsuario.setTelefone("35319090");
		novoUsuario.setTipoPessoa(TipoPessoa.MESTRE_CERVEJEIRO);

		Endereco endereco = new Endereco();
		endereco.setBairro("SantoInacio");
		endereco.setCep("39100-000");
		endereco.setCidade(new Cidade());

		novoUsuario.setEndereco(null);
	}

}