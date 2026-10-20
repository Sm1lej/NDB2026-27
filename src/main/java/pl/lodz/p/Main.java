package pl.lodz.p;

import Data.adapters.AppointmentRepositoryJpa;
import Data.adapters.ClientRepositoryJpa;
import Data.adapters.ServiceRepositoryJpa;
import Data.adapters.VehicleRepositoryJpa;
import Data.mappers.*;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
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

          try (EntityManagerFactory emf = Persistence.createEntityManagerFactory("WarsztatPU")) {
              ClientRepository clientRepo = new ClientRepositoryJpa(emf, new ClientMapper());
              AppointmentRepository appointmentRepo = new AppointmentRepositoryJpa(emf,
                      new AppointmentMapper(new ClientMapper(), new ServiceMapper(), new InvoiceMapper()));
              ServiceRepository serviceRepo = new ServiceRepositoryJpa(emf, new ServiceMapper());
              VehicleRepository vehicleRepo = new VehicleRepositoryJpa(emf, new VehicleMapper());


              // Harmonogram pracy
              List<WorkingHours.WeekDay> workingDays = Arrays.asList(
                      WorkingHours.WeekDay.Monday,
                      WorkingHours.WeekDay.Tuesday,
                      WorkingHours.WeekDay.Wednesday,
                      WorkingHours.WeekDay.Thursday,
                      WorkingHours.WeekDay.Friday
              );
              WorkingHours workingHours = new WorkingHours(LocalTime.of(8, 0), LocalTime.of(16, 0), workingDays);

              ClientManager clientManager = new ClientManager(clientRepo);
              VehicleManager vehicleManager = new VehicleManager(vehicleRepo, clientRepo);
              ServiceManager serviceManager = new ServiceManager(serviceRepo);
              AppointmentScheduler scheduler = new AppointmentScheduler(workingHours, appointmentRepo);
              AppointmentManager appointmentManager = new AppointmentManager(appointmentRepo, scheduler, clientRepo);


              UserInterface menu = new UserInterface(clientManager, vehicleManager, serviceManager, appointmentManager);
              menu.run();

          }



    }
}
