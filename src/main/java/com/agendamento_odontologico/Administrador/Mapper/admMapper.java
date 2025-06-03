package com.agendamento_odontologico.Administrador.Mapper;


import com.agendamento_odontologico.Administrador.DTO.admDTO;
import com.agendamento_odontologico.Administrador.Molde.admMolde;
import org.springframework.stereotype.Component;

@Component
public class admMapper {

    public admMolde map(admDTO dto){
        admMolde molde = new admMolde();
        molde.setId(dto.getId());
        molde.setName(dto.getNome());
        molde.setEmail(dto.getEmail());
        molde.setDataNascimento(dto.getDataNascimento());
        molde.setSenha(dto.getSenha());

        return molde;
    }
    public admDTO map(admMolde molde){
        admDTO dto = new admDTO();
        dto.setId(molde.getId());
        dto.setNome(molde.getName());
        dto.setEmail(molde.getEmail());
        dto.setDataNascimento(molde.getDataNascimento());
        dto.setSenha(molde.getSenha());

        return dto;
    }
}
