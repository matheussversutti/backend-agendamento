package com.agendamento_odontologico.Funcionarios.Molde;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "FUNCIONARIOS")
public class FuncionarioMolde {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NOME")
    private String nome;

    @Column(name = "CPF", unique = true)
    private String cpf;

    @Column(name = "EMAIL" , unique = true)
    private String email;

    @Column(name = "SENHA")
    private String senha;

    @Column(name = "CRO", unique = true)
    private String cro;
}
