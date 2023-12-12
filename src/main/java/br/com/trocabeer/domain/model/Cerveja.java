package br.com.trocabeer.domain.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Cerveja implements Serializable {

	private static final long serialVersionUID = 6017089396038913300L;

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@EqualsAndHashCode.Include
	private UUID id;

	@NotNull
	@Column(nullable = false)
	@Size(min = 1, max = 255)
	private String nome;

	@ManyToOne
	@JoinColumn(name = "estilo_id")
	private Estilo estilo;

	@Embedded
	private InformacaoTecnica descricaoTecnica;

	@CreationTimestamp
	@Column(nullable = false, columnDefinition = "timestamp")
	private OffsetDateTime dataCadastro;

	@UpdateTimestamp
	@Column(nullable = false, columnDefinition = "timestamp")
	private OffsetDateTime dataAtualizacao;

	@NotNull
	@Column(nullable = false)
	private Boolean ativo = true; // Defina um valor padrão como true

	@ElementCollection
	private List<String> copoSugerido = new ArrayList<>(); // Inicialize a lista

	@ElementCollection
	private List<String> comidaHarmonizada = new ArrayList<>(); // Inicialize a lista

	@OneToMany(mappedBy = "cerveja", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ComplementoCerveja> complementos = new ArrayList<>(); // Inicialize a lista

	@ManyToOne
	private Usuario usuario;
	
	@NotNull
	private BigDecimal estoqueMovimento = new BigDecimal(0.0);
	
	@Transient
	private Boolean possuiTroca = false;
}
