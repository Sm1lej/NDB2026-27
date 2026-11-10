package Data.MongoDB.repositories;

import Data.MongoDB.documents.ServiceDocument;
import Data.MongoDB.mappers.ServiceMapper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.*;
import org.bson.Document;
import model.Service;
import repositories.ServiceRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;

public class MongoServiceRepository implements ServiceRepository {

    private final MongoCollection<ServiceDocument> collection;
    private final MongoCollection<Document> counters;
    private final ServiceMapper serviceMapper;

    public MongoServiceRepository(MongoDatabase database,
                                  ServiceMapper serviceMapper) {
        this.collection = database.getCollection(
                "services",
                ServiceDocument.class
        );

        this.counters = database.getCollection(
                "counters",
                Document.class
        );

        this.serviceMapper = serviceMapper;
    }

    @Override
    public Service save(Service service) {
        if (service == null) {
            throw new IllegalArgumentException("Usługa nie może być nullem");
        }

        if (service.getId() != null) {
            collection.replaceOne(
                    eq("_id", service.getId()),
                    serviceMapper.toDocument(service),
                    new ReplaceOptions().upsert(true));
        }
        else {
            Integer newServiceId = getNewServiceId();
            service.setId(newServiceId);

            collection.insertOne(serviceMapper.toDocument(service));
        }

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

    private Integer getNewServiceId() {
        Document counter = counters.findOneAndUpdate(
                Filters.eq("_id", "services"),
                Updates.inc("seq", 1),
                new FindOneAndUpdateOptions().
                        upsert(true).
                        returnDocument(ReturnDocument.AFTER)
        );

        return counter.getInteger("seq");
    }
}