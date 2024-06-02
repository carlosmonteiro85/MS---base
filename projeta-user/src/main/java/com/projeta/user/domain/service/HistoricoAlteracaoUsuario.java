package com.projeta.user.domain.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.Diffable;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.springframework.stereotype.Service;

import com.projeta.user.api.dto.response.HistoricoOutputDTO;
import com.projeta.user.config.Diff;
import com.projeta.user.domain.model.CredencialUsuario;
import com.projeta.user.domain.model.Especialidades;
import com.projeta.user.domain.model.RevEntity;
import com.projeta.user.domain.model.Usuario;
import com.projeta.user.domain.model.util.Utilitarios;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HistoricoAlteracaoUsuario {

  @PersistenceContext
  private EntityManager entityManager;

  private final Diff diff;

  public List<HistoricoOutputDTO> buscarHistorico(Long id) {
    List<HistoricoOutputDTO> allRevisions = new ArrayList<>();
    AuditReader auditReader = AuditReaderFactory.get(entityManager);
    List<Number> revisions = auditReader.getRevisions(Usuario.class, id);

    for (int i = revisions.size() - 1; i > 0; i--) {
      Number currentRevisionNumber = revisions.get(i);
      Number previousRevisionNumber = revisions.get(i - 1);
      Usuario currentRevision = buscarLinhaRevisao(id, currentRevisionNumber.longValue());
      Usuario previousRevision = buscarLinhaRevisao(id, previousRevisionNumber.longValue());

      RevEntity currentRevisionRevEntity = auditReader.findRevision(RevEntity.class, currentRevisionNumber);
      List<HistoricoOutputDTO> changes = getChanges(previousRevision, currentRevision, currentRevisionRevEntity);
      allRevisions.addAll(changes);
    }

    return allRevisions;
  }

  private List<HistoricoOutputDTO> getChanges(Diffable<?> original, Diffable<?> revision,
      RevEntity currentrevisionRevEntity) {
    List<HistoricoOutputDTO> changes = new ArrayList<>();
    changes.addAll(diff.gerarHistorico(original, revision, currentrevisionRevEntity));
    return changes;
  }

  private Usuario buscarLinhaRevisao(Long id, Long rev) {
    String queryString = "SELECT " +
        " U.AVATAR, " +
        " U.CELULAR, " +
        " U.DATA_NACIMENTO, " +
        " U.NOME, " +
        " U.TELEFONE , " +
        " U.ID_CREDENCIAL, " +
        " U.ID_ESPECIALIDADE  " +
        "FROM " +
        "AUDITORIA_USUARIO AS U " +
        "JOIN " +
        "REV_AUDITORIA AS R ON R.REV = U.REV " +
        "WHERE U.ID = :id AND R.REV = :rev ORDER BY R.REV";
    Query query = entityManager
        .createNativeQuery(queryString)
        .setParameter("id", id)
        .setParameter("rev", rev);
    List<Object[]> resultList =  query.getResultList();
    return resultList.stream().map(this::bindUserForObject).findFirst().orElse(null);
  }

  private Usuario bindUserForObject(Object[] obj) {
    return Usuario.builder()
        .avatar(obj[0] != null ? obj[0].toString() : null)
        .celular(obj[1] != null ? obj[1].toString() : null)
        .dataNacimento(obj[2] != null ? Utilitarios.stringToLocalDate(obj[2].toString()) : null)
        .nome(obj[3] != null ? obj[3].toString() : null)
        .telefone(obj[4] != null ? obj[4].toString() : null)
        .credencial(obj[5] != null ? new CredencialUsuario((Long) obj[5]) : null)
        .especialidade(obj[6] != null ? new Especialidades((Long) obj[6]) : null)
        .build();
  }
}
