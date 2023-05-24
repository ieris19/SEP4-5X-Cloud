package dk.via.sep4.cloud.data.dto;

import lombok.Data;
import org.json.JSONObject;

@Data
public class SensorState {
    private boolean isOn;

    public SensorState() {
    }
    public SensorState(String jsonString) {
        JSONObject dataJson = new JSONObject(jsonString);
        this.isOn=dataJson.getBoolean("isOn");
    }
    public SensorState(boolean isOn) {
        this.isOn = isOn;
    }
}
