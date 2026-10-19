package entity;

import jakarta.persistence.*;

@Entity
@Table(name = "DefaultClientTypes")
public class DefaultClientTypeEnt extends ClientTypeEnt{
    public DefaultClientTypeEnt() {
        super();
    }
}
