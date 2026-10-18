package entity;

import jakarta.persistence.*;

@Entity
@Table(name = "motorbikes")
public class MotorbikeEnt extends VehicleEnt {

    private boolean hasBox;

    public MotorbikeEnt() {}

    public boolean isHasBox() {
        return hasBox; }
    public void setHasBox(boolean hasBox) {
        this.hasBox = hasBox; }
}