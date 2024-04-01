package com.prototype.security.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prototype.security.domain.model.CredencialUsuario;

public interface CredencialUsuarioRepository extends JpaRepository<CredencialUsuario, Long> {
  Optional<CredencialUsuario> findByEmail(String email);
  Optional<CredencialUsuario> findByCpf(String cpf);
  Optional<CredencialUsuario> findByUsername(String cpf);

}
