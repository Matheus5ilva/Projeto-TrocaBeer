package br.com.trocabeer.domain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.trocabeer.TrocabeerApplication;
import br.com.trocabeer.domain.model.Cerveja;
import br.com.trocabeer.domain.model.Usuario;
import br.com.trocabeer.domain.repository.CervejaRepository;
import br.com.trocabeer.domain.repository.TrocaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class CervejaService {

	private static Logger logger = LoggerFactory.getLogger(CervejaService.class);

	@Autowired
	private CervejaRepository cervejaRepository;

	@Autowired
	private TrocaRepository trocaRepository;

	@Transactional
	public Cerveja salvar(Cerveja cerveja) {
		try {
			logger.info("Cadastrando cerveja");
			return cervejaRepository.save(cerveja);
		} catch (Exception e) {
			logger.error("Erro ao cadastrar cerveja:" + e.getMessage());
		}
		return null;
	}

	@Transactional
	public void excluir(UUID cervejaId) {
		Cerveja cerveja = buscarOuFalhar(cervejaId);
		try {
			if (cerveja != null) {
				logger.info("Deletando cerveja");
				cervejaRepository.delete(cerveja);
				cervejaRepository.flush();
			} else {
				throw new EntityNotFoundException("Cerveja não encontrada");
			}
		} catch (EntityNotFoundException e) {
			logger.error("Cerveja de id: " + cervejaId + " não encontrada." + e.getMessage());
		}

	}

	//Metodo na listagem de cerveja da tela Cervejas
	@Transactional
	public List<Cerveja> listarCervejas(Usuario usuario) {
		List<Cerveja> cervejas = new ArrayList<Cerveja>();

		for (Cerveja cerveja : cervejaRepository.findByUsuario(usuario)) {
			logger.info("Cerveja por usuario");
			if (!trocaRepository.findByCerveja(cerveja).isEmpty()) {
				logger.debug("Essa cerveja de id: " + cerveja.getId() + "Possui troca");
				cerveja.setPossuiTroca(true);
			}
			cervejas.add(cerveja);
		}

		return cervejas;
	}

	@Transactional
	public Cerveja buscarCerveja(UUID cervejaId) {
		try {
			logger.info("Buscando cerveja de id: " + cervejaId);
			return buscarOuFalhar(cervejaId);
		} catch (EntityNotFoundException e) {
			logger.error("Cerveja de id: " + cervejaId + " não encontrada" + e.getMessage());
		}
		return null;

	}

	@Transactional
	public Cerveja buscarOuFalhar(UUID cervejaId) {
		return cervejaRepository.findById(cervejaId)
				.orElseThrow(() -> new EntityNotFoundException("Cerveja não encontrada"));
	}
}
