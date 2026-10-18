package pl.lodz.p;

import managers.*;
import repositories.AppointmentRepository;
import repositories.ClientRepository;
import repositories.ServiceRepository;
import repositories.VehicleRepository;
import userInterface.UserInterface;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
//        // Inicjalizacja repozytoriów
//        ClientRepository clientRepo = new ClientRepository();
//        VehicleRepository vehicleRepo = new VehicleRepository();
//        ServiceRepository serviceRepo = new ServiceRepository();
//        AppointmentRepository appointmentRepo = new AppointmentRepository();
//
//        // Harmonogram pracy
//        List<WorkingHours.WeekDay> workingDays = Arrays.asList(
//                WorkingHours.WeekDay.Monday,
//                WorkingHours.WeekDay.Tuesday,
//                WorkingHours.WeekDay.Wednesday,
//                WorkingHours.WeekDay.Thursday,
//                WorkingHours.WeekDay.Friday
//        );
//        WorkingHours workingHours = new WorkingHours(LocalTime.of(8, 0), LocalTime.of(16, 0), workingDays);
//
//        // Inicjalizacja menedżerów
//        ClientManager clientManager = new ClientManager(clientRepo);
//        VehicleManager vehicleManager = new VehicleManager(vehicleRepo, clientRepo);
//        ServiceManager serviceManager = new ServiceManager(serviceRepo);
//        AppointmentScheduler scheduler = new AppointmentScheduler(workingHours, appointmentRepo);
//        AppointmentManager appointmentManager = new AppointmentManager(appointmentRepo, scheduler, clientRepo);
//
//        // Menu
//        UserInterface menu = new UserInterface(clientManager, vehicleManager, serviceManager, appointmentManager);
//        menu.run();

    }
}
