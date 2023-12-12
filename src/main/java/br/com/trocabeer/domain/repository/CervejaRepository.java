package br.com.trocabeer.domain.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.trocabeer.domain.model.Cerveja;
import br.com.trocabeer.domain.model.Cidade;
import br.com.trocabeer.domain.model.Usuario;
import br.com.trocabeer.domain.model.Estilo;


@Repository
public interface CervejaRepository extends JpaRepository<Cerveja, UUID> {

	List<Cerveja> findByUsuario(Usuario usuario);
	
	List<Cerveja> findByUsuario_Endereco_CidadeAndUsuario_AtivoAndUsuarioNotAndAtivoAndEstoqueMovimentoGreaterThan(Cidade cidade, Boolean usuarioAtivo, Usuario usuario,Boolean ativo, BigDecimal estoqueMovimento);

	Integer countByUsuario(Usuario usuario);
	
	List<Cerveja> findByEstilo(Estilo estilo);

}
