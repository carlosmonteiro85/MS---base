package com.prototype.security.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prototype.security.domain.model.Permissao;


public interface PermissaoRepository extends JpaRepository<Permissao, Long> {
}
