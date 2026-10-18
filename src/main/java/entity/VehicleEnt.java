package entity;

import jakarta.persistence.*;

@Entity
@Table(name = "vehicles")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class VehicleEnt {

    @Id
    private String vinNumber;

    private String registrationNumber;
    private String brand;
    private String model;
    private int engineDisplacement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private ClientEnt owner;

    public VehicleEnt() {}


    public String getVinNumber() { return vinNumber; }
    public void setVinNumber(String vinNumber) { this.vinNumber = vinNumber; }
    public ClientEnt getOwner() { return owner; }
    public void setOwner(ClientEnt owner) { this.owner = owner; }
}