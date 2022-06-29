package br.com.urbainski.springwebfluxsample.people;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class People {
    private String id;
    private String cpf;
    private String nome;
    private LocalDate dataNascimento;
}
