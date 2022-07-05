package br.com.urbainski.springwebfluxsample.people.infra.controller;

import br.com.urbainski.springwebfluxsample.people.PeopleOperations;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/peoples")
public class PeopleController {

    private final PeopleOperations operations;
    private final PeopleRestMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(operationId = "insert", description = "Método para salvar uma nova pessoa", responses = {
            @ApiResponse(responseCode = "201", description = "Pessoa salva com sucesso"),
            @ApiResponse(responseCode = "500", description = "Caso algum erro aconteca")
    })
    public Mono<PeopleDTO> insert(@RequestBody @Valid Mono<PeopleDTO> dto) {
        return dto.map(mapper::toPeople)
                .map(operations::insert)
                .flatMap(value -> value)
                .map(mapper::toPeopleDTO)
                .log();
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(operationId = "update", description = "Método para atualizar uma pessoa", responses = {
            @ApiResponse(responseCode = "200", description = "Pessoa atualizada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Caso algum erro aconteca")
    })
    public Mono<PeopleDTO> update(@RequestBody @Valid Mono<PeopleDTO> dto) {
        return dto.map(mapper::toPeople)
                .map(operations::update)
                .flatMap(value -> value)
                .map(mapper::toPeopleDTO)
                .log();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Método para pesquisar uma pessoa pelo sue identificador", responses = {
            @ApiResponse(responseCode = "200", description = "Pessoa encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Caso a pessoa não seja encontrada pelo seu identificador"),
            @ApiResponse(responseCode = "500", description = "Caso algum outro erro aconteca")
    })
    public Mono<PeopleDTO> findById(@PathVariable("id") String id) {
        return operations.findById(id)
                .map(mapper::toPeopleDTO)
                .log();
    }

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(operationId = "findAll", description = "Método para listar todas as pessoas", responses = {
            @ApiResponse(responseCode = "200", description = "Pessoas listadas com sucesso")
    })
    public Flux<PeopleDTO> findAll() {
        return operations.findAll()
                .map(mapper::toPeopleDTO)
                .log();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(operationId = "deleteById", description = "Método para deletar uma pessoa", responses = {
            @ApiResponse(responseCode = "200", description = "Pessoa excluída com sucesso"),
            @ApiResponse(responseCode = "404", description = "Caso a pessoa não seja encontrada pelo seu identificador"),
            @ApiResponse(responseCode = "500", description = "Caso algum outro erro aconteca")
    })
    public Mono<Void> deleteById(@PathVariable("id") String id) {
        return operations.deleteById(id).log();
    }

}
