package br.com.trocabeer.controller.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import br.com.trocabeer.domain.model.Troca;
import br.com.trocabeer.domain.model.Usuario;
import br.com.trocabeer.domain.model.enums.StatusTroca;
import br.com.trocabeer.domain.model.enums.TipoTroca;
import lombok.Data;

@Data
public class TrocaListaDTO {

	private UUID id;

	private Date dataSolicitado;

	private Usuario apreciador;

	private Usuario mestreCervejeiro;

	private String cervejaNome;

	private TipoTroca tipoTroca;

	private BigDecimal quantidadeTroca;

	private StatusTroca status;

	public TrocaListaDTO toTrocaDTO(Troca troca) {

		ModelMapper modelMapper = new ModelMapper();

		TrocaListaDTO trocaDTO = modelMapper.map(troca, this.getClass());

		trocaDTO.setCervejaNome(troca.getCerveja().getNome());
		trocaDTO.setTipoTroca(troca.getComplementoCerveja().getTipoTroca());

		return trocaDTO;
	}

	public List<TrocaListaDTO> toListaTrocaDTO(List<Troca> trocas) {

		return trocas.stream().map(troca -> toTrocaDTO(troca)).collect(Collectors.toList());

	}
}
