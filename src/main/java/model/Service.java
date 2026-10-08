package model;
import java.time.Duration;

/**
 * Klasa bazowa reprezentująca usługę warsztatową.
 * Zawiera podstawowe informacje o usłudze, takie jak identyfikator, opis, cena bazowa oraz szacowany czas trwania.
 */
public abstract class Service {

    private int id;
    private String description;
    private double basePrice;
    private Duration estimatedDurationTime;

    public Service(int id, String description, double basePrice, Duration estimatedDurationTime) {

        if (id < 0) {
            throw new RuntimeException("ID usługi nie może być ujemne.");
        }

        if (description == null || description.isEmpty()) {
            throw new RuntimeException("Opis usługi nie może być pusty.");
        }

        if (basePrice < 0) {
            throw new RuntimeException("Cena bazowa nie może być ujemna.");
        }

        if (estimatedDurationTime == null || estimatedDurationTime.isNegative()) {
            throw new RuntimeException("Czas trwania usługi jest nieprawidłowy.");
        }

        this.id = id;
        this.description = description;
        this.basePrice = basePrice;
        this.estimatedDurationTime = estimatedDurationTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public double getPriceForClient() {
        return basePrice;
    }

    public Duration getEstimatedTime() {
        return estimatedDurationTime;
    }

    public void setEstimatedDurationTime(Duration estimatedDurationTime) {
        this.estimatedDurationTime = estimatedDurationTime;
    }

    public String getInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(getId()).append("\n")
                .append("Opis: ").append(getDescription() == null || getDescription().isEmpty() ? "brak" : getDescription()).append("\n")
                .append("Cena bazowa: ").append(getBasePrice()).append(" PLN\n")
                .append("Szacowany czas: ").append(getEstimatedTime()).append("\n");
        return sb.toString();
    }
}
