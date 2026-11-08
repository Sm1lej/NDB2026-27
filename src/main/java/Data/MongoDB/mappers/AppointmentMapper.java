package Data.MongoDB.mappers;

import Data.MongoDB.documents.AppointmentDocument;
import Data.MongoDB.documents.ClientDocument;
import Data.MongoDB.documents.InvoiceDocument;
import Data.MongoDB.documents.ServiceDocument;
import model.Appointment;
import model.Client;
import model.Invoice;
import model.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

public class AppointmentMapper {
    private final ClientMapper clientMapper = new ClientMapper();
    private final InvoiceMapper invoiceMapper = new InvoiceMapper();
    private final ServiceMapper serviceMapper = new ServiceMapper();

    public Appointment toDomain(AppointmentDocument appointmentDoc,
                                ClientDocument clientDocument,
                                ArrayList<ServiceDocument> servicesDoc) {
        LocalDateTime date = appointmentDoc.getDate().toInstant()
                .atZone(ZoneId.of("Europe/Warsaw")).toLocalDateTime();

        Appointment appointment = new Appointment(
                appointmentDoc.getId(),
                date,
                clientMapper.toDomain(clientDocument)
                );

        Invoice invoice = invoiceMapper.toDomain(appointmentDoc.getInvoice(), appointment);

        appointment.addInvoice(invoice);

        for (ServiceDocument s : servicesDoc) {
            Service service = serviceMapper.toDomain(s);
            appointment.addService(service);
        }

        if (appointmentDoc.isArchive()) {
            appointment.setArchive(true);
        }

        return appointment;
    }

    public AppointmentDocument toDocument(Appointment appointment) {
        AppointmentDocument appointmentDoc = new AppointmentDocument();
        Date date = Date.from(appointment.getDate().
                atZone(ZoneId.of("Europe/Warsaw")).toInstant());

        appointmentDoc.setDate(date);
        appointmentDoc.setId(appointment.getId());
        appointmentDoc.setInvoice(invoiceMapper.toDocument(appointment.getInvoice()));
        appointmentDoc.setArchive(appointment.isArchive());
        appointmentDoc.setClientPesel(appointment.getClient().getPesel());

        ArrayList<Integer> servicesIDs = new ArrayList<>();
        for (Service s : appointment.getServices()) {
            servicesIDs.add(s.getId());
        }
        appointmentDoc.setServiceIds(servicesIDs);

        return appointmentDoc;
    }
}
