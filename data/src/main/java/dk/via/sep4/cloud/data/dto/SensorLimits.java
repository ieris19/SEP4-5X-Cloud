package dk.via.sep4.cloud.data.dto;

import org.bson.Document;
import org.json.JSONObject;

/**
 * This class is used to store the sensor limits data in a Java environment.
 */
public record SensorLimits(int minTemperature, int maxTemperature, int minHumidity, int maxHumidity, int maxCo2)
implements JsonTransformable {
    public static SensorLimits fromJson(String jsonString) {
        int minTemperature, maxTemperature, minHumidity, maxHumidity, maxCo2;
        JSONObject dataJson = new JSONObject(jsonString);
        minTemperature = dataJson.getInt("minTemperature");
        maxTemperature = dataJson.getInt("maxTemperature");
        minHumidity = dataJson.getInt("minHumidity");
        maxHumidity = dataJson.getInt("maxHumidity");
        maxCo2 = dataJson.getInt("maxCo2");
        return new SensorLimits(minTemperature, maxTemperature, minHumidity, maxHumidity, maxCo2);
    }

    public Document toBSON() {
        return new Document("type", "limit values")
                .append("minTemperature", this.minTemperature)
                .append("maxTemperature", this.maxTemperature)
                .append("minHumidity", this.minHumidity)
                .append("maxHumidity", this.maxHumidity)
                .append("maxCo2", this.maxCo2);
    }

    public JSONObject toJSON() {
        return new JSONObject()
                .put("minTemperature", this.minTemperature)
                .put("maxTemperature", this.maxTemperature)
                .put("minHumidity", this.minHumidity)
                .put("maxHumidity", this.maxHumidity)
                .put("maxCo2", this.maxCo2);
    }
}
