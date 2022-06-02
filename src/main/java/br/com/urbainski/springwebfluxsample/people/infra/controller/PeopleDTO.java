package br.com.urbainski.springwebfluxsample.people.infra.controller;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PeopleDTO {
    private String id;
    private String cpf;
    private String nome;
    private LocalDate dataNascimento;
}
