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
        Client client = new Client("Janusz", "Pogorzelski", "98765432109",
                "bigapple@email.com", "123123123", "Diamond");
        clientRepository.save(client);

        Service service = new RepairService("Przegląd okresowy", 200.0,
                Duration.ofHours(1), false, true, false);
        serviceRepository.save(service);

        Appointment appointment = new Appointment(LocalDateTime.now().plusDays(1), client);
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
    void deleteAppointmentTest() {
        Client client = new Client("Piotr", "Skowyrski", "12345678901",
                "izak@email.com", "111222333", "Silver");
        clientRepository.save(client);

        Service service = new RepairService("Drobna naprawa", 80.0,
                Duration.ofMinutes(20), false, false, false);
        serviceRepository.save(service);

        Appointment appointment = new Appointment(LocalDateTime.now().plusDays(2), client);
        appointment.addService(service);

        Invoice invoice = new Invoice(appointment);
        appointment.setInvoice(invoice);

        Appointment saved = appointmentRepository.save(appointment);

        appointmentRepository.deleteById(saved.getId());

        Optional<Appointment> deleted = appointmentRepository.findById(saved.getId());
        assertTrue(deleted.isEmpty());
    }
}