package entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clients")
public class ClientEnt {

    @Id
    private String pesel;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String lastName;

    private String email;
    private String phoneNumber;
    private boolean isArchive;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VehicleEnt> vehicles = new ArrayList<>();

    public ClientEnt() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getPesel() { return pesel; }
    public void setPesel(String pesel) { this.pesel = pesel; }
    public List<VehicleEnt> getVehicles() { return vehicles; }
    public void setVehicles(List<VehicleEnt> vehicles) { this.vehicles = vehicles; }
}