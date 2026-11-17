package Data.MongoDB.repositories;

import Data.MongoDB.MongoConfig;
import Data.MongoDB.mappers.ClientMapper;
import Data.MongoDB.mappers.VehicleMapper;
import model.Car;
import model.Client;
import model.Vehicle;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
        vehicleRepository = new MongoVehicleRepository(mongoConfig.getDatabase(), vehicleMapper, clientRepository);
    }

    @AfterAll
    static void close() {
        mongoConfig.close();
    }

    @BeforeEach
    void setUpClient() {
        Client client = new Client("Jarosław", "Jarząbkowski", "12345678901",
                "pasha@email.com", "123123123", "Gold");
        clientRepository.save(client);
    }

    @Test
    void createAndGetVehicleTest() {
        Optional<Client> ownerOpt = clientRepository.findById("12345678901");
        assertTrue(ownerOpt.isPresent());
        Client owner = ownerOpt.get();

        Vehicle car = new Car(owner, "VIN123456789", "EL12345", 2003,
                "Audi", "A3", "Srebrny", 1600, 120, 1300,
                "Cupe", "Benzyna", 3);

        vehicleRepository.save(car);

        Optional<Vehicle> vehicleFromMongo = vehicleRepository.findById("VIN123456789");

        assertTrue(vehicleFromMongo.isPresent());
        assertEquals("Audi", vehicleFromMongo.get().getBrand());
        assertEquals("A3", vehicleFromMongo.get().getModel());
        assertNotNull(vehicleFromMongo.get().getOwner());
        assertEquals("12345678901", vehicleFromMongo.get().getOwner().getPesel());
    }

    @Test
    void findByClientPeselTest() {
        List<Vehicle> vehicles = vehicleRepository.findByClientPesel("12345678901");
        assertFalse(vehicles.isEmpty());
        assertEquals("12345678901", vehicles.get(0).getOwner().getPesel());
    }

    @Test
    void findByRegistrationNumberTest() {
        List<Vehicle> vehicles = vehicleRepository.findByRegistrationNumber("EL12345");
        assertFalse(vehicles.isEmpty());
        assertEquals("VIN123456789", vehicles.get(0).getVinNumber());
    }

    @Test
    void findByBrandAndModelTest() {
        List<Vehicle> vehicles = vehicleRepository.findByBrandAndModel("Audi", "A3");
        assertFalse(vehicles.isEmpty());
    }

    @Test
    void deleteVehicleTest() {
        vehicleRepository.deleteById("VIN123456789");
        Optional<Vehicle> vehicle = vehicleRepository.findById("VIN123456789");
        assertTrue(vehicle.isEmpty());
    }
}
