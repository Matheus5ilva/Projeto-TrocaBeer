package br.com.trocabeer.controller.dto;

import br.com.trocabeer.domain.model.Avaliacao;
import br.com.trocabeer.domain.model.Cerveja;
import br.com.trocabeer.domain.model.Troca;
import br.com.trocabeer.domain.model.Usuario;
import br.com.trocabeer.domain.model.enums.StatusTroca;
import br.com.trocabeer.domain.model.enums.TipoTroca;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
public class AvaliacaoListaDTO {

	private UUID id;
	private String nomeCerveja;
	private Usuario apreciador;
	private Usuario mestreCervejeiro;
	private Date dataSolicitado;
	private Troca troca;

	public AvaliacaoListaDTO toAvaliacaoDTO(Avaliacao avaliacao) {

		ModelMapper modelMapper = new ModelMapper();

		AvaliacaoListaDTO avaliacaoDTO = modelMapper.map(avaliacao, this.getClass());

		avaliacaoDTO.setNomeCerveja(avaliacao.getTroca().getCerveja().getNome());
		return avaliacaoDTO;
	}

	public List<AvaliacaoListaDTO> toListaAvaliacaoDTO(List<Avaliacao> avaliacoes) {

		return avaliacoes.stream().map(avaliacao -> toAvaliacaoDTO(avaliacao)).collect(Collectors.toList());

	}
}
