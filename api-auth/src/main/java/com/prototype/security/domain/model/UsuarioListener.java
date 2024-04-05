package com.prototype.security.domain.model;

import org.hibernate.envers.RevisionListener;

public class UsuarioListener implements RevisionListener {

  @Override
  public void newRevision(Object revisionEntity) {

    // OBJECT PRINCIPAL = SECURITYCONTEXTHOLDER.GETCONTEXT().GETAUTHENTICATION().GETPRINCIPAL();
    // STRING USERNAME = PRINCIPAL INSTANCEOF USERDETAILS ? ((USERDETAILS) PRINCIPAL).GETUSERNAME() : PRINCIPAL.TOSTRING();

    RevEntity revEntity = (RevEntity) revisionEntity;
    revEntity.setUsuario("Usuario logado");
    revEntity.setTimestemp(System.currentTimeMillis());
  }
}
