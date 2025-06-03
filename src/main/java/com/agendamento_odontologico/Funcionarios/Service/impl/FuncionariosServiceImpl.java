package com.agendamento_odontologico.Funcionarios.Service.impl;

import com.agendamento_odontologico.Consultas.Molde.ConsultasMolde;
import com.agendamento_odontologico.Consultas.Repository.ConsultasRepository;
import com.agendamento_odontologico.Funcionarios.DTO.FuncionarioDTO;
import com.agendamento_odontologico.Funcionarios.Mapper.FuncionariosMapper;
import com.agendamento_odontologico.Funcionarios.Molde.FuncionarioMolde;
import com.agendamento_odontologico.Funcionarios.Repository.FuncionariosRepository;
import com.agendamento_odontologico.Funcionarios.Service.FuncionariosService;
import com.agendamento_odontologico.Notificacoes.Molde.NotificacaoMolde;
import com.agendamento_odontologico.Notificacoes.Repository.NotificacaoRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FuncionariosServiceImpl implements FuncionariosService {

    private final FuncionariosRepository repository;
    private final FuncionariosMapper mapper;
    private final ConsultasRepository consultasRepository;
    private final NotificacaoRepository notificacaoRepository;

    public FuncionariosServiceImpl(FuncionariosRepository repository, FuncionariosMapper mapper,  ConsultasRepository consultasRepository, NotificacaoRepository notificacaoRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.consultasRepository = consultasRepository;
        this.notificacaoRepository = notificacaoRepository;

    }

    @Override
    public List<FuncionarioDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<?> findById(Long id) {
        Optional<FuncionarioMolde> funcionario = repository.findById(id);
        if (funcionario.isPresent()) {
            FuncionarioDTO dto = mapper.map(funcionario.get());
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Funcionário com ID " + id + " não foi encontrado.");
        }
    }

    @Override
    public FuncionarioDTO insert(FuncionarioDTO dto) {
        if (repository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("E-mail já cadastrado!");
        }
        if (repository.findByCpf(dto.getCpf()).isPresent()) {
            throw new RuntimeException("CPF já cadastrado!");
        }
        if (repository.findByCro(dto.getCro()).isPresent()) {
            throw new RuntimeException("CRO já cadastrado!");
        }

        FuncionarioMolde novoFuncionario = mapper.map(dto);
        FuncionarioMolde salvo = repository.save(novoFuncionario);
        return mapper.map(salvo);
    }

    @Override
    public ResponseEntity<?> update(Long id, FuncionarioDTO dto) {
        Optional<FuncionarioMolde> funcionarioExistente = repository.findById(id);
        if (funcionarioExistente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Funcionário com ID " + id + " não foi encontrado.");
        }

        FuncionarioMolde funcionario = funcionarioExistente.get();

        // Verificação de CPF duplicado
        if (dto.getCpf() != null && !dto.getCpf().equals(funcionario.getCpf())) {
            Optional<FuncionarioMolde> cpfExistente = repository.findByCpf(dto.getCpf());
            if (cpfExistente.isPresent() && !cpfExistente.get().getId().equals(id)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("CPF já cadastrado!");
            }
            funcionario.setCpf(dto.getCpf());
        }

        // Verificação de CRO duplicado
        if (dto.getCro() != null && !dto.getCro().equals(funcionario.getCro())) {
            Optional<FuncionarioMolde> croExistente = repository.findByCro(dto.getCro());
            if (croExistente.isPresent() && !croExistente.get().getId().equals(id)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("CRO já cadastrado!");
            }
            funcionario.setCro(dto.getCro());
        }

        // Verificação de e-mail duplicado
        if (dto.getEmail() != null && !dto.getEmail().equals(funcionario.getEmail())) {
            Optional<FuncionarioMolde> emailExistente = repository.findByEmail(dto.getEmail());
            if (emailExistente.isPresent() && !emailExistente.get().getId().equals(id)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("E-mail já cadastrado!");
            }
            funcionario.setEmail(dto.getEmail());
        }

        // Demais campos
        if (dto.getNome() != null) {
            funcionario.setNome(dto.getNome());
        }

        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            funcionario.setSenha(dto.getSenha());
        }

        FuncionarioMolde salvo = repository.save(funcionario);
        return ResponseEntity.ok(mapper.map(salvo));
    }

    @Override
    @Transactional
    public ResponseEntity<?> delete(Long id) {
        Optional<FuncionarioMolde> funcionario = repository.findById(id);
        if (funcionario.isPresent()) {
            String nomeDr = "Dr(a) " + funcionario.get().getNome();
            List<ConsultasMolde> consultas = consultasRepository.findByDrContaining(nomeDr);

            // Agrupar consultas por paciente para evitar notificações duplicadas
            Map<Long, List<ConsultasMolde>> consultasPorPaciente = consultas.stream()
                    .filter(c -> c.getPaciente() != null)
                    .collect(Collectors.groupingBy(c -> c.getPaciente().getId()));

            // Atualizar consultas e criar notificações
            for (ConsultasMolde consulta : consultas) {
                consulta.setStatus(ConsultasMolde.Status.CANCELADA);
                consulta.setDataCancelamento(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                consulta.setMotivoCancelamento("Especialista não está mais disponível no sistema");
            }

            // Salvar consultas atualizadas
            consultasRepository.saveAll(consultas);

            // Criar notificações para cada paciente
            for (Map.Entry<Long, List<ConsultasMolde>> entry : consultasPorPaciente.entrySet()) {
                Long pacienteId = entry.getKey();
                List<ConsultasMolde> consultasPaciente = entry.getValue();

                // Criar notificação
                NotificacaoMolde notificacao = new NotificacaoMolde();
                notificacao.setPacienteId(pacienteId);
                notificacao.setTitulo("Consultas Canceladas");
                notificacao.setMensagem(
                        String.format("Infelizmente o %s não está mais disponível no sistema.\n\n" +
                                        "Suas consultas foram canceladas:\n%s\n\n" +
                                        "Por favor, entre em contato com a clínica para reagendar com outro especialista.",
                                nomeDr,
                                consultasPaciente.stream()
                                        .map(c -> String.format("- %s às %s: %s",
                                                c.getData(), c.getHorario(), c.getServico()))
                                        .collect(Collectors.joining("\n"))
                        )
                );
                notificacao.setData(LocalDateTime.now());
                notificacao.setLida(false);

                notificacaoRepository.save(notificacao);
            }

            // Deletar o funcionário
            repository.deleteById(id);

            return ResponseEntity.ok("Funcionário com ID " + id + " foi deletado com sucesso.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Funcionário com ID " + id + " não foi encontrado.");
        }
    }
}

