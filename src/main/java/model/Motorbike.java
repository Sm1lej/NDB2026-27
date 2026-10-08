package model;

/**
 * Klasa reprezentująca motocykl, dziedzicząca po klasie Vehicle.
 * Zawiera dodatkowe informacje specyficzne dla motocykla, takie jak system hamulcowy.
 */
public class Motorbike extends Vehicle {

    private String brakeSystem;

    /**
     * Konstruktor klasy Motorbike, tworzący obiekt motocykla o wskazanych parametrach.
     */
    public Motorbike(Client owner, String vinNumber, String registrationNumber, int yearOfProduction,
                     String brand, String model, String color, double engineDisplacement, int enginePower, int weight,
                     String brakeSystem) {

        // Wywołanie konstruktora klasy nadrzędnej (Vehicle)
        super(owner, vinNumber, registrationNumber, yearOfProduction, brand, model, color, engineDisplacement, enginePower, weight);

        // Walidacja danych specyficznych dla motocykla
        if (brakeSystem == null || brakeSystem.isEmpty()) {
            throw new RuntimeException("System hamulcowy nie może być pusty.");
        }

        if (brakeSystem.length() > 100) {
            throw new RuntimeException("Nazwa systemu hamulcowego nie może przekraczać 100 znaków.");
        }

        // Weryfikacja, czy system hamulcowy zawiera wyłącznie litery
        for (int i = 0; i < brakeSystem.length(); i++) {
            if (!Character.isLetter(brakeSystem.charAt(i))) {
                throw new RuntimeException("Nazwa systemu hamulcowego może zawierać wyłącznie litery.");
            }
        }

        this.brakeSystem = brakeSystem;
    }

    public String getBrakeSystem() {
        return brakeSystem;
    }

    public void setBrakeSystem(String brakeSystem) {
        this.brakeSystem = brakeSystem;
    }

    @Override
    public String getInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.getInfo())
                .append("System hamulcowy: ").append(getBrakeSystem()).append("\n");
        return sb.toString();
    }
}
