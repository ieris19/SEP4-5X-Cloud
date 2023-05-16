package dk.via.sep4.cloud.data.repository;

import com.ieris19.lib.files.config.FileProperties;
import com.mongodb.client.*;
import dk.via.sep4.cloud.data.SensorLimits;
import dk.via.sep4.cloud.data.SensorReading;
import dk.via.sep4.cloud.data.UserCredentials;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;

@Repository
public class MongoRepository implements DataRepository {
    private MongoClient client;
    private MongoDatabase db;
    private MongoCollection<Document> readings;
    private MongoCollection<Document> extras;
    private final Logger logger = LoggerFactory.getLogger(MongoRepository.class);

    public MongoRepository() {
        try (FileProperties secrets = FileProperties.getInstance("secrets")) {
            String connectionURL = secrets.getProperty("mongodb.url");
            init(connectionURL, "SEP4");
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("At least one of the accessed properties in secrets.properties doesn't exist", e);
        } catch (IOException e) {
            throw new IllegalStateException("Error reading secrets.properties", e);
        }

    }

    void init(String connectionString, String databaseName) {
        client = MongoClients.create(connectionString);
        db = client.getDatabase(databaseName);
        this.readings = db.getCollection("READINGS");
        this.extras = db.getCollection("EXTRAS");
        logger.info("Successfully established a connection to MongoDB");
    }

    void clearEntireDatabase() {
       db.drop();
    }

    @Override
    public void insertReading(SensorReading reading) {
        Document readingDocument = new Document("pir", reading.isPir())
                .append("temperature", reading.getTemperature())
                .append("humidity", reading.getHumidity())
                .append("co2", reading.getCo2())
                .append("sound", reading.getSound())
                .append("light", reading.getLight())
                .append("code", reading.getCode())
                .append("time", reading.getTimeReceived());

        readings.insertOne(readingDocument);
    }

    @Override
    public SensorReading[] getReadings() {
        FindIterable<Document> allReadings = readings.find();

        ArrayList<SensorReading> list = new ArrayList<SensorReading>();
        try (MongoCursor<Document> cursor = allReadings.iterator()) {
            while (cursor.hasNext()) {
                list.add(new SensorReading(cursor.next().toJson()));
            }
        }
        return list.toArray(new SensorReading[0]);
    }

    @Override
    public void insertLimits(SensorLimits limits) {
        Document limitsDocument = new Document("type", "limit values")
                .append("minTemperature", limits.getMinTemperature())
                .append("maxTemperature", limits.getMaxTemperature())
                .append("minHumidity", limits.getMinHumidity())
                .append("maxHumidity", limits.getMaxHumidity())
                .append("maxCo2", limits.getMaxCo2());
        extras.insertOne(limitsDocument);
    }

    @Override
    public SensorLimits getLimits() {
        Document filter = new Document("type", "limit values");
        FindIterable<Document> dbResult = extras.find(filter);
        try (MongoCursor<Document> cursor = dbResult.iterator()) {
            return new SensorLimits(cursor.next().toJson());
        }
    }

    @Override
    public void updateLimits(SensorLimits limits) {
        Document filter = new Document("type", "limit values");

        Document limitsDocument = new Document("type", "limit values")
                .append("minTemperature", limits.getMinTemperature())
                .append("maxTemperature", limits.getMaxTemperature())
                .append("minHumidity", limits.getMinHumidity())
                .append("maxHumidity", limits.getMaxHumidity())
                .append("maxCo2", limits.getMaxCo2());
        extras.findOneAndDelete(filter);
        extras.insertOne(limitsDocument);
    }

    @Override
    public void insertCredentials(UserCredentials credentials) {
        Document credentialsDocument = new Document("type", "user credentials")
                .append("username", credentials.getUsername())
                .append("password", credentials.getPassword());
        extras.insertOne(credentialsDocument);
    }

    @Override
    public UserCredentials getCredentials() {
        Document filter = new Document("type", "user credentials");

        FindIterable<Document> credentialsResult = extras.find(filter);

        try (MongoCursor<Document> cursor = credentialsResult.iterator()) {
            return new UserCredentials(cursor.next().toJson());
        }
    }

    @Override
    public void close() {
        logger.info("Closing connection to MongoDB");
    }
}
