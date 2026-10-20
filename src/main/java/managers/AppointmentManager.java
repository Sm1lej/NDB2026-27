package managers;

import model.Appointment;
import model.Client;
import model.Invoice;
import model.Service;
import repositories.AppointmentRepository;
import repositories.ClientRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class AppointmentManager {

    private final AppointmentScheduler appointmentScheduler;
    private final AppointmentRepository appointmentRepository;
    private final ClientRepository clientRepository;

    public AppointmentManager(AppointmentRepository appointmentRepository, AppointmentScheduler appointmentScheduler,
            ClientRepository clientRepository) {

        this.appointmentRepository = Objects.requireNonNull(
                appointmentRepository,
                "Nieprawidłowe repozytorium wizyt."
        );

        this.appointmentScheduler = Objects.requireNonNull(
                appointmentScheduler,
                "Nieprawidłowy harmonogram."
        );

        this.clientRepository = Objects.requireNonNull(
                clientRepository,
                "Nieprawidłowe repozytorium klientów."
        );
    }

    public Appointment registerAppointment(
            List<Service> services,
            LocalDateTime date,
            String clientPesel) {

        if (services == null || services.isEmpty()) {
            throw new IllegalArgumentException(
                    "Lista usług jest pusta."
            );
        }

        if (date == null) {
            throw new IllegalArgumentException(
                    "Data wizyty nie może być null."
            );
        }

        if (clientPesel == null || clientPesel.isBlank()) {
            throw new IllegalArgumentException(
                    "PESEL klienta nie może być pusty."
            );
        }

        Client client = clientRepository.findById(clientPesel)
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Nie znaleziono klienta o podanym numerze PESEL."
                ));

        Duration duration = Duration.ZERO;

        for (Service service : services) {
            if (service == null) {
                throw new IllegalArgumentException(
                        "Usługa nie może być null."
                );
            }

            duration = duration.plus(service.getEstimatedTime());
        }

        if (!appointmentScheduler.canScheduleAppointment(date, duration)) {
            throw new IllegalStateException(
                    "Nie można zaplanować wizyty w podanym terminie."
            );
        }

        Appointment appointment = new Appointment(date, client);

        for (Service service : services) {
            appointment.addService(service);
        }

        Invoice invoice = new Invoice(appointment);
        appointment.addInvoice(invoice);

        return appointmentRepository.save(appointment);
    }

    public void unregisterAppointment(Appointment appointment) {

        Appointment existingAppointment =
                getExistingAppointment(appointment);

        existingAppointment.setArchive(true);

        appointmentRepository.save(existingAppointment);
    }

    public void cancelAppointment(Appointment appointment) {

        Appointment existingAppointment =
                getExistingAppointment(appointment);

        appointmentRepository.deleteById(existingAppointment.getId());
    }

    private Appointment getExistingAppointment(Appointment appointment) {

        if (appointment == null || appointment.getId() == null) {
            throw new IllegalArgumentException(
                    "Wizyta musi posiadać identyfikator."
            );
        }

        return appointmentRepository.findById(appointment.getId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Nie znaleziono wizyty o ID: " + appointment.getId()
                ));
    }

    public List<Appointment> findByClientPesel(String clientPesel) {
        return appointmentRepository.findByClientPesel(clientPesel);
    }

    public List<Appointment> findByDate(LocalDateTime date) {
        return appointmentRepository.findByDate(date);
    }

    public Optional<Appointment> findById(Integer id) {
        return appointmentRepository.findById(id);
    }


    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }
}
