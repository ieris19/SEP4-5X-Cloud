package dk.via.sep4.cloud.data.dto;

import org.bson.Document;
import org.json.JSONObject;

public record ControlState(boolean isOn) {
    public ControlState(boolean isOn) {
        this.isOn = isOn;
    }

    public static ControlState fromJson(String jsonString) {
        JSONObject dataJson = new JSONObject(jsonString);
        return new ControlState(dataJson.getBoolean("isOn"));
    }

    public Document toBSON() {
        return new Document("type", "state")
                .append("isOn", this.isOn);
    }

    public JSONObject toJSON() {
        return new JSONObject().put("isOn", this.isOn);
    }
}
