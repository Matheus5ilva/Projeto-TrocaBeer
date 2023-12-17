package br.com.trocabeer.domain.repository;

import br.com.trocabeer.domain.model.Avaliacao;
import br.com.trocabeer.domain.model.Troca;
import br.com.trocabeer.domain.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, UUID> {

	List<Avaliacao> findByApreciador(Usuario apreciador);
	List<Avaliacao> findByMestreCervejeiroOrApreciador(Usuario mestreCervejeiro, Usuario apreciador);
}
