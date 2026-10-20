package Data.adapters;

import Data.entity.ClientEnt;
import Data.mappers.ClientMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import model.Client;
import repositories.ClientRepository;

import java.util.List;
import java.util.Optional;

public class ClientRepositoryJpa implements ClientRepository {

    private final EntityManagerFactory emf;
    private final ClientMapper mapper;

    public ClientRepositoryJpa(EntityManagerFactory emf, ClientMapper mapper) {
        this.emf = emf;
        this.mapper = mapper;
    }

    @Override
    public Client save(Client client) {
        if (client == null) {
            throw new IllegalArgumentException("Klient nie może być nullem");
        }

        try (EntityManager em = emf.createEntityManager();) {
            EntityTransaction transaction = em.getTransaction();
            try {
                transaction.begin();

                ClientEnt clientEnt = em.find(ClientEnt.class, client.getPesel());

                if (clientEnt == null) {

                    clientEnt = mapper.toEntity(client);
                    em.persist(clientEnt);

                } else {
                    updateClientEnt(clientEnt, client);
                }

                em.flush();

                Client result = mapper.toDomain(clientEnt);

                transaction.commit();

                return result;

            } catch (RuntimeException e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw e;
            }
        }
    }

    private void updateClientEnt(ClientEnt clientEnt, Client client) {
        clientEnt.setName(client.getName());
        clientEnt.setLastName(client.getLastName());
        clientEnt.setEmail(client.getEmail());
        clientEnt.setPhoneNumber(client.getPhoneNumber());
        clientEnt.setClientType(client.getClientType());
        clientEnt.setArchive(client.isArchive());
    }

    @Override
    public Optional<Client> findById(String pesel) {
        try (EntityManager em = emf.createEntityManager()) {
            ClientEnt clientEnt = em.find(ClientEnt.class, pesel);

            return Optional.ofNullable(clientEnt)
                    .map(mapper::toDomain);
        }
    }

    @Override
    public List<Client> findAll() {
        try (EntityManager em = emf.createEntityManager()) {
            List<ClientEnt> entities = em.createQuery("SELECT c FROM ClientEnt c",
                    ClientEnt.class).getResultList();

            return entities.stream()
                    .map(mapper::toDomain)
                    .toList();
        }
    }

    @Override
    public void deleteById(String pesel) {




        try (EntityManager em = emf.createEntityManager();)
        {
            EntityTransaction transaction = em.getTransaction();
            try {

                transaction.begin();

                ClientEnt clientEnt = em.find(ClientEnt.class, pesel);

                if (clientEnt == null) {
                    throw new IllegalArgumentException(
                            "Nie znaleziono klienta o PESEL: " + pesel);
                }

                em.remove(clientEnt);

                transaction.commit();

            } catch (RuntimeException e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw e;
            }
        }
    }

    @Override
    public List<Client> findByNameAndLastName(String name, String lastName) {
        if (name == null || name.isBlank()
                || lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException(
                    "Imię i nazwisko nie mogą być puste"
            );
        }

        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("""
                            SELECT c FROM ClientEnt c
                            WHERE c.name = :name AND c.lastName = :lastName
                            """,
                            ClientEnt.class)
                    .setParameter("name", name)
                    .setParameter("lastName", lastName)
                    .getResultStream()
                    .map(mapper::toDomain)
                    .toList();
        }
    }

    @Override
    public List<Client> findByPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new IllegalArgumentException(
                    "Numer telefonu nie może być pusty"
            );
        }

        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery(
                            """
                            SELECT c
                            FROM ClientEnt c
                            WHERE c.phoneNumber = :phoneNumber
                            """,
                            ClientEnt.class)
                    .setParameter("phoneNumber", phoneNumber)
                    .getResultStream()
                    .map(mapper::toDomain)
                    .toList();
        }
    }

    @Override
    public List<Client> findByArchive() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery(
                            """
                            SELECT c
                            FROM ClientEnt c
                            WHERE c.isArchive = true
                            """,
                            ClientEnt.class)
                    .getResultStream()
                    .map(mapper::toDomain)
                    .toList();
        }
    }
}