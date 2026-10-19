package entity;

import jakarta.persistence.*;

@Entity
@Table(name = "clientTypes")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class ClientTypeEnt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public ClientTypeEnt() {}

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
}
