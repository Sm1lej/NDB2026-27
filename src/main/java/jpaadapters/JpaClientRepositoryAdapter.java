package jpaadapters;

import jakarta.persistence.OptimisticLockException;
import jparepositories.JpaClientRepository;
import model.Client;
import entity.ClientEnt;
import jakarta.persistence.EntityManager;
import repositories.ClientRepository;
import model.Address;
import entity.AddressEnt;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class JpaClientRepositoryAdapter implements ClientRepository {
    private EntityManager em;
    private JpaClientRepository jpaClientRepository;
    public JpaClientRepositoryAdapter(EntityManager em) {
        this.em = em;
        this.jpaClientRepository = new JpaClientRepository(em);
    }
    private ClientEnt toClientEnt(Client client) {
        if  (client == null) {
            throw new IllegalArgumentException("Client is null");
        }
        ClientEnt clientEnt = new ClientEnt();
        clientEnt.setName(client.getName());
        clientEnt.setLastName(client.getLastName());
        clientEnt.setPesel(client.getPesel());
        clientEnt.setEmail(client.getEmail());
        clientEnt.setPhoneNumber(client.getPhoneNumber());

        Address address = client.getAddress();
        clientEnt.setAddress(new AddressEnt(
                address.getCity(),
                address.getStreet(),
                address.getNumber()
        ));
        return clientEnt;
    }

    private Client toClient(ClientEnt clientEnt) {
        if (clientEnt == null) {
            throw new IllegalArgumentException("ClientEnt is null");
        }

        AddressEnt addressEnt = clientEnt.getAddress();
        Address address = new Address(
                addressEnt.getCity(),
                addressEnt.getStreet(),
                addressEnt.getNumber()
        );

        return new Client(
                clientEnt.getName(),
                clientEnt.getLastName(),
                clientEnt.getPesel(),
                clientEnt.getEmail(),
                clientEnt.getPhoneNumber(),
                address
        );
    }


    @Override
    public List<Client> findByLastName(String lastName) {
        return List.of();
    }

    @Override
    public List<Client> findByNameAndLastName(String name, String lastName) {
        return List.of();
    }

    @Override
    public List<Client> findByEmailAddress(String email) {
        return List.of();
    }

    @Override
    public List<Client> findByPhoneNumber(String phoneNumber) {
        return List.of();
    }

    @Override
    public List<Client> findByArchive() {
        return List.of();
    }

    @Override
    public Client save(Client entity) {
        return null;
    }

    @Override
    public Optional<Client> findById(String s) {
        return Optional.empty();
    }

    @Override
    public List<Client> findAll() {
        return List.of();
    }

    @Override
    public void deleteById(String s) {

    }

    public void add(Client client) {
        ClientEnt clientEnt = toClientEnt(client);
        em.getTransaction().begin();
        try {
            jpaClientRepository.add(clientEnt);
            em.getTransaction().commit();

        }catch (OptimisticLockException e) {
            if(em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new IllegalStateException(e.getMessage());
        }catch (RuntimeException e) {
            if(em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new IllegalStateException(e.getMessage());
        }
    }
    public void remove(Client client) {
        ClientEnt clientEnt = toClientEnt(client);
        em.getTransaction().begin();
        try {
            ClientEnt found = jpaClientRepository.findbyPhoneNumber(client.getPhoneNumber());
            if (found == null) {
                throw new NoSuchElementException("Client with name " + client.getName() + " not found");
            }
            jpaClientRepository.remove(found);
            em.getTransaction().commit();
        }catch (OptimisticLockException e) {
            if(em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        }catch (RuntimeException e) {
            if(em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        }
    }


}
