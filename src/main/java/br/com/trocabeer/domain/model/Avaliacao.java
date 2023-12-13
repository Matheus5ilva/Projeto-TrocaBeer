package br.com.trocabeer.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Avaliacao implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @EqualsAndHashCode.Include
    private UUID avaliacaoId;
    @ManyToOne
    private Usuario mestreCervejeiro;
    @ManyToOne
    private Usuario apreciador;
    @ManyToOne
    private Troca troca;
    @Column
    @Size(min = 1, max = 5000)
    private String avaliacao;
    @CreationTimestamp
    @Column(nullable = false, columnDefinition = "timestamp")
    private OffsetDateTime dataCadastro;

}
