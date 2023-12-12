package br.com.trocabeer.controller;

import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.trocabeer.controller.dto.UsuarioAlterarSenhaDTO;
import br.com.trocabeer.controller.dto.UsuarioEsqueciSenhaDTO;
import br.com.trocabeer.controller.mappers.CervejaToListaCervejaDTOMapper;
import br.com.trocabeer.domain.model.Usuario;
import br.com.trocabeer.domain.repository.CervejaRepository;
import br.com.trocabeer.domain.repository.EstiloRepository;
import br.com.trocabeer.domain.repository.UsuarioRepository;
import br.com.trocabeer.domain.service.CervejaService;
import br.com.trocabeer.domain.service.UsuarioService;
import br.com.trocabeer.domain.service.utils.EmailService;
import jakarta.validation.Valid;

@Controller
@ControllerAdvice
public class IndexController {

	private static Logger logger = LoggerFactory.getLogger(IndexController.class);

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private EmailService emailService;
	
	@GetMapping("/")
	public String index() {
		return "index";
	}

	@GetMapping("/esqueci-senha")
	public String esqueciSenhaTemplete(Model model) {

		model.addAttribute("usuarioDTO", new UsuarioEsqueciSenhaDTO());
		return "esqueci-senha";

	}

	@PostMapping("/esqueci-senha")
	public String esqueciSenha(@Valid @ModelAttribute("usuarioDTO") UsuarioEsqueciSenhaDTO usuarioDTO,
			BindingResult bindingResult, Model model) {

		Optional<Usuario> buscaUsuario = usuarioRepository.findByEmail(usuarioDTO.getEmail());

		if (bindingResult.hasErrors() || buscaUsuario.isEmpty()) {
			logger.debug("E-mail não cadastrado.");
			bindingResult.rejectValue("email", "email", "E-mail não cadastrado.");

			model.addAttribute("usuarioDTO", usuarioDTO);
			return "esqueci-senha";

		}

		// REFATORAR PARA UTILIZAR O DTO
		Usuario usuario = buscaUsuario.get();
		usuario.setTokenAlterarSenha(UUID.randomUUID().toString());

		usuarioService.salvar(usuario);
		try {
			logger.info("Iniciando o envio do e-mail para " + usuario.getEmail());
			SimpleMailMessage emailAlterar = new SimpleMailMessage();
			emailAlterar.setFrom("Contato - TrocaBeer <matheus0verfl0w27@gmail.com>");
			emailAlterar.setTo(usuario.getEmail());
			emailAlterar.setSubject("Recuperar Senha - Trocabeer");
			emailAlterar.setText("Olá " + usuario.getNome()
					+ ",\nRecebemos uma solicitação para redefinir a senha da sua conta no Trocabeer.\nPara criar uma nova senha, clique no link abaixo:\n"
					+ "https://03d1-177-124-74-41.ngrok-free.app/alterar-senha?token=" + usuario.getTokenAlterarSenha());
			emailService.sendEmail(emailAlterar);
		} catch (Exception e) {
			logger.error("Erro ao enviar e-mail: " + e.getMessage());
		}

		return "redirect:/login";
	}

	@GetMapping("/alterar-senha")
	public String alterarSenhaTemplete(Model model, @RequestParam("token") String token) {

		logger.info("Estou na rotina de alterarSenhaTemplete");
		Optional<Usuario> usuarioRecuperado = usuarioRepository.findByTokenAlterarSenha(token);

		if (usuarioRecuperado.isEmpty()) {
			return "redirect:/login";
		}

		model.addAttribute("usuario", usuarioRecuperado.get());
		model.addAttribute("usuarioDTO", new UsuarioAlterarSenhaDTO());
		model.addAttribute("token", token);
		return "alterar-senha";
	}

	@PostMapping("/alterar-senha")
	public String alterarSenha(Model model, @RequestParam("token") String token,
			@Valid @ModelAttribute("usuarioDTO") UsuarioAlterarSenhaDTO usuarioDTO, BindingResult bindingResult) {

		Optional<Usuario> usuarioRecuperado = usuarioRepository.findByTokenAlterarSenha(token);
		Usuario usuario = usuarioRecuperado.get();

		if (bindingResult.hasErrors() || !usuarioDTO.getSenha().equals(usuarioDTO.getConfirmarSenha())) {

			if (!usuarioDTO.getSenha().equals(usuarioDTO.getConfirmarSenha())) {
				logger.debug("Senha ou confirmar senha são diferentes.");
				bindingResult.rejectValue("senha", "confirmarSenha", "Senhas diferentes.");
				bindingResult.rejectValue("confirmarSenha", "senha", "Senhas diferentes.");
			}

			model.addAttribute("usuario", usuario);
			model.addAttribute("usuarioDTO", usuarioDTO);

			return "alterar-senha";
		}

		usuario.setSenha(new BCryptPasswordEncoder().encode(usuarioDTO.getSenha()));
		usuario.setTokenAlterarSenha(null);
		usuarioService.salvar(usuario);
		logger.info("Usuario teve a senha alterada");
		return "redirect:/";

	}

	@GetMapping("/login")
	public String login() {
		return "login";

	}
	
	@GetMapping("/errors")
	public String errors() {
		return "not-found";

	}
	
	@GetMapping("/politica")
	public String politica() {
		return "politica";

	}
	
}
