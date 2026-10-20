package Data.adapters;

import Data.entity.ClientEnt;
import Data.entity.CarEnt;
import Data.entity.MotorbikeEnt;
import Data.entity.VehicleEnt;
import Data.mappers.VehicleMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import model.Car;
import model.Motorbike;
import model.Vehicle;
import repositories.VehicleRepository;


import java.util.List;
import java.util.Optional;

public class VehicleRepositoryJpa implements VehicleRepository {

    private final EntityManagerFactory emf;
    private final VehicleMapper mapper;

    public VehicleRepositoryJpa(EntityManagerFactory emf, VehicleMapper mapper) {
        this.emf = emf;
        this.mapper = mapper;
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Pojazd nie może być nullem");
        }

        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction transaction = em.getTransaction();

            try {
                transaction.begin();

                VehicleEnt vehicleEnt = em.find(
                        VehicleEnt.class,
                        vehicle.getVinNumber()
                );

                ClientEnt owner = em.find(
                        ClientEnt.class,
                        vehicle.getOwner().getPesel()
                );

                if (owner == null) {
                    throw new IllegalArgumentException(
                            "Nie znaleziono klienta o PESELu: "
                                    + vehicle.getOwner().getPesel()
                    );
                }

                if (vehicleEnt == null) {
                    vehicleEnt = mapper.toEntity(vehicle, owner);
                    em.persist(vehicleEnt);
                } else {
                    updateVehicleEnt(vehicleEnt, vehicle, owner);
                }

                em.flush();
                Vehicle result = mapper.toDomain(vehicleEnt);

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

    private void updateVehicleEnt(
            VehicleEnt vehicleEnt,
            Vehicle vehicle,
            ClientEnt owner
    ) {
        if (vehicleEnt instanceof CarEnt carEnt && vehicle instanceof Car car) {
            carEnt.setBodyType(car.getBodyType());
            carEnt.setFuelType(car.getFuelType());
            carEnt.setDoorsCount(car.getDoorsCount());
        } else if (vehicleEnt instanceof MotorbikeEnt motorbikeEnt
                && vehicle instanceof Motorbike motorbike) {
            motorbikeEnt.setBrakeSystem(motorbike.getBrakeSystem());
        } else {
            throw new IllegalArgumentException(
                    "Nie można zmienić typu istniejącego pojazdu"
            );
        }

        vehicleEnt.setOwner(owner);
        vehicleEnt.setRegistrationNumber(vehicle.getRegistrationNumber());
        vehicleEnt.setYearOfProduction(vehicle.getYearOfProduction());
        vehicleEnt.setBrand(vehicle.getBrand());
        vehicleEnt.setModel(vehicle.getModel());
        vehicleEnt.setColor(vehicle.getColor());
        vehicleEnt.setEngineDisplacement(vehicle.getEngineDisplacement());
        vehicleEnt.setEnginePower(vehicle.getEnginePower());
        vehicleEnt.setWeight(vehicle.getWeight());
    }

    @Override
    public Optional<Vehicle> findById(String vinNumber) {
        try (EntityManager em = emf.createEntityManager()) {
            return Optional.ofNullable(em.find(VehicleEnt.class, vinNumber))
                    .map(mapper::toDomain);
        }
    }

    @Override
    public List<Vehicle> findAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery(
                            "SELECT v FROM VehicleEnt v",
                            VehicleEnt.class
                    )
                    .getResultStream()
                    .map(mapper::toDomain)
                    .toList();
        }
    }

    @Override
    public void deleteById(String vinNumber) {
        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction transaction = em.getTransaction();

            try {
                transaction.begin();

                VehicleEnt vehicleEnt = em.find(VehicleEnt.class, vinNumber);
                if (vehicleEnt == null) {
                    throw new IllegalArgumentException(
                            "Nie znaleziono pojazdu o VIN: " + vinNumber
                    );
                }

                em.remove(vehicleEnt);
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
    public List<Vehicle> findByClientPesel(String clientPesel) {
        return findByQuery(
                "SELECT v FROM VehicleEnt v WHERE v.owner.pesel = :value",
                clientPesel
        );
    }

    @Override
    public List<Vehicle> findByVinNumber(String vinNumber) {
        return findByQuery(
                "SELECT v FROM VehicleEnt v WHERE v.vinNumber = :value",
                vinNumber
        );
    }

    @Override
    public List<Vehicle> findByRegistrationNumber(String registrationNumber) {
        return findByQuery(
                "SELECT v FROM VehicleEnt v WHERE v.registrationNumber = :value",
                registrationNumber
        );
    }

    @Override
    public List<Vehicle> findByBrandAndModel(String brand, String model) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery(
                            """
                            SELECT v FROM VehicleEnt v
                            WHERE v.brand = :brand AND v.model = :model
                            """,
                            VehicleEnt.class
                    )
                    .setParameter("brand", brand)
                    .setParameter("model", model)
                    .getResultStream()
                    .map(mapper::toDomain)
                    .toList();
        }
    }

    @Override
    public List<Vehicle> findByEngineDisplacement(double engineDisplacement) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery(
                            "SELECT v FROM VehicleEnt v "
                                    + "WHERE v.engineDisplacement = :value",
                            VehicleEnt.class
                    )
                    .setParameter("value", (double) engineDisplacement)
                    .getResultStream()
                    .map(mapper::toDomain)
                    .toList();
        }
    }

    private List<Vehicle> findByQuery(String jpql, String value) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery(jpql, VehicleEnt.class)
                    .setParameter("value", value)
                    .getResultStream()
                    .map(mapper::toDomain)
                    .toList();
        }
    }
}