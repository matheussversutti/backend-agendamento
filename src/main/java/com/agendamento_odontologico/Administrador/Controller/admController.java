package com.agendamento_odontologico.Administrador.Controller;

import com.agendamento_odontologico.Administrador.DTO.admDTO;
import com.agendamento_odontologico.Administrador.Service.impl.admServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/administrador")
@CrossOrigin(origins = "*")
public class admController {

    private admServiceImpl service;

    public admController(admServiceImpl service) {
        this.service = service;
    }

    @GetMapping("/administradores")
    public ResponseEntity<List<admDTO>> listarAdm() {
        List<admDTO> listar = service.findAll();
        return ResponseEntity.ok(listar);
    }
    @GetMapping("/listar/{id}")
    public ResponseEntity<?> listarAdmId(@PathVariable Long id){
        return service.findById(id);
    }
    @PostMapping("/criar")
    public ResponseEntity<?> criarAdm(@RequestBody admDTO dto) {
        try {
            admDTO criado = service.insert(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(criado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<?> update (@PathVariable Long id, @RequestBody admDTO dto){
        return service.update(id, dto);
    }
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<?> deletarCliente(@PathVariable Long id){
        return service.delete(id);
    }
}
