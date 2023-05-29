package dk.via.sep4.cloud.data.dto;

import lombok.Data;
import org.bson.Document;
import org.json.JSONObject;
/**
 * This class is used to store the sensor limits data in a Java environment.
 */
@Data
public class SensorLimits {
    private int minTemperature;
    private int maxTemperature;
    private int minHumidity;
    private int maxHumidity;
    private int maxCo2;

    public SensorLimits(){

    }

    public SensorLimits(int minTemperature, int maxTemperature, int minHumidity, int maxHumidity, int maxCo2) {
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
        this.minHumidity = minHumidity;
        this.maxHumidity = maxHumidity;
        this.maxCo2 = maxCo2;
    }

    public SensorLimits(String jsonString) {
        JSONObject dataJson = new JSONObject(jsonString);
        this.minTemperature= dataJson.getInt("minTemperature");
        this.maxTemperature= dataJson.getInt("maxTemperature");
        this.minHumidity = dataJson.getInt("minHumidity");
        this.maxHumidity = dataJson.getInt("maxHumidity");
        this.maxCo2 = dataJson.getInt("maxCo2");
    }

    public Document toBSON() {
        Document limitsDocument = new Document("type", "limit values")
                .append("minTemperature", this.minTemperature)
                .append("maxTemperature", this.maxTemperature)
                .append("minHumidity", this.minHumidity)
                .append("maxHumidity", this.maxHumidity)
                .append("maxCo2", this.maxCo2);
        return limitsDocument;
    }

    public JSONObject toJSON() {
        JSONObject object=new JSONObject();

        object.put("minTemperature", this.minTemperature);
        object.put("maxTemperature", this.maxTemperature);
        object.put("minHumidity", this.minHumidity);
        object.put("maxHumidity", this.maxHumidity);
        object.put("maxCo2", this.maxCo2);

        return object;
    }
}
