package com.agendamento_odontologico.Notificacoes.Repository;

import com.agendamento_odontologico.Notificacoes.Molde.NotificacaoMolde;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacaoRepository extends JpaRepository<NotificacaoMolde, Long> {
    List<NotificacaoMolde> findByPacienteId(Long pacienteId);
}
