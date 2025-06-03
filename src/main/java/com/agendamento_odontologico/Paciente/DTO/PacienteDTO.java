package com.agendamento_odontologico.Paciente.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PacienteDTO {

    private Long id;
    private String cpf;
    private String email;
    private String dataNascimento;
    private String senha;
    private String nome;
}
