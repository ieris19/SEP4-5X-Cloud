package dk.via.sep4.cloud.data.repository;

import com.ieris19.lib.files.config.FileProperties;
import com.mongodb.client.*;
import dk.via.sep4.cloud.data.DataRepository;
import dk.via.sep4.cloud.data.dto.SensorLimits;
import dk.via.sep4.cloud.data.dto.SensorReading;
import dk.via.sep4.cloud.data.dto.SensorState;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * This class is used to implement the DataRepository interface.
 * The methods are implemented to return data in the form of Java class objects, so that the system can process it more easily.
 */
public class MongoRepository implements DataRepository {
    private MongoClient client;
    private MongoDatabase db;
    private MongoCollection<Document> readings;
    private MongoCollection<Document> extras;
    private final Logger logger = LoggerFactory.getLogger(MongoRepository.class);

    public MongoRepository() {
        try (FileProperties secrets = FileProperties.getInstance("secrets")) {
            String connectionURL = secrets.getProperty("mongodb.url");
            logger.debug(connectionURL);
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
        initUniqueObjects();
    }

    void initUniqueObjects() {
        Document filter = new Document("type", "limit values");
        FindIterable<Document> dbResult = extras.find(filter);
        if (dbResult.first() == null) {
            SensorLimits limits = new SensorLimits(10, 35, 20, 80, 3000);
            insertLimits(limits);
        }
        filter = new Document("type", "state");
        dbResult = extras.find(filter);
        if (dbResult.first() == null) {
            SensorState state = new SensorState(false);
            extras.insertOne(state.toBSON());
        }
    }

    void clearEntireDatabase() {
        db.drop();
    }

    @Override
    public void insertReading(SensorReading reading) {
        readings.insertOne(reading.toBSON());
    }

    @Override
    public SensorReading[] getReadings(String date) {
        Timestamp start = Timestamp.valueOf(date + " 00:00:00");
        Timestamp end = Timestamp.valueOf(date + " 23:59:59");

        FindIterable<Document> allReadings = readings.find(new Document("time", new Document("$gte", start).append("$lte", end)));

//        FindIterable<Document> allReadings = readings.find();

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
        extras.insertOne(limits.toBSON());
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
    public void addComment(String id, String comment) {
        Document filter = new Document("_id", id);
        Document update = new Document("$set", new Document("comment", comment));
        readings.updateOne(filter, update);
    }

    @Override
    public void updateLimits(SensorLimits limits) {
        Document filter = new Document("type", "limit values");

        extras.findOneAndDelete(filter);
        extras.insertOne(limits.toBSON());
    }

    @Override
    public void insertState(SensorState state) {
        extras.insertOne(state.toBSON());
    }

    @Override
    public SensorState getState() {
        Document filter = new Document("type", "state");
        FindIterable<Document> dbResult = extras.find(filter);
        try (MongoCursor<Document> cursor = dbResult.iterator()) {
            return new SensorState(cursor.next().toJson());
        }
    }

    @Override
    public void updateState(SensorState state) {
        Document filter = new Document("type", "state");

        extras.findOneAndDelete(filter);
        extras.insertOne(state.toBSON());
    }

    @Override
    public void close() {
        client.close();
        logger.info("Closing connection to MongoDB");
    }
}
