package dk.via.sep4.cloud.data;

import lombok.Data;
import org.json.JSONObject;

@Data
public class SensorLimits {
    private double temperature;
    private int humidity;
    private int co2;
    private int sound;
    private int light;

    public SensorLimits(){

    }
    public SensorLimits(double temperature, int humidity, int co2, int sound, int light) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.co2 = co2;
        this.sound = sound;
        this.light = light;
    }
    public SensorLimits(String jsonString) {
        JSONObject dataJson = new JSONObject(jsonString);
        this.temperature= dataJson.getDouble("temperature");
        this.humidity = dataJson.getInt("humidity");
        this.co2 = dataJson.getInt("co2");
        this.sound = dataJson.getInt("sound");
        this.light = dataJson.getInt("light");
    }
}
