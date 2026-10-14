package managers;

import java.time.LocalTime;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;


public class WorkingHours {
    private LocalTime startTime;
    private LocalTime endTime;
    private List<WeekDay> workingDays;

    public enum WeekDay {
        Monday,
        Tuesday,
        Wednesday,
        Thursday,
        Friday,
        Saturday,
        Sunday
    }
    /**
     * Konstruktor klasy WorkingHours, tworzący obiekt z podanymi godzinami i dniami roboczymi.
     *
     * @param startTime Czas rozpoczęcia pracy
     * @param endTime Czas zakończenia pracy
     * @param workingDays Lista dni roboczych
     */
    public WorkingHours(LocalTime startTime, LocalTime endTime, List<WeekDay> workingDays) {
        if (startTime == null || endTime == null) {
            throw new RuntimeException("Nieprawidłowy czas pracy.");
        }

        if (startTime.isAfter(endTime)) {
            throw new RuntimeException("Czas rozpoczęcia pracy jest późniejszy niż czas zakończenia.");
        }

        // Sprawdzamy czy jest minimum 1h pracy, jeśli nie, zgłoszony zostanie wyjątek
        if (Duration.between(startTime, endTime).toHours() < 1) {
            throw new RuntimeException("Czas pracy musi wynosić co najmniej 1 godzinę.");
        }

        this.startTime = startTime;
        this.endTime = endTime;
        this.workingDays = workingDays != null ? new ArrayList<>(workingDays) : new ArrayList<>();
    }

    /**
     * Sprawdza, czy dany dzień tygodnia jest dniem roboczym.
     *
     * @param dayOfWeek Indeks dnia tygodnia (0-7, zgodny z WeekDay)
     * @return Wartość logiczna wskazująca, czy dzień jest roboczy
     */
    public boolean isWorkingDay(int dayOfWeek) {
        if (dayOfWeek < 0 || dayOfWeek > 7) {
            throw new RuntimeException("Nieprawidłowy dzień tygodnia.");
        }

        if (dayOfWeek >= WeekDay.values().length) {
            return false;
        }

        WeekDay day = WeekDay.values()[dayOfWeek];
        return workingDays.contains(day);
    }

    /**
     * Sprawdza, czy podany czas mieści się w godzinach pracy.
     *
     * @param time Czas do sprawdzenia
     * @return Wartość logiczna wskazująca, czy czas mieści się w godzinach pracy
     */
    public boolean isWithinWorkingHours(LocalTime time) {
        if (time == null) {
            throw new RuntimeException("Nieprawidłowy czas.");
        }

        return !time.isBefore(startTime) && !time.isAfter(endTime);
    }

    /**
     * Zwraca czas rozpoczęcia pracy.
     *
     * @return Czas rozpoczęcia pracy
     */
    public LocalTime getStartTime() {
        return startTime;
    }

    /**
     * Zwraca czas zakończenia pracy.
     *
     * @return Czas zakończenia pracy
     */
    public LocalTime getEndTime() {
        return endTime;
    }

    /**
     * Ustawia zakres dni roboczych.
     *
     * @param from Początkowy dzień roboczy
     * @param to Końcowy dzień roboczy
     */
    public void setWorkingDaysRange(WeekDay from, WeekDay to) {
        if (from == null || to == null || from.ordinal() < 0 || from.ordinal() > 7 || to.ordinal() < 0 || to.ordinal() > 7) {
            throw new RuntimeException("Nieprawidłowe dni robocze.");
        }

        if (from.ordinal() > to.ordinal()) {
            throw new RuntimeException("Nieprawidłowy zakres dni roboczych.");
        }

        workingDays.clear();
        for (int i = from.ordinal(); i <= to.ordinal(); i++) {
            workingDays.add(WeekDay.values()[i]);
        }
    }
}
