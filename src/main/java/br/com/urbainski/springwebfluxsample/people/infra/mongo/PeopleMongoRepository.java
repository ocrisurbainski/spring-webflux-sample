package br.com.urbainski.springwebfluxsample.people.infra.mongo;

import br.com.urbainski.springwebfluxsample.exception.NotFoundDocumentException;
import br.com.urbainski.springwebfluxsample.people.People;
import br.com.urbainski.springwebfluxsample.people.PeopleRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@AllArgsConstructor
public class PeopleMongoRepository implements PeopleRepository {

    private final PeopleSpringRepository repository;
    private final PeopleMongoMapper mapper;

    @Override
    public Mono<People> insert(People people) {
        return Mono.just(people)
                .map(mapper::toPeopleDocument)
                .flatMap(repository::save)
                .map(mapper::toPeople)
                .doOnSuccess(pd -> log.info("Sucesso ao cadastrar nova pessoa com o id: {}", pd.getId()))
                .doOnError(throwable -> onErrorInsert(throwable, people));
    }

    @Override
    public Mono<People> update(People people) {
        return Mono.just(people)
                .map(mapper::toPeopleDocument)
                .flatMap(repository::save)
                .map(mapper::toPeople)
                .doOnSuccess(pd -> log.info("Sucesso ao atualizar nova pessoa com o id: {}", pd.getId()))
                .doOnError(throwable -> onErrorUpdate(throwable, people));
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

    private void onErrorInsert(Throwable throwable, People people) {
        var logMessage = "Erro ao cadastrar nova pessoa: {}, causa: {}";
        var json = ToStringBuilder.reflectionToString(people, ToStringStyle.JSON_STYLE);
        log.error(logMessage, json, throwable.getMessage());
    }

    private void onErrorUpdate(Throwable throwable, People people) {
        var logMessage = "Erro ao atualizar pessoa: {}, causa: {}";
        var json = ToStringBuilder.reflectionToString(people, ToStringStyle.JSON_STYLE);
        log.error(logMessage, json, throwable.getMessage());
    }

}
