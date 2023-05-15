package dk.via.sep4.cloud.data;

import lombok.Data;
import org.json.JSONObject;

@Data
public class SensorLimits {
    private double minTemperature;
    private double maxTemperature;
    private int minHumidity;
    private int maxHumidity;
    private int maxCo2;

    public SensorLimits(){

    }

    public SensorLimits(double minTemperature, double maxTemperature, int minHumidity, int maxHumidity, int maxCo2) {
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
        this.minHumidity = minHumidity;
        this.maxHumidity = maxHumidity;
        this.maxCo2 = maxCo2;
    }

    public SensorLimits(String jsonString) {
        JSONObject dataJson = new JSONObject(jsonString);
        this.minTemperature= dataJson.getDouble("minTemperature");
        this.maxTemperature= dataJson.getDouble("maxTemperature");
        this.minHumidity = dataJson.getInt("minHumidity");
        this.maxHumidity = dataJson.getInt("maxHumidity");
        this.maxCo2 = dataJson.getInt("maxCo2");
    }
}
