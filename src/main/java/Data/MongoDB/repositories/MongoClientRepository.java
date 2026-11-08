package Data.MongoDB.repositories;

import Data.MongoDB.documents.ClientDocument;
import Data.MongoDB.mappers.ClientMapper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import model.Client;
import repositories.ClientRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MongoClientRepository implements ClientRepository {
    private final MongoCollection<ClientDocument> collection;
    private final ClientMapper clientMapper;

    public MongoClientRepository(MongoDatabase database,
                                 ClientMapper clientMapper) {
        collection = database.getCollection(
                "clients",
                ClientDocument.class
        );

        this.clientMapper = clientMapper;
    }

    @Override
    public Client save(Client client) {
        collection.replaceOne(
                Filters.eq("_id", client.getPesel()),
                clientMapper.toDocument(client),
                new ReplaceOptions().upsert(true));

        return client;
    }

    @Override
    public Optional<Client> findById(String pesel) {
        ClientDocument clientDoc =
                collection.find(Filters.eq("_id", pesel)).first();
        return Optional.ofNullable(clientDoc)
                .map(clientMapper::toDomain);
    }

    @Override
    public List<Client> findAll() {
        List<ClientDocument> clientDocuments =
                collection.find().into(new ArrayList<>());

        return clientDocuments.stream().
                map(clientMapper::toDomain).
                toList();
    }

    @Override
    public void deleteById(String pesel) {
        UpdateResult result = collection.updateOne(Filters.eq("_id", pesel),
                Updates.set("isArchive", true));

        if (result.getMatchedCount() == 0) {
            throw new IllegalArgumentException(
                    "Nie znaleziono klienta o peselu: " + pesel);
        }
    }


    @Override
    public List<Client> findByNameAndLastName(String name, String lastName) {
        List<ClientDocument> clientDoc =
                collection.find(Filters.and(Filters.eq("name", name),
                        Filters.eq("lastName", lastName)))
                        .into(new ArrayList<>());

        return clientDoc.stream()
                .map(clientMapper::toDomain)
                .toList();
    }

    @Override
    public List<Client> findByPhoneNumber(String phoneNumber) {
        List<ClientDocument> clientDoc =
                collection.find(Filters.eq("phoneNumber", phoneNumber))
                        .into(new ArrayList<>());

        return clientDoc.stream()
                .map(clientMapper::toDomain)
                .toList();
    }

    @Override
    public List<Client> findByArchive() {
        List<ClientDocument> clientDoc =
                collection.find(Filters.eq("isArchive", true))
                        .into(new ArrayList<>());

        return clientDoc
                .stream()
                .map(clientMapper::toDomain)
                .toList();
    }
}
