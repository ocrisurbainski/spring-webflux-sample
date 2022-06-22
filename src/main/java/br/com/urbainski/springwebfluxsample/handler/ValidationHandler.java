package br.com.urbainski.springwebfluxsample.handler;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.util.Locale;
import java.util.stream.Collectors;

@ControllerAdvice
@AllArgsConstructor
public class ValidationHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<Mono<ResponseErrorDTO>> handleWebExchangeBindException(WebExchangeBindException ex) {
        var errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(this::convertToErrorDTO)
                .collect(Collectors.toList());
        var responseError = buildResponseError(
                HttpStatus.BAD_REQUEST,
                messageSource.getMessage("msg.validation.failed", null, Locale.getDefault()),
                errors);
        return ResponseEntity.badRequest().body(responseError);
    }

    private Mono<ResponseErrorDTO> buildResponseError(HttpStatus status, String message, Object details) {
        var responseError = new ResponseErrorDTO(message, String.valueOf(status.value()), details);
        return Mono.just(responseError);
    }

    private ErrorDTO convertToErrorDTO(ObjectError objectError) {
        String message = objectError.getDefaultMessage();

        String field = null;
        if (objectError instanceof FieldError) {
            field = ((FieldError) objectError).getField();
        }

        if (field != null) {
            return new ErrorFieldDTO(field, message);
        }

        return new ErrorDTO(message);
    }

}
