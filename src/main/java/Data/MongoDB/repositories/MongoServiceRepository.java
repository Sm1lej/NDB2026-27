package Data.MongoDB.repositories;

import Data.MongoDB.documents.ServiceDocument;
import Data.MongoDB.mappers.ServiceMapper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOptions;
import model.Service;
import repositories.ServiceRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;

public class MongoServiceRepository implements ServiceRepository {

    private final MongoCollection<ServiceDocument> collection;
    private final ServiceMapper serviceMapper;

    public MongoServiceRepository(MongoDatabase database,
                                  ServiceMapper serviceMapper) {
        this.collection = database.getCollection(
                "services",
                ServiceDocument.class
        );
        this.serviceMapper = serviceMapper;
    }

    @Override
    public Service save(Service service) {
        if (service == null) {
            throw new IllegalArgumentException("Usługa nie może być nullem");
        }

        ServiceDocument document = serviceMapper.toDocument(service);

        collection.replaceOne(
                eq("_id", document.getId()),
                document,
                new ReplaceOptions().upsert(true)
        );

        return service;
    }

    @Override
    public Optional<Service> findById(Integer id) {
        ServiceDocument doc = collection.find(eq("_id", id)).first();
        if (doc == null) {
            return Optional.empty();
        }

        return Optional.of(serviceMapper.toDomain(doc));
    }

    @Override
    public List<Service> findAll() {
        List<ServiceDocument> documents = collection.find().into(new ArrayList<>());

        return documents.stream()
                .map(serviceMapper::toDomain)
                .toList();
    }

    @Override
    public List<Service> findByBasePrice(double basePrice) {
        List<ServiceDocument> documents = collection.find(eq("basePrice", basePrice))
                .into(new ArrayList<>());

        return documents.stream()
                .map(serviceMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Integer id) {
        long deletedCount = collection.deleteOne(eq("_id", id))
                .getDeletedCount();

        if (deletedCount == 0) {
            throw new IllegalArgumentException(
                    "Nie znaleziono usługi o ID: " + id
            );
        }
    }
}