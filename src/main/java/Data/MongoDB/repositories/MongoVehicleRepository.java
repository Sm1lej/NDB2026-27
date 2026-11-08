package Data.MongoDB.repositories;

import Data.MongoDB.documents.VehicleDocument;
import Data.MongoDB.mappers.VehicleMapper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOptions;
import model.Client;
import model.Vehicle;
import repositories.ClientRepository;
import repositories.VehicleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class MongoVehicleRepository implements VehicleRepository {

    private final MongoCollection<VehicleDocument> collection;
    private final VehicleMapper vehicleMapper;
    private final ClientRepository clientRepository;

    public MongoVehicleRepository(MongoDatabase database,
                                  VehicleMapper vehicleMapper,
                                  ClientRepository clientRepository) {
        this.collection = database.getCollection(
                "vehicles",
                VehicleDocument.class
        );
        this.vehicleMapper = vehicleMapper;
        this.clientRepository = clientRepository;
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Pojazd nie może być nullem");
        }

        VehicleDocument document = vehicleMapper.toDocument(vehicle);

        collection.replaceOne(
                eq("_id", document.getVinNumber()),
                document,
                new ReplaceOptions().upsert(true)
        );

        return vehicle;
    }

    @Override
    public Optional<Vehicle> findById(String id) {
        VehicleDocument doc = collection.find(eq("_id", id)).first();
        if (doc == null) {
            return Optional.empty();
        }

        Client owner = doc.getOwnerPesel() != null
                ? clientRepository.findById(doc.getOwnerPesel()).orElse(null)
                : null;

        return Optional.of(vehicleMapper.toDomain(doc, owner));
    }

    @Override
    public List<Vehicle> findAll() {
        List<VehicleDocument> documents = collection.find().into(new ArrayList<>());

        return documents.stream()
                .map(doc -> {
                    Client owner = doc.getOwnerPesel() != null
                            ? clientRepository.findById(doc.getOwnerPesel()).orElse(null)
                            : null;
                    return vehicleMapper.toDomain(doc, owner);
                })
                .toList();
    }

    @Override
    public List<Vehicle> findByClientPesel(String clientPesel) {
        Client owner = clientRepository.findById(clientPesel).orElse(null);

        List<VehicleDocument> documents = collection.find(eq("ownerPesel", clientPesel))
                .into(new ArrayList<>());

        return documents.stream()
                .map(doc -> vehicleMapper.toDomain(doc, owner))
                .toList();
    }

    @Override
    public List<Vehicle> findByVinNumber(String vinNumber) {
        List<VehicleDocument> documents = collection.find(eq("_id", vinNumber))
                .into(new ArrayList<>());

        return documents.stream()
                .map(doc -> {
                    Client owner = doc.getOwnerPesel() != null
                            ? clientRepository.findById(doc.getOwnerPesel()).orElse(null)
                            : null;
                    return vehicleMapper.toDomain(doc, owner);
                })
                .toList();
    }

    @Override
    public List<Vehicle> findByRegistrationNumber(String registrationNumber) {
        List<VehicleDocument> documents = collection.find(eq("registrationNumber", registrationNumber))
                .into(new ArrayList<>());

        return documents.stream()
                .map(doc -> {
                    Client owner = doc.getOwnerPesel() != null
                            ? clientRepository.findById(doc.getOwnerPesel()).orElse(null)
                            : null;
                    return vehicleMapper.toDomain(doc, owner);
                })
                .toList();
    }

    @Override
    public List<Vehicle> findByBrandAndModel(String brand, String model) {
        List<VehicleDocument> documents = collection.find(and(
                        eq("brand", brand),
                        eq("model", model)
                ))
                .into(new ArrayList<>());

        return documents.stream()
                .map(doc -> {
                    Client owner = doc.getOwnerPesel() != null
                            ? clientRepository.findById(doc.getOwnerPesel()).orElse(null)
                            : null;
                    return vehicleMapper.toDomain(doc, owner);
                })
                .toList();
    }

    @Override
    public List<Vehicle> findByEngineDisplacement(double engineDisplacement) {
        List<VehicleDocument> documents = collection.find(eq("engineDisplacement", engineDisplacement))
                .into(new ArrayList<>());

        return documents.stream()
                .map(doc -> {
                    Client owner = doc.getOwnerPesel() != null
                            ? clientRepository.findById(doc.getOwnerPesel()).orElse(null)
                            : null;
                    return vehicleMapper.toDomain(doc, owner);
                })
                .toList();
    }

    @Override
    public void deleteById(String id) {
        long deletedCount = collection.deleteOne(eq("_id", id))
                .getDeletedCount();

        if (deletedCount == 0) {
            throw new IllegalArgumentException(
                    "Nie znaleziono pojazdu o numerze VIN: " + id
            );
        }
    }
}