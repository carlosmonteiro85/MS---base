package com.projeta.user.domain.model;

import java.time.LocalDate;

import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.projeta.user.domain.model.util.Utilitarios;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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

@Audited
@AuditTable("auditoria_usuario")
@EntityListeners(AuditingEntityListener.class)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "usuario")
public class Usuario implements Diffable<Usuario> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String nome;

  private String celular;

  private String telefone;

  private LocalDate dataNacimento;

  @Builder.Default
  private String avatar = "avatar/avatar.png";

  @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinColumn(name = "id_especialidade")
  private Especialidades especialidade;

  @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
  @JoinColumn(name = "id_credencial")
  private CredencialUsuario credencial;

  @Override
  public DiffResult<Usuario> diff(Usuario obj) {
    return new DiffBuilder<>(this, obj, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("nome", Utilitarios.validarObjeto(this.nome),  Utilitarios.validarObjeto(obj.getNome()))
        .append("celular",  Utilitarios.validarObjeto(this.celular), Utilitarios.validarObjeto(obj.getCelular()))
        .append("telefone",  Utilitarios.validarObjeto(this.telefone),  Utilitarios.validarObjeto(obj.getTelefone()))
        .append("dataNacimento", this.dataNacimento, obj.getDataNacimento())
        .append("especialidade", 
               this.especialidade != null  ? this.especialidade.getDescricao() : "" ,
               obj.getEspecialidade() != null ? obj.getEspecialidade().getDescricao() : "")   
        .append("cpf",  this.credencial.getCpf(), obj.getCredencial().getCpf())
        .append("email", this.credencial.getEmail(), obj.getCredencial().getEmail())
        .append("password", this.credencial.getPassword(),
            obj.getCredencial().getPassword())
        .append("avatar", this.credencial.getIsBlocked(),
            obj.getCredencial().getIsBlocked())
        .build();
  }
}