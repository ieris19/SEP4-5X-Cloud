package dk.via.sep4.cloud.data;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import dk.via.sep4.cloud.data.repository.SensorRepository;
import org.bson.Document;

public class DBrepository {
    private MongoClient client;
    private MongoDatabase db;
    private MongoCollection collection;

    public DBrepository()
    {
        this.client=MongoClients.create("mongodb+srv://315221:sep4xg5cloud@sep4cluster.bm7ahiy.mongodb.net/?retryWrites=true&w=majority");
        this.db=client.getDatabase("SEP4DB");
        this.collection=db.getCollection("SEP4COLLECTION");
        System.out.println("Connected to DB");
        UpdateOptions options = new UpdateOptions().upsert(true);
    }

    public void insertReading(SensorReading reading)
    {
        Document DBreading = new Document("type", "reading")
                .append("pir", reading.isPir())
                .append("temperature", reading.getTemperature())
                .append("humidity", reading.getHumidity())
                .append("co2", reading.getCo2())
                .append("sound", reading.getSound())
                .append("light", reading.getLight())
                .append("code", reading.getCode())
                .append("time", reading.getTimeReceived());

        collection.insertOne(DBreading);
    }

    public void updateTemperatureLimit(SensorLimits limits)
    {
        Document filter=new Document("_id", 1);

        Document DBlimit=new Document("_id", 1)
                .append("type", "temperature limit")
                .append("value", limits.getTemperature());

        collection.findOneAndDelete(filter);
        collection.insertOne(DBlimit);
    }
    public void updateHumidityLimit(SensorLimits limits)
    {
        Document filter=new Document("_id", 2);

        Document DBlimit=new Document("_id", 2)
                .append("type", "humidity limit")
                .append("value", limits.getHumidity());

        collection.findOneAndDelete(filter);
        collection.insertOne(DBlimit);
    }
    public void updateCo2Limit(SensorLimits limits)
    {
        Document filter=new Document("_id", 3);

        Document DBlimit=new Document("_id", 3)
                .append("type", "co2 limit")
                .append("value", limits.getCo2());

        collection.findOneAndDelete(filter);
        collection.insertOne(DBlimit);
    }
    public void updateSoundLimit(SensorLimits limits)
    {
        Document filter=new Document("_id", 4);

        Document DBlimit=new Document("_id", 4)
                .append("type", "sound limit")
                .append("value", limits.getSound());

        collection.findOneAndDelete(filter);
        collection.insertOne(DBlimit);
    }
    public void updateLightLimit(SensorLimits limits)
    {
        Document filter=new Document("_id", 5);

        Document DBlimit=new Document("_id", 5)
                .append("type", "light limit")
                .append("value", limits.getLight());

        collection.findOneAndDelete(filter);
        collection.insertOne(DBlimit);
    }

    public void setUp(SensorLimits limits) {
        Document temperatureLimit=new Document("_id", 1)
                .append("type", "temperature limit")
                .append("value", limits.getTemperature());

        Document humiditylimit=new Document("_id", 2)
                .append("type", "humidity limit")
                .append("value", limits.getHumidity());

        Document co2Limit=new Document("_id", 3)
                .append("type", "co2 limit")
                .append("value", limits.getCo2());

        Document soundLimit=new Document("_id", 4)
                .append("type", "sound limit")
                .append("value", limits.getSound());

        Document lightLimit=new Document("_id", 5)
                .append("type", "light limit")
                .append("value", limits.getLight());

        collection.insertOne(temperatureLimit);
        collection.insertOne(humiditylimit);
        collection.insertOne(co2Limit);
        collection.insertOne(soundLimit);
        collection.insertOne(lightLimit);
    }
}
