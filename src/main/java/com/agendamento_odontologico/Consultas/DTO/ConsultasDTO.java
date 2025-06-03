package com.agendamento_odontologico.Consultas.DTO;

import com.agendamento_odontologico.Consultas.Molde.ConsultasMolde;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConsultasDTO {
    private Long id;
    private String nome;
    private String data;
    private String horario;
    private String dr;
    private String servico;
    private Long pacienteId;
    private ConsultasMolde.Status status;
    private String dataCancelamento;
    private String motivoCancelamento;

    // Construtor para facilitar a conversão de ConsultasMolde para DTO
    public ConsultasDTO(ConsultasMolde consulta) {
        this.id = consulta.getId();
        this.nome = consulta.getNome();
        this.data = consulta.getData();
        this.horario = consulta.getHorario();
        this.dr = consulta.getDr();
        this.servico = consulta.getServico();
        this.pacienteId = consulta.getPaciente() != null ? consulta.getPaciente().getId() : null;
        this.status = consulta.getStatus();
        this.dataCancelamento = consulta.getDataCancelamento();
        this.motivoCancelamento = consulta.getMotivoCancelamento();
    }
}