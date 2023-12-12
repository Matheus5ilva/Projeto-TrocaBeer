package br.com.trocabeer.core.security;

import br.com.trocabeer.domain.model.Usuario;
import br.com.trocabeer.domain.model.enums.TipoPessoa;
import br.com.trocabeer.domain.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private UsuarioRepository usuarioRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Usuario usuario = usuarioRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("Usuário ou senha não foi encontrado"));

    UsuarioDetails usuarioDetails = new UsuarioDetails();
    usuarioDetails.setUsuario(usuario);

    return new User(usuarioDetails.getUsername(), usuarioDetails.getPassword(), usuarioDetails.isEnabled(), true, true, true,
        usuarioDetails.getAuthorities());
  }
}