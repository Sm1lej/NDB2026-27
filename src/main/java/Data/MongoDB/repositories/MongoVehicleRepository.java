package Data.MongoDB.repositories;

import Data.MongoDB.documents.ClientDocument;
import Data.MongoDB.documents.VehicleDocument;
import Data.MongoDB.mappers.ClientMapper;
import Data.MongoDB.mappers.VehicleMapper;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOptions;
import model.Client;
import model.Vehicle;
import repositories.VehicleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class MongoVehicleRepository implements VehicleRepository {

    private final MongoCollection<VehicleDocument> vehicleCollection;
    private final MongoCollection<ClientDocument> clientCollection;
    private final VehicleMapper vehicleMapper;
    private final ClientMapper clientMapper;

    public MongoVehicleRepository(MongoDatabase database,
                                  VehicleMapper vehicleMapper,
                                  ClientMapper clientMapper) {
        this.vehicleCollection = database.getCollection(
                "vehicles",
                VehicleDocument.class
        ).withWriteConcern(WriteConcern.MAJORITY);

        this.clientCollection = database.getCollection(
                "clients",
                ClientDocument.class
        ).withWriteConcern(WriteConcern.MAJORITY);

        this.vehicleMapper = vehicleMapper;
        this.clientMapper = clientMapper;
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Pojazd nie może być nullem");
        }
        if (vehicle.getOwner() != null) {
            ClientDocument clientDoc = clientCollection.find(
                    eq("_id", vehicle.getOwner().getPesel())
            ).first();

            if (clientDoc == null) {
                throw new IllegalArgumentException(
                        "Nie można zapisać pojazdu. Właściciel o numerze PESEL "
                                + vehicle.getOwner().getPesel() + " nie istnieje."
                );
            }
        }
        VehicleDocument document = vehicleMapper.toDocument(vehicle);

        vehicleCollection.replaceOne(
                eq("_id", document.getVinNumber()),
                document,
                new ReplaceOptions().upsert(true)
        );

        return vehicle;
    }

    @Override
    public Optional<Vehicle> findById(String id) {
        VehicleDocument doc = vehicleCollection.find(eq("_id", id)).first();
        if (doc == null) {
            return Optional.empty();
        }

        Client owner = null;
        if (doc.getOwnerPesel() != null) {
            ClientDocument clientDoc = clientCollection.find(eq("_id", doc.getOwnerPesel())).first();
            if (clientDoc != null) {
                owner = clientMapper.toDomain(clientDoc);
            }
        }

        return Optional.of(vehicleMapper.toDomain(doc, owner));
    }

    @Override
    public List<Vehicle> findAll() {
        List<VehicleDocument> documents = vehicleCollection.find().into(new ArrayList<>());

        List<Vehicle> vehicles = new ArrayList<>();
        for (VehicleDocument doc : documents) {
            findById(doc.getVinNumber()).ifPresent(vehicles::add);
        }
        return vehicles;
    }

    @Override
    public List<Vehicle> findByClientPesel(String clientPesel) {
        List<VehicleDocument> documents = vehicleCollection.find(eq("ownerPesel", clientPesel))
                .into(new ArrayList<>());

        List<Vehicle> vehicles = new ArrayList<>();
        for (VehicleDocument doc : documents) {
            findById(doc.getVinNumber()).ifPresent(vehicles::add);
        }
        return vehicles;
    }

    @Override
    public List<Vehicle> findByVinNumber(String vinNumber) {
        List<VehicleDocument> documents = vehicleCollection.find(eq("_id", vinNumber))
                .into(new ArrayList<>());

        List<Vehicle> vehicles = new ArrayList<>();
        for (VehicleDocument doc : documents) {
            findById(doc.getVinNumber()).ifPresent(vehicles::add);
        }
        return vehicles;
    }

    @Override
    public List<Vehicle> findByRegistrationNumber(String registrationNumber) {
        List<VehicleDocument> documents = vehicleCollection.find(eq("registrationNumber", registrationNumber))
                .into(new ArrayList<>());

        List<Vehicle> vehicles = new ArrayList<>();
        for (VehicleDocument doc : documents) {
            findById(doc.getVinNumber()).ifPresent(vehicles::add);
        }
        return vehicles;
    }

    @Override
    public List<Vehicle> findByBrandAndModel(String brand, String model) {
        List<VehicleDocument> documents = vehicleCollection.find(and(
                        eq("brand", brand),
                        eq("model", model)
                ))
                .into(new ArrayList<>());

        List<Vehicle> vehicles = new ArrayList<>();
        for (VehicleDocument doc : documents) {
            findById(doc.getVinNumber()).ifPresent(vehicles::add);
        }
        return vehicles;
    }

    @Override
    public List<Vehicle> findByEngineDisplacement(double engineDisplacement) {
        List<VehicleDocument> documents = vehicleCollection.find(eq("engineDisplacement", engineDisplacement))
                .into(new ArrayList<>());

        List<Vehicle> vehicles = new ArrayList<>();
        for (VehicleDocument doc : documents) {
            findById(doc.getVinNumber()).ifPresent(vehicles::add);
        }
        return vehicles;
    }

    @Override
    public void deleteById(String id) {
        long deletedCount = vehicleCollection.deleteOne(eq("_id", id))
                .getDeletedCount();

        if (deletedCount == 0) {
            throw new IllegalArgumentException(
                    "Nie znaleziono pojazdu o numerze VIN: " + id
            );
        }
    }
}