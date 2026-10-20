package Data.adapters;

import Data.mappers.ClientMapper;
import Data.mappers.VehicleMapper;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import model.Car;
import model.Client;
import model.Vehicle;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class VehicleRepositoryJpaTest {

    private EntityManagerFactory emf;
    private VehicleRepositoryJpa vehicleRepo;
    private ClientRepositoryJpa clientRepo;

    @BeforeAll
    void setUp() {
        emf = Persistence.createEntityManagerFactory("WarsztatPU");

        clientRepo = new ClientRepositoryJpa(emf, new ClientMapper());
        vehicleRepo = new VehicleRepositoryJpa(emf, new VehicleMapper());
    }

    @AfterAll
    void tearDown() {
        if (emf != null) {
            emf.close();
        }
    }

    private Client createTestClient(String pesel) {
        return clientRepo.save(new Client(
                "Jan",
                "Kowalski",
                pesel,
                "jan@example.com",
                "123456789",
                "Default"
        ));
    }

    private Vehicle createTestVehicle(Client owner, String vinNumber) {
        // Tutaj utwórz Car zgodnie z konstruktorem Twojej klasy.
        Car car = new Car(owner, vinNumber, "rejestracja", 2003,
                "Audi", "A3", "szary", 1600, 105,
                1300, "HatchBack", "benzyna", 3);

        return vehicleRepo.save(car);
    }

    @Test
    void save() {
        Client client = createTestClient("00000000001");

        Vehicle vehicle = createTestVehicle(client, "TESTVIN0000000001");

        assertNotNull(vehicle);
        assertEquals("TESTVIN0000000001", vehicle.getVinNumber());

        assertTrue(vehicleRepo.findById(vehicle.getVinNumber()).isPresent());
    }

    @Test
    void findById() {
        Client client = createTestClient("00000000002");
        Vehicle vehicle = createTestVehicle(client, "TESTVIN0000000002");

        Optional<Vehicle> found = vehicleRepo.findById(vehicle.getVinNumber());

        assertTrue(found.isPresent());
        assertEquals(vehicle.getVinNumber(), found.get().getVinNumber());
        assertEquals(client.getPesel(), found.get().getOwner().getPesel());
    }

    @Test
    void findAll() {
        Client client = createTestClient("00000000003");

        Vehicle first = createTestVehicle(client, "TESTVIN0000000003");
        Vehicle second = createTestVehicle(client, "TESTVIN0000000004");

        List<Vehicle> vehicles = vehicleRepo.findAll();

        assertTrue(vehicles.stream()
                .anyMatch(v -> v.getVinNumber().equals(first.getVinNumber())));

        assertTrue(vehicles.stream()
                .anyMatch(v -> v.getVinNumber().equals(second.getVinNumber())));
    }

    @Test
    void deleteById() {
        Client client = createTestClient("00000000004");
        Vehicle vehicle = createTestVehicle(client, "TESTVIN0000000005");

        vehicleRepo.deleteById(vehicle.getVinNumber());

        assertTrue(vehicleRepo.findById(vehicle.getVinNumber()).isEmpty());
    }

    @Test
    void findByClientPesel() {
        Client firstClient = createTestClient("00000000005");
        Client secondClient = createTestClient("00000000006");

        Vehicle first = createTestVehicle(firstClient, "TESTVIN0000000006");
        Vehicle second = createTestVehicle(firstClient, "TESTVIN0000000007");
        Vehicle other = createTestVehicle(secondClient, "TESTVIN0000000008");

        List<Vehicle> vehicles =
                vehicleRepo.findByClientPesel(firstClient.getPesel());

        assertTrue(vehicles.stream()
                .anyMatch(v -> v.getVinNumber().equals(first.getVinNumber())));

        assertTrue(vehicles.stream()
                .anyMatch(v -> v.getVinNumber().equals(second.getVinNumber())));

        assertFalse(vehicles.stream()
                .anyMatch(v -> v.getVinNumber().equals(other.getVinNumber())));
    }

    @Test
    void findByVinNumber() {
        Client client = createTestClient("00000000007");
        Vehicle vehicle = createTestVehicle(client, "TESTVIN0000000000");

        List<Vehicle> vehicles =
                vehicleRepo.findByVinNumber(vehicle.getVinNumber());

        assertTrue(vehicles.stream()
                .anyMatch(v -> v.getVinNumber().equals(vehicle.getVinNumber())));
    }

    @Test
    void findByRegistrationNumber() {
        Client client = createTestClient("00000000008");
        Vehicle vehicle = createTestVehicle(client, "TESTVIN0000000010");

        List<Vehicle> vehicles = vehicleRepo.findByRegistrationNumber(
                vehicle.getRegistrationNumber()
        );

        assertTrue(vehicles.stream()
                .anyMatch(v -> v.getVinNumber().equals(vehicle.getVinNumber())));
    }

    @Test
    void findByBrandAndModel() {
        Client client = createTestClient("00000000009");
        Vehicle vehicle = createTestVehicle(client, "TESTVIN0000000011");

        List<Vehicle> vehicles = vehicleRepo.findByBrandAndModel(
                vehicle.getBrand(),
                vehicle.getModel()
        );

        assertTrue(vehicles.stream()
                .anyMatch(v -> v.getVinNumber().equals(vehicle.getVinNumber())));
    }

    @Test
    void findByEngineDisplacement() {
        Client client = createTestClient("00000000010");
        Vehicle vehicle = createTestVehicle(client, "TESTVIN0000000012");

        List<Vehicle> vehicles = vehicleRepo.findByEngineDisplacement(
                vehicle.getEngineDisplacement()
        );

        assertTrue(vehicles.stream()
                .anyMatch(v -> v.getVinNumber().equals(vehicle.getVinNumber())));
    }
}