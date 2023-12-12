package br.com.trocabeer.controller;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.trocabeer.controller.dto.TrocaListaDTO;
import br.com.trocabeer.domain.model.Troca;
import br.com.trocabeer.domain.model.Usuario;
import br.com.trocabeer.domain.model.enums.StatusTroca;
import br.com.trocabeer.domain.model.enums.TipoPessoa;
import br.com.trocabeer.domain.repository.TrocaRepository;
import br.com.trocabeer.domain.repository.UsuarioRepository;
import br.com.trocabeer.domain.service.CervejaService;
import br.com.trocabeer.domain.service.TrocaService;

@Controller
@RequestMapping("/trocas")
public class TrocaGerencialController {

	private static Logger logger = LoggerFactory.getLogger(TrocaGerencialController.class);
	
	@Autowired
	private CervejaService cervejaService;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private TrocaService trocaService;

	@Autowired
	private TrocaRepository trocaRepository;

	@GetMapping
	public String listarTrocas(Model model, Authentication authentication) {
		Usuario usuario = buscaUsuario(authentication.getName());

		if (usuario.getTipoPessoa().equals(TipoPessoa.APRECIADOR)) {
			List<Troca> trocas = trocaRepository.findByApreciador(usuario);
			model.addAttribute("usuario", usuario);
			model.addAttribute("trocas", new TrocaListaDTO().toListaTrocaDTO(trocas));

			return "gerencial/trocas-cervejeiro";
		}

		List<Troca> trocas = trocaRepository.findByMestreCervejeiroOrApreciador(usuario, usuario);

		model.addAttribute("usuario", usuario);
		model.addAttribute("trocas", new TrocaListaDTO().toListaTrocaDTO(trocas));

		return "gerencial/trocas";
	}

	@GetMapping("/aceita/{trocaId}")
	public String aceitaTrocaCerveja(@PathVariable UUID trocaId) {
		logger.debug("Estou dentro da rotina de aceitaTrocaCerveja");
		trocaService.atualizaStatus(trocaId, StatusTroca.ACEITO);
		return "redirect:/trocas";
	}

	@GetMapping("/recusado/{trocaId}")
	public String rejeitaTrocaCerveja(@PathVariable UUID trocaId) {
		logger.debug("Estou dentro da rotina de rejeitaTrocaCerveja");
		trocaService.atualizaStatus(trocaId, StatusTroca.RECUSADO);
		return "redirect:/trocas";
	}

	private Usuario buscaUsuario(String email) {
		return usuarioRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Usuário ou senha não foi encontrado"));
	}

}
