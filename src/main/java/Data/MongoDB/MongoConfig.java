package Data.MongoDB;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MongoConfig implements AutoCloseable {

    private final MongoClient mongoClient;
    private final MongoDatabase database;

    public MongoConfig(String connectionString) {

        PojoCodecProvider pojoCodecProvider =
                PojoCodecProvider.builder()
                        .register("Data.MongoDB.documents")
                        .conventions(Conventions.DEFAULT_CONVENTIONS)
                        .build();

        CodecRegistry codecRegistry = fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(pojoCodecProvider)
        );

        mongoClient = MongoClients.create(connectionString);

        database = mongoClient
                .getDatabase("workshop")
                .withCodecRegistry(codecRegistry);
    }

    public MongoDatabase getDatabase() {
        return database;
    }

    @Override
    public void close() {
        mongoClient.close();
    }
}