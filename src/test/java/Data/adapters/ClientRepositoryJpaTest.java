package Data.adapters;

import Data.mappers.ClientMapper;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import model.Client;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ClientRepositoryJpaTest {

    private EntityManagerFactory emf;
    private ClientRepositoryJpa clientRepo;

    @BeforeAll
    void setUp() {
        emf = Persistence.createEntityManagerFactory("WarsztatPU");
        clientRepo = new ClientRepositoryJpa(emf, new ClientMapper());
    }

    @AfterAll
    void close() {
        if (emf != null) {
            emf.close();
        }
    }

    private Client createTestClient(String pesel) {
        Client client = new Client(
                "Jan",
                "Kowalski",
                pesel,
                "jan@example.com",
                "123456789",
                "Default"
        );

        return clientRepo.save(client);
    }

    @Test
    void save() {
        Client client = createTestClient("00000000001");

        assertNotNull(client);

        Optional<Client> found = clientRepo.findById(client.getPesel());

        assertTrue(found.isPresent());
        assertEquals("Jan", found.get().getName());
        assertEquals("Kowalski", found.get().getLastName());
    }

    @Test
    void findById() {
        Client client = createTestClient("00000000002");

        Optional<Client> found = clientRepo.findById(client.getPesel());

        assertTrue(found.isPresent());
        assertEquals(client.getPesel(), found.get().getPesel());
        assertEquals(client.getEmail(), found.get().getEmail());
    }

    @Test
    void findAll() {
        Client first = createTestClient("00000000003");
        Client second = createTestClient("00000000004");

        List<Client> clients = clientRepo.findAll();

        assertTrue(clients.stream()
                .anyMatch(c -> c.getPesel().equals(first.getPesel())));

        assertTrue(clients.stream()
                .anyMatch(c -> c.getPesel().equals(second.getPesel())));
    }

    @Test
    void deleteById() {
        Client client = createTestClient("00000000005");

        clientRepo.deleteById(client.getPesel());

        List<Client> archivedClients = clientRepo.findByArchive();

        assertTrue(archivedClients.stream()
                .anyMatch(c -> c.getPesel().equals(client.getPesel())));
    }

    @Test
    void findByNameAndLastName() {
        Client client = createTestClient("00000000006");

        List<Client> clients = clientRepo.findByNameAndLastName(
                "Jan",
                "Kowalski"
        );

        assertTrue(clients.stream()
                .anyMatch(c -> c.getPesel().equals(client.getPesel())));
    }

    @Test
    void findByPhoneNumber() {
        Client client = createTestClient("00000000007");

        List<Client> clients = clientRepo.findByPhoneNumber("123456789");

        assertTrue(clients.stream()
                .anyMatch(c -> c.getPesel().equals(client.getPesel())));
    }

    @Test
    void findByArchive() {
        Client client = createTestClient("00000000008");

        clientRepo.deleteById(client.getPesel());

        List<Client> archivedClients = clientRepo.findByArchive();

        assertTrue(archivedClients.stream()
                .anyMatch(c -> c.getPesel().equals(client.getPesel())));

        assertTrue(archivedClients.stream()
                .allMatch(Client::isArchive));
    }
}