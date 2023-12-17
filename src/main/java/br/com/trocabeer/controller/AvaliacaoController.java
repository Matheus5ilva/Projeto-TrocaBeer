package br.com.trocabeer.controller;

import br.com.trocabeer.controller.dto.*;
import br.com.trocabeer.domain.model.*;
import br.com.trocabeer.domain.model.enums.StatusTroca;
import br.com.trocabeer.domain.model.enums.TipoPessoa;
import br.com.trocabeer.domain.repository.AvaliacaoRepository;
import br.com.trocabeer.domain.repository.CervejaRepository;
import br.com.trocabeer.domain.repository.TrocaRepository;
import br.com.trocabeer.domain.repository.UsuarioRepository;
import br.com.trocabeer.domain.service.TrocaService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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


	/**
	@PostMapping
	public String troca(@Valid @ModelAttribute("trocaDTO") TrocaIndexDTO trocaDto, BindingResult bindingResult,
			RedirectAttributes attributes, Model model, Authentication authentication) {

		String email = authentication.getName();

		Usuario usuario = usuarioRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Usuário ou senha não foi encontrado"));
		Cidade cidadeDoUsuario = usuario.getEndereco().getCidade();

		List<Cerveja> cervejasDisponiveis = cervejaRepository
				.findByUsuario_Endereco_CidadeAndUsuario_AtivoAndUsuarioNotAndAtivoAndEstoqueMovimentoGreaterThan(cidadeDoUsuario,
						true, usuario, true, BigDecimal.ZERO);
		if (bindingResult.hasErrors()
				|| trocaDto.getQuantidadeProposta().compareTo(trocaDto.getCerveja().getEstoqueMovimento()) > 0) {

			if (trocaDto.getQuantidadeProposta().compareTo(trocaDto.getCerveja().getEstoqueMovimento()) > 0) {
				logger.debug("A quantidade proposta excede o nível de disponibilidade em estoque.");
				bindingResult.rejectValue("quantidadeProposta", "quantidadeProposta",
						"A quantidade proposta excede o nível de disponibilidade em estoque.");
			}
			trocaDto.setQuantidadeProposta(BigDecimal.ZERO);
			trocaDto.getCerveja().setId(trocaDto.getCerveja().getId());
			if (TipoPessoa.MESTRE_CERVEJEIRO.equals(usuario.getTipoPessoa())) {
				Integer numeroCerveja = cervejaRepository.countByUsuario(usuario);
				Integer numeroTroca = trocaRepository.countByMestreCervejeiro(usuario);
				Integer numeroTrocaPendente = trocaRepository.countByMestreCervejeiroAndStatus(usuario,
						StatusTroca.PENDENTE);
				model.addAttribute("relatorioDTO",
						new RelatorioIndexDTO(numeroTroca, numeroCerveja, numeroTrocaPendente));
			} else {
				model.addAttribute("relatorioDTO", new RelatorioIndexDTO(0, 0, 0));
			}
			model.addAttribute("pesquisaDTO", new PesquisaDTO());
			model.addAttribute("trocaDTO", trocaDto);
			model.addAttribute("cervejas", new CervejaIndexDTO().toListaCervejaDTO(cervejasDisponiveis));
			model.addAttribute("endereco", cidadeDoUsuario.getNome() + " - " + cidadeDoUsuario.getEstado().getSigla());
			model.addAttribute("usuario", usuario);
			return "gerencial/index";
		}

		trocaService.salvar(trocaDto.toTroca(usuario));

		return "redirect:/trocas";
	}
	*/
	private Usuario buscaUsuario(String email) {
		return usuarioRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Usuário ou senha não foi encontrado"));
	}

}
