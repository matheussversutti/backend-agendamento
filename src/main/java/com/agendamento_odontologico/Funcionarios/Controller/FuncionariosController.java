package com.agendamento_odontologico.Funcionarios.Controller;

import com.agendamento_odontologico.Funcionarios.DTO.FuncionarioDTO;
import com.agendamento_odontologico.Funcionarios.Service.FuncionariosService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/funcionarios")
@CrossOrigin(origins = "*")
public class FuncionariosController {

    private final FuncionariosService service;

    public FuncionariosController(FuncionariosService service) {
        this.service = service;
    }

    @GetMapping("/listar")
    public ResponseEntity<List<FuncionarioDTO>> listarTodos() {
        List<FuncionarioDTO> funcionarios = service.findAll();
        return ResponseEntity.ok(funcionarios);
    }

    @GetMapping("/listar/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping("/criar")
    public ResponseEntity<?> criarFuncionario(@RequestBody FuncionarioDTO dto) {
        try {
            FuncionarioDTO criado = service.insert(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(criado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<?> atualizarFuncionario(@PathVariable Long id, @RequestBody FuncionarioDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<?> deletarFuncionario(@PathVariable Long id) {
        return service.delete(id);
    }
}
