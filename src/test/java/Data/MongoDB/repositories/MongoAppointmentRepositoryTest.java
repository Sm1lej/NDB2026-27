package Data.MongoDB.repositories;

import Data.MongoDB.MongoConfig;
import Data.MongoDB.mappers.AppointmentMapper;
import Data.MongoDB.mappers.ClientMapper;
import Data.MongoDB.mappers.InvoiceMapper;
import Data.MongoDB.mappers.ServiceMapper;
import model.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import repositories.AppointmentRepository;
import repositories.ClientRepository;
import repositories.ServiceRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MongoAppointmentRepositoryTest {
    private static MongoConfig mongoConfig;
    private static AppointmentRepository appointmentRepository;
    private static ClientRepository clientRepository;
    private static ServiceRepository serviceRepository;

    @BeforeAll
    static void setup() {
        mongoConfig = new MongoConfig(
                "mongodb://mongo1:27017,mongo2:27018,mongo3:27019/"
                        + "?replicaSet=rs0&retryWrites=true"
        );

        ClientMapper clientMapper = new ClientMapper();
        ServiceMapper serviceMapper = new ServiceMapper();
        InvoiceMapper invoiceMapper = new InvoiceMapper();
        AppointmentMapper appointmentMapper = new AppointmentMapper();

        clientRepository = new MongoClientRepository(mongoConfig.getDatabase(), clientMapper);
        serviceRepository = new MongoServiceRepository(mongoConfig.getDatabase(), serviceMapper);
        appointmentRepository = new MongoAppointmentRepository(
                mongoConfig.getDatabase(),
                appointmentMapper,
                invoiceMapper,
                serviceMapper
        );
    }

    @AfterAll
    static void close() {
        mongoConfig.close();
    }

    @Test
    void createAndGetAppointmentTest() {
        Client client = new Client("Robert", "Kubica", "44433322211",
                "robert@email.com", "600700800", "Diamond");
        clientRepository.save(client);

        Service service = new RepairService("Przegląd okresowy", 200.0,
                Duration.ofHours(1), false, true, false);
        serviceRepository.save(service);

        Appointment appointment = new Appointment(LocalDateTime.now().plusDays(1).withNano(0), client);
        appointment.addService(service);

        Invoice invoice = new Invoice(appointment);
        appointment.setInvoice(invoice);

        Appointment savedAppointment = appointmentRepository.save(appointment);
        assertNotNull(savedAppointment.getId());

        Optional<Appointment> appointmentFromMongo = appointmentRepository.findById(savedAppointment.getId());

        assertTrue(appointmentFromMongo.isPresent());
        Appointment foundAppointment = appointmentFromMongo.get();
        assertEquals(client.getPesel(), foundAppointment.getClient().getPesel());
        assertFalse(foundAppointment.getServices().isEmpty());
    }

    @Test
    void findByClientPeselTest() {
        String pesel = "44433322211";
        Client client = new Client("Robert", "Kubica", pesel,
                "robert@email.com", "600700800", "Diamond");
        clientRepository.save(client);

        Service service = new DiagnosticService("Diagnostyka komputerowa", 150.0,
                Duration.ofMinutes(45), true, true, true);
        serviceRepository.save(service);

        Appointment appointment = new Appointment(LocalDateTime.now().plusDays(3).withNano(0), client);
        appointment.addService(service);
        appointment.setInvoice(new Invoice(appointment));

        Appointment saved = appointmentRepository.save(appointment);

        List<Appointment> appointments = appointmentRepository.findByClientPesel(pesel);

        assertFalse(appointments.isEmpty());
        assertTrue(appointments.stream().anyMatch(a -> a.getId().equals(saved.getId())));
        assertEquals(pesel, appointments.get(0).getClient().getPesel());
    }

    @Test
    void findByDateTest() {
        Client client = new Client("Robert", "Kubica", "44433322211",
                "robert@email.com", "600700800", "Diamond");
        clientRepository.save(client);

        Service service = new RepairService("Wymiana opon", 120.0,
                Duration.ofMinutes(30), true, false, false);
        serviceRepository.save(service);

        LocalDateTime exactDate = LocalDateTime.now().plusDays(5).withNano(0);
        Appointment appointment = new Appointment(exactDate, client);
        appointment.addService(service);
        appointment.setInvoice(new Invoice(appointment));

        Appointment saved = appointmentRepository.save(appointment);

        List<Appointment> appointments = appointmentRepository.findByDate(exactDate);

        assertFalse(appointments.isEmpty());
        assertTrue(appointments.stream().anyMatch(a -> a.getId().equals(saved.getId())));
    }

    @Test
    void findByDateRangeTest() {
        Client client = new Client("Robert", "Kubica", "44433322211",
                "robert@email.com", "600700800", "Diamond");
        clientRepository.save(client);

        Service service = new DiagnosticService("Przegląd hamulców", 100.0,
                Duration.ofMinutes(30), false, false, true);
        serviceRepository.save(service);

        LocalDateTime appointmentDate = LocalDateTime.now().plusDays(10).withNano(0);
        Appointment appointment = new Appointment(appointmentDate, client);
        appointment.addService(service);
        appointment.setInvoice(new Invoice(appointment));

        Appointment saved = appointmentRepository.save(appointment);

        LocalDateTime from = appointmentDate.minusHours(2);
        LocalDateTime to = appointmentDate.plusHours(2);

        List<Appointment> appointments = appointmentRepository.findByDateRange(from, to);

        assertFalse(appointments.isEmpty());
        assertTrue(appointments.stream().anyMatch(a -> a.getId().equals(saved.getId())));
    }

    @Test
    void deleteAppointmentTest() {
        Client client = new Client("Robert", "Kubica", "44433322211",
                "robert@email.com", "600700800", "Diamond");
        clientRepository.save(client);

        Service service = new RepairService("Drobna naprawa", 80.0,
                Duration.ofMinutes(20), false, false, false);
        serviceRepository.save(service);

        Appointment appointment = new Appointment(LocalDateTime.now().plusDays(2).withNano(0), client);
        appointment.addService(service);

        Invoice invoice = new Invoice(appointment);
        appointment.setInvoice(invoice);

        Appointment saved = appointmentRepository.save(appointment);

        appointmentRepository.deleteById(saved.getId());

        Optional<Appointment> deleted = appointmentRepository.findById(saved.getId());
        assertTrue(deleted.isEmpty());
    }
}