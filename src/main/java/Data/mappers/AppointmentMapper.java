package Data.mappers;

import Data.entity.AppointmentEnt;
import Data.entity.ClientEnt;
import Data.entity.ServiceEnt;

import model.Appointment;
import model.Client;
import model.Service;

import java.util.List;

public class AppointmentMapper {

    private final ClientMapper clientMapper;
    private final ServiceMapper serviceMapper;
    private final InvoiceMapper invoiceMapper;

    public AppointmentMapper(ClientMapper clientMapper, ServiceMapper serviceMapper,
                             InvoiceMapper invoiceMapper ) {
        this.clientMapper = clientMapper;
        this.serviceMapper = serviceMapper;
        this.invoiceMapper = invoiceMapper;
    }

    public AppointmentEnt toEntity(Appointment appointment, ClientEnt clientEnt,
            List<ServiceEnt> serviceEntities) {
        if (appointment == null) {
            return null;
        }

        AppointmentEnt appointmentEnt = new AppointmentEnt(
                appointment.getDate(),
                clientEnt,
                appointment.isArchive()
        );

        appointmentEnt.getServices().addAll(serviceEntities);

        if (appointment.getInvoice() != null) {
            appointmentEnt.setInvoice(invoiceMapper.toEntity(appointment.getInvoice(),
                    appointmentEnt));
        }

        return appointmentEnt;
    }

    public Appointment toDomain(AppointmentEnt appointmentEnt) {
        if (appointmentEnt == null) {
            return null;
        }

        Client client = clientMapper.toDomain(appointmentEnt.getClient());

        Appointment appointment = new Appointment(appointmentEnt.getId(), appointmentEnt.getDate(),
                client);

        appointment.setArchive(appointmentEnt.isArchive());

        for (ServiceEnt serviceEnt : appointmentEnt.getServices()) {
            Service service = serviceMapper.toDomain(serviceEnt);
            appointment.addService(service);
        }

        if (appointmentEnt.getInvoice() != null) {
            appointment.addInvoice(invoiceMapper.toDomain(appointmentEnt.getInvoice(), appointment));
        }

        return appointment;
    }
}
