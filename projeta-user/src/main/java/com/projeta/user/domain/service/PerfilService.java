package com.projeta.user.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.projeta.user.api.dto.response.RoleRespose;
import com.projeta.user.domain.exception.ObjectNotFoundException;
import com.projeta.user.domain.model.Perfil;
import com.projeta.user.domain.repository.PerfilRepository;

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
