package br.com.trocabeer;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.trocabeer.controller.GerencialController;

@SpringBootTest
public class GerencialControllerTest {

	
	@Autowired
	private GerencialController gerencialController;
	
	@Test
	public void contextLoads() throws Exception {
		assertThat(gerencialController).isNotNull();
	}
	
}