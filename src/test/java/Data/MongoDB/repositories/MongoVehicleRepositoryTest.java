package Data.MongoDB.repositories;

import Data.MongoDB.MongoConfig;
import Data.MongoDB.mappers.ClientMapper;
import Data.MongoDB.mappers.VehicleMapper;
import model.Car;
import model.Client;
import model.Vehicle;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import repositories.ClientRepository;
import repositories.VehicleRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class MongoVehicleRepositoryTest {
    private static VehicleRepository vehicleRepository;
    private static ClientRepository clientRepository;
    private static MongoConfig mongoConfig;

    @BeforeAll
    static void setup() {
        mongoConfig = new MongoConfig(
                "mongodb://mongo1:27017,mongo2:27018,mongo3:27019/"
                        + "?replicaSet=rs0&retryWrites=true"
        );

        ClientMapper clientMapper = new ClientMapper();
        clientRepository = new MongoClientRepository(mongoConfig.getDatabase(), clientMapper);

        VehicleMapper vehicleMapper = new VehicleMapper();

        vehicleRepository = new MongoVehicleRepository(
                mongoConfig.getDatabase(),
                vehicleMapper,
                clientMapper
        );
    }

    @AfterAll
    static void close() {
        mongoConfig.close();
    }

    @Test
    void createAndGetVehicleTest() {
        Client client = new Client("Robert", "Kubica", "44433322211",
                "robert@email.com", "600700800", "Diamond");
        clientRepository.save(client);

        Vehicle car = new Car(client, "VIN123456789", "EL12345", 2003,
                "Audi", "A3", "Czerwony", 1600, 120, 1300,
                "Cupe", "Benzyna", 3);

        vehicleRepository.save(car);

        Optional<Vehicle> vehicleFromMongo = vehicleRepository.findById("VIN123456789");

        assertTrue(vehicleFromMongo.isPresent());
        assertEquals("Audi", vehicleFromMongo.get().getBrand());
        assertEquals("A3", vehicleFromMongo.get().getModel());
        assertNotNull(vehicleFromMongo.get().getOwner());
        assertEquals("44433322211", vehicleFromMongo.get().getOwner().getPesel());
    }

    @Test
    void findByClientPeselTest() {
        Client client = new Client("Robert", "Kubica", "44433322211",
                "robert@email.com", "600700800", "Diamond");
        clientRepository.save(client);

        Vehicle car = new Car(client, "VIN123456789", "EL12345", 2003,
                "Audi", "A3", "Czerwony", 1600, 120, 1300,
                "Cupe", "Benzyna", 3);
        vehicleRepository.save(car);

        List<Vehicle> vehicles = vehicleRepository.findByClientPesel("44433322211");
        assertFalse(vehicles.isEmpty());
        assertEquals("44433322211", vehicles.get(0).getOwner().getPesel());
    }

    @Test
    void findByRegistrationNumberTest() {
        Client client = new Client("Robert", "Kubica", "44433322211",
                "robert@email.com", "600700800", "Diamond");
        clientRepository.save(client);

        Vehicle car = new Car(client, "VIN123456789", "EL12345", 2003,
                "Audi", "A3", "Czerwony", 1600, 120, 1300,
                "Cupe", "Benzyna", 3);
        vehicleRepository.save(car);

        List<Vehicle> vehicles = vehicleRepository.findByRegistrationNumber("EL12345");
        assertFalse(vehicles.isEmpty());
        assertEquals("VIN123456789", vehicles.get(0).getVinNumber());
    }

    @Test
    void findByBrandAndModelTest() {
        Client client = new Client("Robert", "Kubica", "44433322211",
                "robert@email.com", "600700800", "Diamond");
        clientRepository.save(client);

        Vehicle car = new Car(client, "VIN123456789", "EL12345", 2003,
                "Audi", "A3", "Czerwony", 1600, 120, 1300,
                "Cupe", "Benzyna", 3);
        vehicleRepository.save(car);

        List<Vehicle> vehicles = vehicleRepository.findByBrandAndModel("Audi", "A3");
        assertFalse(vehicles.isEmpty());
    }

    @Test
    void deleteVehicleTest() {
        Client client = new Client("Robert", "Kubica", "44433322211",
                "robert@email.com", "600700800", "Diamond");
        clientRepository.save(client);

        Vehicle car = new Car(client, "VIN123456789", "EL12345", 2003,
                "Audi", "A3", "Czerwony", 1600, 120, 1300,
                "Cupe", "Benzyna", 3);
        vehicleRepository.save(car);

        vehicleRepository.deleteById("VIN123456789");
        Optional<Vehicle> vehicle = vehicleRepository.findById("VIN123456789");
        assertTrue(vehicle.isEmpty());
    }
}