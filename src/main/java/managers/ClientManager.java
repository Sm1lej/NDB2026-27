package managers;

import repositories.ClientRepository;
import model.Client;
import model.Address;
import java.util.List;

public class ClientManager {
    private ClientRepository clientRepository;

    /**
     * Konstruktor klasy ClientManager, tworzący obiekt menedżera z podanym repozytorium.
     *
     * @param clientRepository Repozytorium klientów
     */
    public ClientManager(ClientRepository clientRepository) {
        if (clientRepository == null) {
            throw new RuntimeException("Nieprawidłowe repozytorium klientów.");
        }
        this.clientRepository = clientRepository;
    }

    /**
     * Rejestruje nowego klienta z podanymi danymi.
     *
     * @param name Imię klienta
     * @param lastName Nazwisko klienta
     * @param email Adres e-mail klienta
     * @param phoneNumber Numer telefonu klienta
     * @param pesel Numer PESEL klienta
     * @param city Miasto zamieszkania klienta
     * @param street Ulica zamieszkania klienta
     * @param number Numer domu/mieszkania klienta
     */
    public void registerClient(String name, String lastName, String email,
                               String phoneNumber, String pesel,
                               String city, String street, String number) {

        // Przeszukujemy repo klientów, sprawdzamy czy klient z podanym peselem znajduje się już w repozytorium
        List<Client> existingClients = clientRepository.findByPesel(pesel);
        if (!existingClients.isEmpty()) {
            throw new RuntimeException("Klient o podanym numerze PESEL już istnieje w repozytorium.");
        }

        // Tworzenie adresu
        Address address;
        try {
            address = new Address(city, street, number);
        } catch (RuntimeException e) {
            throw e;
        }

        // Tworzenie klienta
        try {
            Client client = new Client(name, lastName, email, phoneNumber, pesel, address);
            clientRepository.addElement(client);
        } catch (RuntimeException e) {
            throw e;
        }
    }

    /**
     * Wypisuje klienta z systemu na podstawie numeru PESEL.
     *
     * @param pesel Numer PESEL klienta do wypisania
     */
    public void unregisterClient(String pesel) {
        List<Client> existingClients = findByPesel(pesel);
        if (existingClients.isEmpty()) {
            throw new RuntimeException("Nie znaleziono klienta w repozytorium.");
        }

        if (existingClients.get(0) == null) {
            throw new RuntimeException("Nieprawidłowy obiekt klienta.");
        }

        existingClients.get(0).setArchive(true);
    }

    /**
     * Wyszukuje klientów na podstawie nazwiska.
     *
     * @param lastName Nazwisko, według którego wyszukiwani są klienci
     * @return Lista klientów o podanym nazwisku
     */
    public List<Client> findByLastName(String lastName) {
        return clientRepository.findByLastName(lastName);
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
     * Wyszukuje klientów na podstawie adresu e-mail.
     *
     * @param email Adres e-mail, według którego wyszukiwani są klienci
     * @return Lista klientów o podanym adresie e-mail
     */
    public List<Client> findByEmailAddress(String email) {
        return clientRepository.findByEmailAddress(email);
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
    public List<Client> findByPesel(String pesel) {
        return clientRepository.findByPesel(pesel);
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

    /**
     * Generuje raport dotyczący klientów.
     *
     * @return Tekstowa reprezentacja danych raportu
     */
    public String report() {
        return clientRepository.report();
    }
}
