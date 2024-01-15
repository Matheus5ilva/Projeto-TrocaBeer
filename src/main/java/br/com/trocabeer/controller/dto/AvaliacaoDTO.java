package br.com.trocabeer.controller.dto;

import br.com.trocabeer.domain.model.Avaliacao;
import br.com.trocabeer.domain.model.Cerveja;
import br.com.trocabeer.domain.model.Troca;
import br.com.trocabeer.domain.model.Usuario;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.util.Date;
import java.util.UUID;

@Data
public class AvaliacaoDTO {

    private UUID id;
    private String nomeCerveja;
    private String apreciador;
    private String mestreCervejeiro;
    private Date dataSolicitado;
    private String avaliacao;

    public AvaliacaoDTO toAvaliacaoDTO(Avaliacao avaliacao) {

        AvaliacaoDTO avaliacaoDTO = new AvaliacaoDTO();
        avaliacaoDTO.setId(avaliacao.getAvaliacaoId());
        avaliacaoDTO.setNomeCerveja(avaliacao.getTroca().getCerveja().getNome());
        avaliacaoDTO.setApreciador(avaliacao.getApreciador().getNome());
        avaliacaoDTO.setMestreCervejeiro(avaliacao.getMestreCervejeiro().getNome());
        avaliacaoDTO.setDataSolicitado(avaliacao.getTroca().getDataSolicitado());
        if(avaliacao.getAvaliacaoId() == null || avaliacao.getAvaliacao().isEmpty()){
            avaliacaoDTO.setAvaliacao("");
        }else{
            avaliacaoDTO.setAvaliacao(avaliacao.getAvaliacao());
        }


        return avaliacaoDTO;
    }
}
