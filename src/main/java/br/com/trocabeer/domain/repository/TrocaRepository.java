package br.com.trocabeer.domain.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.trocabeer.domain.model.Cerveja;
import br.com.trocabeer.domain.model.Troca;
import br.com.trocabeer.domain.model.Usuario;
import br.com.trocabeer.domain.model.enums.StatusTroca;

@Repository
public interface TrocaRepository extends JpaRepository<Troca, UUID> {

	List<Troca> findByMestreCervejeiroOrApreciador(Usuario mestreCervejeiro, Usuario apreciador);
	
	Integer countByMestreCervejeiro(Usuario mestreCervejeiro);
	
	Integer countByMestreCervejeiroAndStatus(Usuario mestreCervejeiro, StatusTroca status);
	
	List<Troca> findByApreciador(Usuario apreciador);
	
	List<Troca> findByCerveja(Cerveja cerveja);

}
