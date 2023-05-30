package dk.via.sep4.cloud.data.dto;

import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

/**
 * This class is used to store the sensor readings data in a Java environment.
 */
public record SensorReading(String id, boolean pir, double temperature, int humidity, int co2, int sound, int light,
                            String code, Timestamp timeReceived, String comment) {
    public SensorReading(boolean pir, double temperature, int humidity, int co2, int sound, int light,
                         String code, Timestamp timeReceived) {
        this("", pir, temperature, humidity, co2, sound, light, code, timeReceived, "");
    }

    public static SensorReading fromJson(String jsonString) {
        boolean pir;
        double temperature;
        Timestamp timeReceived;
        String id, errorCode, comment;
        int humidity, co2, sound, light;
        JSONObject dataJson = new JSONObject(jsonString);
        id = dataJson.getJSONObject("_id").getString("$oid");
        pir = dataJson.getBoolean("pir");
        temperature = dataJson.getDouble("temperature");
        humidity = dataJson.getInt("humidity");
        co2 = dataJson.getInt("co2");
        sound = dataJson.getInt("sound");
        light = dataJson.getInt("light");
        errorCode = dataJson.get("code").toString();
        try {
            comment = dataJson.getString("comment");
        } catch (JSONException e) {
            comment = "";
        }
        JSONObject dateJson = dataJson.getJSONObject("time");
        JSONObject timeJson = new JSONObject(dateJson.toString());

        String pattern = "yyyy-MM-dd'T'HH:mm:ss";
        DateTimeFormatter format = new DateTimeFormatterBuilder()
                .appendPattern(pattern)
                .appendFraction(ChronoField.MILLI_OF_SECOND, 0, 3, true)
                .appendLiteral("Z")
                .toFormatter();
        timeReceived = Timestamp.valueOf(LocalDateTime.parse(timeJson.getString("$date"), format));
        return new SensorReading(id, pir, temperature, humidity, co2, sound, light,
                errorCode, timeReceived, comment);
    }

    public Document toBSON() {
        return new Document("pir", this.pir)
                .append("temperature", this.temperature)
                .append("humidity", this.humidity)
                .append("co2", this.co2)
                .append("sound", this.sound)
                .append("light", this.light)
                .append("code", this.code)
                .append("time", this.timeReceived)
                .append("comment", this.comment);
    }

    public JSONObject toJSON() {
        JSONObject object = new JSONObject();
        object.put("id", this.id);
        object.put("pir", this.pir);
        object.put("temperature", this.temperature);
        object.put("humidity", this.humidity);
        object.put("co2", this.co2);
        object.put("sound", this.sound);
        object.put("light", this.light);
        object.put("code", this.code);
        object.put("time", this.timeReceived);
        object.put("comment", this.comment);

        return object;
    }

    @Override
    public String toString() {
        return String.format("Reading[pir=%s, temperature=%s, humidity=%s, co2=%s, sound=%s, light=%s, code=%s, timeReceived=%s]", pir, temperature, humidity, co2, sound, light, code, timeReceived);
    }
}