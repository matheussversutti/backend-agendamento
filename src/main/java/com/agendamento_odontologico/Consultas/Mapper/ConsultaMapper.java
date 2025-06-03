package com.agendamento_odontologico.Consultas.Mapper;

import com.agendamento_odontologico.Consultas.DTO.ConsultasDTO;
import com.agendamento_odontologico.Consultas.Molde.ConsultasMolde;
import com.agendamento_odontologico.Paciente.Molde.PacienteMolde;
import org.springframework.stereotype.Component;

@Component
public class ConsultaMapper {


    public ConsultasMolde map(ConsultasDTO dto) {
        ConsultasMolde molde = new ConsultasMolde();
        molde.setId(dto.getId());
        molde.setNome(dto.getNome());
        molde.setData(dto.getData());
        molde.setHorario(dto.getHorario());
        molde.setDr(dto.getDr());
        molde.setServico(dto.getServico());
        molde.setStatus(dto.getStatus() != null ? dto.getStatus() : ConsultasMolde.Status.AGENDADA);
        molde.setDataCancelamento(dto.getDataCancelamento());
        molde.setMotivoCancelamento(dto.getMotivoCancelamento());

        if (dto.getPacienteId() != null) {
            PacienteMolde paciente = new PacienteMolde();
            paciente.setId(dto.getPacienteId());
            molde.setPaciente(paciente);
        }
        return molde;
    }

    public ConsultasDTO map(ConsultasMolde molde) {
        ConsultasDTO dto = new ConsultasDTO();
        dto.setId(molde.getId());
        dto.setNome(molde.getNome());
        dto.setData(molde.getData());
        dto.setHorario(molde.getHorario());
        dto.setDr(molde.getDr());
        dto.setServico(molde.getServico());
        dto.setStatus(molde.getStatus());
        dto.setDataCancelamento(molde.getDataCancelamento());
        dto.setMotivoCancelamento(molde.getMotivoCancelamento());

        if (molde.getPaciente() != null) {
            dto.setPacienteId(molde.getPaciente().getId());
        }

        return dto;
    }
}
// ... existing code ...