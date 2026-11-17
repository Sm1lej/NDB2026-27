package pl.lodz.p;

import Data.MongoDB.MongoConfig;
import Data.MongoDB.repositories.MongoAppointmentRepository;
import Data.MongoDB.repositories.MongoClientRepository;
import Data.MongoDB.mappers.*;
import Data.MongoDB.repositories.MongoServiceRepository;
import Data.MongoDB.repositories.MongoVehicleRepository;
import Data.PostgreSQL.repositories.repositories.AppointmentRepositoryJpa;
import Data.PostgreSQL.repositories.repositories.ClientRepositoryJpa;
import Data.PostgreSQL.repositories.repositories.ServiceRepositoryJpa;
import Data.PostgreSQL.repositories.repositories.VehicleRepositoryJpa;
import com.mongodb.client.MongoDatabase;
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
    static void main() {
//        // Inicjalizacja repozytoriów

          try (MongoConfig mongoConfig = 
                       new MongoConfig("mongodb://mongo1:27017,mongo2:27018,mongo3:27019/" +
                               "?replicaSet=rs0&retryWrites=true")) {
              MongoDatabase database = mongoConfig.getDatabase();
              
              ClientRepository clientRepo = new MongoClientRepository(database, new ClientMapper());
              AppointmentRepository appointmentRepo = new MongoAppointmentRepository(database,
                      new AppointmentMapper(), new InvoiceMapper(), new ServiceMapper());

              ServiceRepository serviceRepo = new MongoServiceRepository(database, new ServiceMapper());
              VehicleRepository vehicleRepo = new MongoVehicleRepository(database, new VehicleMapper(),
                                                                            new ClientMapper());


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


              DataInitializer dataInitializer = new DataInitializer(clientRepo, vehicleRepo,
                      serviceRepo, appointmentRepo);
              dataInitializer.initialize();

              UserInterface menu = new UserInterface(clientManager, vehicleManager, serviceManager, appointmentManager);
              menu.run();
          }
    }
}
