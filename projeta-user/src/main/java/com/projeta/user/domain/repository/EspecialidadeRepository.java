package com.projeta.user.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projeta.user.domain.model.Especialidades;

@Repository
public interface EspecialidadeRepository extends JpaRepository<Especialidades, Long>{
}
