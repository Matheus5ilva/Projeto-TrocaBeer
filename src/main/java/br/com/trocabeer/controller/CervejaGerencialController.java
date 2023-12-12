package br.com.trocabeer.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.trocabeer.controller.dto.CervejaCadastroDTO;
import br.com.trocabeer.controller.dto.CervejaComplementoCadastroDTO;
import br.com.trocabeer.controller.dto.CervejaEditarDTO;
import br.com.trocabeer.controller.dto.CervejaListaDTO;
import br.com.trocabeer.core.utils.NullUtils;
import br.com.trocabeer.domain.model.Cerveja;
import br.com.trocabeer.domain.model.Estilo;
import br.com.trocabeer.domain.model.Usuario;
import br.com.trocabeer.domain.repository.CervejaRepository;
import br.com.trocabeer.domain.repository.EstiloRepository;
import br.com.trocabeer.domain.repository.UsuarioRepository;
import br.com.trocabeer.domain.service.CervejaService;
import br.com.trocabeer.domain.service.ComplementoCervejaService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/cervejas")
@ControllerAdvice
public class CervejaGerencialController {

	private static Logger logger = LoggerFactory.getLogger(CervejaGerencialController.class);

	@Autowired
	private EstiloRepository estiloRepository;

	@Autowired
	private CervejaService cervejaService;

	@Autowired
	private CervejaRepository cervejaRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private ComplementoCervejaService complementoCervejaService;

	@GetMapping
	public String listarCervejas(Model model, Authentication authentication) {

		logger.info("Entrando na tela inicial de cerveja: listarCervejas");
		Usuario usuario = buscaUsuario(authentication.getName());

		List<Cerveja> cervejas = cervejaService.listarCervejas(usuario);

		model.addAttribute("numeroCerveja", cervejas.size());
		model.addAttribute("usuario", usuario);
		model.addAttribute("cervejas", new CervejaListaDTO().toListaCervejaDTO(cervejas));

		return "gerencial/cervejas";
	}

	@GetMapping("/adicionar")
	public String exibirFormularioCriacao(Model model, Authentication authentication) {

		logger.info("Entrando na tela de cadastrar cerveja: exibirFormularioCriacao");

		List<Estilo> estilos = estiloRepository.findAll();

		model.addAttribute("usuario", this.buscaUsuario(authentication.getName()));
		model.addAttribute("estilos", estilos);
		model.addAttribute("cervejaDTO", new CervejaCadastroDTO());
		return "gerencial/criar-cerveja";
	}

	@PostMapping("/adicionar")
	public String salvarCerveja(@Valid @ModelAttribute("cervejaDTO") CervejaCadastroDTO cervejaDTO,
			BindingResult bindingResult, Authentication authentication, RedirectAttributes attributes, Model model) {

		if (bindingResult.hasErrors() || cervejaDTO.getDataFabricacao().after(cervejaDTO.getDataValidade())) {
			logger.debug("A data de fabricação é posterior à data de validade.");
			bindingResult.rejectValue("dataFabricacao", "dataValidade",
					"A data de fabricação é posterior à data de validade.");
			bindingResult.rejectValue("dataValidade", "dataFabricacao",
					"A data de validade é inferior à data de fabricação.");
			List<Estilo> estilos = estiloRepository.findAll();

			model.addAttribute("usuario", this.buscaUsuario(authentication.getName()));
			model.addAttribute("estilos", estilos);
			return "gerencial/criar-cerveja";
		}

		Cerveja cerveja = cervejaService.salvar(cervejaDTO.toCerveja(this.buscaUsuario(authentication.getName())));
		complementoCervejaService.salvar(cervejaDTO.toComplemento(cerveja));
		return "redirect:/cervejas";
	}

	@GetMapping("/complemento/{cervejaId}")
	public String exibirFormularioComplemento(Model model, @PathVariable UUID cervejaId,
			Authentication authentication) {

		logger.info("Entrando na rotina de complemento cerveja");

		Cerveja cerveja = cervejaService.buscarOuFalhar(cervejaId);

		model.addAttribute("usuario", this.buscaUsuario(authentication.getName()));
		model.addAttribute("complementoDTO", new CervejaComplementoCadastroDTO());
		model.addAttribute("cerveja", cerveja);
		return "gerencial/complemento-cerveja";
	}

	@PostMapping("/complemento/{cervejaId}")
	public String formularioComplemento(@PathVariable UUID cervejaId,
			@Valid @ModelAttribute("complementoDTO") CervejaComplementoCadastroDTO complementoDTO,
			BindingResult bindingResult, RedirectAttributes attributes, Model model, Authentication authentication) {

		if (bindingResult.hasErrors() || complementoDTO.getDataFabricacao().after(complementoDTO.getDataValidade())) {
			logger.debug("A data de validade é inferior à data de fabricação.");
			bindingResult.rejectValue("dataFabricacao", "dataValidade",
					"A data de fabricação é posterior à data de validade.");
			bindingResult.rejectValue("dataValidade", "dataFabricacao",
					"A data de validade é inferior à data de fabricação.");

			model.addAttribute("usuario", this.buscaUsuario(authentication.getName()));
			model.addAttribute("cerveja", cervejaService.buscarOuFalhar(cervejaId));
			return "gerencial/complemento-cerveja";
		}
		complementoCervejaService.salvar(complementoDTO.toComplemento(cervejaService.buscarOuFalhar(cervejaId)));

		return "redirect:/cervejas";
	}

	@GetMapping("/editar/{cervejaId}")
	public String editarTemplate(@PathVariable UUID cervejaId, Model model, Authentication authentication) {
		logger.debug("Entrando na rotina de editar cerveja");
		List<Estilo> estilos = estiloRepository.findAll();

		CervejaEditarDTO cervejaDTO = new CervejaEditarDTO().toCervejaDTO(cervejaService.buscarOuFalhar(cervejaId));

		model.addAttribute("usuario", this.buscaUsuario(authentication.getName()));
		model.addAttribute("estilos", estilos);
		model.addAttribute("cervejaDTO", cervejaDTO);
		model.addAttribute("cervejaId", cervejaId);
		return "gerencial/editar-cerveja";

	}

	@PostMapping("/editar/{cervejaId}")
	public String editarCerveja(@PathVariable UUID cervejaId,
			@Valid @ModelAttribute("cervejaDTO") CervejaEditarDTO cervejaDTO, BindingResult bindingResult,
			RedirectAttributes attributes, Model model, Authentication authentication) {

		if (bindingResult.hasErrors()) {

			List<Estilo> estilos = estiloRepository.findAll();
			model.addAttribute("usuario", this.buscaUsuario(authentication.getName()));
			model.addAttribute("estilos", estilos);
			model.addAttribute("cervejaDTO", cervejaDTO);
			model.addAttribute("cervejaId", cervejaId);

			return "gerencial/editar-cerveja";

		}

		Cerveja cervejaAtual = cervejaService.buscarOuFalhar(cervejaId);
		BeanUtils.copyProperties(cervejaDTO.toCerveja(this.buscaUsuario(authentication.getName())), cervejaAtual, "id",
				"usuario", "dataCadastro", "complementos", "estoqueMovimento");
		model.addAttribute("cerveja", cervejaService.salvar(cervejaAtual));

		return "redirect:/cervejas";
	}

	@GetMapping("/apagar/{cervejaId}")
	public String apagarCerveja(@PathVariable UUID cervejaId) {
		logger.info("Entrando na rotina de apagarCerveja");
		cervejaService.excluir(cervejaId);
		return "redirect:/cervejas";
	}

	private Usuario buscaUsuario(String email) {
		return usuarioRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Usuário ou senha não foi encontrado"));
	}

}
