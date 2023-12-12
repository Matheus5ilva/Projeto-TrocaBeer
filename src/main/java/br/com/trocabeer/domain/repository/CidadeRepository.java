package br.com.trocabeer.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.trocabeer.domain.model.Cidade;
import br.com.trocabeer.domain.model.Estado;

@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Long> {

  List<Cidade> findByEstado(Estado estado);

}
