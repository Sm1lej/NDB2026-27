package managers;
import java.time.LocalDateTime;
import java.time.Duration;
import java.util.List;

import model.Appointment;
import model.Service;
import repositories.AppointmentRepository;

public class AppointmentScheduler {
    private WorkingHours workingHours;
    private AppointmentRepository appointmentRepository;

    /**
     * Konstruktor klasy AppointmentScheduler, tworzący obiekt harmonogramu z podanymi zależnościami.
     *
     * @param workingHours Obiekt określający godziny pracy
     * @param appointmentRepository Repozytorium wizyt
     */
    public AppointmentScheduler(WorkingHours workingHours, AppointmentRepository appointmentRepository) {
        this.workingHours = workingHours;
        this.appointmentRepository = appointmentRepository;
    }

    /**
     * Sprawdza, czy można zaplanować wizytę w podanym terminie.
     *
     * @param startDateTime Data i czas rozpoczęcia wizyty
     * @param duration Czas trwania wizyty
     * @return Wartość logiczna wskazująca, czy termin jest dostępny
     */
    public boolean canScheduleAppointment(LocalDateTime startDateTime, Duration duration) {

        // Sprawdzenie, czy wizyta nie rozciąga się na więcej niż jeden dzień
        if (!startDateTime.plus(duration).toLocalDate().equals(startDateTime.toLocalDate())) {
            return false;
        }

        // Konwersja dnia tygodnia z formatu Java (1=Poniedziałek, ..., 7=Niedziela)
        // na indeks zgodny z enumem WeekDay (Poniedziałek=0, ..., Niedziela=6)
        int javaDay = startDateTime.getDayOfWeek().getValue();
        int weekDayIndex = javaDay - 1;

        // Sprawdzenie, czy podany dzień jest dniem roboczym
        if (!workingHours.isWorkingDay(weekDayIndex)) {
            return false;
        }

        // Pobranie czasu rozpoczęcia i zakończenia wizyty oraz sprawdzenie, czy mieszczą się w godzinach pracy
        var startTime = startDateTime.toLocalTime();
        var endTime = startDateTime.plus(duration).toLocalTime();
        if (startTime.isBefore(workingHours.getStartTime()) || endTime.isAfter(workingHours.getEndTime())) {
            return false;
        }

        // Obliczenie proponowanego końca wizyty
        LocalDateTime proposedEnd = startDateTime.plus(duration);
        List<Appointment> overlappingAppointments = appointmentRepository.findBy(app -> {
            // Pobranie czasu rozpoczęcia wizyty
            LocalDateTime appStart = app.getDate();

            // Obliczenie całkowitego czasu trwania wizyty poprzez sumowanie czasów usług
            long totalSeconds = 0;
            for (Service service : app.getServices()) {
                // Pobranie szacowanego czasu trwania usługi jako Duration
                Duration timePeriod = service.getEstimatedTime();
                // Dodanie czasu trwania tej usługi (w sekundach) do łącznego czasu
                totalSeconds += timePeriod.getSeconds();
            }
            // Konwersja łącznego czasu na Duration
            Duration appDuration = Duration.ofSeconds(totalSeconds);
            // Obliczenie końca wizyty
            LocalDateTime appEnd = appStart.plus(appDuration);
            // Sprawdzenie, czy istnieje nakład czasowy z proponowaną wizytą która jest aktualna/niearchiwalna
            return appStart.isBefore(proposedEnd) && startDateTime.isBefore(appEnd) && !app.isArchive();
        });

        // Zwrócenie true, jeśli nie ma nakładających się wizyt
        return overlappingAppointments.isEmpty();
    }
}
