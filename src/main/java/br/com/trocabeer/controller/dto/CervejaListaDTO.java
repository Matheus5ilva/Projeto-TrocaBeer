package br.com.trocabeer.controller.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import br.com.trocabeer.domain.model.Cerveja;
import br.com.trocabeer.domain.model.Estilo;
import br.com.trocabeer.domain.model.Usuario;
import br.com.trocabeer.domain.model.enums.TipoTroca;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class CervejaListaDTO {

	private UUID id;

	private String nome;

	private Estilo estilo;

	private Boolean ativo;

	private Usuario usuario;

	private Date dataFabricacao;

	private Date dataValidade;

	private BigDecimal estoque;

	@Enumerated(EnumType.STRING)
	private TipoTroca tipoTroca;
	
	private Boolean possuiTroca;

	public CervejaListaDTO toCervejaDTO(Cerveja cerveja) {

		ModelMapper modelMapper = new ModelMapper();

		CervejaListaDTO cervejaDTO = modelMapper.map(cerveja, this.getClass());

		cervejaDTO.setDataFabricacao(
				cerveja.getComplementos().get(cerveja.getComplementos().size() - 1).getDataFabricacao());
		cervejaDTO
				.setDataValidade(cerveja.getComplementos().get(cerveja.getComplementos().size() - 1).getDataValidade());
		cervejaDTO.setEstoque(cerveja.getEstoqueMovimento());
		cervejaDTO.setTipoTroca(cerveja.getComplementos().get(cerveja.getComplementos().size() - 1).getTipoTroca());

		return cervejaDTO;
	}

	public List<CervejaListaDTO> toListaCervejaDTO(List<Cerveja> cervejas) {

		return cervejas.stream().map(cerveja -> toCervejaDTO(cerveja)).collect(Collectors.toList());
	}

}
