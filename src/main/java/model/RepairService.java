package model;
import java.time.Duration;

/**
 * Klasa reprezentująca usługę naprawy, dziedzicząca po klasie Service.
 * Zawiera informacje specyficzne dla usługi naprawy, takie jak zmiana opon,
 * uzupełnianie płynów i naprawa układu hamulcowego.
 */
public class RepairService extends Service {

    private boolean changeTires;
    private boolean fillFluids;
    private boolean fixBrakeSystem;

    /**
     * Konstruktor klasy RepairService, tworzący obiekt usługi naprawy o wskazanych parametrach.
     */
    public RepairService(String description, double basePrice, Duration estimatedDurationTime,
                         boolean changeTires, boolean fillFluids, boolean fixBrakeSystem) {

        super(description, basePrice, estimatedDurationTime);
        this.changeTires = changeTires;
        this.fillFluids = fillFluids;
        this.fixBrakeSystem = fixBrakeSystem;
    }

    public RepairService(Integer id, String description, double basePrice, Duration estimatedDurationTime,
                         boolean changeTires, boolean fillFluids, boolean fixBrakeSystem){
        this(description, basePrice, estimatedDurationTime, changeTires, fillFluids, fixBrakeSystem);
        super.setId(id);
    }

    public boolean isChangeTires() {
        return changeTires;
    }

    public void setChangeTires(boolean changeTires) {
        this.changeTires = changeTires;
    }

    public boolean isFillFluids() {
        return fillFluids;
    }

    public void setFillFluids(boolean fillFluids) {
        this.fillFluids = fillFluids;
    }

    public boolean isFixBrakeSystem() {
        return fixBrakeSystem;
    }

    public void setFixBrakeSystem(boolean fixBrakeSystem) {
        this.fixBrakeSystem = fixBrakeSystem;
    }

    @Override
    public double getPriceForClient() {
        double price = super.getBasePrice();
        if (changeTires) {
            price += 75;
        }
        if (fillFluids) {
            price += 50;
        }
        if (fixBrakeSystem) {
            price += 150;
        }
        return price;
    }

    @Override
    public Duration getTotalEstimatedTime() {
        Duration duration = super.getEstimatedTime();
        if (duration == null) {
            duration = Duration.ZERO;
        }
        if (changeTires) {
            duration = duration.plusMinutes(20);
        }
        if (fillFluids) {
            duration = duration.plusMinutes(10);
        }
        if (fixBrakeSystem) {
            duration = duration.plusMinutes(30);
        }
        return duration;
    }

    @Override
    public String getInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.getInfo())
                .append("Typ usługi: Naprawcza\n")
                .append("Wymiana opon: ").append(changeTires ? "Tak" : "Nie").append("\n")
                .append("Uzupełnianie płynów: ").append(fillFluids ? "Tak" : "Nie").append("\n")
                .append("Naprawa hamulców: ").append(fixBrakeSystem ? "Tak" : "Nie").append("\n")
                .append("Całkowita cena: ").append(getPriceForClient()).append(" PLN\n")
                .append("Całkowity czas: ").append(getEstimatedTime()).append("\n");
        return sb.toString();
    }
}
