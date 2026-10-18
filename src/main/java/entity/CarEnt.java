package entity;

import jakarta.persistence.*;

@Entity
@Table(name = "cars")
public class CarEnt extends VehicleEnt {

    private int numberOfDoors;

    public CarEnt() {}

    public int getNumberOfDoors() {
        return numberOfDoors; }
    public void setNumberOfDoors(int numberOfDoors) {
        this.numberOfDoors = numberOfDoors; }
}