package br.com.urbainski.springwebfluxsample.handler;

import lombok.Getter;

@Getter
public class ErrorFieldDTO extends ErrorDTO {

    private final String field;


    public ErrorFieldDTO(String field, String message) {
        super(message);
        this.field = field;
    }

}
