package com.prototype.security.config;

// import br.gov.mg.fhemig.mspessoa.api.dto.output.HistoricoOutputDTO;
// import br.gov.mg.fhemig.mspessoa.core.security.RevEntity;
// import br.gov.mg.fhemig.mspessoa.domain.exception.NegocioException;
// import br.gov.mg.fhemig.mspessoa.domain.service.utils.Utilitarios;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;

import com.prototype.security.api.dto.response.HistoricoOutputDTO;
import com.prototype.security.domain.exception.NegocioException;
import com.prototype.security.domain.model.RevEntity;
import com.prototype.security.domain.model.util.Utilitarios;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class Diff {

	public List<HistoricoOutputDTO> gerarHistorico(final Diffable objetoAntigo, final Diffable objetoNovo, RevEntity rev) {
		List<HistoricoOutputDTO> relatorioDeAlteracao = new ArrayList<>();
		try {
			DiffResult<?> diffResult = objetoAntigo.diff(objetoNovo);
			for (org.apache.commons.lang3.builder.Diff<?> diff : diffResult) {

				HistoricoOutputDTO historicoAlteracaoDTO = new HistoricoOutputDTO();

				historicoAlteracaoDTO.setCampo(Utilitarios.formatarDescricao(diff.getFieldName()));
  				historicoAlteracaoDTO.setDescricao(Utilitarios.formatarDescricao(diff.getFieldName()));
				historicoAlteracaoDTO.setSituacaoAnterior(validar(diff.getLeft()));
				historicoAlteracaoDTO.setSituacaoAtual(validar(diff.getRight()));
				historicoAlteracaoDTO.setDataAlteracao(LocalDateTime.ofInstant(Instant.ofEpochMilli(rev.getTimestemp()),
						ZoneId.systemDefault()));
				historicoAlteracaoDTO.setResponsavel(rev.getUsuario());
				historicoAlteracaoDTO.setIdAuditoria(rev.getRev());
				relatorioDeAlteracao.add(historicoAlteracaoDTO);
			}

		} catch (Exception e) {
			throw new NegocioException("Erro ao gerar histórico " + e.getMessage());
		}
		return relatorioDeAlteracao;
	}

	public static String validar(Object campo) {
		return Objects.nonNull(campo) ? campo.toString() : null;
	}
}
