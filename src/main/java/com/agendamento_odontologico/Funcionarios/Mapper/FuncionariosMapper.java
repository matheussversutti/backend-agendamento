package com.agendamento_odontologico.Funcionarios.Mapper;

import com.agendamento_odontologico.Funcionarios.DTO.FuncionarioDTO;
import com.agendamento_odontologico.Funcionarios.Molde.FuncionarioMolde;
import org.springframework.stereotype.Component;

@Component
public class FuncionariosMapper {


    public FuncionarioMolde map(FuncionarioDTO dto) {
        FuncionarioMolde molde = new FuncionarioMolde();
        molde.setId(dto.getId());
        molde.setNome(dto.getNome());
        molde.setCpf(dto.getCpf());
        molde.setEmail(dto.getEmail());
        molde.setSenha(dto.getSenha());
        molde.setCro(dto.getCro());

        return molde;
    }

    public FuncionarioDTO map(FuncionarioMolde molde) {
        FuncionarioDTO dto = new FuncionarioDTO();
        dto.setId(molde.getId());
        dto.setNome(molde.getNome());
        dto.setCpf(molde.getCpf());
        dto.setEmail(molde.getEmail());
        dto.setSenha(molde.getSenha());
        dto.setCro(molde.getCro());

        return dto;
    }
}
