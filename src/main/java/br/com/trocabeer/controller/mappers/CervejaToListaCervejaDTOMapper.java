package br.com.trocabeer.controller.mappers;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Component;

import br.com.trocabeer.controller.dto.CervejaComplementoCadastroDTO;
import br.com.trocabeer.controller.dto.CervejaDTO;
import br.com.trocabeer.controller.dto.CervejaIndexDTO;
import br.com.trocabeer.controller.dto.CervejaListaDTO;
import br.com.trocabeer.controller.dto.ListaCervejaIndexDTO;
import br.com.trocabeer.domain.model.Cerveja;
import br.com.trocabeer.domain.model.ComplementoCerveja;
import br.com.trocabeer.domain.model.Usuario;

@Component
public class CervejaToListaCervejaDTOMapper {

	public CervejaListaDTO map(Cerveja cerveja) {

		CervejaListaDTO listaCerveja = new CervejaListaDTO().toCervejaDTO(cerveja);		
		
		return listaCerveja;

	}

	/*
	 * public CervejaListaDTO map(Cerveja cerveja) { CervejaListaDTO listaCerveja =
	 * new CervejaListaDTO(); listaCerveja.setId(cerveja.getId());
	 * listaCerveja.setEstilo(cerveja.getEstilo());
	 * listaCerveja.setNome(cerveja.getNome());
	 * listaCerveja.setUsuario(cerveja.getUsuario());
	 * listaCerveja.setAtivo(cerveja.getAtivo());
	 * 
	 * ComplementoCerveja ultimoComplemento = encontrarUltimoComplemento(cerveja);
	 * if (ultimoComplemento != null) {
	 * listaCerveja.setDataFabricacao(ultimoComplemento.getDataFabricacao());
	 * listaCerveja.setDataValidade(ultimoComplemento.getDataValidade());
	 * listaCerveja.setTipoTroca(ultimoComplemento.getTipoTroca());
	 * listaCerveja.setEstoque(ultimoComplemento.getEstoque()); }
	 * 
	 * return listaCerveja; }
	 */
	public ListaCervejaIndexDTO mapIndex(Cerveja cerveja) {
		ListaCervejaIndexDTO listaCerveja = new ListaCervejaIndexDTO();
		listaCerveja.setId(cerveja.getId());
		listaCerveja.setEstilo(cerveja.getEstilo());
		listaCerveja.setNome(cerveja.getNome());
		listaCerveja.setUsuario(cerveja.getUsuario());
		listaCerveja.setCidade(cerveja.getUsuario().getEndereco().getCidade());

		ComplementoCerveja ultimoComplemento = encontrarUltimoComplemento(cerveja);
		if (ultimoComplemento != null) {
			listaCerveja.setTipoTroca(ultimoComplemento.getTipoTroca());
		}

		return listaCerveja;
	}

	public CervejaIndexDTO mapCerveja(Cerveja cerveja) {

		CervejaIndexDTO cervejaDTO = new CervejaIndexDTO();

		//cervejaDTO.setId(cerveja.getId());
		///cervejaDTO.setCerveja(cerveja);

		ComplementoCerveja ultimoComplemento = encontrarUltimoComplemento(cerveja);
		if (ultimoComplemento != null) {
			cervejaDTO.setComplemento(ultimoComplemento);
		}

		return cervejaDTO;

	}

	public CervejaDTO mapInputCerveja(Cerveja cerveja) {

		CervejaDTO cervejaDTO = new CervejaDTO();

		cervejaDTO.setId(cerveja.getId());
		cervejaDTO.setNome(cerveja.getNome());
		cervejaDTO.setEstilo(cerveja.getEstilo());
		cervejaDTO.setAtivo(cerveja.getAtivo());
		cervejaDTO.setUsuario(cerveja.getUsuario());
		cervejaDTO.setDescricaoTecnica(cerveja.getDescricaoTecnica());
		cervejaDTO.setComidaHarmonizada(cerveja.getComidaHarmonizada());
		cervejaDTO.setCopoSugerido(cerveja.getCopoSugerido());

		ComplementoCerveja ultimoComplemento = encontrarUltimoComplemento(cerveja);
		if (ultimoComplemento != null) {
			cervejaDTO.setTipoTroca(ultimoComplemento.getTipoTroca());
			cervejaDTO.setEstoque(ultimoComplemento.getEstoque());
			cervejaDTO.setDataFabricacao(ultimoComplemento.getDataFabricacao());
			cervejaDTO.setDataValidade(ultimoComplemento.getDataValidade());
		}

		return cervejaDTO;

	}

	private ComplementoCerveja encontrarUltimoComplemento(Cerveja cerveja) {
		List<ComplementoCerveja> complementos = cerveja.getComplementos();
		if (!complementos.isEmpty()) {
			complementos.sort(Comparator.comparing(ComplementoCerveja::getId).reversed());

			return complementos.get(0);
		}

		return null;
	}
}
