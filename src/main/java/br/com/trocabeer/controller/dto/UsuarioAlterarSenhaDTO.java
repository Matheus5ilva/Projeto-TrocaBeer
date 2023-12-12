package br.com.trocabeer.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioAlterarSenhaDTO {

	@NotBlank(message = "A senha é obrigatória.")
	@Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres.")
	private String senha;

	@NotBlank(message = "A senha é obrigatória.")
	@Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres.")
	private String confirmarSenha;

}