package model;

/**
 * Klasa reprezentująca pojazd.
 * Zawiera podstawowe informacje o pojeździe, takie jak numer VIN,
 * numer rejestracyjny, rok produkcji, marka, model, kolor, pojemność silnika,
 * moc silnika, waga oraz informacje o właścicielu.
 */
public abstract class Vehicle {

    private final Client owner;
    private String vinNumber;
    private String registrationNumber;
    private int yearOfProduction;
    private String brand;
    private String model;
    private String color;
    private double engineDisplacement;
    private int enginePower;
    private int weight;

    public Vehicle(Client owner, String vinNumber, String registrationNumber, int yearOfProduction,
                   String brand, String model, String color, double engineDisplacement, int enginePower, int weight) {

        // Podstawowa walidacja z użyciem RuntimeException
        if (owner == null) {
            throw new RuntimeException("Właściciel pojazdu nie może być nullem.");
        }

        if (vinNumber == null || vinNumber.isEmpty()) {
            throw new RuntimeException("Numer VIN nie może być pusty.");
        }

        if (yearOfProduction < 1900) {
            throw new RuntimeException("Rok produkcji jest nieprawidłowy (musi być większy lub równy 1900).");
        }

        if (weight <= 0) {
            throw new RuntimeException("Waga pojazdu musi być większa od 0.");
        }

        if (brand.length() > 20 ){
            throw new RuntimeException("Nazwa marki nie może być dłuższa niż 20 znaków.");
        }

        if (model.length() > 20 ){
            throw new RuntimeException("Nazwa modelu nie może być dłuższa niż 20 znaków.");
        }

        if (engineDisplacement < 50 || engineDisplacement > 10000) {
            throw new RuntimeException("Pojemność silnika jest poza zakresem (50-10000).");
        }

        if (enginePower < 1 || enginePower > 2000) {
            throw new RuntimeException("Moc silnika jest poza zakresem (1-2000).");
        }
        if (color.length() > 20){
            throw new RuntimeException("Nazwa koloru jest zbyt długa.");
        }


        // Przypisanie wartości po pomyślnej walidacji
        this.owner = owner;
        this.vinNumber = vinNumber;
        this.registrationNumber = registrationNumber;
        this.yearOfProduction = yearOfProduction;
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.engineDisplacement = engineDisplacement;
        this.enginePower = enginePower;
        this.weight = weight;
    }

    public Client getOwner() {
        return owner;
    }

    public String getVinNumber() {
        return vinNumber;
    }

    public void setVinNumber(String vinNumber) {
        this.vinNumber = vinNumber;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public int getYearOfProduction() {
        return yearOfProduction;
    }

    public void setYearOfProduction(int yearOfProduction) {
        this.yearOfProduction = yearOfProduction;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public double getEngineDisplacement() {
        return engineDisplacement;
    }

    public void setEngineDisplacement(double engineDisplacement) {
        this.engineDisplacement = engineDisplacement;
    }

    public int getEnginePower() {
        return enginePower;
    }

    public void setEnginePower(int enginePower) {
        this.enginePower = enginePower;
    }

    public int getWeight() {
        return weight;
    }

    public String getInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("Dane pojazdu:\n")
                .append("Vin: ").append(getVinNumber()).append("\n")
                .append("Numer rejestracyjny: ").append(getRegistrationNumber()).append("\n")
                .append("Rok produkcji: ").append(getYearOfProduction()).append("\n")
                .append("Marka: ").append(getBrand()).append("\n")
                .append("Kolor: ").append(getColor()).append("\n")
                .append("Pojemność silnika: ").append(getEngineDisplacement()).append(".cm³\n")
                .append("Moc silnika: ").append(getEnginePower()).append(" km\n")
                .append("Waga: ").append(getWeight()).append(" kg\n");
        return sb.toString();
    }
}