package com.prototype.security.domain.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.prototype.security.domain.model.CredencialUsuario;
import com.prototype.security.domain.repository.CredencialUsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CredencialUsuarioCuston {

  private final CredencialUsuarioRepository repository;

  public Optional<CredencialUsuario> findByLogin(String login) {
    Optional<CredencialUsuario> credencial = repository.findByUsername(login);

    if (credencial.isEmpty()) {
      credencial = repository.findByEmail(login);
    }

    if (credencial.isEmpty()) {
      credencial = repository.findByCpf(login);
    }

    return credencial;
  }
}
