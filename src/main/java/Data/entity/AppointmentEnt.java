package Data.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "appointments")
public class AppointmentEnt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDateTime date;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_pesel", referencedColumnName = "pesel", nullable = false)
    private ClientEnt client;

    @OneToOne(mappedBy = "appointment", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private InvoiceEnt invoice;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "appointment_services",
            joinColumns = @JoinColumn(name = "appointment_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id"))
    private List<ServiceEnt> services = new ArrayList<>();

    private boolean isArchive;

    protected AppointmentEnt() {}

    public AppointmentEnt(LocalDateTime date, ClientEnt client, boolean isArchive) {
        this.date = date;
        this.client = client;
        this.isArchive = isArchive;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
    public ClientEnt getClient() { return client; }
    public void setClient(ClientEnt client) { this.client = client; }
    public InvoiceEnt getInvoice() { return invoice; }
    public void setInvoice(InvoiceEnt invoice) {
        if (this.invoice != null && this.invoice != invoice) {
            this.invoice.setAppointment(null);
        }
        this.invoice = invoice;
        if (invoice != null && invoice.getAppointment() != this) {
            invoice.setAppointment(this);
        }
    }
    public List<ServiceEnt> getServices() { return services; }
    public void setServices(List<ServiceEnt> services) { this.services = services; }
    public boolean isArchive() { return isArchive; }
    public void setArchive(boolean archive) { isArchive = archive; }
}
