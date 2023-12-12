package br.com.trocabeer.controller.dto;

import java.util.UUID;

import br.com.trocabeer.domain.model.Cidade;
import br.com.trocabeer.domain.model.Estilo;
import br.com.trocabeer.domain.model.Usuario;
import br.com.trocabeer.domain.model.enums.TipoTroca;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class ListaCervejaIndexDTO {

	private UUID id;

	private String nome;

	private Estilo estilo;

	private Boolean ativo;

	private Usuario usuario;

	private Cidade cidade;

	@Enumerated(EnumType.STRING)
	private TipoTroca tipoTroca;
}
