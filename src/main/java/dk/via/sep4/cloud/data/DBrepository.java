package dk.via.sep4.cloud.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.*;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;

import java.util.ArrayList;

import static org.apache.coyote.http11.Constants.a;

public class DBrepository {
    private MongoClient client;
    private MongoDatabase db;
    private MongoCollection readings;
    private MongoCollection extras;
    private ObjectMapper mapper;

    public DBrepository()
    {
        this.client=MongoClients.create("mongodb+srv://315221:sep4xg5cloud@sep4cluster.bm7ahiy.mongodb.net/?retryWrites=true&w=majority");
        this.db=client.getDatabase("SEP4DB");
        this.readings=db.getCollection("SEP4READINGS");
        this.extras=db.getCollection("SEP4EXTRAS");
        System.out.println("Connected to DB");
        UpdateOptions options = new UpdateOptions().upsert(true);
        this.mapper=new ObjectMapper();
    }

    public void insertReading(SensorReading reading)
    {
        Document DBreading = new Document("pir", reading.isPir())
                .append("temperature", reading.getTemperature())
                .append("humidity", reading.getHumidity())
                .append("co2", reading.getCo2())
                .append("sound", reading.getSound())
                .append("light", reading.getLight())
                .append("code", reading.getCode())
                .append("time", reading.getTimeReceived());

        readings.insertOne(DBreading);
    }
    public SensorReading[] getReadings() throws JsonProcessingException {
        FindIterable DBreadings=readings.find();

        MongoCursor<Document> cursor = DBreadings.iterator();
        ArrayList<SensorReading> list = new ArrayList<SensorReading>();

        while(cursor.hasNext())
        {
            list.add(new SensorReading(cursor.next().toJson()));
        }

        return list.toArray(new SensorReading[0]);
    }
    public SensorLimits getLimits() {
        Document filter=new Document("type", "limit values");

        FindIterable DBlimits=extras.find(filter);

        MongoCursor<Document> cursor = DBlimits.iterator();
        return new SensorLimits(cursor.next().toJson());
    }
    public void updateLimits(SensorLimits limits)
    {
        Document filter=new Document("type", "limit values");

        Document DBlimits=new Document("type", "limit values")
                .append("temperature", limits.getTemperature())
                .append("humidity", limits.getHumidity())
                .append("co2", limits.getCo2())
                .append("sound", limits.getSound())
                .append("light", limits.getLight());

        extras.findOneAndDelete(filter);
        extras.insertOne(DBlimits);
    }
    public void setUp(SensorLimits limits) {
        Document DBlimits=new Document("type", "limit values")
                .append("temperature", limits.getTemperature())
                .append("humidity", limits.getHumidity())
                .append("co2", limits.getCo2())
                .append("sound", limits.getSound())
                .append("light", limits.getLight());

        extras.insertOne(DBlimits);
    }
}
