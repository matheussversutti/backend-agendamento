package com.agendamento_odontologico.Funcionarios.Service;

import com.agendamento_odontologico.Funcionarios.DTO.FuncionarioDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface FuncionariosService {

    List<FuncionarioDTO> findAll();

    ResponseEntity<?> findById(Long id);

    FuncionarioDTO insert(FuncionarioDTO dto);

    ResponseEntity<?> update(Long id, FuncionarioDTO dto);

    ResponseEntity<?> delete(Long id);
}
