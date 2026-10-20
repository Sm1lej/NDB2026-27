package Data.adapters;

import Data.mappers.AppointmentMapper;
import Data.mappers.ClientMapper;
import Data.mappers.InvoiceMapper;
import Data.mappers.ServiceMapper;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import model.Appointment;
import model.Client;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AppointmentRepositoryJpaTest {

    private EntityManagerFactory emf;
    private AppointmentRepositoryJpa appointmentRepo;
    private ClientRepositoryJpa clientRepo;

    @BeforeAll
    void setUp() {
        emf = Persistence.createEntityManagerFactory("WarsztatPU");

        ClientMapper clientMapper = new ClientMapper();

        clientRepo = new ClientRepositoryJpa(emf, clientMapper);

        appointmentRepo = new AppointmentRepositoryJpa(emf, new AppointmentMapper(clientMapper,
                        new ServiceMapper(),
                        new InvoiceMapper()));
    }

    @AfterAll
    void close() {
        if (emf != null) {
            emf.close();
        }
    }

    private Client createTestClient() {
        Client client = new Client(
                "Test",
                "Testowy",
                "12312312311",
                "test@test.pl",
                "123456789",
                "Default"
        );

        return clientRepo.save(client);
    }

    @Test
    void shouldSaveAndFindAppointment() {
        Client client = createTestClient();

        Appointment appointment = new Appointment(
                LocalDateTime.now().plusDays(5),
                client
        );

        Appointment saved = appointmentRepo.save(appointment);

        assertNotNull(saved.getId());

        Appointment found = appointmentRepo.findById(saved.getId())
                .orElseThrow();

        assertEquals(saved.getId(), found.getId());
        assertEquals(client.getPesel(), found.getClient().getPesel());
    }

    @Test
    void shouldUpdateAppointmentDate() {
        Client client = createTestClient();

        Appointment appointment = appointmentRepo.save(
                new Appointment(LocalDateTime.now().plusDays(5), client)
        );

        LocalDateTime newDate = LocalDateTime.now().plusDays(10).withNano(0);

        appointment.setDate(newDate);
        appointmentRepo.save(appointment);

        Appointment found = appointmentRepo.findById(appointment.getId())
                .orElseThrow();

        assertEquals(newDate, found.getDate());
    }

    @Test
    void shouldFindAppointmentsByDateRange() {
        Client client = createTestClient();

        LocalDateTime date = LocalDateTime.now().plusDays(15);

        Appointment appointment = appointmentRepo.save(
                new Appointment(date, client)
        );

        List<Appointment> results = appointmentRepo.findByDateRange(
                date.minusHours(1),
                date.plusHours(1)
        );

        assertTrue(results.stream()
                .anyMatch(a -> a.getId().equals(appointment.getId())));
    }

    @Test
    void shouldRejectUpdateOfNonExistingAppointment() {
        Client client = createTestClient();

        Appointment appointment = new Appointment(
                999999999,
                LocalDateTime.now().plusDays(5),
                client
        );

        assertThrows(IllegalArgumentException.class,
                () -> appointmentRepo.save(appointment));
    }
}