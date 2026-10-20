package Data.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "cars")
@PrimaryKeyJoinColumn(name = "vin_number")
public class CarEnt extends VehicleEnt {
    @Column(nullable = false)
    private String bodyType;
    @Column(nullable = false)
    private String fuelType;
    @Column(nullable = false)
    private int doorsCount;

    protected CarEnt() { super(); }

    public CarEnt(ClientEnt owner, String vinNumber, String registrationNumber, int yearOfProduction,
                  String brand, String model, String color, double engineDisplacement, int enginePower, int weight,
                  String bodyType, String fuelType, int doorsCount) {
        super(owner, vinNumber, registrationNumber, yearOfProduction, brand, model, color,
                engineDisplacement, enginePower, weight);
        this.bodyType = bodyType;
        this.fuelType = fuelType;
        this.doorsCount = doorsCount;
    }

    public String getBodyType() { return bodyType; }
    public void setBodyType(String bodyType) { this.bodyType = bodyType; }
    public String getFuelType() { return fuelType; }
    public void setFuelType(String fuelType) { this.fuelType = fuelType; }
    public int getDoorsCount() { return doorsCount; }
    public void setDoorsCount(int doorsCount) { this.doorsCount = doorsCount; }
}
