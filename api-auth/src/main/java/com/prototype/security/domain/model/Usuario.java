package com.prototype.security.domain.model;

import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "usuario")
public class Usuario {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String nome;

  private String celular;

  private String telefone;
  
  private LocalDate dataNacimento;

  private String  avatar;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "id_especialidade")
  private Especialidades especialidade;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "id_credencial")
  private CredencialUsuario  credencial;
}
