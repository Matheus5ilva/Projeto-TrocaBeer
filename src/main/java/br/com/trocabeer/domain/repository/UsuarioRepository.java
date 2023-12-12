package br.com.trocabeer.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.trocabeer.domain.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
	Optional<Usuario> findByEmail(String email);

	Optional<Usuario> findByTokenAlterarSenha(String tokenAlterarSenha);
	
	List<Usuario> findByNomeContainingIgnoreCase(String nome);
}
