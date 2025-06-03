package com.agendamento_odontologico.Consultas.Service.Impl;

import com.agendamento_odontologico.Consultas.DTO.ConsultasDTO;
import com.agendamento_odontologico.Consultas.Mapper.ConsultaMapper;
import com.agendamento_odontologico.Consultas.Molde.ConsultasMolde;
import com.agendamento_odontologico.Consultas.Repository.ConsultasRepository;
import com.agendamento_odontologico.Consultas.Service.ConsultasService;
import com.agendamento_odontologico.Paciente.Molde.PacienteMolde;
import com.agendamento_odontologico.Paciente.Repository.PacienteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConsultasServiceImpl implements ConsultasService {

    private final ConsultasRepository repository;
    private final ConsultaMapper mapper;
    private final PacienteRepository pacienteRepository;

    public ConsultasServiceImpl(ConsultasRepository repository, ConsultaMapper mapper, PacienteRepository pacienteRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.pacienteRepository = pacienteRepository;
    }

    @Override
    public List<ConsultasDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<?> findById(Long id) {
        Optional<ConsultasMolde> consulta = repository.findById(id);
        if (consulta.isPresent()) {
            return ResponseEntity.ok(mapper.map(consulta.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Consulta com ID " + id + " não foi encontrada.");
        }
    }

    @Override
    public ConsultasDTO criar(ConsultasDTO dto) {
        if (dto.getPacienteId() != null) {
            Optional<PacienteMolde> paciente = pacienteRepository.findById(dto.getPacienteId());
            if (paciente.isEmpty()) {
                throw new RuntimeException("Paciente não encontrado");
            }
        }

        ConsultasMolde consulta = mapper.map(dto);
        consulta.setStatus(ConsultasMolde.Status.AGENDADA); // Status inicial sempre AGENDADA

        if (dto.getPacienteId() != null) {
            PacienteMolde paciente = pacienteRepository.findById(dto.getPacienteId()).get();
            consulta.setPaciente(paciente);
        }

        ConsultasMolde salva = repository.save(consulta);
        return mapper.map(salva);
    }

    @Override
    public ResponseEntity<?> update(Long id, ConsultasDTO dto) {
        Optional<ConsultasMolde> consultaExistente = repository.findById(id);
        if (consultaExistente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Consulta com ID " + id + " não foi encontrada.");
        }

        ConsultasMolde consulta = consultaExistente.get();

        if (dto.getNome() != null) consulta.setNome(dto.getNome());
        if (dto.getData() != null) consulta.setData(dto.getData());
        if (dto.getHorario() != null) consulta.setHorario(dto.getHorario());
        if (dto.getDr() != null) consulta.setDr(dto.getDr());
        if (dto.getServico() != null) consulta.setServico(dto.getServico());

        ConsultasMolde salva = repository.save(consulta);
        return ResponseEntity.ok(mapper.map(salva));
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        Optional<ConsultasMolde> consulta = repository.findById(id);
        if (consulta.isPresent()) {
            repository.deleteById(id);
            return ResponseEntity.ok("Consulta com ID " + id + " foi deletada com sucesso.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Consulta com ID " + id + " não foi encontrada.");
        }
    }

    @Override
    public ResponseEntity<?> cancelarConsulta(Long id) {
        Optional<ConsultasMolde> consultaOpt = repository.findById(id);
        if (consultaOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Consulta não encontrada.");
        }

        ConsultasMolde consulta = consultaOpt.get();
        if (consulta.getStatus() == ConsultasMolde.Status.CANCELADA) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Consulta já está cancelada.");
        }

        consulta.setStatus(ConsultasMolde.Status.CANCELADA);
        consulta.setDataCancelamento(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        ConsultasMolde updated = repository.save(consulta);
        return ResponseEntity.ok(mapper.map(updated));
    }

    @Override
    public ResponseEntity<?> concluirConsulta(Long id) {
        Optional<ConsultasMolde> consultaOpt = repository.findById(id);
        if (consultaOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Consulta não encontrada.");
        }

        ConsultasMolde consulta = consultaOpt.get();
        if (consulta.getStatus() == ConsultasMolde.Status.CONCLUIDA) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Consulta já está concluída.");
        }

        if (consulta.getStatus() == ConsultasMolde.Status.CANCELADA) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Não é possível concluir uma consulta cancelada.");
        }

        consulta.setStatus(ConsultasMolde.Status.CONCLUIDA);
        ConsultasMolde updated = repository.save(consulta);
        return ResponseEntity.ok(mapper.map(updated));
    }

    @Override
    public ResponseEntity<?> reagendarConsulta(Long id, ConsultasDTO dto) {
        Optional<ConsultasMolde> consultaOpt = repository.findById(id);
        if (consultaOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Consulta não encontrada.");
        }

        ConsultasMolde consulta = consultaOpt.get();
        if (consulta.getStatus() != ConsultasMolde.Status.CANCELADA) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Apenas consultas canceladas podem ser reagendadas.");
        }

        consulta.setData(dto.getData());
        consulta.setHorario(dto.getHorario());
        consulta.setStatus(ConsultasMolde.Status.AGENDADA);
        consulta.setDataCancelamento(null); // Limpa a data de cancelamento

        ConsultasMolde updated = repository.save(consulta);
        return ResponseEntity.ok(mapper.map(updated));
    }
}