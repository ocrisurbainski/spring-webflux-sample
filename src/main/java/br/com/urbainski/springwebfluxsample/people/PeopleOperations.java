package br.com.urbainski.springwebfluxsample.people;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PeopleOperations {

    Mono<People> insert(People people);

    Mono<People> update(People people);

    Mono<People> findById(String id);

    Flux<People> findAll();

    Mono<Void> deleteById(String id);

}
