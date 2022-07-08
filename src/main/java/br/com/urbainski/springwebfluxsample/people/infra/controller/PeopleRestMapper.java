package br.com.urbainski.springwebfluxsample.people.infra.controller;

import br.com.urbainski.springwebfluxsample.people.People;
import br.com.urbainski.springwebfluxsample.people.infra.controller.dto.*;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PeopleRestMapper {

    PeopleResponseDTO toPeopleResponseDTO(People people);

    People toPeople(CreatePeopleRequestDTO dto);

    CreatePeopleResponseDTO toCreatePeopleResponseDTO(People people);

    People toPeople(UpdatePeopleRequestDTO dto);

    UpdatePeopleResponseDTO toUpdatePeopleResponseDTO(People people);

}
