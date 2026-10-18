package entity;

import jakarta.persistence.*;

@Entity
@Table(name = "invoices")
public class InvoiceEnt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false)
    private AppointmentEnt appointment;

    @Column(nullable = false)
    private boolean isPaid;

    public InvoiceEnt() {}

    public InvoiceEnt(AppointmentEnt appointment, boolean isPaid) {
        this.appointment = appointment;
        this.isPaid = isPaid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
