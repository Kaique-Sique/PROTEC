package com.example.protec.maquinas;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MaquinasRepository extends JpaRepository<Maquinas, UUID> {
    Optional<Maquinas> findByNome(String nome);

    boolean existsByNome(String nome);
}