package br.com.trocabeer.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.trocabeer.controller.dto.CervejaIndexDTO;
import br.com.trocabeer.controller.dto.PesquisaDTO;
import br.com.trocabeer.controller.dto.RelatorioIndexDTO;
import br.com.trocabeer.controller.dto.TrocaIndexDTO;
import br.com.trocabeer.domain.model.Cerveja;
import br.com.trocabeer.domain.model.Cidade;
import br.com.trocabeer.domain.model.Usuario;
import br.com.trocabeer.domain.model.enums.StatusTroca;
import br.com.trocabeer.domain.model.enums.TipoPessoa;
import br.com.trocabeer.domain.repository.CervejaRepository;
import br.com.trocabeer.domain.repository.TrocaRepository;
import br.com.trocabeer.domain.repository.UsuarioRepository;
import br.com.trocabeer.domain.service.TrocaService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/troca")
@ControllerAdvice
public class TrocaController {

	private static Logger logger = LoggerFactory.getLogger(TrocaController.class);

	@Autowired
	private CervejaRepository cervejaRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private TrocaService trocaService;

	@Autowired
	private TrocaRepository trocaRepository;

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

	private Usuario buscaUsuario(String email) {
		return usuarioRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Usuário ou senha não foi encontrado"));
	}

}
