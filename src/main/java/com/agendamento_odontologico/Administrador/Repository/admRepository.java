package com.agendamento_odontologico.Administrador.Repository;

import com.agendamento_odontologico.Administrador.Molde.admMolde;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface admRepository extends JpaRepository<admMolde, Long> {

    Optional<admMolde> findByEmail(String email);
}
