package Data.document;

import org.bson.codecs.pojo.annotations.BsonId;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDocument {

    @BsonId
    private Integer id;

    private LocalDateTime date;
    private String clientPesel;
    private List<Integer> serviceIds = new ArrayList<>();
    private InvoiceDocument invoice;
    private boolean archive;

    public AppointmentDocument() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getClientPesel() {
        return clientPesel;
    }

    public void setClientPesel(String clientPesel) {
        this.clientPesel = clientPesel;
    }

    public List<Integer> getServiceIds() {
        return serviceIds;
    }

    public void setServiceIds(List<Integer> serviceIds) {
        this.serviceIds = serviceIds;
    }

    public InvoiceDocument getInvoice() {
        return invoice;
    }

    public void setInvoice(InvoiceDocument invoice) {
        this.invoice = invoice;
    }

    public boolean isArchive() {
        return archive;
    }

    public void setArchive(boolean archive) {
        this.archive = archive;
    }
}