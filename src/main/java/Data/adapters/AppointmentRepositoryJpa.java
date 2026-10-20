package Data.adapters;

import Data.entity.AppointmentEnt;
import Data.entity.ClientEnt;
import Data.entity.InvoiceEnt;
import Data.entity.ServiceEnt;
import Data.mappers.AppointmentMapper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

import model.Appointment;
import model.Service;

import repositories.AppointmentRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AppointmentRepositoryJpa implements AppointmentRepository {

    private final EntityManagerFactory emf;
    private final AppointmentMapper mapper;

    public AppointmentRepositoryJpa(EntityManagerFactory emf, AppointmentMapper mapper) {
        this.emf = emf;
        this.mapper = mapper;
    }

    @Override
    public Appointment save(Appointment appointment) {
        if (appointment == null) {
            throw new IllegalArgumentException(
                    "Wizyta nie może być nullem"
            );
        }

        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction transaction = em.getTransaction();

            try {
                transaction.begin();

                ClientEnt clientEnt = em.find(ClientEnt.class,
                        appointment.getClient().getPesel());

                if (clientEnt == null) {
                    throw new IllegalArgumentException(
                            "Klient nie istnieje w bazie");
                }

                List<ServiceEnt> serviceEntities = findServiceEntities(em, appointment);

                AppointmentEnt appointmentEnt;

                if (appointment.getId() == null) {
                    // NOWA WIZYTA

                    appointmentEnt = mapper.toEntity(appointment, clientEnt,
                            serviceEntities);

                    em.persist(appointmentEnt);

                } else {
                    // ISTNIEJĄCA WIZYTA

                    appointmentEnt = em.find(AppointmentEnt.class,
                            appointment.getId());

                    if (appointmentEnt == null) {
                        throw new IllegalArgumentException("Nie znaleziono wizyty o ID: "
                                        + appointment.getId());
                    }

                    updateAppointmentEnt(appointmentEnt, appointment, clientEnt,
                            serviceEntities);
                }

                em.flush();

                Appointment result = mapper.toDomain(appointmentEnt);

                transaction.commit();

                return result;

            } catch (RuntimeException e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw e;
            }
        }
    }

    private List<ServiceEnt> findServiceEntities(EntityManager em, Appointment appointment) {
        List<ServiceEnt> serviceEntities = new ArrayList<>();

        for (Service service : appointment.getServices()) {
            if (service.getId() == null) {
                throw new IllegalArgumentException("Usługa nie ma ID");
            }

            ServiceEnt serviceEnt = em.find(ServiceEnt.class, service.getId());

            if (serviceEnt == null) {
                throw new IllegalArgumentException(
                        "Nie znaleziono usługi o ID: "
                                + service.getId()
                );
            }

            serviceEntities.add(serviceEnt);
        }

        return serviceEntities;
    }

    private void updateAppointmentEnt(AppointmentEnt appointmentEnt, Appointment appointment,
            ClientEnt clientEnt, List<ServiceEnt> serviceEntities) {

        appointmentEnt.setDate(appointment.getDate());
        appointmentEnt.setClient(clientEnt);
        appointmentEnt.setArchive(appointment.isArchive());

        appointmentEnt.getServices().clear();
        appointmentEnt.getServices().addAll(serviceEntities);

        if (appointment.getInvoice() != null) {
            InvoiceEnt invoiceEnt = appointmentEnt.getInvoice();

            if (invoiceEnt == null) {
                // Faktura nie istniała — tworzymy nową
                invoiceEnt = new InvoiceEnt(appointmentEnt,
                        appointment.getInvoice().isPaid());

                appointmentEnt.setInvoice(invoiceEnt);

            } else {
                // Faktura istnieje — aktualizujemy stan
                invoiceEnt.setPaid(
                        appointment.getInvoice().isPaid()
                );
            }
        }
    }

    @Override
    public Optional<Appointment> findById(Integer id) {

        try (EntityManager em = emf.createEntityManager()) {
            AppointmentEnt appointmentEnt = em.find(AppointmentEnt.class,
                    id);

            return Optional.ofNullable(appointmentEnt)
                    .map(mapper::toDomain);
        }
    }

    @Override
    public List<Appointment> findAll() {
        try (EntityManager em = emf.createEntityManager()) {
            List<AppointmentEnt> appointmentEntities =
                    em.createQuery(
                            "SELECT a FROM AppointmentEnt a",
                            AppointmentEnt.class).getResultList();

            return appointmentEntities.stream()
                    .map(mapper::toDomain)
                    .toList();
        }
    }

    @Override
    public void deleteById(Integer id) {

        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction transaction = em.getTransaction();

            try {
                transaction.begin();

                AppointmentEnt appointmentEnt = em.find(AppointmentEnt.class, id);

                if (appointmentEnt == null) {
                    throw new IllegalArgumentException(
                            "Nie znaleziono wizyty o ID: " + id);
                }

                em.remove(appointmentEnt);

                transaction.commit();

            } catch (RuntimeException e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw e;
            }
        }
    }

    @Override
    public List<Appointment> findByClientPesel(String clientPesel) {
        if (clientPesel == null || clientPesel.isBlank()) {
            throw new IllegalArgumentException(
                    "PESEL klienta nie może być pusty");
        }

        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery(
                            """
                            SELECT a FROM AppointmentEnt a
                            WHERE a.client.pesel = :clientPesel
                            """,
                            AppointmentEnt.class
                    )
                    .setParameter("clientPesel", clientPesel)
                    .getResultStream()
                    .map(mapper::toDomain)
                    .toList();
        }
    }

    @Override
    public List<Appointment> findByDate(LocalDateTime date) {
        if (date == null) {
            throw new IllegalArgumentException(
                    "Data nie może być nullem");
        }

        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery(
                            """
                            SELECT a FROM AppointmentEnt a
                            WHERE a.date = :date
                            """,
                            AppointmentEnt.class)
                    .setParameter("date", date)
                    .getResultStream()
                    .map(mapper::toDomain)
                    .toList();
        }
    }

}
