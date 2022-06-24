package br.com.urbainski.springwebfluxsample.handler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Slf4j
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

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<Mono<ResponseErrorDTO>> handleDuplicateKeyException(DuplicateKeyException ex) {

        var message = "";
        if (ex != null && ex.getMessage() != null) {
            try {
                message = ex.getMessage().substring(ex.getMessage().indexOf("index:") + 6);
                message = message.substring(0, message.indexOf("'"));
                message = message.trim();
            } catch (Exception ex2) {
                log.error(ex2.getMessage(), ex2);
                message = ex.getMessage();
            }
        }

        var field = "";
        if (!message.isEmpty()) {
            try {
                field = message.substring(message.indexOf("{") + 1);
                field = field.substring(0, field.indexOf(":"));
                field = field.trim();
            } catch (Exception ex2) {
                log.error(ex2.getMessage(), ex2);
                field = "";
            }
        }

        var errorDto = field.isEmpty() ? new ErrorDTO(message) : new ErrorFieldDTO(field, message);
        var responseError = buildResponseError(
                HttpStatus.CONFLICT,
                messageSource.getMessage("msg.duplication.key", null, Locale.getDefault()),
                List.of(errorDto));
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(responseError);
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
