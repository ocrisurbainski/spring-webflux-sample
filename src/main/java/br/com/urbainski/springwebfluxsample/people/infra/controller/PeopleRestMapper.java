package br.com.urbainski.springwebfluxsample.people.infra.controller;

import br.com.urbainski.springwebfluxsample.people.People;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PeopleRestMapper {

    People toPeople(PeopleDTO dto);

}
