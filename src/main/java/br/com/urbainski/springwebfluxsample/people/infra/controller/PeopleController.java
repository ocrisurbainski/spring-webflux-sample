package br.com.urbainski.springwebfluxsample.people.infra.controller;

import br.com.urbainski.springwebfluxsample.people.People;
import br.com.urbainski.springwebfluxsample.people.PeopleOperations;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/people")
@AllArgsConstructor
public class PeopleController {

    private final PeopleOperations operations;
    private final PeopleRestMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<People> insert(@RequestBody PeopleDTO dto) {
        var people = mapper.toPeople(dto);
        return operations.insert(people).log();
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Mono<People> update(@RequestBody PeopleDTO dto) {
        var people = mapper.toPeople(dto);
        return operations.update(people).log();
    }

    @RequestMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<People> findById(@PathVariable("id") String id) {
        return operations.findById(id).log();
    }

    @RequestMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<People> findAll() {
        return operations.findAll().log();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Void> deleteById(@PathVariable("id") String id) {
        return operations.deleteById(id).log();
    }

}
