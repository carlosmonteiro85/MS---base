package com.projeta.user.domain.service;

import org.springframework.stereotype.Service;

import com.projeta.user.api.dto.DominiosTelaUser;
import com.projeta.user.api.dto.EspecialidadeResquest;
import com.projeta.user.api.dto.response.PerfilRequest;
import com.projeta.user.api.dto.response.SampleItemRequest;
import com.projeta.user.domain.model.Perfil;
import com.projeta.user.domain.model.Permissao;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DominiosService {

  private final EspecialidadeService especialidadeService;
  private final PerfilService perfilService;

  public DominiosTelaUser bindDominiosTelaUser(){
    return DominiosTelaUser.builder()
              .especialidades(especialidadeService.findAll().stream().map(this::bindEspecialidade).toList())
              .perfils(perfilService.findAll().stream().map(this::bindPerfil).toList())
           .build();
  }

  public SampleItemRequest bindEspecialidade(EspecialidadeResquest especialidade) {
    return SampleItemRequest.builder()
          .id(especialidade.getId())
          .descricao(especialidade.getDescricao())
        .build();
  }

  public PerfilRequest bindPerfil(Perfil perfil) {
    return PerfilRequest.builder()
          .id(perfil.getId())
          .descricao(perfil.getDescricao())
          .permissoes(perfil.getPermisoes().stream().map(this::bindPermissao).toList())
        .build();
  }

  public SampleItemRequest bindPermissao(Permissao permissao) {
    return SampleItemRequest.builder()
          .id(permissao.getId())
          .descricao(permissao.getTipoPermisao().getDescricao())
        .build();
  }
}
