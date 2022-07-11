package br.com.urbainski.springwebfluxsample.people.infra.controller;

import br.com.urbainski.springwebfluxsample.handler.ResponseErrorDTO;
import br.com.urbainski.springwebfluxsample.people.infra.controller.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PeopleController {

    @Operation(operationId = "insert", description = "Método para salvar uma nova pessoa")
    @ApiResponse(responseCode = "201", description = "Pessoa salva com sucesso")
    @ApiResponse(responseCode = "400", description = "Caso algum erro aconteça",
            content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseErrorDTO.class))
            })
    @ApiResponse(responseCode = "409", description = "Caso seja tentado inserir uma pessoa com o CPF que já existe no banco de dados",
            content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseErrorDTO.class))
            })
    Mono<ResponseEntity<CreatePeopleResponseDTO>> insert(Mono<CreatePeopleRequestDTO> dto, UriComponentsBuilder uriComponentsBuilder);

    @Operation(operationId = "update", description = "Método para atualizar uma pessoa")
    @ApiResponse(responseCode = "200", description = "Pessoa atualizada com sucesso")
    @ApiResponse(responseCode = "400", description = "Caso algum erro aconteça",
            content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseErrorDTO.class))
            })
    @ApiResponse(responseCode = "409", description = "Caso seja tentado atualizar uma pessoa com o CPF que já existe no banco de dados",
            content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseErrorDTO.class))
            })
    Mono<UpdatePeopleResponseDTO> update(Mono<UpdatePeopleRequestDTO> dto);

    @Operation(description = "Método para pesquisar uma pessoa pelo sue identificador")
    @ApiResponse(responseCode = "200", description = "Pessoa encontrado com sucesso")
    @ApiResponse(responseCode = "404", description = "Caso a pessoa não seja encontrada pelo seu identificador")
    Mono<GetPeopleByIdResponseDTO> findById(@Parameter(name = "id", description = "Identificador da pessoa a ser pesquisada") String id);

    @Operation(operationId = "findAll", description = "Método para listar todas as pessoas")
    @ApiResponse(responseCode = "200", description = "Pessoas listadas com sucesso")
    Flux<GetAllPeopleResponseDTO> findAll();

    @Operation(operationId = "deleteById", description = "Método para deletar uma pessoa")
    @ApiResponse(responseCode = "200", description = "Pessoa excluída com sucesso")
    @ApiResponse(responseCode = "404", description = "Caso a pessoa não seja encontrada pelo seu identificador")
    Mono<Void> deleteById(@Parameter(name = "id", description = "Identificador da pessoa a ser deletada") String id);

}
