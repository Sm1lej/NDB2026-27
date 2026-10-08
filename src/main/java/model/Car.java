package model;

/**
 * Klasa reprezentująca samochód, dziedzicząca po klasie Vehicle.
 * Zawiera dodatkowe informacje specyficzne dla samochodu, takie jak typ nadwozia, typ paliwa i liczba drzwi.
 */
public class Car extends Vehicle {

    private String bodyType;
    private String fuelType;
    private int doorsCount;

    /**
     * Konstruktor klasy Car, tworzący obiekt samochodu o wskazanych parametrach.
     */
    public Car(Client owner, String vinNumber, String registrationNumber, int yearOfProduction,
               String brand, String model, String color, double engineDisplacement, int enginePower, int weight,
               String bodyType, String fuelType, int doorsCount) {

        // Wywołanie konstruktora klasy nadrzędnej (Vehicle)
        super(owner, vinNumber, registrationNumber, yearOfProduction, brand, model, color, engineDisplacement, enginePower, weight);

        // Walidacja danych specyficznych dla samochodu
        if (bodyType == null || bodyType.isEmpty()) {
            throw new RuntimeException("Typ nadwozia nie może być pusty.");
        }

        if (fuelType == null || fuelType.isEmpty()) {
            throw new RuntimeException("Typ paliwa nie może być pusty.");
        }

        if (doorsCount < 0 || doorsCount > 8) {
            throw new RuntimeException("Liczba drzwi musi mieścić się w przedziale od 0 do 8.");
        }

        // Weryfikacja, czy typ nadwozia zawiera wyłącznie litery
        for (int i = 0; i < bodyType.length(); i++) {
            if (!Character.isLetter(bodyType.charAt(i))) {
                throw new RuntimeException("Typ nadwozia może zawierać wyłącznie litery.");
            }
        }

        // Weryfikacja, czy typ paliwa zawiera wyłącznie litery
        for (int i = 0; i < fuelType.length(); i++) {
            if (!Character.isLetter(fuelType.charAt(i))) {
                throw new RuntimeException("Typ paliwa może zawierać wyłącznie litery.");
            }
        }

        this.bodyType = bodyType;
        this.fuelType = fuelType;
        this.doorsCount = doorsCount;
    }

    public String getBodyType() {
        return bodyType;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public int getDoorsCount() {
        return doorsCount;
    }

    public void setDoorsCount(int doorsCount) {
        this.doorsCount = doorsCount;
    }

    @Override
    public String getInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.getInfo())
                .append("Typ nadwozia: ").append(getBodyType()).append("\n")
                .append("Typ paliwa: ").append(getFuelType()).append("\n")
                .append("Liczba drzwi: ").append(getDoorsCount()).append("\n");
        return sb.toString();
    }
}