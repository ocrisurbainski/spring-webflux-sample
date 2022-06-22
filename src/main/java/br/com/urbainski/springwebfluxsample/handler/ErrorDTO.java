package br.com.urbainski.springwebfluxsample.handler;

import lombok.Getter;

@Getter
public class ErrorDTO {

    private final String message;

    public ErrorDTO(String message) {
        this.message = message;
    }

}
