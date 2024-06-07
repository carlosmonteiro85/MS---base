package com.prototype.security.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prototype.security.domain.model.Especialidades;

@Repository
public interface EspecialidadeRepository extends JpaRepository<Especialidades, Long>{
}
