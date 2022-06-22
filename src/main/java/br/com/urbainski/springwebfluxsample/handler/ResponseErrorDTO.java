package br.com.urbainski.springwebfluxsample.handler;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseErrorDTO {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime timestamp;
    private final String message;
    private final String code;
    private final Object details;

    public ResponseErrorDTO(String message, String code, Object details) {
        this.message = message;
        this.code = code;
        this.details = details;
        this.timestamp = LocalDateTime.now();
    }
}
