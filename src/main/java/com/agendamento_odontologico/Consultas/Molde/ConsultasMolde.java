package com.agendamento_odontologico.Consultas.Molde;

import com.agendamento_odontologico.Paciente.Molde.PacienteMolde;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "CONSULTAS")
public class ConsultasMolde {

    public enum Status {
        AGENDADA,
        CONCLUIDA,
        CANCELADA
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NOME_PACIENTE", nullable = false)
    private String nome;

    @Column(name = "DATA_CONSULTA", nullable = false)
    private String data;

    @Column(name = "HORARIO", nullable = false)
    private String horario;

    @Column(name = "NOME_DR", nullable = false)
    private String dr;

    @Column(name = "SERVIÇO", nullable = false)
    private String servico;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    private Status status = Status.AGENDADA; // Valor padrão é AGENDADA

    @Column(name = "DATA_CANCELAMENTO")
    private String dataCancelamento;

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private PacienteMolde paciente;

    @Column(name = "motivo_cancelamento")
    private String motivoCancelamento;
}