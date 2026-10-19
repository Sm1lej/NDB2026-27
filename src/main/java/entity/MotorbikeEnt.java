package entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "motorbikes")
public class MotorbikeEnt extends VehicleEnt {

    private String brakeSystem;

    public MotorbikeEnt() {
        super();
    }

    public MotorbikeEnt(ClientEnt owner, String vinNumber, String registrationNumber, int yearOfProduction,
                        String brand, String model, String color, double engineDisplacement, int enginePower, int weight,
                        String brakeSystem) {
        super(owner, vinNumber, registrationNumber, yearOfProduction, brand, model, color, engineDisplacement, enginePower, weight);
        this.brakeSystem = brakeSystem;
    }

    public String getBrakeSystem() {
        return brakeSystem;
    }

    public void setBrakeSystem(String brakeSystem) {
        this.brakeSystem = brakeSystem;
    }
}