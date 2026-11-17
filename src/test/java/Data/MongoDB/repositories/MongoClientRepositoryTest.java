package Data.MongoDB.repositories;

import Data.MongoDB.MongoConfig;

import Data.MongoDB.mappers.ClientMapper;
import model.Client;
import org.junit.jupiter.api.*;
import repositories.ClientRepository;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MongoClientRepositoryTest {
    private static MongoConfig mongoConfig;
    private static ClientRepository clientRepository;

    @BeforeAll
    static void setup() {
        mongoConfig = new MongoConfig(
                "mongodb://mongo1:27017,mongo2:27018,mongo3:27019/"
                        + "?replicaSet=rs0&retryWrites=true"
        );

        clientRepository = new MongoClientRepository(mongoConfig.getDatabase(),
                                                        new ClientMapper());
    }

    @AfterAll
    static void close() {
        mongoConfig.close();
    }

    @Test
    void createAndGetClient() {
        Client client = new Client("Cezary", "Pazura", "00000000001",
                "email0@email.com", "123123123", "Diamond");

        Client savedClient = clientRepository.save(client);

        Optional<Client> clientFromMongo = clientRepository.findById(client.getPesel());

        assertTrue(clientFromMongo.isPresent());
        assertEquals(client.getName(), clientFromMongo.get().getName());
        assertEquals(client.getLastName(), clientFromMongo.get().getLastName());

    }

    @Test
    void updateClientTest() {
        Client client = new Client("Cezary", "Pazura", "00000000001",
                "email0@email.com", "123123123", "Diamond");

        client.setClientType("Gold");
        clientRepository.save(client);

        Optional<Client> clientFromMongo = clientRepository.findById(client.getPesel());

        assertTrue(clientFromMongo.isPresent());
        assertEquals("Gold", clientFromMongo.get().getClientType());

    }

    @Test
    void deleteClientByIdTest() {
        Client client = new Client("Cezary", "Pazura", "00000000001",
                "email1@email.com", "123123123", "Diamond");

        clientRepository.deleteById(client.getPesel());

        Optional<Client> clientFromMongo = clientRepository.findById(client.getPesel());

        assertTrue(clientFromMongo.isPresent());
        assertTrue(clientFromMongo.get().isArchive());

    }

    @Test
    void findByNameAndLastName() {
        Client client = new Client("Tomek", "Atomek", "00000000002",
                "email2@email.com", "123777123", "Diamond");

        clientRepository.save(client);

        List<Client> clientsFromMongo = clientRepository.findByNameAndLastName(
                client.getName(),
                client.getLastName()
        );

        assertFalse(clientsFromMongo.isEmpty());
        assertEquals(clientsFromMongo.get(0).getPesel(), client.getPesel());
    }

    @Test
    void findByPhoneNumber() {
        Client client = new Client("Tomek", "Atomek", "00000000002",
                "email2@email.com", "123777123", "Diamond");


        List<Client> clientsFromMongo = clientRepository.findByPhoneNumber(client.getPhoneNumber());

        assertFalse(clientsFromMongo.isEmpty());
        assertEquals(clientsFromMongo.get(0).getPesel(), client.getPesel());
    }

    @Test
    void findByArchive() {
        Client client = new Client("Cezary", "Pazura", "00000000001",
                "email@email.com", "123123123", "Diamond");

        client.setArchive(true);
        clientRepository.save(client);

        List<Client> clientsFromMongo = clientRepository.findByArchive();

        assertFalse(clientsFromMongo.isEmpty());
        assertEquals(client.getPesel(), clientsFromMongo.get(0).getPesel());
    }

}