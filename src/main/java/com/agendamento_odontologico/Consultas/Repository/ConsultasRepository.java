package com.agendamento_odontologico.Consultas.Repository;

import com.agendamento_odontologico.Consultas.Molde.ConsultasMolde;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ConsultasRepository extends JpaRepository<ConsultasMolde, Long> {
    List<ConsultasMolde> findByPacienteId(Long pacienteId);
    List<ConsultasMolde> findByDrContaining(String nomeDr);
}
