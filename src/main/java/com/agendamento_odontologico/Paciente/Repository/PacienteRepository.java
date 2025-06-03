package com.agendamento_odontologico.Paciente.Repository;

import com.agendamento_odontologico.Paciente.Molde.PacienteMolde;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PacienteRepository extends JpaRepository<PacienteMolde, Long> {

    Optional<PacienteMolde> findByEmail(String email);

    Optional<PacienteMolde> findByCpf(String cpf);
}
