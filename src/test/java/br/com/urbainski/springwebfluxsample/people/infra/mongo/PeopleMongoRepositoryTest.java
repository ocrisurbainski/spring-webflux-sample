package br.com.urbainski.springwebfluxsample.people.infra.mongo;

import br.com.urbainski.springwebfluxsample.exception.NotFoundDocumentException;
import br.com.urbainski.springwebfluxsample.people.People;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DuplicateKeyException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataMongoTest
@Import({PeopleMongoRepository.class, PeopleMongoMapperImpl.class})
public class PeopleMongoRepositoryTest {

    @Autowired
    private PeopleMongoRepository peopleMongoRepository;

    @Autowired
    private PeopleSpringRepository peopleSpringRepository;

    @BeforeEach
    public void beforeEachTest() {
        var mono = peopleSpringRepository.deleteAll();

        StepVerifier.create(mono)
                .expectComplete()
                .verify();
    }

    @Test
    public void shouldSaveAPeople() {
        var people = People.builder()
                .nome("Fulano")
                .cpf("64969493014")
                .dataNascimento(LocalDate.now())
                .build();

        var mono = peopleMongoRepository.insert(people);

        StepVerifier.create(mono)
                .assertNext(peopleDb -> {
                    assertNotNull(peopleDb.getId());
                    assertEquals("64969493014", peopleDb.getCpf());
                    assertEquals("Fulano", peopleDb.getNome());
                    assertEquals(LocalDate.now(), peopleDb.getDataNascimento());
                })
                .expectComplete()
                .verify();
    }

    @Test
    public void shouldUpdateAPeople() {
        var people = People.builder()
                .nome("Fulano")
                .cpf("80078893020")
                .dataNascimento(LocalDate.now())
                .build();

        var monoSave = peopleMongoRepository.insert(people);

        final var map = new HashMap<String, People>();

        StepVerifier.create(monoSave)
                .assertNext(peopleSaved -> {
                    assertNotNull(peopleSaved);

                    map.put("people", peopleSaved);
                })
                .expectComplete()
                .verify();

        var peopleSaved = map.get("people");

        assertEquals("Fulano", peopleSaved.getNome());

        peopleSaved.setNome("Fulano da Silva");

        var mono = peopleMongoRepository.update(peopleSaved);

        StepVerifier.create(mono)
                .assertNext(peopleDb -> {
                    assertNotNull(peopleDb.getId());
                    assertEquals("80078893020", peopleDb.getCpf());
                    assertEquals("Fulano da Silva", peopleDb.getNome());
                    assertEquals(LocalDate.now(), peopleDb.getDataNascimento());
                })
                .expectComplete()
                .verify();
    }

    @Test
    public void shouldFindPeopleById() {
        var people = People.builder()
                .nome("Fulano")
                .cpf("75198811050")
                .dataNascimento(LocalDate.now())
                .build();

        var monoSave = peopleMongoRepository.insert(people);

        final var map = new HashMap<String, String>();

        StepVerifier.create(monoSave)
                .assertNext(peopleSaved -> {
                    assertNotNull(peopleSaved);
                    assertNotNull(peopleSaved.getId());

                    map.put("id", peopleSaved.getId());
                })
                .expectComplete()
                .verify();

        var mono = peopleMongoRepository.findById(map.get("id"));

        StepVerifier.create(mono)
                .assertNext(peopleDb -> {
                    assertEquals("75198811050", peopleDb.getCpf());
                    assertEquals("Fulano", peopleDb.getNome());
                    assertEquals(LocalDate.now(), peopleDb.getDataNascimento());
                })
                .expectComplete()
                .verify();
    }

    @Test
    public void notShouldFindPeopleById() {
        var mono = peopleMongoRepository.findById("1");

        StepVerifier.create(mono)
                .expectError(NotFoundDocumentException.class)
                .verify();
    }

    @Test
    public void shouldDeletePeopleById() {
        var people = People.builder()
                .nome("Fulano")
                .cpf("77255084060")
                .dataNascimento(LocalDate.now())
                .build();

        var monoSave = peopleMongoRepository.insert(people);

        final var map = new HashMap<String, String>();

        StepVerifier.create(monoSave)
                .assertNext(peopleSaved -> {
                    assertNotNull(peopleSaved);
                    assertNotNull(peopleSaved.getId());

                    map.put("id", peopleSaved.getId());
                })
                .expectComplete()
                .verify();

        var monoDelete = peopleMongoRepository.deleteById(map.get("id"));

        StepVerifier.create(monoDelete)
                .expectComplete()
                .verify();

        var monoFind = peopleMongoRepository.findById(map.get("id"));

        StepVerifier.create(monoFind)
                .expectError(NotFoundDocumentException.class)
                .verify();
    }

    @Test
    public void notShouldDeletePeopleById() {
        var monoDelete = peopleMongoRepository.deleteById("1");

        StepVerifier.create(monoDelete)
                .expectError(NotFoundDocumentException.class)
                .verify();
    }

    @Test
    public void shouldFindAllPeoples() {
        var people = People.builder()
                .nome("Fulano")
                .cpf("77255084060")
                .dataNascimento(LocalDate.now())
                .build();

        var monoPeople1 = peopleMongoRepository.insert(people);

        people = People.builder()
                .nome("Fulano")
                .cpf("75198811050")
                .dataNascimento(LocalDate.now())
                .build();

        var monoPeople2 = peopleMongoRepository.insert(people);

        var fluxFindAll = peopleMongoRepository.findAll();

        StepVerifier.create(monoPeople1)
                .assertNext(Assertions::assertNotNull)
                .expectComplete()
                .verify();

        StepVerifier.create(monoPeople2)
                .assertNext(Assertions::assertNotNull)
                .expectComplete()
                .verify();

        StepVerifier.create(fluxFindAll)
                .expectNextCount(2)
                .expectComplete()
                .verify();
    }

    @Test
    public void notShouldSaveTwoPeopleWithSameCpf() {
        var people = People.builder()
                .nome("Fulano")
                .cpf("77255084060")
                .dataNascimento(LocalDate.now())
                .build();

        var monoPeople1 = peopleMongoRepository.insert(people);

        StepVerifier.create(monoPeople1)
                .assertNext(Assertions::assertNotNull)
                .expectComplete()
                .verify();

        var monoPeople2 = peopleMongoRepository.insert(people);

        validateMonoDuplicateKey(monoPeople2);
    }

    @Test
    public void notShouldUpdatePeopleWithSameCpf() {
        var people1 = People.builder()
                .nome("Fulano")
                .cpf("77255084060")
                .dataNascimento(LocalDate.now())
                .build();

        var monoPeople1 = peopleMongoRepository.insert(people1);

        StepVerifier.create(monoPeople1)
                .assertNext(Assertions::assertNotNull)
                .expectComplete()
                .verify();

        var people2 = People.builder()
                .nome("Beltrano")
                .cpf("41649174071")
                .dataNascimento(LocalDate.now())
                .build();

        var monoPeople2 = peopleMongoRepository.insert(people2);

        final var map = new HashMap<String, People>();

        StepVerifier.create(monoPeople2)
                .assertNext(peopleDb -> {
                    assertNotNull(peopleDb);
                    map.put("people", peopleDb);
                })
                .expectComplete()
                .verify();

        var peopleDb = map.get("people");
        peopleDb.setCpf("77255084060");

        var monoPeople3 = peopleMongoRepository.update(people2);

        validateMonoDuplicateKey(monoPeople3);
    }

    private void validateMonoDuplicateKey(Mono<People> mono) {
        StepVerifier.create(mono)
                .expectErrorMatches(throwable -> throwable instanceof DuplicateKeyException && throwable.getMessage().contains("'E11000 duplicate key error collection: people_db.peoples index: idx_peoples_cpf_unique dup key:"))
                .verify();
    }

}