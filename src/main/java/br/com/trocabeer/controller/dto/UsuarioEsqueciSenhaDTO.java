package br.com.trocabeer.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UsuarioEsqueciSenhaDTO {

	@NotBlank(message = "O email é obrigatório.")
	@Email(message = "O email deve ser válido.")
	private String email;
}