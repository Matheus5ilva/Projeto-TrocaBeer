package br.com.trocabeer.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import br.com.trocabeer.controller.dto.CidadeDTO;
import br.com.trocabeer.controller.dto.UsuarioAlterarSenhaDTO;
import br.com.trocabeer.controller.dto.UsuarioEditarDTO;
import br.com.trocabeer.controller.dto.UsuarioRegistroDTO;
import br.com.trocabeer.domain.model.Estado;
import br.com.trocabeer.domain.model.Usuario;
import br.com.trocabeer.domain.repository.EstadoRepository;
import br.com.trocabeer.domain.repository.UsuarioRepository;
import br.com.trocabeer.domain.service.UsuarioService;
import jakarta.validation.Valid;

@Controller
@ControllerAdvice
public class UsuarioController {

	private static Logger logger = LoggerFactory.getLogger(UsuarioController.class);

	@Autowired
	private EstadoRepository estadoRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private UsuarioService usuarioService;

	@GetMapping("/registro")
	public String registroTemplate(Model model) {
		model.addAttribute("usuarioDTO", new UsuarioRegistroDTO());
		List<Estado> estados = estadoRepository.findAll();
		model.addAttribute("estados", estados);
		return "registro";
	}

	@PostMapping("/registro")
	public String registroSalvar(@Valid @ModelAttribute("usuarioDTO") UsuarioRegistroDTO usuarioDTO,
			BindingResult bindingResult, Model model) {
		Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(usuarioDTO.getEmail());
		if (bindingResult.hasErrors() || usuarioExistente.isPresent()) {
			logger.debug("Erro ao preencher o cadastro cervejeiro/mestre cervejeiro.");
			if (usuarioExistente.isPresent()) {
				bindingResult.rejectValue("email", "email", "E-mail já cadastrado.");
			}

			List<Estado> estados = estadoRepository.findAll();
			model.addAttribute("estados", estados);
			model.addAttribute("cidadeIdSelecionada", usuarioDTO.getCidade().getId());

			return "registro";
		}

		usuarioService.salvar(usuarioDTO.toUsuario());
		logger.info("Usuário salvo com sucesso!");
		return "redirect:/login";
	}

	@GetMapping("/usuario/editar/{usuarioId}")
	public String editarTemplate(@PathVariable UUID usuarioId, Model model, Authentication authentication) {

		Usuario usuario = usuarioService.buscarOuFalhar(usuarioId);
		List<Estado> estados = estadoRepository.findAll();
		UsuarioEditarDTO usuarioDto = new UsuarioEditarDTO().toUsuarioDTO(usuario);

		model.addAttribute("usuario", usuario);
		model.addAttribute("usuarioDTO", usuarioDto);
		model.addAttribute("usuarioId", usuarioId);
		model.addAttribute("estados", estados);
		model.addAttribute("cidadeIdSelecionada", usuarioDto.getCidade().getId());

		return "/gerencial/editar-usuario";
	}

	@PostMapping("/usuario/editar/{usuarioId}")
	public String editarUsuario(@Valid @ModelAttribute("usuarioDTO") UsuarioEditarDTO usuarioDTO,
			BindingResult bindingResult, Model model, @PathVariable UUID usuarioId, Authentication authentication) {
		String email = authentication.getName();

		Usuario usuario = usuarioRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Usuário ou senha não foi encontrado"));
		Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(usuarioDTO.getEmail());
		if (bindingResult.hasErrors() || (usuarioExistente.isPresent() && !email.equals(usuarioDTO.getEmail()))) {
			logger.debug("Erro ao preencher o editar cervejeiro/mestre cervejeiro.");
			if (usuarioExistente.isPresent()) {
				bindingResult.rejectValue("email", "email", "E-mail já cadastrado.");
			}
			List<Estado> estados = estadoRepository.findAll();

			model.addAttribute("usuario", usuario);
			model.addAttribute("usuarioDTO", usuarioDTO);
			model.addAttribute("usuarioId", usuarioId);
			model.addAttribute("estados", estados);
			model.addAttribute("cidadeIdSelecionada", usuarioDTO.getCidade().getId());

			return "/gerencial/editar-usuario";

		}

		Usuario usuarioAtual = usuarioService.buscarOuFalhar(usuarioId);
		BeanUtils.copyProperties(usuarioDTO.toUsuario(), usuarioAtual, "id", "senha", "dataCadastro", "premium");
		usuarioService.salvar(usuarioAtual);
		return "redirect:/home";
	}

	@GetMapping("/usuario/editar/{usuarioId}/alterar-senha")
	public String trocarSenhaTemplate(@PathVariable UUID usuarioId, Model model, Authentication authentication) {
		Usuario usuario = usuarioService.buscarOuFalhar(usuarioId);
		model.addAttribute("usuario", usuario);
		model.addAttribute("usuarioDTO", new UsuarioAlterarSenhaDTO());
		model.addAttribute("usuarioId", usuarioId);
		return "/gerencial/alterar-senha";
	}

	@PostMapping("/usuario/editar/{usuarioId}/alterar-senha")
	public String trocarSenha(@Valid @ModelAttribute("usuarioDTO") UsuarioAlterarSenhaDTO usuarioDTO,
			BindingResult bindingResult, @PathVariable UUID usuarioId, Model model) {

		Usuario usuario = usuarioService.buscarOuFalhar(usuarioId);

		if (bindingResult.hasErrors()) {
			logger.error("Erro ao preencher o cadastro cervejeiro/mestre cervejeiro.");
			model.addAttribute("usuario", usuario);
			model.addAttribute("usuarioDTO", usuarioDTO);
			model.addAttribute("usuarioId", usuarioId);

			return "/gerencial/alterar-senha";
		}

		usuario.setSenha(new BCryptPasswordEncoder().encode(usuarioDTO.getSenha()));

		model.addAttribute("usuario", usuarioService.salvar(usuario));
		return "redirect:/usuario/editar/" + usuario.getId();
	}

	@GetMapping("/usuario/apagar/{usuarioId}")
	public String apagarUsuario(@PathVariable UUID usuarioId, Authentication authentication) {
		Usuario usuario = usuarioService.buscarOuFalhar(usuarioId);
		usuario.setAtivo(false);
		usuarioService.salvar(usuario);
		return "redirect:/";
	}
}
