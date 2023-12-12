package br.com.trocabeer.domain.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import br.com.trocabeer.domain.service.utils.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import br.com.trocabeer.domain.model.Cerveja;
import br.com.trocabeer.domain.model.ComplementoCerveja;
import br.com.trocabeer.domain.model.Troca;
import br.com.trocabeer.domain.model.enums.StatusTroca;
import br.com.trocabeer.domain.repository.TrocaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class TrocaService {

	private static Logger logger = LoggerFactory.getLogger(TrocaService.class);

	@Autowired
	private TrocaRepository trocaRepository;

	@Autowired
	private CervejaService cervejaService;

	@Autowired
	private ComplementoCervejaService complementoCervejaService;

	@Autowired
	private EmailService emailService;

	@Transactional
	public Troca salvar(Troca troca) {
		try {
			return trocaRepository.save(troca);
		} catch (Exception e) {
			logger.error("Erro ao cadastrar troca:" + e.getMessage());
		}
		return null;

	}

	@Transactional
	public Troca atualizaStatus(UUID trocaId, StatusTroca status) {

		Troca troca = this.buscarOuFalhar(trocaId);

		troca.setStatus(status);

		if (status.equals(StatusTroca.ACEITO)) {
			Cerveja cerveja = cervejaService.buscarCerveja(troca.getCerveja().getId());
			cerveja.setEstoqueMovimento(cerveja.getEstoqueMovimento().subtract(troca.getQuantidadeTroca()));

		}

		this.enviarEmail(troca);

		return trocaRepository.save(troca);
	}

	@Transactional
	public Troca buscarOuFalhar(UUID trocaId) {
		return trocaRepository.findById(trocaId).orElseThrow(() -> new EntityNotFoundException("Troca não encontrada"));
	}

	private void enviarEmail(Troca troca){
		try {
			logger.info("Iniciando o envio do e-mail para " + troca.getApreciador().getEmail());
			SimpleMailMessage emailInfoTroca = new SimpleMailMessage();
			emailInfoTroca.setFrom("Contato - TrocaBeer <matheus0verfl0w27@gmail.com>");
			emailInfoTroca.setTo(troca.getApreciador().getEmail());
			emailInfoTroca.setSubject("Resposta do Mestre Cervejeiro");
			if(StatusTroca.ACEITO.equals(troca.getStatus())){
				emailInfoTroca.setText("Olá " + troca.getApreciador().getNome()
						+ ",\nO mestre cervejeiro " + troca.getMestreCervejeiro().getNome() + " analisou sua proposta e foi " + troca.getStatus().getDescricao()
						+ ".\nEntre em contato pelo e-mail cadastrado ou pelo telefone cadastrado no sistema para se conhecerem." +
						"\n\n\nE-mail automatico, não responda.");
			}else if(StatusTroca.RECUSADO.equals(troca.getStatus())){
				emailInfoTroca.setText("Olá " + troca.getApreciador().getNome()
						+ ",\nO mestre cervejeiro " + troca.getMestreCervejeiro().getNome() + " analisou sua proposta e infelizmente " + troca.getStatus().getDescricao()
						+ ".\nNão desista, quem sabe outro mestre cervejeiro aceite sua proposta." +
						"\n\n\nE-mail automatico, não responda.");
			}

			emailService.sendEmail(emailInfoTroca);
		} catch (Exception e) {
			logger.error("Erro ao enviar e-mail: " + e.getMessage());
		}
	}

}
