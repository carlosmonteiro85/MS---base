package com.projeta.user.api.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoricoOutputDTO {
	private Long idAuditoria;
	private String responsavel;
	private LocalDateTime dataAlteracao;
	private String campo;
	private String descricao;
	private String situacaoAnterior;
	private String situacaoAtual;
}
