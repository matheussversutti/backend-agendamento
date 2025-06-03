package com.agendamento_odontologico.Notificacoes.Molde;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "notificacoes")
public class NotificacaoMolde {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "paciente_id")
    private Long pacienteId;

    private String titulo;

    @Column(length = 1000)
    private String mensagem;

    private LocalDateTime data;

    private boolean lida;
}
