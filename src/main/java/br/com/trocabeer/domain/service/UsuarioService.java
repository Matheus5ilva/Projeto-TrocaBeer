package br.com.trocabeer.domain.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.trocabeer.domain.model.Usuario;
import br.com.trocabeer.domain.repository.UsuarioRepository;

@Service
public class UsuarioService {

	private static Logger logger = LoggerFactory.getLogger(UsuarioService.class);

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Transactional
	public Usuario salvar(Usuario usuario) {
		try {
			return usuarioRepository.save(usuario);
		} catch (Exception e) {
			logger.error("Erro ao cadastrar usuário:" + e.getMessage());
		}
		return null;
	}

	@Transactional
	public void excluir(UUID usuarioId) {
		try {
			usuarioRepository.deleteById(usuarioId);
			usuarioRepository.flush();
		} catch (EmptyResultDataAccessException e) {
			logger.error("Não pode excluir este usuario" + e.getMessage());
			throw new RuntimeException("Não pode excluir este usuario");
		} catch (DataIntegrityViolationException e) {
			logger.error("Não pode excluir usuario" + e.getMessage());
			throw new RuntimeException(String.format("Não pode excluir usuario"));
		}
	}

	public Usuario buscarOuFalhar(UUID usuarioId) {
		return usuarioRepository.findById(usuarioId)
				.orElseThrow(() -> new RuntimeException("Não foi encontrado Usuario"));
	}
}
