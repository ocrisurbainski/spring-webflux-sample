package br.com.urbainski.springwebfluxsample.people.infra.controller;

import br.com.urbainski.springwebfluxsample.people.PeopleOperations;
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
public class PeopleControllerImpl implements PeopleController {

    private final PeopleOperations operations;
    private final PeopleRestMapper mapper;

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<PeopleDTO> insert(@RequestBody @Valid Mono<PeopleDTO> dto) {
        return dto.map(mapper::toPeople)
                .map(operations::insert)
                .flatMap(value -> value)
                .map(mapper::toPeopleDTO)
                .log();
    }

    @Override
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Mono<PeopleDTO> update(@RequestBody @Valid Mono<PeopleDTO> dto) {
        return dto.map(mapper::toPeople)
                .map(operations::update)
                .flatMap(value -> value)
                .map(mapper::toPeopleDTO)
                .log();
    }

    @Override
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<PeopleDTO> findById(@PathVariable("id") String id) {
        return operations.findById(id)
                .map(mapper::toPeopleDTO)
                .log();
    }

    @Override
    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<PeopleDTO> findAll() {
        return operations.findAll()
                .map(mapper::toPeopleDTO)
                .log();
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Void> deleteById(@PathVariable("id") String id) {
        return operations.deleteById(id).log();
    }

}
