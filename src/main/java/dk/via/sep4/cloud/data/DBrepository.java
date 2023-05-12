package dk.via.sep4.cloud.data;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
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
    }

    public void insertReading(SensorReading reading)
    {
        Document DBreading = new Document("isOpen", reading.isOpen())
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

}
