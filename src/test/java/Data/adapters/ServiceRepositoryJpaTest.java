package Data.adapters;

import Data.mappers.ServiceMapper;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;
import model.Service;
import model.DiagnosticService;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ServiceRepositoryJpaTest {

    private EntityManagerFactory emf;
    private ServiceRepositoryJpa serviceRepo;

    @BeforeAll
    void setUp() {
        emf = Persistence.createEntityManagerFactory("WarsztatPU");
        serviceRepo = new ServiceRepositoryJpa(emf, new ServiceMapper());
    }

    @AfterAll
    void close() {
        if (emf != null) {
            emf.close();
        }
    }

    private Service createTestService() {
        DiagnosticService service = new DiagnosticService(
                "Diagnostyka testowa",
                150.0,
                Duration.ofMinutes(30),
                true,
                false,
                false
        );

        return serviceRepo.save(service);
    }

    @Test
    void save() {
        Service service = createTestService();

        assertNotNull(service.getId());

        Optional<Service> found = serviceRepo.findById(service.getId());

        assertTrue(found.isPresent());
        assertEquals(service.getId(), found.get().getId());
    }

    @Test
    void findById() {
        Service service = createTestService();

        Optional<Service> found = serviceRepo.findById(service.getId());

        assertTrue(found.isPresent());
        assertEquals(service.getId(), found.get().getId());
        assertEquals(service.getDescription(), found.get().getDescription());
    }

    @Test
    void findAll() {
        Service first = createTestService();
        Service second = createTestService();

        List<Service> services = serviceRepo.findAll();

        assertTrue(services.stream()
                .anyMatch(s -> s.getId().equals(first.getId())));

        assertTrue(services.stream()
                .anyMatch(s -> s.getId().equals(second.getId())));
    }

    @Test
    void deleteById() {
        Service service = createTestService();

        Integer id = service.getId();

        assertTrue(serviceRepo.findById(id).isPresent());

        serviceRepo.deleteById(id);

        assertTrue(serviceRepo.findById(id).isEmpty());
    }

    @Test
    void findByBasePrice() {
        Service service = createTestService();

        List<Service> services = serviceRepo.findByBasePrice(
                service.getBasePrice()
        );

        assertTrue(services.stream()
                .anyMatch(s -> s.getId().equals(service.getId())));
    }


}