package br.com.trocabeer.controller.dto;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.com.trocabeer.domain.model.Cidade;
import br.com.trocabeer.domain.model.Estado;
import br.com.trocabeer.domain.model.Usuario;
import br.com.trocabeer.domain.model.enums.TipoPessoa;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CidadeDTO {

	@NotNull(message = "A cidade é obrigatório.")
	private Long id;

	private String nome;
	
	private Estado estado;

	public Cidade toCidade() {

		ModelMapper modelMapper = new ModelMapper();

		return modelMapper.map(this, Cidade.class);
	}
	
	public CidadeDTO toCidadeDTO(Cidade cidade) {

		ModelMapper modelMapper = new ModelMapper();

		return modelMapper.map(cidade, this.getClass());

	}
}
