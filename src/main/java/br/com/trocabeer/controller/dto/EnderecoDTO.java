package br.com.trocabeer.controller.dto;

import br.com.trocabeer.domain.model.Cidade;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EnderecoDTO {

    @NotBlank(message = "O CEP é obrigatório.")
    private String cep;

    @NotBlank(message = "O logradouro é obrigatório.")
    private String logradouro;

    private String numero;

    private String complemento;

    @NotBlank(message = "O bairro é obrigatório.")
    private String bairro;

    @NotNull(message = "A cidade é obrigatória.")
    private Cidade cidade;
}
