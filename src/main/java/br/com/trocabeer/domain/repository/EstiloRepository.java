package br.com.trocabeer.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.trocabeer.domain.model.Estilo;

@Repository
public interface EstiloRepository extends JpaRepository<Estilo, Long> {
}