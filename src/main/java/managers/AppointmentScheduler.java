
package managers;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

import model.Appointment;
import repositories.AppointmentRepository;

public class AppointmentScheduler {

    private final WorkingHours workingHours;
    private final AppointmentRepository appointmentRepository;

    public AppointmentScheduler(
            WorkingHours workingHours,
            AppointmentRepository appointmentRepository) {

        this.workingHours = Objects.requireNonNull(
                workingHours,
                "Godziny pracy nie mogą być null."
        );

        this.appointmentRepository = Objects.requireNonNull(
                appointmentRepository,
                "Repozytorium wizyt nie może być null."
        );
    }

    public boolean canScheduleAppointment(
            LocalDateTime startDateTime,
            Duration duration) {

        if (startDateTime == null || duration == null) {
            throw new IllegalArgumentException(
                    "Data i czas trwania nie mogą być null."
            );
        }

        if (duration.isNegative() || duration.isZero()) {
            throw new IllegalArgumentException(
                    "Czas trwania wizyty musi być dodatni."
            );
        }

        LocalDateTime proposedEnd = startDateTime.plus(duration);

        // Wizyta nie może trwać dłużej niż jeden dzień.
        if (!proposedEnd.toLocalDate()
                .equals(startDateTime.toLocalDate())) {
            return false;
        }

        int weekDayIndex = startDateTime.getDayOfWeek().getValue() - 1;

        // Sprawdzenie dnia roboczego.
        if (!workingHours.isWorkingDay(weekDayIndex)) {
            return false;
        }

        // Sprawdzenie godzin pracy.
        if (startDateTime.toLocalTime()
                .isBefore(workingHours.getStartTime())
                || proposedEnd.toLocalTime()
                .isAfter(workingHours.getEndTime())) {
            return false;
        }

        // Pobieramy wizyty z tego samego dnia.
        LocalDateTime dayStart = startDateTime
                .toLocalDate()
                .atStartOfDay();

        LocalDateTime nextDayStart = dayStart.plusDays(1);

        List<Appointment> appointments =
                appointmentRepository.findByDateRange(
                        dayStart,
                        nextDayStart
                );

        // Sprawdzamy kolizje terminów.
        for (Appointment appointment : appointments) {

            if (appointment.isArchive()) {
                continue;
            }

            LocalDateTime existingStart = appointment.getDate();

            Duration existingDuration = Duration.ofMinutes(
                    appointment.getTotalDurationInMinutes()
            );

            LocalDateTime existingEnd =
                    existingStart.plus(existingDuration);

            boolean overlaps =
                    existingStart.isBefore(proposedEnd)
                            && startDateTime.isBefore(existingEnd);

            if (overlaps) {
                return false;
            }
        }

        return true;
    }
}
