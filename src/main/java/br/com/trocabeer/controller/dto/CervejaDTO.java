package br.com.trocabeer.controller.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;

import br.com.trocabeer.domain.model.Estilo;
import br.com.trocabeer.domain.model.InformacaoTecnica;
import br.com.trocabeer.domain.model.Usuario;
import br.com.trocabeer.domain.model.enums.TipoTroca;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CervejaDTO {

	private UUID id;
	

	@NotNull
	@Size(min = 1, max = 255)
	private String nome;

	@NotNull
	private Estilo estilo;

	private Boolean ativo;

	@NotNull
	private Usuario usuario;

	private InformacaoTecnica descricaoTecnica;

	@ElementCollection
	private List<String> copoSugerido;

	@ElementCollection
	private List<String> comidaHarmonizada;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dataFabricacao;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dataValidade;

	private BigDecimal estoque;

	@Enumerated(EnumType.STRING)
	private TipoTroca tipoTroca;

}
