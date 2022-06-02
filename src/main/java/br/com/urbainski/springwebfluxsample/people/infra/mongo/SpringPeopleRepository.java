package br.com.urbainski.springwebfluxsample.people.infra.mongo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface SpringPeopleRepository extends ReactiveMongoRepository<PeopleDocument, String> {
}
