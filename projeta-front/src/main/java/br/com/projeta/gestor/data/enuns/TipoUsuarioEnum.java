package br.com.projeta.gestor.data.enuns;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum TipoUsuarioEnum {
  
  SUPERADMIN("1", "Super admin" ), 
  ADMIN("2", "Administrador"), 
  SECRETARIA("3", "Srecetaria"), 
  MEDICO("4", "Médico"),
  FATURISTA("5", "Faturista"),
  FINANCEIRO("6", "Financeiro"),
  PACIENTES("7", "Root");

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
