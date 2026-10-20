package Data.adapters;

import Data.entity.ServiceEnt;
import Data.entity.RepairServiceEnt;
import Data.entity.DiagnosticServiceEnt;
import Data.mappers.ServiceMapper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

import model.Service;
import model.RepairService;
import model.DiagnosticService;

import repositories.ServiceRepository;

import java.util.List;
import java.util.Optional;

public class ServiceRepositoryJpa implements ServiceRepository {

    private final EntityManagerFactory emf;
    private final ServiceMapper mapper;

    public ServiceRepositoryJpa(EntityManagerFactory emf, ServiceMapper mapper) {
        this.emf = emf;
        this.mapper = mapper;
    }

    @Override
    public Service save(Service service) {
        if (service == null) {
            throw new IllegalArgumentException("Usługa nie może być nullem");
        }

        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction transaction = em.getTransaction();

            try {
                transaction.begin();

                ServiceEnt serviceEnt;

                if (service.getId() == null) {
                    serviceEnt = mapper.toEntity(service);
                    em.persist(serviceEnt);

                } else {
                    serviceEnt = em.find(ServiceEnt.class,
                            service.getId());

                    if (serviceEnt == null) {
                        throw new IllegalArgumentException("Nie znaleziono usługi o ID: " + service.getId());
                    }
                    updateServiceEnt(serviceEnt, service);
                }

                em.flush();

                Service result = mapper.toDomain(serviceEnt);

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

    private void updateServiceEnt(ServiceEnt serviceEnt, Service service) {
        if (serviceEnt instanceof RepairServiceEnt repairServiceEnt
                && service instanceof RepairService repairService) {

            repairServiceEnt.setChangeTires(repairService.isChangeTires());
            repairServiceEnt.setFillFluids(repairService.isFillFluids());
            repairServiceEnt.setFixBrakeSystem(repairService.isFixBrakeSystem());

        } else if (serviceEnt instanceof DiagnosticServiceEnt diagnosticServiceEnt
                && service instanceof DiagnosticService diagnosticService) {

            diagnosticServiceEnt.setCheckTires(diagnosticService.isCheckTires());
            diagnosticServiceEnt.setCheckFluids(diagnosticService.isCheckFluids());
            diagnosticServiceEnt.setCheckBrake(diagnosticService.isCheckBrake());

        } else {
            throw new IllegalArgumentException("Nie można zmienić rodzaju istniejącej usługi");
        }

        serviceEnt.setDescription(service.getDescription());
        serviceEnt.setBasePrice(service.getBasePrice());

        serviceEnt.setEstimatedDurationMinutes(service.getTotalEstimatedTime().toMinutes());
    }

    @Override
    public Optional<Service> findById(Integer id) {

        try (EntityManager em = emf.createEntityManager()) {
            ServiceEnt serviceEnt = em.find(ServiceEnt.class, id);

            return Optional.ofNullable(serviceEnt)
                    .map(mapper::toDomain);
        }
    }

    @Override
    public List<Service> findAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery(
                            "SELECT s FROM ServiceEnt s",
                            ServiceEnt.class)
                    .getResultStream()
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

                ServiceEnt serviceEnt = em.find(ServiceEnt.class, id);

                if (serviceEnt == null) {
                    throw new IllegalArgumentException(
                            "Nie znaleziono usługi o ID: " + id);
                }

                em.remove(serviceEnt);

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
    public List<Service> findByBasePrice(double basePrice) {
        if (basePrice < 0) {
            throw new IllegalArgumentException(
                    "Cena bazowa nie może być ujemna");
        }

        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery(
                            """
                            SELECT s FROM ServiceEnt s
                            WHERE s.basePrice = :basePrice
                            """,
                            ServiceEnt.class)
                    .setParameter("basePrice", basePrice)
                    .getResultStream()
                    .map(mapper::toDomain)
                    .toList();
        }
    }
}