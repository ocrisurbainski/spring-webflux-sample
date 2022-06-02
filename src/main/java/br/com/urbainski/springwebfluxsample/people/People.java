package br.com.urbainski.springwebfluxsample.people;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class People {
    private String id;
    private String cpf;
    private String nome;
    private LocalDate dataNascimento;
}
