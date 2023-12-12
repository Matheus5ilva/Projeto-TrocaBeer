package br.com.trocabeer.domain.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import br.com.trocabeer.domain.model.enums.StatusTroca;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Troca implements Serializable {

	private static final long serialVersionUID = 6017089396038913300L;

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@EqualsAndHashCode.Include
	private UUID id;
	
	@CreationTimestamp
	@Column(nullable = false, columnDefinition = "timestamp")
	private Date dataSolicitado;


	@CreationTimestamp
	@Column(nullable = false, columnDefinition = "timestamp")
	private OffsetDateTime dataCadastro;

	@UpdateTimestamp
	@Column(nullable = false, columnDefinition = "timestamp")
	private OffsetDateTime dataAtualizacao;

	@ManyToOne
	private Usuario apreciador;

	@ManyToOne
	private Usuario mestreCervejeiro;

	@ManyToOne
	private Cerveja cerveja;

	@ManyToOne
	private ComplementoCerveja complementoCerveja;

	private BigDecimal quantidadeTroca;

	@Enumerated(EnumType.STRING)
	private StatusTroca status;
}
