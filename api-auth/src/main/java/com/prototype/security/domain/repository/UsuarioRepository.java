package com.prototype.security.domain.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.prototype.security.api.dto.response.UsuarioResponseFilter;
import com.prototype.security.domain.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
  @Query("SELECT u FROM Usuario u JOIN CredencialUsuario c ON c.id = u.credencial.id WHERE c.username = :username")
  Optional<Usuario> finByUsernome(@Param("username") String username);

  @Query("SELECT u FROM Usuario u JOIN CredencialUsuario c ON c.id = u.credencial.id WHERE c.cpf = :cpf")
  Optional<Usuario> finByCpf(@Param("cpf") String cpf);

  @Query("SELECT u FROM Usuario u JOIN CredencialUsuario c ON c.id = u.credencial.id WHERE c.email = :email")
  Optional<Usuario> finByEmail(@Param("email") String email);

  @Query("SELECT DISTINCT new com.prototype.security.api.dto.response.UsuarioResponseFilter(" +
      "u.id, u.nome, u.celular, u.telefone, e.descricao, u.dataNacimento, " +
      "c.username, c.cpf, c.email, p.descricao) " +
      "FROM Usuario u " +
      "JOIN u.credencial c " +
      "LEFT JOIN Especialidades e on u.especialidade.id = e.id " +
      "JOIN c.perfils p " +
      "WHERE " +
      "(:nome IS NULL OR LOWER(u.nome) LIKE LOWER(CONCAT('%', :nome, '%'))) AND" +
      "(:cpf IS NULL OR c.cpf LIKE CONCAT('%', :cpf, '%')) " +
      "AND (:dataInicio IS NULL OR u.dataNacimento >= :dataInicio) " +
      "AND (:dataFim IS NULL OR u.dataNacimento <= :dataFim) " +
      "AND (:especialidades IS NULL OR u.especialidade.id IN :especialidades) " +
      "AND (:perfils IS NULL OR p.id IN :perfils)")
  Page<UsuarioResponseFilter> findAllFilters(
      @Param("nome") String nome,
      @Param("cpf") String cpf,
      @Param("dataInicio") LocalDate dataInicio,
      @Param("dataFim") LocalDate dataFim,
      @Param("especialidades") List<Long> especialidades,
      @Param("perfils") List<Long> perfils,
      Pageable pageable);
}