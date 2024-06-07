package com.prototype.security.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.prototype.security.api.dto.EspecialidadeResquest;
import com.prototype.security.domain.exception.ObjectNotFoundException;
import com.prototype.security.domain.model.Especialidades;
import com.prototype.security.domain.repository.EspecialidadeRepository;

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
