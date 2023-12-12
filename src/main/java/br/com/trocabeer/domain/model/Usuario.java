package br.com.trocabeer.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
public class Usuario extends Pessoa {

	private static final long serialVersionUID = -8802361757660245371L;

	@NotBlank(message = "O email é obrigatório.")
	@Email(message = "O email deve ser válido.")
	@Column(unique = true, nullable = false)
	private String email;

	@NotBlank(message = "A senha é obrigatória.")
	@Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres.")
	@Column(nullable = false)
	private String senha;
	
	
	private String tokenAlterarSenha;
	
}
