package com.projeta.user.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.projeta.user.domain.model.Permissao;
import com.projeta.user.domain.repository.PermissaoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PermissaoService {

  private final PermissaoRepository permissaiRepository;

  public List<Permissao> findAllPermissao() {
    return permissaiRepository.findAll();
  }
}
