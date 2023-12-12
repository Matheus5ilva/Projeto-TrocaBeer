package br.com.trocabeer.domain.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Embeddable
@Data
public class Endereco {

  
    @NotBlank(message = "O CEP é obrigatório.")
    private String cep;

    @NotBlank(message = "O logradouro é obrigatório.")
    private String logradouro;

    @NotBlank(message = "O número é obrigatório.")
    private String numero;

    private String complemento;

    @NotBlank(message = "O bairro é obrigatório.")
    private String bairro;

    @ManyToOne
    @JoinColumn(name = "endereco_cidade_id")
    @NotNull(message = "A cidade é obrigatória.")
    private Cidade cidade;
}