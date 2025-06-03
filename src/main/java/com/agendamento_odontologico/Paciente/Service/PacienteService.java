package com.agendamento_odontologico.Paciente.Service;

import com.agendamento_odontologico.Paciente.DTO.PacienteDTO;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface PacienteService {

    List<PacienteDTO> findAll();

    ResponseEntity<?> findById(Long id);

    PacienteDTO insert(PacienteDTO dto);

    ResponseEntity<?> update(Long id, PacienteDTO dto);

    ResponseEntity<?> delete(Long id);
}
