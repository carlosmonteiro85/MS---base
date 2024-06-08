package com.projeta.user.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmailAnexoDTO {
	private String nome;
	private byte[] conteudo;
	private String extensao;
}
