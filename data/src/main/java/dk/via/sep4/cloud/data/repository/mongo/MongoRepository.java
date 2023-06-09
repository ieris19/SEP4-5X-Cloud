package dk.via.sep4.cloud.data.repository.mongo;

import com.ieris19.lib.files.config.FileProperties;
import com.mongodb.client.*;
import dk.via.sep4.cloud.data.dto.ControlState;
import dk.via.sep4.cloud.data.dto.SensorLimits;
import dk.via.sep4.cloud.data.dto.SensorReading;
import dk.via.sep4.cloud.data.repository.DataOperationResult;
import dk.via.sep4.cloud.data.repository.DataRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * This class is used to implement the DataRepository interface.
 * The methods are implemented to return data in the form of Java class objects, so that the system can process it more easily.
 */
@Slf4j
public class MongoRepository implements DataRepository {
    private MongoClient client;
    private MongoDatabase db;
    private MongoCollection<Document> readings;
    private MongoCollection<Document> extras;

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
        log.info("Successfully established a connection to MongoDB");
        db = client.getDatabase(databaseName);
        this.readings = db.getCollection("READINGS");
        this.extras = db.getCollection("EXTRAS");
        initUniqueObjects();
        log.debug("Successfully initialized database");
    }

    void initUniqueObjects() {
        Document filter = new Document("type", "limit values");
        FindIterable<Document> dbResult = extras.find(filter);
        if (dbResult.first() == null) {
            log.warn("No limits found in database, inserting default values");
            SensorLimits limits = new SensorLimits(10, 35, 20, 80, 3000);
            assert extras.insertOne(limits.toBSON()).wasAcknowledged() : "Unacknowledged insert of default limits";
        }
        filter = new Document("type", "state");
        dbResult = extras.find(filter);
        if (dbResult.first() == null) {
            log.warn("No control state found in database, inserting default values");
            ControlState state = new ControlState(false);
            assert extras.insertOne(state.toBSON()).wasAcknowledged() : "Unacknowledged insert of default control state";
        }
    }

    @Override
    public SensorReading[] getReadings(String date) {
        Timestamp start = Timestamp.valueOf(date + " 00:00:00");
        Timestamp end = Timestamp.valueOf(date + " 23:59:59");
        FindIterable<Document> allReadings = readings.find(new Document("time", new Document("$gte", start).append("$lte", end)));

        ArrayList<SensorReading> list = new ArrayList<>();
        try (MongoCursor<Document> cursor = allReadings.iterator()) {
            while (cursor.hasNext()) {
                list.add(SensorReading.fromJson(cursor.next().toJson()));
            }
        }
        return list.toArray(new SensorReading[0]);
    }

    @Override
    public SensorLimits getLimits() {
        Document filter = new Document("type", "limit values");
        FindIterable<Document> dbResult = extras.find(filter);
        try (MongoCursor<Document> cursor = dbResult.iterator()) {
            return SensorLimits.fromJson(cursor.next().toJson());
        }
    }

    @Override
    public ControlState getState() {
        Document filter = new Document("type", "state");
        FindIterable<Document> dbResult = extras.find(filter);
        try (MongoCursor<Document> cursor = dbResult.iterator()) {
            return ControlState.fromJson(cursor.next().toJson());
        }
    }

    @Override
    public DataOperationResult insertReading(SensorReading reading) {
        return MongoOperationResult.from(readings.insertOne(reading.toBSON()));
    }

    @Override
    public DataOperationResult addComment(String id, String comment) {
        Document filter = new Document("_id", new Document("$eq", new ObjectId(id)));
        Document update = new Document("$set", new Document("comment", comment));
        log.debug(update.toJson());
        return MongoOperationResult.from(readings.updateOne(filter, update));
    }

    @Override
    public DataOperationResult updateLimits(SensorLimits limits) {
        Document filter = new Document("type", new Document("$eq", "limit values"));

        Document update = new Document("$set", limits.toBSON());
        log.debug(update.toJson());
        return MongoOperationResult.from(extras.updateOne(filter, update));
    }

    @Override
    public DataOperationResult updateState(ControlState state) {
        Document filter = new Document("type", new Document("$eq", "state"));

        Document update = new Document("$set", state.toBSON());
        log.debug(update.toJson());
        return MongoOperationResult.from(extras.updateOne(filter, update));
    }

    @Override
    public void close() {
        client.close();
        log.info("Gracefully closing connection to MongoDB");
    }
}
