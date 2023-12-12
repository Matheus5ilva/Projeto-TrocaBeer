package br.com.trocabeer.domain.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import br.com.trocabeer.TrocabeerApplication;
import br.com.trocabeer.domain.model.Cerveja;
import br.com.trocabeer.domain.model.ComplementoCerveja;
import br.com.trocabeer.domain.repository.ComplementoCervejaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class ComplementoCervejaService {

	private static Logger logger = LoggerFactory.getLogger(ComplementoCervejaService.class);

	@Autowired
	private ComplementoCervejaRepository complementoCervejaRepository;

	@Autowired
	private CervejaService cervejaService;

	@Transactional
	public ComplementoCerveja salvar(ComplementoCerveja complementoCerveja) {
		try {
			logger.error("Iniciando o cadastro do complemento cerveja.");
			return complementoCervejaRepository.save(complementoCerveja);
		} catch (Exception e) {
			logger.error("Erro ao cadastrar complemento cerveja:" + e.getMessage());
		}
		return null;
	}

	@Transactional
	public void excluir(UUID complementoCervejaId) {
		try {
			complementoCervejaRepository.deleteById(complementoCervejaId);
			complementoCervejaRepository.flush();
		} catch (EmptyResultDataAccessException e) {
			logger.error("Complemento não encontrado " + e.getMessage());
			throw new EntityNotFoundException("Complemento não encontrado");
		} catch (DataIntegrityViolationException e) {
			logger.error(
					"Não pode excluir complemento devido a uma violação de integridade de dados." + e.getMessage());
			throw new RuntimeException("Não pode excluir complemento devido a uma violação de integridade de dados.");
		}
	}

	/*
	 * @Transactional public ComplementoCerveja getUltimoComplemento(Cerveja
	 * cerveja) { List<ComplementoCerveja> complementos =
	 * complementoCervejaRepository.findByCerveja(cerveja); if
	 * (!complementos.isEmpty()) { return Collections.max(complementos,
	 * Comparator.comparing(ComplementoCerveja::getId)); } return null; }
	 */
	
	@Transactional
	public ComplementoCerveja buscarOuFalhar(UUID complementoCervejaId) {
		return complementoCervejaRepository.findById(complementoCervejaId)
				.orElseThrow(() -> new EntityNotFoundException("Complemento não encontrado"));
	}
}
