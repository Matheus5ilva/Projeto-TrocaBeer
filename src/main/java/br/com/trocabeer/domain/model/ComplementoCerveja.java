package br.com.trocabeer.domain.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import br.com.trocabeer.domain.model.enums.TipoTroca;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class ComplementoCerveja implements Serializable {

	private static final long serialVersionUID = 7334156185559597071L;

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@EqualsAndHashCode.Include
	private UUID id;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "data_fabricacao")
	private Date dataFabricacao;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "data_validade")
	private Date dataValidade;

	@NotNull
	private BigDecimal estoque = BigDecimal.ZERO; // Defina um valor padrão como zero

	@Enumerated(EnumType.STRING)
	@NotNull
	private TipoTroca tipoTroca;

	@ManyToOne
	private Cerveja cerveja;
}
