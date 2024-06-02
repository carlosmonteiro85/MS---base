package com.projeta.user.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeta.user.domain.model.CredencialUsuario;

public interface CredencialUsuarioRepository extends JpaRepository<CredencialUsuario, Long> {
  Optional<CredencialUsuario> findByEmail(String email);
  Optional<CredencialUsuario> findByCpf(String cpf);
  Optional<CredencialUsuario> findByUsername(String cpf);

}
