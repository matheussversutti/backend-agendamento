package com.agendamento_odontologico.Paciente.Molde;


import com.agendamento_odontologico.Consultas.Molde.ConsultasMolde;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "paciente")
public class PacienteMolde {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NOME")
    private String nome;

    @Column(name = "CPF", unique = true)
    private String cpf;

    @Column(name = "E-MAIL", unique = true)
    private String email;

    @Column(name = "DATA_NASCIMENTO")
    private String dataNascimento;

    @Column(name = "SENHA")
    private String senha;

    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConsultasMolde> consultas = new ArrayList<>();

}
