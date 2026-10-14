package managers;
import model.Appointment;
import model.Service;
import repositories.AppointmentRepository;
import repositories.ClientRepository;
import model.Client;
import model.Invoice;
import java.time.LocalDateTime;
import java.time.Duration;
import java.util.List;

public class AppointmentManager {
    private AppointmentScheduler appointmentScheduler;
    private AppointmentRepository appointmentRepository;
    private ClientRepository clientRepository;

    /**
     * Konstruktor klasy AppointmentManager, tworzący obiekt menedżera z podanymi zależnościami.
     *
     * @param appointmentRepository Repozytorium wizyt
     * @param appointmentScheduler Harmonogram wizyt
     * @param clientRepository Repozytorium klientów
     */
    public AppointmentManager(AppointmentRepository appointmentRepository,
                              AppointmentScheduler appointmentScheduler,
                              ClientRepository clientRepository) {
        if (appointmentRepository == null) {
            throw new RuntimeException("Nieprawidłowe repozytorium wizyt.");
        }
        if (appointmentScheduler == null) {
            throw new RuntimeException("Nieprawidłowy harmonogram.");
        }
        if (clientRepository == null) {
            throw new RuntimeException("Nieprawidłowe repozytorium klientów.");
        }

        this.appointmentRepository = appointmentRepository;
        this.appointmentScheduler = appointmentScheduler;
        this.clientRepository = clientRepository;
    }

    /**
     * Rejestruje nową wizytę z podanymi usługami, datą i numerem PESEL klienta.
     *
     * @param services Lista usług
     * @param date Data i czas wizyty
     * @param clientPesel Numer PESEL klienta
     */
    public void registerAppointment(List<Service> services, LocalDateTime date, String clientPesel) {
        if (services == null || services.isEmpty()) {
            throw new RuntimeException("Lista usług jest pusta.");
        }

        List<Client> existingClients = clientRepository.findByPesel(clientPesel);
        if (existingClients.isEmpty()) {
            throw new RuntimeException("Nie znaleziono klienta o podanym numerze PESEL.");
        }

        long totalSeconds = 0;
        for (Service service : services) {
            if (service != null) {
                Duration timePeriod = service.getEstimatedTime();
                totalSeconds += timePeriod.getSeconds();
            }
        }
        Duration duration = Duration.ofSeconds(totalSeconds);

        if (!appointmentScheduler.canScheduleAppointment(date, duration)) {
            throw new RuntimeException("Nie można zaplanować wizyty w podanym terminie.");
        }

        int newId = appointmentRepository.getSize() + 1;

        try {
            Appointment appointment = new Appointment(newId, date, existingClients.get(0));

            for (Service service : services) {
                appointment.addService(service);
            }

            Invoice invoice = new Invoice(newId, appointment);
            appointment.addInvoice(invoice);

            appointmentRepository.addElement(appointment);
        } catch (RuntimeException e) {
            throw e;
        }
    }

    /**
     * Wypisuje wizytę z systemu, zachowując jej dane.
     *
     * @param appointment Wizyta do wypisania
     */
    public void unregisterAppointment(Appointment appointment) {
        if (appointment == null) {
            throw new RuntimeException("Wizyta nie może być null.");
        }

        List<Appointment> existingAppointments = findByClientPesel(appointment.getClient().getPesel());
        if (existingAppointments.isEmpty()) {
            throw new RuntimeException("Nie znaleziono wizyty dla podanego klienta.");
        }

        existingAppointments.get(0).setArchive(true);
    }

    /**
     * Anuluje wizytę, usuwając ją z harmonogramu.
     *
     * @param appointment Wizyta do anulowania
     */
    public void cancelAppointment(Appointment appointment) {
        if (appointment == null) {
            throw new RuntimeException("Wizyta nie może być null.");
        }

        List<Appointment> existingAppointments = findByClientPesel(appointment.getClient().getPesel());
        if (existingAppointments.isEmpty()) {
            throw new RuntimeException("Nie znaleziono wizyty dla podanego klienta.");
        }

        appointmentRepository.removeElement(appointment);
    }

    /**
     * Wyszukuje wizyty powiązane z określonym klientem.
     *
     * @param clientPesel Numer PESEL klienta
     * @return Lista wizyt powiązanych z klientem
     */
    public List<Appointment> findByClientPesel(String clientPesel) {
        return appointmentRepository.findByClientPesel(clientPesel);
    }

    /**
     * Wyszukuje wizyty odbywające się w określonym dniu.
     *
     * @param date Data, dla której wyszukiwane są wizyty
     * @return Lista wizyt z podanej daty
     */
    public List<Appointment> findByDate(LocalDateTime date) {
        return appointmentRepository.findByDate(date);
    }

    /**
     * Wyszukuje wizyty na podstawie identyfikatora.
     *
     * @param id Identyfikator wizyty
     * @return Lista wizyt o podanym identyfikatorze
     */
    public List<Appointment> findById(int id) {
        return appointmentRepository.findById(id);
    }

    /**
     * Wyszukuje zarchiwizowane wizyty.
     *
     * @return Lista zarchiwizowanych wizyt
     */
    public List<Appointment> findByArchive() {
        return appointmentRepository.findByArchive();
    }

    /**
     * Zwraca wszystkie zarejestrowane wizyty.
     *
     * @return Lista wszystkich wizyt
     */
    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }

    /**
     * Generuje raport dotyczący wizyt.
     *
     * @return Tekstowa reprezentacja danych raportu
     */
    public String report() {
        return appointmentRepository.report();
    }
}
