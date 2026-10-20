package managers;

import repositories.ClientRepository;
import model.Client;
import java.util.List;
import java.util.Optional;

public class ClientManager {
    private final ClientRepository clientRepository;

    public ClientManager(ClientRepository clientRepository) {
        if (clientRepository == null) {
            throw new RuntimeException("Nieprawidłowe repozytorium klientów.");
        }
        this.clientRepository = clientRepository;
    }

    public Client registerClient(String name, String lastName, String pesel,
                                 String email, String phoneNumber,
                                 String clientType) {

        if (clientRepository.findById(pesel).isPresent()) {
            throw new IllegalArgumentException(
                    "Klient o podanym numerze PESEL już istnieje."
            );
        }

        Client client = new Client(
                name, lastName, pesel, email, phoneNumber, clientType
        );

        return clientRepository.save(client);
    }

    private Client getExistingClient(String pesel) {

        return clientRepository.findById(pesel)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Nie znaleziono klienta o peselu: " + pesel
                ));
    }

    /**
     * Wypisuje klienta z systemu na podstawie numeru PESEL.
     *
     * @param pesel Numer PESEL klienta do wypisania
     */
    public void unregisterClient(String pesel) {
        Client client = getExistingClient(pesel);
        client.setArchive(true);
        clientRepository.save(client);
    }

    public void changeClientType(String pesel, String type) {
        Client client = getExistingClient(pesel);

        client.setClientType(type);
        clientRepository.save(client);
    }


    /**
     * Wyszukuje klientów na podstawie imienia i nazwiska.
     *
     * @param name Imię, według którego wyszukiwani są klienci
     * @param lastName Nazwisko, według którego wyszukiwani są klienci
     * @return Lista klientów o podanym imieniu i nazwisku
     */
    public List<Client> findByNameAndLastName(String name, String lastName) {
        return clientRepository.findByNameAndLastName(name, lastName);
    }


    /**
     * Wyszukuje klientów na podstawie numeru telefonu.
     *
     * @param phoneNumber Numer telefonu, według którego wyszukiwani są klienci
     * @return Lista klientów o podanym numerze telefonu
     */
    public List<Client> findByPhoneNumber(String phoneNumber) {
        return clientRepository.findByPhoneNumber(phoneNumber);
    }

    /**
     * Wyszukuje klientów na podstawie numeru PESEL.
     *
     * @param pesel Numer PESEL, według którego wyszukiwani są klienci
     * @return Lista klientów o podanym numerze PESEL
     */
    public Optional<Client> findByPesel(String pesel) {
        return clientRepository.findById(pesel);
    }

    /**
     * Wyszukuje zarchiwizowanych klientów.
     *
     * @return Lista klientów oznaczonych jako zarchiwizowani
     */
    public List<Client> findByArchive() {
        return clientRepository.findByArchive();
    }

    /**
     * Zwraca wszystkich zarejestrowanych klientów.
     *
     * @return Lista wszystkich klientów
     */
    public List<Client> findAll() {
        return clientRepository.findAll();
    }
}
