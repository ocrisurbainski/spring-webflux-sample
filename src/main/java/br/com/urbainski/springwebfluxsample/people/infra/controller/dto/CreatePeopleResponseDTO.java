package br.com.urbainski.springwebfluxsample.people.infra.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class CreatePeopleResponseDTO {

    @Schema(description = "Identificador da pessoa", example = "1")
    private String id;

    @CPF
    @Schema(description = "CPF da pessoa", example = "22010576055")
    private String cpf;

    @NotBlank
    @Schema(description = "Nome da pessoa", example = "Fulano da Silva")
    private String nome;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Schema(description = "Data de nascimento da pessoa", example = "2000-01-01")
    private LocalDate dataNascimento;

}
