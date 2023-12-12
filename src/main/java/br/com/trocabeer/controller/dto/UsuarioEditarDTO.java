package br.com.trocabeer.controller.dto;

import java.util.UUID;

import org.modelmapper.ModelMapper;

import br.com.trocabeer.domain.model.Cidade;
import br.com.trocabeer.domain.model.Usuario;
import br.com.trocabeer.domain.model.enums.TipoPessoa;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioEditarDTO {

	private UUID id;

	@NotBlank(message = "O nome é obrigatório.")
	private String nome;

	@NotBlank(message = "O email é obrigatório.")
	@Email(message = "O email deve ser válido.")
	private String email;

	// @NotNull(message = "O endereço é obrigatório.")
	private EnderecoDTO endereco = new EnderecoDTO();

	@NotNull(message = "O tipo de pessoa é obrigatório.")
	private TipoPessoa tipoPessoa;

	@NotBlank(message = "O telefone é obrigatório.")
	private String telefone;

	private Boolean ativo;

	@NotBlank(message = "O CEP é obrigatório.")
	private String cep;

	@NotBlank(message = "O logradouro é obrigatório.")
	private String logradouro;

	private String numero;

	private String complemento;

	@NotBlank(message = "O bairro é obrigatório.")
	private String bairro;

	@NotNull(message = "A cidade é obrigatória.")
	private CidadeDTO cidade = new CidadeDTO();

	public Usuario toUsuario() {

		this.getEndereco().setLogradouro(this.getLogradouro());
		this.getEndereco().setNumero(this.getNumero());
		this.getEndereco().setBairro(this.getBairro());
		this.getEndereco().setCep(this.getCep());
		this.getEndereco().setComplemento(this.getComplemento());
		this.getEndereco().setCidade(this.getCidade().toCidade());
		this.setAtivo(true);

		ModelMapper modelMapper = new ModelMapper();

		return modelMapper.map(this, Usuario.class);
	}

	public UsuarioEditarDTO toUsuarioDTO(Usuario usuario) {

		ModelMapper modelMapper = new ModelMapper();

		UsuarioEditarDTO usuarioDTO = modelMapper.map(usuario, this.getClass());

		usuarioDTO.setLogradouro(usuario.getEndereco().getLogradouro());
		usuarioDTO.setNumero(usuario.getEndereco().getNumero());
		usuarioDTO.setBairro(usuario.getEndereco().getBairro());
		usuarioDTO.setCep(usuario.getEndereco().getCep());
		usuarioDTO.setComplemento(usuario.getEndereco().getComplemento());
		usuarioDTO.setCidade(cidade.toCidadeDTO(usuario.getEndereco().getCidade()));

		return usuarioDTO;
	}
}
