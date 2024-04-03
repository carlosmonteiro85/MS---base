package com.prototype.security.domain.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.prototype.security.domain.model.CredencialUsuario;
import com.prototype.security.domain.repository.CredencialUsuarioRepository;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private CredencialUsuarioRepository repository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<CredencialUsuario> credencial = repository.findByEmail(username);

    if (credencial.isEmpty()) {
      credencial = repository.findByCpf(username);
    }

    if (credencial.isEmpty()) {
      credencial = repository.findByUsername(username);
    }

    return credencial.map(CustomUserDetails::new)
        .orElseThrow(() -> new UsernameNotFoundException("Usuario com o login " + username + " não encontrado"));
  }
}
