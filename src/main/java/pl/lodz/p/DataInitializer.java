package pl.lodz.p;

import model.*;
import repositories.AppointmentRepository;
import repositories.ClientRepository;
import repositories.ServiceRepository;
import repositories.VehicleRepository;

import java.time.Duration;
import java.time.LocalDateTime;

public class DataInitializer {

    private final ClientRepository clientRepository;
    private final VehicleRepository vehicleRepository;
    private final ServiceRepository serviceRepository;
    private final AppointmentRepository appointmentRepository;

    public DataInitializer(
            ClientRepository clientRepository,
            VehicleRepository vehicleRepository,
            ServiceRepository serviceRepository,
            AppointmentRepository appointmentRepository
    ) {
        this.clientRepository = clientRepository;
        this.vehicleRepository = vehicleRepository;
        this.serviceRepository = serviceRepository;
        this.appointmentRepository = appointmentRepository;
    }

    public void initialize() {
        if (!clientRepository.findAll().isEmpty()) {
            return;
        }

        Client firstClient = clientRepository.save(new Client(
                "Jan",
                "Kowalski",
                "90000000001",
                "jan.kowalski@example.com",
                "501234567",
                "Default"
        ));

        Client secondClient = clientRepository.save(new Client(
                "Anna",
                "Nowak",
                "90000000002",
                "anna.nowak@example.com",
                "502345678",
                "Gold"
        ));

        Car car = new Car(firstClient, "abcdfghnhfrtersdw", "EL321", 2003,
                "Audi", "A3", "szary", 1600, 105,
                1300, "HatchBack", "benzyna", 3);

        Motorbike motorbike = new Motorbike(secondClient, "1234fghnDFrtersdw", "EP666", 2003,
                "Jawa", "SuperSzybka", "czerwony", 1000, 105,
                130, "HatchBack");
        vehicleRepository.save(car);
        vehicleRepository.save(motorbike);

        Service firstService = serviceRepository.save(
                new DiagnosticService(
                        "Diagnostyka układu hamulcowego",
                        150.0,
                        Duration.ofMinutes(123),
                        false,
                        true,
                        false
                )
        );

        Service secondService = serviceRepository.save(
                new RepairService("Naprawa czegos tam napewno", 150.0, Duration.ofMinutes(67),
                        true,
                        false,
                        false
                )
        );

        Appointment firstAppointment = new Appointment(
                LocalDateTime.now().plusDays(5).withNano(0),
                firstClient
        );

        firstAppointment.addService(firstService);
        appointmentRepository.save(firstAppointment);

        Appointment secondAppointment = new Appointment(
                LocalDateTime.now().plusDays(7).withNano(0),
                secondClient
        );

        secondAppointment.addService(secondService);
        appointmentRepository.save(secondAppointment);
    }
}