package br.com.trocabeer.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.trocabeer.domain.model.Estado;

@Repository
public interface EstadoRepository extends JpaRepository<Estado, Long> {
}