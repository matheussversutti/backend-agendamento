package com.agendamento_odontologico.Administrador.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class admDTO {

    private Long id;
    private String nome;
    private String email;
    private String dataNascimento;
    private String senha;
}
