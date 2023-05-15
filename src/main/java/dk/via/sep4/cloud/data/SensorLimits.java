package dk.via.sep4.cloud.data;

import lombok.Data;
import org.json.JSONObject;

@Data
public class SensorLimits {
    private double minTemperature;
    private double maxTemperature;
    private int minHumidity;
    private int maxHumidity;
    private int minCo2;
    private int maxCo2;
    private int minSound;
    private int maxSound;
    private int minLight;
    private int maxLight;

    public SensorLimits(){

    }

    public SensorLimits(double minTemperature, double maxTemperature, int minHumidity, int maxHumidity, int minCo2, int maxCo2, int minSound, int maxSound, int minLight, int maxLight) {
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
        this.minHumidity = minHumidity;
        this.maxHumidity = maxHumidity;
        this.minCo2 = minCo2;
        this.maxCo2 = maxCo2;
        this.minSound = minSound;
        this.maxSound = maxSound;
        this.minLight = minLight;
        this.maxLight = maxLight;
    }

    public SensorLimits(String jsonString) {
        JSONObject dataJson = new JSONObject(jsonString);
        this.minTemperature= dataJson.getDouble("minTemperature");
        this.maxTemperature= dataJson.getDouble("maxTemperature");
        this.minHumidity = dataJson.getInt("minHumidity");
        this.maxHumidity = dataJson.getInt("maxHumidity");
        this.minCo2 = dataJson.getInt("minCo2");
        this.maxCo2 = dataJson.getInt("maxCo2");
        this.minSound = dataJson.getInt("minSound");
        this.maxSound = dataJson.getInt("maxSound");
        this.minLight = dataJson.getInt("minLight");
        this.maxLight = dataJson.getInt("maxLight");
    }
}
