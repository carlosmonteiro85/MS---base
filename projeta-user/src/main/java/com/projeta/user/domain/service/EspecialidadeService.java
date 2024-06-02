package com.projeta.user.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.projeta.user.api.dto.EspecialidadeResquest;
import com.projeta.user.domain.exception.ObjectNotFoundException;
import com.projeta.user.domain.model.Especialidades;
import com.projeta.user.domain.repository.EspecialidadeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EspecialidadeService {

  private final EspecialidadeRepository especialidadeRepository;

  public List<EspecialidadeResquest> findAll() {
    return especialidadeRepository.findAll().stream()
        .map(this::bindEspecialidade).toList();
  }

  private EspecialidadeResquest bindEspecialidade(Especialidades especialidade) {
    return EspecialidadeResquest.builder()
        .id(especialidade.getId())
        .descricao(especialidade.getDescricao())
        .build();
  }

  public Especialidades findById(Long id){
    return especialidadeRepository.findById(id)
      .orElseThrow(() -> new ObjectNotFoundException("Não foi encontrado uma especialidade com o Id: " + id));
  }
}
