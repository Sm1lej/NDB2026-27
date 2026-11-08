package Data.MongoDB.repositories;
import Data.MongoDB.documents.VehicleDocument;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class MongoVehicleRepository {

    private final MongoCollection<VehicleDocument> vehicles;

    public MongoVehicleRepository(MongoDatabase database) {
        this.vehicles = database.getCollection(
                "vehicles",
                VehicleDocument.class
        );
    }

    public VehicleDocument save(VehicleDocument vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Dokument pojazdu nie może być nullem");
        }

        vehicles.replaceOne(
                eq("_id", vehicle.getVinNumber()),
                vehicle,
                new ReplaceOptions().upsert(true)
        );

        return vehicle;
    }

    public Optional<VehicleDocument> findByVinNumber(String vinNumber) {
        return Optional.ofNullable(
                vehicles.find(eq("_id", vinNumber)).first()
        );
    }

    public List<VehicleDocument> findAll() {
        return vehicles.find().into(new ArrayList<>());
    }

    public List<VehicleDocument> findByOwnerPesel(String ownerPesel) {
        return vehicles.find(eq("ownerPesel", ownerPesel))
                .into(new ArrayList<>());
    }

    public List<VehicleDocument> findByRegistrationNumber(String registrationNumber) {
        return vehicles.find(eq("registrationNumber", registrationNumber))
                .into(new ArrayList<>());
    }

    public List<VehicleDocument> findByBrandAndModel(String brand, String model) {
        return vehicles.find(and(
                        eq("brand", brand),
                        eq("model", model)
                ))
                .into(new ArrayList<>());
    }

    public List<VehicleDocument> findByEngineDisplacement(double displacement) {
        return vehicles.find(eq("engineDisplacement", displacement))
                .into(new ArrayList<>());
    }

    public void deleteByVinNumber(String vinNumber) {
        long deletedCount = vehicles.deleteOne(eq("_id", vinNumber))
                .getDeletedCount();

        if (deletedCount == 0) {
            throw new IllegalArgumentException(
                    "Nie znaleziono pojazdu o numerze VIN: " + vinNumber
            );
        }
    }
}