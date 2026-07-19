package com.mercadona.nuriabravo.infrastructure.persistence;

import com.mercadona.nuriabravo.domain.model.Store;
import com.mercadona.nuriabravo.domain.model.Worker;
import com.mercadona.nuriabravo.infrastructure.persistence.jpa.StoreJpa;
import com.mercadona.nuriabravo.infrastructure.persistence.jpa.WorkerJpa;
import com.mercadona.nuriabravo.infrastructure.persistence.jpa.repository.StoreJpaRepository;
import com.mercadona.nuriabravo.infrastructure.persistence.jpa.repository.WorkerJpaRepository;
import com.mercadona.nuriabravo.infrastructure.persistence.mapper.StoreJpaMapperImpl;
import com.mercadona.nuriabravo.infrastructure.persistence.mapper.WorkerJpaMapperImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({WorkerRepositoryImpl.class, WorkerJpaMapperImpl.class, StoreJpaMapperImpl.class})
class WorkerRepositoryTest {

    @Autowired
    private WorkerRepositoryImpl workerRepository;

    @Autowired
    private WorkerJpaRepository workerJpaRepository;

    @Autowired
    private StoreJpaRepository storeJpaRepository;

    @Test
    void save_shouldPersistWorkerAndMapBackToDomain() {
        StoreJpa store = storeJpaRepository.save(
                StoreJpa.builder().code("TEST01").name("Tienda de prueba").build()
        );

        Worker worker = Worker.builder()
                .firstName("Ana")
                .lastName("López")
                .dni("11223344C")
                .contractHours(6)
                .store(Store.builder().id(store.getId()).code(store.getCode()).name(store.getName()).build())
                .build();

        Worker saved = workerRepository.save(worker);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getFirstName()).isEqualTo("Ana");
        assertThat(saved.getStore().getId()).isEqualTo(store.getId());
    }

    @Test
    void findById_shouldReturnWorker_whenExists() {
        StoreJpa store = storeJpaRepository.save(
                StoreJpa.builder().code("TEST02").name("Tienda de prueba 2").build()
        );
        WorkerJpa persisted = workerJpaRepository.save(
                WorkerJpa.builder()
                        .firstName("Carlos")
                        .lastName("Ruiz")
                        .dni("55667788D")
                        .contractHours(8)
                        .store(store)
                        .build()
        );

        Optional<Worker> result = workerRepository.findById(persisted.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getFirstName()).isEqualTo("Carlos");
    }

    @Test
    void findById_shouldReturnEmpty_whenNotExists() {
        Optional<Worker> result = workerRepository.findById(999999L);

        assertThat(result).isEmpty();
    }

    @Test
    void findAll_shouldReturnAllPersistedWorkers() {
        List<Worker> result = workerRepository.findAll();

        assertThat(result).isNotNull();
    }

    @Test
    void existsById_shouldReturnTrue_whenWorkerExists() {
        StoreJpa store = storeJpaRepository.save(
                StoreJpa.builder().code("TEST03").name("Tienda de prueba 3").build()
        );
        WorkerJpa persisted = workerJpaRepository.save(
                WorkerJpa.builder()
                        .firstName("Elena")
                        .lastName("Díaz")
                        .dni("99001122E")
                        .contractHours(4)
                        .store(store)
                        .build()
        );

        assertThat(workerRepository.existsById(persisted.getId())).isTrue();
    }

    @Test
    void existsById_shouldReturnFalse_whenWorkerDoesNotExist() {
        assertThat(workerRepository.existsById(999999L)).isFalse();
    }

    @Test
    void deleteById_shouldRemoveWorker() {
        StoreJpa store = storeJpaRepository.save(
                StoreJpa.builder().code("TEST04").name("Tienda de prueba 4").build()
        );
        WorkerJpa persisted = workerJpaRepository.save(
                WorkerJpa.builder()
                        .firstName("Marcos")
                        .lastName("Vidal")
                        .dni("33445566F")
                        .contractHours(5)
                        .store(store)
                        .build()
        );

        workerRepository.deleteById(persisted.getId());

        assertThat(workerJpaRepository.existsById(persisted.getId())).isFalse();
    }
}