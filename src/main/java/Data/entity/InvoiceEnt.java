package Data.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "invoices")
public class InvoiceEnt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false, unique = true)
    private AppointmentEnt appointment;

    private boolean isPaid;

    public InvoiceEnt() {}

    public InvoiceEnt(AppointmentEnt appointment, boolean isPaid) {
        this.appointment = appointment;
        this.isPaid = isPaid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public AppointmentEnt getAppointment() {
        return appointment;
    }

    public void setAppointment(AppointmentEnt appointment) {
        this.appointment = appointment;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }
}
