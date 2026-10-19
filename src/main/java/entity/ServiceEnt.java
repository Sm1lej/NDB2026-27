package entity;

import jakarta.persistence.*;

@Entity
@Table(name = "services")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class ServiceEnt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private double basePrice;

    public ServiceEnt() {}

    public ServiceEnt(String description, double basePrice) {
        this.description = description;
        this.basePrice = basePrice;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public double getBasePrice() {
        return basePrice;
    }
    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }
}