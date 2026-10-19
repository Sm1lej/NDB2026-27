package entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "appointments")
public class AppointmentEnt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private ClientEnt client;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id")
    private InvoiceEnt invoice;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    private List<ServiceEnt> services = new ArrayList<>();

    @Column(nullable = false)
    private boolean isArchive;

    public AppointmentEnt() {}

    public AppointmentEnt(LocalDateTime date, ClientEnt client, boolean isArchive) {
        this.date = date;
        this.client = client;
        this.isArchive = isArchive;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public ClientEnt getClient() {
        return client;
    }

    public void setClient(ClientEnt client) {
        this.client = client;
    }

    public InvoiceEnt getInvoice() {
        return invoice;
    }

    public void setInvoice(InvoiceEnt invoice) {
        this.invoice = invoice;
    }

    public List<ServiceEnt> getServices() {
        return services;
    }

    public void setServices(List<ServiceEnt> services) {
        this.services = services;
    }

    public boolean isArchive() {
        return isArchive;
    }

    public void setArchive(boolean archive) {
        this.isArchive = archive;
    }
}
