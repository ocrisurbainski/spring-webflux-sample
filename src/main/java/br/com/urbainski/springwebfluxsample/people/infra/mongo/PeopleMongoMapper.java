package br.com.urbainski.springwebfluxsample.people.infra.mongo;

import br.com.urbainski.springwebfluxsample.people.People;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PeopleMongoMapper {

    PeopleDocument toPeopleDocument(People people);

    People toPeople(PeopleDocument peopleDocument);

}
