package br.com.urbainski.springwebfluxsample.people.infra.controller;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class PeopleDTO {
    private String id;
    @CPF
    private String cpf;
    @NotBlank
    private String nome;
    @NotNull
    private LocalDate dataNascimento;
}
