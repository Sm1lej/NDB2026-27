package model;
import java.time.Duration;

/**
 * Klasa reprezentująca usługę diagnostyczną, dziedzicząca po klasie Service.
 * Zawiera informacje specyficzne dla usługi diagnostycznej, takie jak sprawdzanie opon,
 * układu hamulcowego i stanu płynów.
 */
public class DiagnosticService extends Service {

    private boolean checkTires;
    private boolean checkFluids;
    private boolean checkBrake;

    /**
     * Konstruktor klasy DiagnosticService, tworzący obiekt usługi diagnostycznej o wskazanych parametrach.
     */
    public DiagnosticService(String description, double basePrice, Duration estimatedDurationTime,
                             boolean checkTires, boolean checkFluids, boolean checkBrake) {

        super(description, basePrice, estimatedDurationTime);
        this.checkTires = checkTires;
        this.checkFluids = checkFluids;
        this.checkBrake = checkBrake;
    }

    public DiagnosticService(Integer id, String description, double basePrice, Duration estimatedDurationTime,
                         boolean checkTires, boolean checkFluids, boolean checkBrake){
        this(description, basePrice, estimatedDurationTime, checkTires, checkFluids, checkBrake);
        super.setId(id);
    }

    public boolean isCheckTires() {
        return checkTires;
    }

    public void setCheckTires(boolean checkTires) {
        this.checkTires = checkTires;
    }

    public boolean isCheckFluids() {
        return checkFluids;
    }

    public void setCheckFluids(boolean checkFluids) {
        this.checkFluids = checkFluids;
    }

    public boolean isCheckBrake() {
        return checkBrake;
    }

    public void setCheckBrake(boolean checkBrake) {
        this.checkBrake = checkBrake;
    }

    @Override
    public double getPriceForClient() {
        double price = super.getPriceForClient();
        if (checkTires) {
            price += 25;
        }
        if (checkBrake) {
            price += 30;
        }
        if (checkFluids) {
            price += 20;
        }
        return price;
    }

    @Override
    public Duration getEstimatedTime() {
        Duration duration = super.getEstimatedTime();
        if (duration == null) {
            duration = Duration.ZERO;
        }
        if (checkTires) {
            duration = duration.plusMinutes(10);
        }
        if (checkBrake) {
            duration = duration.plusMinutes(15);
        }
        if (checkFluids) {
            duration = duration.plusMinutes(5);
        }
        return duration;
    }

    @Override
    public String getInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.getInfo())
                .append("Typ usługi: Diagnostyczna\n")
                .append("Sprawdzenie opon: ").append(checkTires ? "Tak" : "Nie").append("\n")
                .append("Sprawdzenie hamulców: ").append(checkBrake ? "Tak" : "Nie").append("\n")
                .append("Sprawdzenie płynów: ").append(checkFluids ? "Tak" : "Nie").append("\n")
                .append("Całkowita cena: ").append(getPriceForClient()).append(" PLN\n")
                .append("Całkowity czas: ").append(getEstimatedTime()).append("\n");
        return sb.toString();
    }
}