package br.com.urbainski.springwebfluxsample.people;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.AssertTrue;

@Service
@AllArgsConstructor
public class PeopleFacade implements PeopleOperations {

    private final PeopleRepository repository;

    @Override
    @AssertTrue
    public Mono<People> insert(People people) {
        return repository.insert(people);
    }

    @Override
    @AssertTrue
    public Mono<People> update(People people) {
        return repository.update(people);
    }

    @Override
    public Mono<People> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Flux<People> findAll() {
        return repository.findAll();
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return repository.deleteById(id);
    }

}
