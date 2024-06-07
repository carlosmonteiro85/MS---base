package com.prototype.security.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.prototype.security.domain.model.Token;

public interface TokenRepository extends JpaRepository<Token, UUID> {

  @Query(value = """
      SELECT t FROM Token t INNER JOIN CredencialUsuario c
      ON t.credencial.id = c.id
      WHERE c.id = :id and (t.expired = false or t.revoked = false)
      """)
  List<Token> obterTokensUsuario(Long id);

  Optional<Token> findByToken(String token);
}
