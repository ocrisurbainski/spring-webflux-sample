package br.com.urbainski.springwebfluxsample.people.infra.mongo;

import br.com.urbainski.springwebfluxsample.exception.NotFoundDocumentException;
import br.com.urbainski.springwebfluxsample.people.People;
import br.com.urbainski.springwebfluxsample.people.PeopleRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@AllArgsConstructor
public class MongoPeopleRepository implements PeopleRepository {

    private final SpringPeopleRepository repository;
    private final PeopleMapper mapper;

    @Override
    public Mono<People> insert(People people) {
        return save(people, true);
    }

    @Override
    public Mono<People> update(People people) {
        return save(people, false);
    }

    @Override
    public Mono<People> findById(String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundDocumentException()))
                .map(mapper::toPeople)
                .doOnError(subscription -> log.error("Erro no ::findById::", subscription.getCause()));
    }

    @Override
    public Flux<People> findAll() {
        return repository.findAll()
                .map(mapper::toPeople)
                .doOnError(subscription -> log.error("Erro no ::findAll::", subscription.getCause()));
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundDocumentException()))
                .then(repository.deleteById(id));
    }

    private Mono<People> save(People people, boolean isInsert) {
        var labelAcao = isInsert ? "cadastrar" : "atualizar";
        var peopleDocument = mapper.toPeopleDocument(people);
        return repository.save(peopleDocument)
                .map(mapper::toPeople)
                .doOnSuccess(pd -> log.info("Sucesso ao {} nova pessoa com o id: {}", labelAcao, pd.getId()))
                .doOnError(subscription -> log.error("Erro ao {} nova pessoa com o id", labelAcao, subscription.getCause()));
    }

}
