package com.agendamento_odontologico.Administrador.Service;

import com.agendamento_odontologico.Administrador.DTO.admDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface admService {

    List<admDTO> findAll();

    ResponseEntity<?> findById(Long id);

    admDTO insert(admDTO dto);

    ResponseEntity<?> update(Long id, admDTO dto);

    ResponseEntity<?> delete(Long id);
}
