package com.projeta.user.domain.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.projeta.user.domain.model.CredencialUsuario;
import com.projeta.user.domain.repository.CredencialUsuarioRepository;

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
