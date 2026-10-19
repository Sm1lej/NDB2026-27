package entity;

import jakarta.persistence.*;

@Entity
@Table(name = "vehicles")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class VehicleEnt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id")
    private ClientEnt owner;

    @Column(unique = true)
    private String vinNumber;
    private String registrationNumber;
    private int yearOfProduction;
    private String brand;
    private String model;
    private String color;
    private double engineDisplacement;
    private int enginePower;
    private int weight;

    protected VehicleEnt() {}

    public VehicleEnt(ClientEnt owner, String vinNumber, String registrationNumber, int yearOfProduction,
                      String brand, String model, String color, double engineDisplacement, int enginePower, int weight) {
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

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public ClientEnt getOwner() { return owner; }
    public void setOwner(ClientEnt owner) { this.owner = owner; }

    public String getVinNumber() { return vinNumber; }
    public void setVinNumber(String vinNumber) { this.vinNumber = vinNumber; }

    public String getRegistrationNumber() { return registrationNumber; }
    public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }

    public int getYearOfProduction() { return yearOfProduction; }
    public void setYearOfProduction(int yearOfProduction) { this.yearOfProduction = yearOfProduction; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public double getEngineDisplacement() { return engineDisplacement; }
    public void setEngineDisplacement(double engineDisplacement) { this.engineDisplacement = engineDisplacement; }

    public int getEnginePower() { return enginePower; }
    public void setEnginePower(int enginePower) { this.enginePower = enginePower; }

    public int getWeight() { return weight; }
    public void setWeight(int weight) { this.weight = weight; }
}