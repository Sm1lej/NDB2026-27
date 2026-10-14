package repositories;
import java.util.List;
import model.Client;

public class ClientRepository extends MainRepository<Client> {

    /**
     * Wyszukuje klientów na podstawie nazwiska.
     *
     * @param lastName Nazwisko, według którego wyszukiwani są klienci
     * @return Lista klientów o podanym nazwisku
     */
    public List<Client> findByLastName(String lastName) {
        return findBy(client -> client.getLastName().equals(lastName));
    }

    /**
     * Wyszukuje klientów na podstawie imienia i nazwiska.
     *
     * @param name Imię, według którego wyszukiwani są klienci
     * @param lastName Nazwisko, według którego wyszukiwani są klienci
     * @return Lista klientów o podanym imieniu i nazwisku
     */
    public List<Client> findByNameAndLastName(String name, String lastName) {
        // Predykat który sprawdza czy imię i nazwisko klienta są zgodne z podanymi
        return findBy(client -> client.getName().equals(name) && client.getLastName().equals(lastName));
    }

    /**
     * Wyszukuje klientów na podstawie adresu e-mail.
     *
     * @param email Adres e-mail, według którego wyszukiwani są klienci
     * @return Lista klientów o podanym adresie e-mail
     */
    public List<Client> findByEmailAddress(String email) {
        // Predykat który sprawdza czy email klienta jest zgodny z podanym
        return findBy(client -> client.getEmail().equals(email));
    }

    /**
     * Wyszukuje klientów na podstawie numeru telefonu.
     *
     * @param phoneNumber Numer telefonu, według którego wyszukiwani są klienci
     * @return Lista klientów o podanym numerze telefonu
     */
    public List<Client> findByPhoneNumber(String phoneNumber) {
        // Predykat który sprawdza czy nr telefonu klienta jest zgodny z podanym
        return findBy(client -> client.getPhoneNumber().equals(phoneNumber));
    }

    /**
     * Wyszukuje klientów na podstawie numeru PESEL.
     *
     * @param pesel Numer PESEL, według którego wyszukiwani są klienci
     * @return Lista klientów o podanym numerze PESEL
     */
    public List<Client> findByPesel(String pesel) {
        // Predykat który sprawdza czy pesel klienta jest zgodny z podanym
        return findBy(client -> client.getPesel().equals(pesel));
    }

    /**
     * Wyszukuje zarchiwizowanych klientów.
     *
     * @return Lista klientów oznaczonych jako zarchiwizowani
     */
    public List<Client> findByArchive() {
        return findBy(Client::isArchive);
    }

    /**
     * Generuje raport dotyczący klientów w repozytorium.
     *
     * @return Tekstowa reprezentacja danych raportu jako łańcuch znaków
     */
    public String report() {
        StringBuilder info = new StringBuilder();

        info.append("Ilość wszystkich klientów: ").append(getSize()).append("\n");

        int repoSize = getSize();


        for (int i = 0; i < repoSize; i++) {
            info.append("Klient ").append(i + 1).append(". ")
                .append(getElement(i).getInfo()).append("\n");
        }

        return info.toString();
    }
}
