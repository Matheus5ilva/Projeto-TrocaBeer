package br.com.trocabeer.controller.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import br.com.trocabeer.domain.model.Cerveja;
import br.com.trocabeer.domain.model.ComplementoCerveja;
import br.com.trocabeer.domain.model.Estilo;
import br.com.trocabeer.domain.model.InformacaoTecnica;
import br.com.trocabeer.domain.model.Usuario;
import lombok.Data;

@Data
public class CervejaIndexDTO {

	private UUID cervejaId;
	private String nome;
	private Estilo estilo;
	private InformacaoTecnica descricaoTecnica;
	private Boolean ativo = true;
	private List<String> copoSugerido = new ArrayList<>();
	private List<String> comidaHarmonizada = new ArrayList<>();
	private ComplementoCerveja complemento = new ComplementoCerveja();
	private Usuario usuario;
	private BigDecimal estoque;

	public CervejaIndexDTO toCervejaDTO(Cerveja cerveja) {

		ModelMapper modelMapper = new ModelMapper();

		CervejaIndexDTO cervejaDTO = modelMapper.map(cerveja, this.getClass());

		cervejaDTO.setComplemento(cerveja.getComplementos().get(cerveja.getComplementos().size() - 1));
		cervejaDTO.setEstoque(cerveja.getEstoqueMovimento());
		return cervejaDTO;
	}

	public List<CervejaIndexDTO> toListaCervejaDTO(List<Cerveja> cervejas) {

		return cervejas.stream().map(cerveja -> toCervejaDTO(cerveja)).collect(Collectors.toList());
	}

}
