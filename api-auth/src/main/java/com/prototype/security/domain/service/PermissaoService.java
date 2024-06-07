package com.prototype.security.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.prototype.security.domain.model.Permissao;
import com.prototype.security.domain.repository.PermissaoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PermissaoService {

  private final PermissaoRepository permissaiRepository;

  public List<Permissao> findAllPermissao() {
    return permissaiRepository.findAll();
  }
}
