package com.prototype.security.api.dto.request;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FiltrosRequest {
  private String nome;
  private String cpf;
  private String celularOuTelefone;
  private LocalDate dataInicio;
  private LocalDate dataFim;
  private List<Long> especialidades;
  private List<Long> perfils;
}
