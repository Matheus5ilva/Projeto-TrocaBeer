package br.com.trocabeer.controller;

import br.com.trocabeer.controller.dto.*;
import br.com.trocabeer.domain.model.*;
import br.com.trocabeer.domain.model.enums.StatusTroca;
import br.com.trocabeer.domain.model.enums.TipoPessoa;
import br.com.trocabeer.domain.repository.AvaliacaoRepository;
import br.com.trocabeer.domain.repository.CervejaRepository;
import br.com.trocabeer.domain.repository.TrocaRepository;
import br.com.trocabeer.domain.repository.UsuarioRepository;
import br.com.trocabeer.domain.service.AvaliacaoService;
import br.com.trocabeer.domain.service.TrocaService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/avaliacoes")
@ControllerAdvice
public class AvaliacaoController {

	private static Logger logger = LoggerFactory.getLogger(AvaliacaoController.class);

	@Autowired
	private CervejaRepository cervejaRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private AvaliacaoRepository avaliacaoRepository;

	@Autowired
	private AvaliacaoService avaliacaoService;

	@GetMapping
	public String listarAvaliacao(Model model, Authentication authentication) {
		Usuario usuario = buscaUsuario(authentication.getName());

		if (usuario.getTipoPessoa().equals(TipoPessoa.APRECIADOR)) {
			List<Avaliacao> avaliacoes = avaliacaoRepository.findByApreciador(usuario);
			model.addAttribute("usuario", usuario);
			model.addAttribute("avaliacoes", new AvaliacaoListaDTO().toListaAvaliacaoDTO(avaliacoes));

			return "gerencial/avaliacoes-apreciador";
		}


		List<Avaliacao> avaliacoes = avaliacaoRepository.findByMestreCervejeiroOrApreciador(usuario, usuario);
		model.addAttribute("usuario", usuario);
		model.addAttribute("avaliacoes", new AvaliacaoListaDTO().toListaAvaliacaoDTO(avaliacoes));

		return "gerencial/avaliacoes";

	}

	@GetMapping("/{avaliacaoId}")
	public String exibirFormularioAvaliacao(Model model, @PathVariable UUID avaliacaoId,
											  Authentication authentication) {

		logger.info("Entrando na rotina de avaliacao cerveja");

		Avaliacao avaliacao = avaliacaoService.buscarOuFalhar(avaliacaoId);

		model.addAttribute("usuario", this.buscaUsuario(authentication.getName()));
		model.addAttribute("avaliacaoDTO", new AvaliacaoDTO().toAvaliacaoDTO(avaliacao));
		return "gerencial/criar-avaliacao";
	}

	@PostMapping("/{avaliacaoId}")
	public String formularioAvaliacao(@Valid @ModelAttribute("avaliacaoDTO") AvaliacaoDTO avaliacaoDTO,
									  BindingResult bindingResult, Model model, @PathVariable UUID avaliacaoId, Authentication authentication) {

		Avaliacao avaliacaoAtual = avaliacaoService.buscarOuFalhar(avaliacaoId);
		avaliacaoAtual.setAvaliacao(avaliacaoDTO.getAvaliacao());
		model.addAttribute("avaliacao", avaliacaoService.salvar(avaliacaoAtual));

		return "redirect:/avaliacoes";
	}

	@GetMapping("/{avaliacaoId}/visualizar")
	public String exibirAvaliacaoEscrito(Model model, @PathVariable UUID avaliacaoId,
											Authentication authentication) {

		logger.info("Entrando na rotina de avaliacao cerveja");

		Avaliacao avaliacao = avaliacaoService.buscarOuFalhar(avaliacaoId);

		model.addAttribute("usuario", this.buscaUsuario(authentication.getName()));
		model.addAttribute("avaliacaoDTO", new AvaliacaoDTO().toAvaliacaoDTO(avaliacao));
		return "gerencial/avaliacao";
	}

	private Usuario buscaUsuario(String email) {
		return usuarioRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Usuário ou senha não foi encontrado"));
	}

}
