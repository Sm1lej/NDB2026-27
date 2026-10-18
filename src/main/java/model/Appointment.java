package model;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Appointment {

    private Integer id;
    private LocalDateTime date;
    private Client client;
    private Invoice invoice;
    private final List<Service> services = new ArrayList<>();
    private boolean isArchive = false;

    public Appointment(LocalDateTime date, Client client) {
        setDate(date);
        setClient(client);
    }

    public Appointment(Integer id, LocalDateTime date, Client client) {
        this(date, client);
        setId(id);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        if (id == null || id <= 0) {
            throw new RuntimeException("Identyfikator wizyty musi być większy od zera");
        }
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        if (date == null) {
            throw new RuntimeException("Data wizyty nie może być nullem");
        }
        if (date.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Data wizyty nie może być w przeszłości");
        }
        this.date = date;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        if (client == null) {
            throw new RuntimeException("Klient nie może być nullem");
        }
        this.client = client;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void addInvoice(Invoice invoice) {
        if (this.invoice != null) {
            throw new RuntimeException("Wizyta posiada już przypisaną fakturę");
        }
        if (invoice == null) {
            throw new RuntimeException("Faktura nie może być nullem");
        }
        this.invoice = invoice;
    }

    public List<Service> getServices() {
        return Collections.unmodifiableList(services);
    }

    public boolean addService(Service service) {
        if (service == null) {
            throw new RuntimeException("Usługa nie może być nullem");
        }

        if (services.contains(service)) {
            return false;
        }

        return services.add(service);
    }

    public boolean removeService(Service service) {
        if (service == null) {
            throw new RuntimeException("Usługa nie może być nullem");
        }

        return services.remove(service);
    }

    public boolean isArchive() {
        return isArchive;
    }

    public void setArchive(boolean archive) {
        isArchive = archive;
    }

    public int getTotalDurationInMinutes() {
        long totalSeconds = 0;

        for (Service service : services) {
            // Przyjmujemy, że service.getEstimatedTime() zwraca java.time.Duration
            totalSeconds += service.getEstimatedTime().getSeconds();
        }

        return (int) (totalSeconds / 60);
    }

    public String getInfo() {
        StringBuilder ss = new StringBuilder();
        ss.append("ID: ").append(getId()).append("\n")
                .append("Data: ").append(getDate()).append("\n")
                .append("Klient: ").append(getClient().getName()).append(" ").append(getClient().getLastName()).append("\n")
                .append("Faktura: ").append(getInvoice() != null ? getInvoice().getId() : "Brak").append("\n")
                .append("Status: ").append(isArchive() ? "archiwalna" : "aktywna").append("\n");

        return ss.toString();
    }
}
