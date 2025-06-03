package com.agendamento_odontologico.Paciente.Mapper;

import com.agendamento_odontologico.Paciente.DTO.PacienteDTO;
import com.agendamento_odontologico.Paciente.Molde.PacienteMolde;
import org.springframework.stereotype.Component;

@Component
public class PacienteMapper {

    public PacienteMolde map(PacienteDTO dto){
        PacienteMolde molde = new PacienteMolde();
        molde.setId(dto.getId());
        molde.setNome(dto.getNome());
        molde.setCpf(dto.getCpf());
        molde.setEmail(dto.getEmail());
        molde.setDataNascimento(dto.getDataNascimento());
        molde.setSenha(dto.getSenha());

        return molde;
    }
    public PacienteDTO map(PacienteMolde molde){
        PacienteDTO dto = new PacienteDTO();
        dto.setId(molde.getId());
        dto.setNome(molde.getNome());
        dto.setCpf(molde.getCpf());
        dto.setEmail(molde.getEmail());
        dto.setDataNascimento(molde.getDataNascimento());
        dto.setSenha(molde.getSenha());

        return dto;
    }
}
