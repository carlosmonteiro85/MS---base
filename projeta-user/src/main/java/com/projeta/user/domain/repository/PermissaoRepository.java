package com.projeta.user.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeta.user.domain.model.Permissao;


public interface PermissaoRepository extends JpaRepository<Permissao, Long> {
}
