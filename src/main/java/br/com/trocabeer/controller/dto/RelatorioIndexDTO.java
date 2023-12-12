package br.com.trocabeer.controller.dto;

import lombok.Data;

@Data
public class RelatorioIndexDTO {

	
	private Integer numeroTroca;
	private Integer numeroCerveja;
	private Integer numeroTrocaPendente;

	public RelatorioIndexDTO(Integer numeroTroca, Integer numeroCerveja, Integer numeroTrocaPendente) {
		this.setNumeroTroca(numeroTroca);
		this.setNumeroCerveja(numeroCerveja);
		this.setNumeroTrocaPendente(numeroTrocaPendente);
	}
}
