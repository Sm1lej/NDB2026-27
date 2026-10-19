package entity;

import jakarta.persistence.*;
import model.Address;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clients")
public class ClientEnt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String pesel;

    private String name;
    private String lastName;

    private String email;
    private String phoneNumber;
    @ManyToOne
    @JoinColumn(name = "address_id")
    private AddressEnt address;
    private boolean isArchive;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VehicleEnt> vehicles = new ArrayList<>();

    public ClientEnt() {}

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getPesel() {
        return pesel;
    }
    public void setPesel(String pesel) {
        this.pesel = pesel;
    }
    public List<VehicleEnt> getVehicles() {
        return vehicles;
    }
    public void setVehicles(List<VehicleEnt> vehicles) {
        this.vehicles = vehicles;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public AddressEnt getAddress() {
        return address;
    }
    public void setAddress(AddressEnt address) {
        this.address = address;
    }
}