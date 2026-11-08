package Data.MongoDB.documents;

public class InvoiceDocument {

    private Integer id;
    private boolean isPaid;

    public InvoiceDocument() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean value) {
        this.isPaid = value;
    }

}