package Data.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clients")
public class ClientEnt {
    @Id
    @Column(length = 11)
    private String pesel;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String lastName;

    private String email;

    private String phoneNumber;

    private String clientType;

    @Column(nullable = false)
    private boolean isArchive;


    @OneToMany(mappedBy = "owner")
    private List<VehicleEnt> vehicles = new ArrayList<>();


    public String getPesel() { return pesel; }

    public void setPesel(String pesel) { this.pesel = pesel; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }

    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getClientType() { return clientType; }

    public void setClientType(String clientType) { this.clientType = clientType; }

    public boolean isArchive() { return isArchive; }

    public void setArchive(boolean archive) { isArchive = archive; }

    public List<VehicleEnt> getVehicles() { return vehicles; }

    public void setVehicles(List<VehicleEnt> vehicles) { this.vehicles = vehicles; }
}
