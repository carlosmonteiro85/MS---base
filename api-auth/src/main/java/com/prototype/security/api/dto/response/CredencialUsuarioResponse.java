package com.prototype.security.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CredencialUsuarioResponse {
	private Long id;
	private String accessToken;
	private String refreshToken;


}
