package br.com.trocabeer.controller.dto;

import br.com.trocabeer.domain.model.Estilo;
import lombok.Data;

@Data
public class PesquisaDTO {

	private String nome;
	private Estilo estilo;

}
