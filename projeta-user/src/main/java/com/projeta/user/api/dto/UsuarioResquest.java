package com.projeta.user.api.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import io.micrometer.common.lang.NonNull;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UsuarioResquest {
  @NonNull
  private String cpf;
  @NonNull
  private String nome;
  private String celular;
  private String telefone;
  @NotNull
  private Long perfil;
  private List<Long> permissoes;
  private Long especialidade;
  private LocalDate dataNacimento;
  @NonNull
  private String email;
  private String avatar;

  @PrePersist
  private void defaultAvatar(){
    if(Objects.isNull(this.avatar)){
      avatar = "avatar/avatar.png";
    }
  }
}
