package entity;

import jakarta.persistence.*;

@Entity
@Table(name = "vehicles")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class VehicleEnt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String vinNumber;

    private String registrationNumber;
    private String brand;
    private String model;
    private int engineDisplacement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private ClientEnt owner;

    public VehicleEnt() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getVinNumber() { return vinNumber; }
    public void setVinNumber(String vinNumber) { this.vinNumber = vinNumber; }
    public ClientEnt getOwner() { return owner; }
    public void setOwner(ClientEnt owner) { this.owner = owner; }
}