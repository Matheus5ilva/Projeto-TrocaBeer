package br.com.trocabeer.domain.model;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import br.com.trocabeer.domain.model.enums.TipoPessoa;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Pessoa implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	private UUID id;

	@NotBlank(message = "O nome é obrigatório.")
	@Column(nullable = false)
	private String nome;

	@Embedded
	@NotNull(message = "O endereço é obrigatório.")
	private Endereco endereco;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@NotNull(message = "O tipo de pessoa é obrigatório.")
	private TipoPessoa tipoPessoa;

	@NotBlank(message = "O telefone é obrigatório.")
	@Column(nullable = false)
	private String telefone;

	@Column
	private Boolean ativo = true;

	@CreationTimestamp
	@Column(nullable = false, columnDefinition = "timestamp")
	private OffsetDateTime dataCadastro;

	@UpdateTimestamp
	@Column(nullable = false, columnDefinition = "timestamp")
	private OffsetDateTime dataAtualizacao;

	@Column
	private Boolean premium = false;
}
