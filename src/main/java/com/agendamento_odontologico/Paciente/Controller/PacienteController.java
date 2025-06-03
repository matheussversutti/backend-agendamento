package com.agendamento_odontologico.Paciente.Controller;

import com.agendamento_odontologico.Paciente.DTO.PacienteDTO;
import com.agendamento_odontologico.Paciente.Service.ipml.PacienteServiceIpml;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/paciente")
@CrossOrigin(origins = "http://127.0.0.1:5500", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS, RequestMethod.HEAD})
public class PacienteController {

    private final PacienteServiceIpml service;

    public PacienteController(PacienteServiceIpml service) {
        this.service = service;
    }

    @GetMapping("/listar")
    public ResponseEntity<List<PacienteDTO>> listarTodos() {
        List<PacienteDTO> pacientes = service.findAll();
        return ResponseEntity.ok(pacientes);
    }

    @GetMapping("/listar/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping("/criar")
    public ResponseEntity<?> criarPaciente(@RequestBody PacienteDTO dto) {
        try {
            PacienteDTO criado = service.insert(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(criado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody PacienteDTO dto) {
        Optional<PacienteDTO> paciente = service.findAll().stream()
                .filter(p -> p.getEmail().equals(dto.getEmail()) && p.getSenha().equals(dto.getSenha()))
                .findFirst();

        if (paciente.isPresent()) {
            return ResponseEntity.ok(paciente.get());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("E-mail ou senha inválidos.");
        }
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<?> atualizarPaciente(@PathVariable Long id, @RequestBody PacienteDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<?> deletarPaciente(@PathVariable Long id) {
        return service.delete(id);
    }
}
