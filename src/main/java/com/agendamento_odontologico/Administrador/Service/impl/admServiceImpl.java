package com.agendamento_odontologico.Administrador.Service.impl;


import com.agendamento_odontologico.Administrador.DTO.admDTO;
import com.agendamento_odontologico.Administrador.Mapper.admMapper;
import com.agendamento_odontologico.Administrador.Molde.admMolde;
import com.agendamento_odontologico.Administrador.Repository.admRepository;
import com.agendamento_odontologico.Administrador.Service.admService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class admServiceImpl implements admService {

    private admRepository repository;
    private admMapper mapper;

    public admServiceImpl(admRepository repository, admMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<admDTO> findAll(){
        List<admMolde> adm = repository.findAll();
        return adm.stream()
                .map(mapper::map)
                .collect(Collectors.toList());
    }
    @Override
    public ResponseEntity<?> findById(Long id) {
        Optional<admMolde> adm = repository.findById(id);
        if (adm.isPresent()) {
            admDTO dto = mapper.map(adm.get());  // Change the type to admDTO
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("ADM COM ID " + id + " NÃO ENCONTRADO");
        }
    }
    @Override
    public admDTO insert(admDTO dto) {
        if (repository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("E-mail já cadastrado!");
        }
        admMolde cliente = mapper.map(dto);
        admMolde salvo = repository.save(cliente);
        return mapper.map(salvo);
    }

    @Override
    public ResponseEntity<?> update(Long id, admDTO dto){
        Optional<admMolde> admExistente = repository.findById(id);
        if (admExistente.isPresent()) {
            admMolde existente = admExistente.get();
            if (dto.getNome() != null) existente.setName(dto.getNome());
            if (dto.getEmail() != null) existente.setEmail(dto.getEmail());
            if (dto.getDataNascimento() != null) existente.setDataNascimento(dto.getDataNascimento());
            if (dto.getSenha() != null) existente.setSenha(dto.getSenha());
            admMolde salvo = repository.save(existente);
            return ResponseEntity.ok(mapper.map(salvo)); // se quiser retornar um DTO
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("CLIENTE COM ID " + id + " NÃO FOI ENCONTRADO!");
        }
    }
    @Override
    public ResponseEntity<?> delete(Long id){
        Optional<admMolde> adm = repository.findById(id);
        if (adm.isPresent()){
            repository.deleteById(id);
            return ResponseEntity.ok("CLIENTE COM ID " + id + " FOI DELETADO COM SUCESSO!");
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("CLIENTE COM ID " + id + " NÃO FOI ENCONTRADO!");
        }
    }

}
