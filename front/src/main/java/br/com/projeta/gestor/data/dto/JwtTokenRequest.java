package br.com.projeta.gestor.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtTokenRequest {
  private Long idCredencial;
  private String accessToken;
  private String refreshToken;
}
