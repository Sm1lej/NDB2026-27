package Data.MongoDB.repositories;

import Data.MongoDB.MongoConfig;
import Data.MongoDB.mappers.ServiceMapper;
import model.RepairService;
import model.Service;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import repositories.ServiceRepository;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MongoServiceRepositoryTest {
    private static MongoConfig mongoConfig;
    private static ServiceRepository serviceRepository;

    @BeforeAll
    static void setup() {
        mongoConfig = new MongoConfig(
                "mongodb://mongo1:27017,mongo2:27018,mongo3:27019/"
                        + "?replicaSet=rs0&retryWrites=true"
        );

        ServiceMapper serviceMapper = new ServiceMapper();
        serviceRepository = new MongoServiceRepository(mongoConfig.getDatabase(), serviceMapper);
    }

    @AfterAll
    static void close() {
        mongoConfig.close();
    }

    @Test
    void createAndGetServiceTest() {
        Service service = new RepairService("Wymiana klocków hamulcowych", 150.0,
                Duration.ofMinutes(45), true, false, true);

        Service savedService = serviceRepository.save(service);
        assertNotNull(savedService.getId());

        Optional<Service> serviceFromMongo = serviceRepository.findById(savedService.getId());

        assertTrue(serviceFromMongo.isPresent());
        assertEquals("Wymiana klocków hamulcowych", serviceFromMongo.get().getDescription());
        assertEquals(150.0, serviceFromMongo.get().getBasePrice());
    }

    @Test
    void findByBasePriceTest() {
        Service service = new RepairService("Wymiana oleju", 100.0,
                Duration.ofMinutes(30), false, true, false);
        serviceRepository.save(service);

        List<Service> services = serviceRepository.findByBasePrice(100.0);
        assertFalse(services.isEmpty());
        assertEquals(100.0, services.get(0).getBasePrice());
    }

    @Test
    void deleteServiceTest() {
        Service service = new RepairService("Testowa usługa do usunięcia", 50.0,
                Duration.ofMinutes(15), false, false, false);
        Service saved = serviceRepository.save(service);

        serviceRepository.deleteById(saved.getId());

        Optional<Service> deleted = serviceRepository.findById(saved.getId());
        assertTrue(deleted.isEmpty());
    }
}