package Data.PostgreSQL.repositories.mappers;

import Data.PostgreSQL.repositories.entity.AppointmentEnt;
import Data.PostgreSQL.repositories.entity.InvoiceEnt;
import model.Appointment;
import model.Invoice;

public class InvoiceMapper {
    public InvoiceEnt toEntity(Invoice invoice, AppointmentEnt appointmentEnt) {
        if (invoice == null) {
            return null;
        }
        return new InvoiceEnt(appointmentEnt, invoice.isPaid());
    }

    public Invoice toDomain(InvoiceEnt invoiceEnt, Appointment appointment) {
        if (invoiceEnt == null) {
            return null;
        }

        Invoice invoice = new Invoice(invoiceEnt.getId(), appointment);
        if (invoiceEnt.isPaid()) {
            invoice.markAsPaid();
        }

        return invoice;
    }
}
