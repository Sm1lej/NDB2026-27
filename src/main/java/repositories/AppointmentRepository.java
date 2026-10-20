package repositories;

import java.time.LocalDateTime;
import java.util.List;
import model.Appointment;

public interface AppointmentRepository extends MainRepository<Appointment, Integer> {

     List<Appointment> findByClientPesel(String clientPesel);

     List<Appointment> findByDate(LocalDateTime date);

//     List<Appointment> findByArchive();
//
//    List<Appointment> findByDateRange(LocalDateTime from, LocalDateTime to);

}