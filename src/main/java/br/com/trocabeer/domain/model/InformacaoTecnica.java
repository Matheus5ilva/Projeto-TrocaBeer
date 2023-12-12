package br.com.trocabeer.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class InformacaoTecnica {

	@Column(name = "informacao_volume")
	private String volume;

	@Column(name = "informacao_coloracao")
	private String coloracao;

	@Column(name = "informacao_amargor")
	private String amargor;

	@Column(name = "informacao_teor_alcoolico")
	private String teorAlcoolico;

	@Column(name = "informacao_temperatura_ideal")
	private String temperaturaIdeal;
}
