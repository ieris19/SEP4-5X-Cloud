package dk.via.sep4.cloud.data;

import com.mongodb.client.*;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class DBrepository {
    private MongoClient client;
    private MongoDatabase db;
    private MongoCollection readings;
    private MongoCollection extras;

    public DBrepository()
    {
        this.client=MongoClients.create("mongodb+srv://315221:sep4xg5cloud@sep4cluster.bm7ahiy.mongodb.net/?retryWrites=true&w=majority");
        this.db=client.getDatabase("SEP4DB");
        this.readings=db.getCollection("SEP4READINGS");
        this.extras=db.getCollection("SEP4EXTRAS");
        System.out.println("Connected to DB");
        UpdateOptions options = new UpdateOptions().upsert(true);
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
    public String getReadings()
    {
        FindIterable DBreadings=readings.find();

        MongoCursor<Document> cursor = DBreadings.iterator();
        List<String> list = new ArrayList<String>();

        while(cursor.hasNext())
            list.add(cursor.next().toJson());

        return list.toString();
    }
    public void updateLimits(SensorLimits limits)
    {
        Document filter=new Document("_id", 1);

        Document DBlimits=new Document("_id", 1)
                .append("type", "limit values")
                .append("temperature", limits.getTemperature())
                .append("humidity", limits.getHumidity())
                .append("co2", limits.getCo2())
                .append("sound", limits.getSound())
                .append("light", limits.getLight());

        extras.findOneAndDelete(filter);
        extras.insertOne(DBlimits);
    }
    public void setUp(SensorLimits limits) {
        Document DBlimits=new Document("_id", 1)
                .append("type", "limit values")
                .append("temperature", limits.getTemperature())
                .append("humidity", limits.getHumidity())
                .append("co2", limits.getCo2())
                .append("sound", limits.getSound())
                .append("light", limits.getLight());

        extras.insertOne(DBlimits);
    }
}