package com.agendamento_odontologico.Paciente.Service.ipml;

import com.agendamento_odontologico.Consultas.Repository.ConsultasRepository;
import com.agendamento_odontologico.Paciente.DTO.PacienteDTO;
import com.agendamento_odontologico.Paciente.Mapper.PacienteMapper;
import com.agendamento_odontologico.Paciente.Molde.PacienteMolde;
import com.agendamento_odontologico.Paciente.Repository.PacienteRepository;
import com.agendamento_odontologico.Paciente.Service.PacienteService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PacienteServiceIpml implements PacienteService {

    private final PacienteRepository repository;
    private final PacienteMapper mapper;

    public PacienteServiceIpml(PacienteRepository repository, PacienteMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }
    @Override
    public List<PacienteDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::map)
                .collect(Collectors.toList());
    }
    @Override
    public ResponseEntity<?> findById(Long id) {
        Optional<PacienteMolde> paciente = repository.findById(id);
        if (paciente.isPresent()) {
            PacienteDTO dto = mapper.map(paciente.get());
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Paciente com ID " + id + " não encontrado.");
        }
    }
    @Override
    public PacienteDTO insert(PacienteDTO dto) {
        if (repository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("E-mail já cadastrado!");
        }
        if (repository.findByCpf(dto.getCpf()).isPresent()) {
            throw new RuntimeException("CPF já cadastrado!");
        }
        PacienteMolde novoPaciente = mapper.map(dto);
        PacienteMolde salvo = repository.save(novoPaciente);
        return mapper.map(salvo);
    }

    @Override
    public ResponseEntity<?> update(Long id, PacienteDTO dto) {
        Optional<PacienteMolde> pacienteExistente = repository.findById(id);
        if (pacienteExistente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Paciente com ID " + id + " não foi encontrado.");
        }
        PacienteMolde paciente = pacienteExistente.get();

        // ⚠️ Verifica se o novo e-mail já está em uso por outro paciente
        if (dto.getEmail() != null && !dto.getEmail().equals(paciente.getEmail())) {
            Optional<PacienteMolde> existente = repository.findByEmail(dto.getEmail());
            if (existente.isPresent() && !existente.get().getId().equals(id)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("E-mail já cadastrado!");
            }
            paciente.setEmail(dto.getEmail());
        }

        // Atualizar nome
        if (dto.getNome() != null) {
            paciente.setNome(dto.getNome());
        }

        // Atualizar senha
        if (dto.getSenha() != null) {
            paciente.setSenha(dto.getSenha());
        }

        // Atualizar data de nascimento
        if (dto.getDataNascimento() != null) {
            paciente.setDataNascimento(dto.getDataNascimento());
        }

        PacienteMolde salvo = repository.save(paciente);
        return ResponseEntity.ok(mapper.map(salvo));
    }


@Override
    @Transactional  // Adicionar esta anotação
    public ResponseEntity<?> delete(Long id) {
        Optional<PacienteMolde> paciente = repository.findById(id);
        if (paciente.isPresent()) {
            // Com a configuração correta do JPA, as consultas serão deletadas automaticamente
            repository.deleteById(id);
            return ResponseEntity.ok("Paciente e suas consultas foram deletados com sucesso.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Paciente com ID " + id + " não foi encontrado.");
        }
    }
}

