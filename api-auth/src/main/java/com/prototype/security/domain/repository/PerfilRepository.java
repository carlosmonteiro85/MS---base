package com.prototype.security.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prototype.security.domain.model.Perfil;


public interface PerfilRepository extends JpaRepository<Perfil, Long> {
  Optional<Perfil> findByDescricao(String descricao);
}
