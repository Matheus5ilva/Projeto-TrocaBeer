package br.com.trocabeer.controller.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;

import br.com.trocabeer.domain.model.Cerveja;
import br.com.trocabeer.domain.model.ComplementoCerveja;
import br.com.trocabeer.domain.model.Estilo;
import br.com.trocabeer.domain.model.enums.TipoTroca;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CervejaComplementoCadastroDTO {

	@NotNull(message = "A data de fabricação é obrigatório.")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dataFabricacao;

	@NotNull(message = "A data de validade é obrigatório.")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dataValidade;

	@Positive(message="O estoque tem que ser maior que 0")
	private BigDecimal estoque = BigDecimal.ZERO; // Defina um valor padrão como zero
	
	private TipoTroca tipoTroca;

	private Cerveja cerveja = new Cerveja();
	
	public ComplementoCerveja toComplemento(Cerveja cerveja) {

		cerveja.setEstoqueMovimento(cerveja.getEstoqueMovimento().add(this.getEstoque()));
		this.setCerveja(cerveja);
		
		ModelMapper modelMapper = new ModelMapper();

		return modelMapper.map(this, ComplementoCerveja.class);
		
	}
}
