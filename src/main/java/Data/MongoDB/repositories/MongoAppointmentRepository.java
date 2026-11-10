package Data.MongoDB.repositories;

import Data.MongoDB.documents.AppointmentDocument;
import Data.MongoDB.documents.ClientDocument;
import Data.MongoDB.documents.ServiceDocument;
import Data.MongoDB.mappers.AppointmentMapper;
import Data.MongoDB.mappers.InvoiceMapper;
import Data.MongoDB.mappers.ServiceMapper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.*;
import model.Appointment;
import model.Invoice;
import model.Service;
import org.bson.Document;
import repositories.AppointmentRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MongoAppointmentRepository implements AppointmentRepository {

    private final MongoCollection<AppointmentDocument> appointmentCollection;
    private final MongoCollection<ServiceDocument> serviceCollection;
    private final MongoCollection<ClientDocument> clientCollection;
    private final MongoCollection<Document> counters;

    private final AppointmentMapper appointmentMapper;
    private final InvoiceMapper invoiceMapper;
    private final ServiceMapper serviceMapper;

    public MongoAppointmentRepository(MongoDatabase database,
                                      AppointmentMapper appointmentMapper,
                                      InvoiceMapper invoiceMapper,
                                      ServiceMapper serviceMapper) {
        appointmentCollection = database.getCollection(
                "appointments",
                AppointmentDocument.class
        );

        serviceCollection = database.getCollection(
                "services",
                ServiceDocument.class
        );

        clientCollection = database.getCollection(
                "clients",
                ClientDocument.class
        );

        counters = database.getCollection(
                "counters",
                Document.class
        );

        this.appointmentMapper = appointmentMapper;
        this.invoiceMapper = invoiceMapper;
        this.serviceMapper = serviceMapper;
    }


    @Override
    public Appointment save(Appointment appointment) {
        if (appointment == null) {
            return null;
        }

        String clientId = appointment.getClient().getPesel();
        ClientDocument client = clientCollection.find(
                Filters.eq("_id", clientId)
        ).first();

        if (client == null) {
            throw new IllegalArgumentException("Klient nie istnieje w bazie");
        }

        if (appointment.getId() == null) {
            Integer newId = getNewId();
            appointment.setId(newId);

            Invoice invoice = appointment.getInvoice();
            invoice.setId(newId);
        }

            List<Integer> serviceIds = findServicesIds(appointment);

            AppointmentDocument appointmentDoc = appointmentMapper.
                    toDocument(appointment);

            appointmentDoc.setServiceIds(serviceIds);

            appointmentCollection.replaceOne(
                    Filters.eq("_id", appointmentDoc.getId()),
                    appointmentDoc,
                    new ReplaceOptions().upsert(true)
            );

        return appointment;
    }

    private List<Integer> findServicesIds(Appointment appointment) {
        List<Integer> serviceIds = new ArrayList<>();

        for (Service service : appointment.getServices()) {
            if (service.getId() == null) {
                throw new IllegalArgumentException("Usługa nie ma ID");
            }

            ServiceDocument serviceDoc = serviceCollection.find(
                    Filters.eq("_id", service.getId())
            ).first();

            if (serviceDoc == null) {
                throw new IllegalArgumentException("Usługa nie istnieje w bazie");
            }

            serviceIds.add(serviceDoc.getId());
        }

        return serviceIds;
    }


    @Override
    public Optional<Appointment> findById(Integer id) {
        AppointmentDocument appointmentDoc = appointmentCollection.find(
                Filters.eq("_id", id)
        ).first();

        if (appointmentDoc == null) {
            return Optional.empty();
        }

        String clientId = appointmentDoc.getClientPesel();
        List<Integer> serviceIds = appointmentDoc.getServiceIds();

        ClientDocument clientDoc = clientCollection.find(
                Filters.eq("_id", clientId)
        ).first();

        List<ServiceDocument> serviceDocs = new ArrayList<>();
        for (Integer i : serviceIds) {
            ServiceDocument serviceDoc = serviceCollection.find(
                    Filters.eq("_id", i)
            ).first();

            serviceDocs.add(serviceDoc);
        }

        return Optional.of(appointmentMapper.toDomain(appointmentDoc, clientDoc, serviceDocs));
    }

    @Override
    public List<Appointment> findAll() {
        List<AppointmentDocument> appointmentDocs = appointmentCollection.
                find().into(new ArrayList<>());

        List<Appointment> appointments = new ArrayList<>();

        for (AppointmentDocument appointmentDoc : appointmentDocs) {
            Appointment appointment = findById(appointmentDoc.getId()).
                    orElseThrow();

            appointments.add(appointment);
        }

        return appointments;
    }

    @Override
    public void deleteById(Integer integer) {

    }

    @Override
    public List<Appointment> findByClientPesel(String clientPesel) {
        return List.of();
    }

    @Override
    public List<Appointment> findByDate(LocalDateTime date) {
        return List.of();
    }

    @Override
    public List<Appointment> findByDateRange(LocalDateTime from, LocalDateTime to) {
        return List.of();
    }

    private Integer getNewId() {
        Document counter = counters.findOneAndUpdate(
                Filters.eq("_id", "appointments"),
                Updates.inc("seq", 1),
                new FindOneAndUpdateOptions().
                        upsert(true).
                        returnDocument(ReturnDocument.AFTER)
        );

        return counter.getInteger("seq");
    }

}
