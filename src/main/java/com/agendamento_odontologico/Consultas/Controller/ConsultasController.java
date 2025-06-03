package com.agendamento_odontologico.Consultas.Controller;

import com.agendamento_odontologico.Consultas.DTO.ConsultasDTO;
import com.agendamento_odontologico.Consultas.Molde.ConsultasMolde;
import com.agendamento_odontologico.Consultas.Repository.ConsultasRepository;
import com.agendamento_odontologico.Consultas.Service.ConsultasService;
import com.agendamento_odontologico.Notificacoes.Molde.NotificacaoMolde;
import com.agendamento_odontologico.Notificacoes.Repository.NotificacaoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/consultas")
@CrossOrigin(origins = "*")
public class ConsultasController {

    private final ConsultasService service;
    private final ConsultasRepository consultasRepository;
    private final NotificacaoRepository notificacaoRepository;

    public ConsultasController(ConsultasService service, ConsultasRepository consultasRepository, NotificacaoRepository notificacaoRepository) {
        this.service = service;
        this.consultasRepository = consultasRepository;
        this.notificacaoRepository = notificacaoRepository;

    }

    @GetMapping("/listar")
    public ResponseEntity<List<ConsultasDTO>> listarTodas() {
        List<ConsultasDTO> consultas = service.findAll();
        return ResponseEntity.ok(consultas);
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping("/criar")
    public ResponseEntity<?> criarConsulta(@RequestBody ConsultasDTO dto) {
        try {
            ConsultasDTO consultaCriada = service.criar(dto);
            return ResponseEntity.ok(consultaCriada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<?> atualizarConsulta(@PathVariable Long id, @RequestBody ConsultasDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<?> deletarConsulta(@PathVariable Long id) {
        return service.delete(id);
    }

    // Novos endpoints para gerenciar status

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelarConsulta(@PathVariable Long id) {
        return service.cancelarConsulta(id);
    }

    @PutMapping("/{id}/concluir")
    public ResponseEntity<?> concluirConsulta(@PathVariable Long id) {
        return service.concluirConsulta(id);
    }

    @PutMapping("/{id}/reagendar")
    public ResponseEntity<?> reagendarConsulta(@PathVariable Long id, @RequestBody ConsultasDTO dto) {
        return service.reagendarConsulta(id, dto);
    }

    @GetMapping("/verificar-cancelamentos/{pacienteId}")
    public ResponseEntity<?> verificarConsultasCanceladas(@PathVariable Long pacienteId) {
        List<ConsultasMolde> consultas = consultasRepository.findByPacienteId(pacienteId);

        // Filtrar apenas consultas que foram canceladas por exclusão do funcionário
        List<ConsultasMolde> consultasCanceladas = consultas.stream()
                .filter(c -> c.getStatus() == ConsultasMolde.Status.CANCELADA &&
                        c.getMotivoCancelamento() != null &&
                        c.getMotivoCancelamento().contains("não está mais disponível no sistema"))
                .collect(Collectors.toList());

        if (!consultasCanceladas.isEmpty()) {
            // Agrupar por funcionário
            Map<String, List<ConsultasMolde>> consultasPorFuncionario = consultasCanceladas.stream()
                    .collect(Collectors.groupingBy(ConsultasMolde::getDr));

            // Criar notificações para cada funcionário
            for (Map.Entry<String, List<ConsultasMolde>> entry : consultasPorFuncionario.entrySet()) {
                String nomeDr = entry.getKey();
                List<ConsultasMolde> consultasDoDr = entry.getValue();

                // Criar notificação
                NotificacaoMolde notificacao = new NotificacaoMolde();
                notificacao.setPacienteId(pacienteId);
                notificacao.setTitulo("Consultas Canceladas");
                notificacao.setMensagem(
                        String.format("Infelizmente o %s não está mais disponível no sistema.\n\n" +
                                        "Suas consultas foram canceladas:\n%s\n\n" +
                                        "Por favor, entre em contato com a clínica para reagendar com outro especialista.",
                                nomeDr,
                                consultasDoDr.stream()
                                        .map(c -> String.format("- %s às %s: %s",
                                                c.getData(), c.getHorario(), c.getServico()))
                                        .collect(Collectors.joining("\n"))
                        )
                );
                notificacao.setData(LocalDateTime.now());
                notificacao.setLida(false);

                notificacaoRepository.save(notificacao);
            }
        }

        return ResponseEntity.ok(consultasCanceladas);
    }
}