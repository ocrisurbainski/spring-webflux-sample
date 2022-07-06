package br.com.urbainski.springwebfluxsample.people.infra.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PeopleController {

    @Operation(operationId = "insert", description = "Método para salvar uma nova pessoa", responses = {
            @ApiResponse(responseCode = "201", description = "Pessoa salva com sucesso"),
            @ApiResponse(responseCode = "400", description = "Caso algum erro aconteça"),
            @ApiResponse(responseCode = "409", description = "Caso seja tentado inserir uma pessoa com o CPF que já existe no banco de dados"),
    })
    Mono<PeopleDTO> insert(Mono<PeopleDTO> dto);

    @Operation(operationId = "update", description = "Método para atualizar uma pessoa", responses = {
            @ApiResponse(responseCode = "200", description = "Pessoa atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Caso algum erro aconteça"),
            @ApiResponse(responseCode = "409", description = "Caso seja tentado atualizar uma pessoa com o CPF que já existe no banco de dados"),
    })
    Mono<PeopleDTO> update(Mono<PeopleDTO> dto);

    @Operation(description = "Método para pesquisar uma pessoa pelo sue identificador", responses = {
            @ApiResponse(responseCode = "200", description = "Pessoa encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Caso a pessoa não seja encontrada pelo seu identificador"),
    })
    Mono<PeopleDTO> findById(@Parameter(name = "id", description = "Identificador da pessoa a ser pesquisada") String id);

    @Operation(operationId = "findAll", description = "Método para listar todas as pessoas", responses = {
            @ApiResponse(responseCode = "200", description = "Pessoas listadas com sucesso"),
    })
    Flux<PeopleDTO> findAll();

    @Operation(operationId = "deleteById", description = "Método para deletar uma pessoa", responses = {
            @ApiResponse(responseCode = "200", description = "Pessoa excluída com sucesso"),
            @ApiResponse(responseCode = "404", description = "Caso a pessoa não seja encontrada pelo seu identificador"),
    })
    Mono<Void> deleteById(@Parameter(name = "id", description = "Identificador da pessoa a ser deletada") String id);

}
