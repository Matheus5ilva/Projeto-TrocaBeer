package br.com.trocabeer.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
import br.com.trocabeer.domain.repository.EstiloRepository;
import br.com.trocabeer.domain.repository.TrocaRepository;
import br.com.trocabeer.domain.repository.UsuarioRepository;
import jakarta.validation.Valid;

@Controller
@ControllerAdvice
public class GerencialController {

	private static Logger logger = LoggerFactory.getLogger(GerencialController.class);

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private EstiloRepository estiloRepository;

	@Autowired
	private CervejaRepository cervejaRepository;
	
	@Autowired
	private TrocaRepository trocaRepository;

	@GetMapping("/home")
	public String index(Model model, Authentication authentication ) {
		logger.info("Entrando na tela inicial: index");
		String email = authentication.getName();

		Usuario usuario = usuarioRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Usuário ou senha não foi encontrado"));

		Cidade cidadeDoUsuario = usuario.getEndereco().getCidade();

		List<Cerveja> cervejasDisponiveis = cervejaRepository.findByUsuario_Endereco_CidadeAndUsuario_AtivoAndUsuarioNotAndAtivoAndEstoqueMovimentoGreaterThan(cidadeDoUsuario, true, usuario,true, BigDecimal.ZERO);

		if(TipoPessoa.MESTRE_CERVEJEIRO.equals(usuario.getTipoPessoa())) {
			Integer numeroCerveja = cervejaRepository.countByUsuario(usuario);
			Integer numeroTroca = trocaRepository.countByMestreCervejeiro(usuario);
			Integer numeroTrocaPendente = trocaRepository.countByMestreCervejeiroAndStatus(usuario, StatusTroca.PENDENTE);
			model.addAttribute("relatorioDTO", new RelatorioIndexDTO(numeroTroca, numeroCerveja, numeroTrocaPendente));
		}else {
			model.addAttribute("relatorioDTO", new RelatorioIndexDTO(0, 0, 0));
		}
		
		
		
		model.addAttribute("estilos", estiloRepository.findAll());
		model.addAttribute("pesquisaDTO", new PesquisaDTO());
		model.addAttribute("trocaDTO", new TrocaIndexDTO());
		model.addAttribute("cervejas", new CervejaIndexDTO().toListaCervejaDTO(cervejasDisponiveis));
		model.addAttribute("endereco", cidadeDoUsuario.getNome() + " - " + cidadeDoUsuario.getEstado().getSigla());
		model.addAttribute("usuario", usuario);
		return "gerencial/index";
	}

	@PostMapping("/pesquisa")
	public String procuraCerveja(@Valid @ModelAttribute("pesquisaDTO") PesquisaDTO pesquisaDTO, Model model,
			Authentication authentication, BindingResult bindingResult) {

		logger.info("Entrando na rotina de pesquisa: procuraCerveja");
		String email = authentication.getName();

		Usuario usuario = usuarioRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Usuário ou senha não foi encontrado"));

		Cidade cidadeDoUsuario = usuario.getEndereco().getCidade();

		List<Cerveja> cervejasDisponiveis = new ArrayList<Cerveja>();

		if (pesquisaDTO.getEstilo().getId() == null
				&& (pesquisaDTO.getNome().isBlank() || pesquisaDTO.getNome().isEmpty())) {
			return "redirect:/home";
		}
		if (pesquisaDTO.getEstilo().getId() != null
				|| (!pesquisaDTO.getNome().isBlank() || !pesquisaDTO.getNome().isEmpty())) {
			List<Cerveja> cervejasEstilo = new ArrayList<Cerveja>();

			List<Usuario> cervejeiro = new ArrayList<Usuario>();

			if (pesquisaDTO.getEstilo().getId() != null) {
				logger.debug("Entrando procurando estilo da pesquisa de id: " + pesquisaDTO.getEstilo().getId());
				cervejasEstilo = cervejaRepository.findByEstilo(pesquisaDTO.getEstilo());
				cervejasDisponiveis = cervejasEstilo;
			} else if (!pesquisaDTO.getNome().isBlank() || !pesquisaDTO.getNome().isEmpty()) {
				logger.debug("Entrando procurando mestre cervejeiro da pesquisa de nome: " + pesquisaDTO.getNome());
				cervejeiro = usuarioRepository.findByNomeContainingIgnoreCase(pesquisaDTO.getNome());
				for (Usuario mestre : cervejeiro) {
					for (Cerveja cerveja : cervejaRepository.findByUsuario(mestre)) {
						cervejasDisponiveis.add(cerveja);
					}
				}
			}

			if (bindingResult.hasErrors() || (cervejasEstilo.isEmpty() && cervejeiro.isEmpty())) {
				logger.debug("Não foi achado cerveja do estiloId: " + pesquisaDTO.getEstilo().getId()
						+ " e nem mestre cervejeiro: " + pesquisaDTO.getNome());
				bindingResult.rejectValue("estilo", "nome", "Ops! Não foi encontrado cervejas disponíveis.");
				
				if(TipoPessoa.MESTRE_CERVEJEIRO.equals(usuario.getTipoPessoa())) {
					Integer numeroCerveja = cervejaRepository.countByUsuario(usuario);
					Integer numeroTroca = trocaRepository.countByMestreCervejeiro(usuario);
					Integer numeroTrocaPendente = trocaRepository.countByMestreCervejeiroAndStatus(usuario, StatusTroca.PENDENTE);
					model.addAttribute("relatorioDTO", new RelatorioIndexDTO(numeroTroca, numeroCerveja, numeroTrocaPendente));
				}else {
					model.addAttribute("relatorioDTO", new RelatorioIndexDTO(0, 0, 0));
				}
				
				model.addAttribute("estilos", estiloRepository.findAll());
				model.addAttribute("pesquisaDTO", pesquisaDTO);
				model.addAttribute("trocaDTO", new TrocaIndexDTO());
				model.addAttribute("cervejas", new CervejaIndexDTO().toListaCervejaDTO(cervejasDisponiveis));
				model.addAttribute("endereco",
						cidadeDoUsuario.getNome() + " - " + cidadeDoUsuario.getEstado().getSigla());
				model.addAttribute("usuario", usuario);
				return "gerencial/index";
			}

		}
		if(TipoPessoa.MESTRE_CERVEJEIRO.equals(usuario.getTipoPessoa())) {
			Integer numeroCerveja = cervejaRepository.countByUsuario(usuario);
			Integer numeroTroca = trocaRepository.countByMestreCervejeiro(usuario);
			Integer numeroTrocaPendente = trocaRepository.countByMestreCervejeiroAndStatus(usuario, StatusTroca.PENDENTE);
			model.addAttribute("relatorioDTO", new RelatorioIndexDTO(numeroTroca, numeroCerveja, numeroTrocaPendente));
		}else {
			model.addAttribute("relatorioDTO", new RelatorioIndexDTO(0, 0, 0));
		}
		model.addAttribute("estilos", estiloRepository.findAll());
		model.addAttribute("pesquisaDTO", new PesquisaDTO());
		model.addAttribute("trocaDTO", new TrocaIndexDTO());
		model.addAttribute("cervejas", new CervejaIndexDTO().toListaCervejaDTO(cervejasDisponiveis));
		model.addAttribute("endereco", cidadeDoUsuario.getNome() + " - " + cidadeDoUsuario.getEstado().getSigla());
		model.addAttribute("usuario", usuario);
		return "gerencial/index";
	}

	@GetMapping("/suporte")
	public String suporteTemplate(Model model, Authentication authentication) {
		logger.info("Entrando na tela de suporte: suporteTemplate");

		String email = authentication.getName();

		Usuario usuario = usuarioRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Usuário ou senha não foi encontrado"));
		model.addAttribute("usuario", usuario);
		return "gerencial/suporte";
	}
}