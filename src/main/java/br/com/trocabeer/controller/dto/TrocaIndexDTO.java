package br.com.trocabeer.controller.dto;

import java.math.BigDecimal;

import org.modelmapper.ModelMapper;

import br.com.trocabeer.domain.model.Cerveja;
import br.com.trocabeer.domain.model.ComplementoCerveja;
import br.com.trocabeer.domain.model.Troca;
import br.com.trocabeer.domain.model.Usuario;
import br.com.trocabeer.domain.model.enums.StatusTroca;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class TrocaIndexDTO {

	@NotNull(message = "Cerveja é obrigatória")
	private Cerveja cerveja;

	@NotNull(message = "Complemento é obrigatório")
	private ComplementoCerveja complemento;

	@Positive(message = "A proposta tem que ser maior que 0")
	private BigDecimal quantidadeProposta = new BigDecimal(0.0);

	public Troca toTroca(Usuario usuario) {
		ModelMapper modelMapper = new ModelMapper();

		Troca troca = modelMapper.map(this, Troca.class);

		troca.setApreciador(usuario);
		troca.setMestreCervejeiro(this.getCerveja().getUsuario());
		troca.setStatus(StatusTroca.PENDENTE);

		return troca;
	}

}
