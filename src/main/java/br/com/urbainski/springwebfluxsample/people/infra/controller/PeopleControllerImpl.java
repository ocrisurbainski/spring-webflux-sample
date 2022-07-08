package br.com.urbainski.springwebfluxsample.people.infra.controller;

import br.com.urbainski.springwebfluxsample.people.PeopleOperations;
import br.com.urbainski.springwebfluxsample.people.infra.controller.dto.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/peoples")
public class PeopleControllerImpl implements PeopleController {

    private final PeopleOperations operations;
    private final PeopleRestMapper mapper;

    @Override
    @PostMapping
    public Mono<ResponseEntity<CreatePeopleResponseDTO>> insert(
            @RequestBody @Valid Mono<CreatePeopleRequestDTO> dto, UriComponentsBuilder uriComponentsBuilder) {
        return dto.map(mapper::toPeople)
                .map(operations::insert)
                .flatMap(value -> value)
                .map(mapper::toCreatePeopleResponseDTO)
                .map(peopleDTO -> {
                    var uri = uriComponentsBuilder
                            .path("/peoples/{id}")
                            .buildAndExpand(peopleDTO.getId())
                            .toUri();
                    return ResponseEntity.created(uri).body(peopleDTO);
                })
                .log();
    }

    @Override
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Mono<UpdatePeopleResponseDTO> update(@RequestBody @Valid Mono<UpdatePeopleRequestDTO> dto) {
        return dto.map(mapper::toPeople)
                .map(operations::update)
                .flatMap(value -> value)
                .map(mapper::toUpdatePeopleResponseDTO)
                .log();
    }

    @Override
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<PeopleResponseDTO> findById(@PathVariable("id") String id) {
        return operations.findById(id)
                .map(mapper::toPeopleResponseDTO)
                .log();
    }

    @Override
    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<PeopleResponseDTO> findAll() {
        return operations.findAll()
                .map(mapper::toPeopleResponseDTO)
                .log();
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Void> deleteById(@PathVariable("id") String id) {
        return operations.deleteById(id).log();
    }

}
