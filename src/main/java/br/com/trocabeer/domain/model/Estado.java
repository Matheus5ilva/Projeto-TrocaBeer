package br.com.trocabeer.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Estado {

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@NotBlank(message = "O nome do estado é obrigatório.")
	@Column(nullable = false)
	private String nome;

	@NotBlank(message = "A sigla do estado é obrigatória.")
	@Size(min = 2, max = 2, message = "A sigla do estado deve ter 2 caracteres.")
	@Column(nullable = false)
	private String sigla;
}
