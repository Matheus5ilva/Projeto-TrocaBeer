package br.com.trocabeer.controller.dto;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.com.trocabeer.domain.model.Cidade;
import br.com.trocabeer.domain.model.Usuario;
import br.com.trocabeer.domain.model.enums.TipoPessoa;
import jakarta.persistence.Column;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioRegistroDTO {

	@NotBlank(message = "O nome é obrigatório.")
	private String nome;

	@NotBlank(message = "O email é obrigatório.")
	@Email(message = "O email deve ser válido.")
	private String email;

	@NotBlank(message = "A senha é obrigatória.")
	@Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres.")
	private String senha;

	// @NotNull(message = "O endereço é obrigatório.")
	private EnderecoDTO endereco = new EnderecoDTO();

	@NotNull(message = "O tipo de pessoa é obrigatório.")
	private TipoPessoa tipoPessoa;

	@NotBlank(message = "O telefone é obrigatório.")
	private String telefone;

	private Boolean ativo = true;

	@NotBlank(message = "O CEP é obrigatório.")
	private String cep;

	@NotBlank(message = "O logradouro é obrigatório.")
	private String logradouro;

	private String numero;

	private String complemento;

	@NotBlank(message = "O bairro é obrigatório.")
	private String bairro;

	@Valid
	@NotNull(message = "A cidade é obrigatória.")
	private CidadeDTO cidade;
	
	private Boolean premium = false;

	public Usuario toUsuario() {

		this.getEndereco().setLogradouro(this.getLogradouro());
		this.getEndereco().setNumero(this.getNumero());
		this.getEndereco().setBairro(this.getBairro());
		this.getEndereco().setCep(this.getCep());
		this.getEndereco().setComplemento(this.getComplemento());
		this.getEndereco().setCidade(this.getCidade().toCidade());
		this.setSenha(new BCryptPasswordEncoder().encode(this.getSenha()));

		ModelMapper modelMapper = new ModelMapper();

		return modelMapper.map(this, Usuario.class);
	}
}
