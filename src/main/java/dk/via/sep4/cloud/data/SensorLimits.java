package dk.via.sep4.cloud.data;

import lombok.Data;

@Data
public class SensorLimits {
    private double temperature;
    private int humidity;
    private int co2;
    private int sound;
    private int light;

    public SensorLimits(){

    }
}
