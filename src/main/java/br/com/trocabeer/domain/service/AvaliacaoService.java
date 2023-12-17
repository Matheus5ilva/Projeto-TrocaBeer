package br.com.trocabeer.domain.service;

import br.com.trocabeer.domain.model.Avaliacao;
import br.com.trocabeer.domain.model.Cerveja;
import br.com.trocabeer.domain.model.Troca;
import br.com.trocabeer.domain.model.enums.StatusTroca;
import br.com.trocabeer.domain.repository.AvaliacaoRepository;
import br.com.trocabeer.domain.repository.TrocaRepository;
import br.com.trocabeer.domain.service.utils.EmailService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AvaliacaoService {

	private static Logger logger = LoggerFactory.getLogger(AvaliacaoService.class);

	@Autowired
	private TrocaRepository trocaRepository;

	@Autowired
	private CervejaService cervejaService;

	@Autowired
	private ComplementoCervejaService complementoCervejaService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private AvaliacaoRepository avaliacaoRepository;

	@Transactional
	public Avaliacao salvar(Avaliacao avaliacao) {
		try {
			return avaliacaoRepository.save(avaliacao);
		} catch (Exception e) {
			logger.error("Erro ao cadastrar troca:" + e.getMessage());
		}
		return null;

	}
	@Transactional
	public  Avaliacao transformaTrocaEmAvaliacao(Troca troca){
		Avaliacao avaliacao = new Avaliacao();
		avaliacao.setTroca(troca);
		avaliacao.setApreciador(troca.getApreciador());
		avaliacao.setMestreCervejeiro(troca.getMestreCervejeiro());
		return avaliacao;
	}
	@Transactional
	public Avaliacao buscarOuFalhar(UUID avaliacaoId) {
		return avaliacaoRepository.findById(avaliacaoId).orElseThrow(() -> new EntityNotFoundException("Avaliação não encontrada"));
	}

}
