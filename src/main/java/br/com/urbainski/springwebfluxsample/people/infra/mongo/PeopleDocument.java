package br.com.urbainski.springwebfluxsample.people.infra.mongo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Getter
@Setter
@Document("peoples")
public class PeopleDocument {
    @Id
    private String id;
    @Indexed(name = "idx_peoples_cpf_unique", unique = true)
    private String cpf;
    private String nome;
    private LocalDate dataNascimento;
}
