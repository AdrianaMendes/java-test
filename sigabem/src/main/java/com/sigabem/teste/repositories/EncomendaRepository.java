package com.sigabem.teste.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sigabem.teste.entities.EncomendaEntity;

@Repository
public interface EncomendaRepository extends JpaRepository<EncomendaEntity, Long> {
}
