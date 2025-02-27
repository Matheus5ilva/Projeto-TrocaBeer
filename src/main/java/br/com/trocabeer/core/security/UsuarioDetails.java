package br.com.trocabeer.core.security;

import br.com.trocabeer.domain.model.Usuario;
import br.com.trocabeer.domain.model.enums.TipoPessoa;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UsuarioDetails implements UserDetails {

  private Usuario usuario;

  public Usuario getUsuario() {
    return usuario;
  }

  public void setUsuario(Usuario usuario) {
    this.usuario = usuario;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    List<GrantedAuthority> authorities = new ArrayList<>();

    if (this.usuario.getTipoPessoa().equals(TipoPessoa.MESTRE_CERVEJEIRO)) {
      authorities.add(RoleModel.ROLE_ADMIN);
    } else if (this.usuario.getTipoPessoa().equals(TipoPessoa.APRECIADOR)) {
      authorities.add(RoleModel.ROLE_USER);
    } else {
      throw new AccessDeniedException("Acesso negado");
    }

    return authorities;
  }

  @Override
  public String getPassword() {
    return this.getUsuario().getSenha();
  }

  @Override
  public String getUsername() {
    return this.getUsuario().getEmail();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return this.getUsuario().getAtivo();
  }
}