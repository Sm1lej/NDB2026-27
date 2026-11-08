package Data.MongoDB.mappers;

import Data.MongoDB.documents.AppointmentDocument;
import Data.MongoDB.documents.InvoiceDocument;
import model.Appointment;
import model.Invoice;

public class InvoiceMapper {
    public Invoice toDomain(InvoiceDocument invoiceDoc,
                            Appointment appointment) {
        Invoice invoice = new Invoice(
                invoiceDoc.getId(),
                appointment
        );

        if (invoiceDoc.isPaid()) {
            invoice.markAsPaid();
        }

        return invoice;
    }

    public InvoiceDocument toDocument(Invoice invoice) {
        InvoiceDocument invoiceDoc = new InvoiceDocument();

        invoiceDoc.setId(invoice.getId());
        invoiceDoc.setPaid(invoice.isPaid());

        return invoiceDoc;
    }
}
