package br.com.projeta.gestor.data.enuns;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum TipoUsuarioEnum {
  
  USER("1", "Cliente" ), 
  MEDICO("2", "Médico"), 
  ADM("3", "Administrador"), 
  MANAGER("4", "Gerente"),
  ROOT("5", "Root");

  @Getter
  private String id;
  @Getter
  private String descricao;
  
  public static TipoUsuarioEnum findById(String id) {
      for (TipoUsuarioEnum tipoUsuario : values()) {
          if (tipoUsuario.getId().equals(id)) {
              return tipoUsuario;
          }
      }
      return null;
  }
}
