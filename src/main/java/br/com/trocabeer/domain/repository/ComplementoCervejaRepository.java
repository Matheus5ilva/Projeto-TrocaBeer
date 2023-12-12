package br.com.trocabeer.domain.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.trocabeer.domain.model.Cerveja;
import br.com.trocabeer.domain.model.ComplementoCerveja;

@Repository
public interface ComplementoCervejaRepository extends JpaRepository<ComplementoCerveja, UUID> {

	List<ComplementoCerveja> findByCerveja(Cerveja cerveja);


}
