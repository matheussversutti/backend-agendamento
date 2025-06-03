package com.agendamento_odontologico.Funcionarios.Repository;

import com.agendamento_odontologico.Funcionarios.Molde.FuncionarioMolde;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FuncionariosRepository extends JpaRepository<FuncionarioMolde, Long> {

    Optional<FuncionarioMolde> findByEmail(String email);

    Optional<FuncionarioMolde> findByCpf(String cpf);

    Optional<FuncionarioMolde> findByCro(String cro);
}
