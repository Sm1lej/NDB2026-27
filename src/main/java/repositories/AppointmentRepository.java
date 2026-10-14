package repositories;

import java.time.LocalDateTime;
import java.util.List;
import model.Appointment;

public class AppointmentRepository extends MainRepository<Appointment> {

    /**
     * Wyszukuje wizyty powiązane z określonym klientem
     *
     * @return Lista wizyt powiązanych z klientem
     * @ numer pesel klienta, którego wizyty mają być znalezione
     */
    public List<Appointment> findByClientPesel(String clientPesel) {
        return findBy(appointment -> appointment.getClient() != null
                && clientPesel.equals(appointment.getClient().getPesel()));
    }

    /**
     * Wyszukuje wizyty odbywające się w określonym dniu/czasie
     *
     * @param date Data, dla której wyszukiwane są wizyty
     * @return Lista wizyt z podanej daty
     */
    public List<Appointment> findByDate(LocalDateTime date) {
        return findBy(appointment -> date.equals(appointment.getDate()));
    }

    /**
     * Wyszukuje wizyty na podstawie identyfikatora
     *
     * @param id Identyfikator wizyty
     * @return Lista wizyt o podanym identyfikatorze
     */
    public List<Appointment> findById(int id) {
        return findBy(appointment -> appointment.getId() == id);
    }

    /**
     * Wyszukuje zarchiwizowane wizyty
     *
     * @return Lista wizyt oznaczonych jako zarchiwizowane
     */
    public List<Appointment> findByArchive() {
        return findBy(Appointment::isArchive);
    }

    /**
     * Generuje raport dotyczący wizyt w repozytorium
     *
     * @return Tekstowa reprezentacja danych raportu jako łańcuch znaków
     */
    public String report() {
        StringBuilder info = new StringBuilder();
        info.append("Ilość wizyt: ").append(getSize()).append("\n");

        int repoSize = getSize();
        for (int i = 0; i < repoSize; i++) {
            info.append("Wizyta ").append(i + 1).append(". ")
                    .append(getElement(i).getInfo()).append("\n");
        }

        return info.toString();
    }
}