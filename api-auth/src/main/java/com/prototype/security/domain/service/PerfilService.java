package com.prototype.security.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.prototype.security.api.dto.response.RoleRespose;
import com.prototype.security.domain.exception.ObjectNotFoundException;
import com.prototype.security.domain.model.Perfil;
import com.prototype.security.domain.repository.PerfilRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PerfilService {

  private final PerfilRepository perfilRepository;

  public RoleRespose getAutorites(String descricaoRole) {
    Perfil perfil = perfilRepository.findByDescricao(descricaoRole.replace("ROLE_", ""))
        .orElseThrow(() -> new ObjectNotFoundException("Perfil não encontrado."));

    return RoleRespose.builder().perfil(perfil.getDescricao())
        .permissoes(perfil.getPermisoes().stream().map(p -> p.getTipoPermisao().name()).toList()).build();

  }

  public List<Perfil> findAll() {
    return perfilRepository.findAll();
  }

  public Perfil findById(Long idPerfil) {
    return perfilRepository.findById(idPerfil)
        .orElseThrow(() -> new ObjectNotFoundException("Perfil não encontrado."));
  }
}
