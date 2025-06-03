package com.agendamento_odontologico.Consultas.Service;

import com.agendamento_odontologico.Consultas.DTO.ConsultasDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ConsultasService {

    List<ConsultasDTO> findAll();
    ResponseEntity<?> findById(Long id);
    ConsultasDTO criar(ConsultasDTO dto);
    ResponseEntity<?> update(Long id, ConsultasDTO dto);
    ResponseEntity<?> delete(Long id);
    ResponseEntity<?> cancelarConsulta(Long id);
    ResponseEntity<?> concluirConsulta(Long id);
    ResponseEntity<?> reagendarConsulta(Long id, ConsultasDTO dto);
}