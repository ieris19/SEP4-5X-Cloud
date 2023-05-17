package dk.via.sep4.cloud.data.dto;

import lombok.Data;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
/**
 * This class is used to store the sensor readings data in a Java environment.
 */
@Data
public class SensorReading {
	private String id;
	private boolean pir;
	private double temperature;
	private int humidity;
	private int co2;
	private int sound;
	private int light;
	private int code;
	private Timestamp timeReceived;

	public SensorReading() {
	}

	public SensorReading(boolean pir, double temperature, int humidity, int co2, int sound, int light, int code, Timestamp timeReceived) {
		this.pir = pir;
		this.temperature = temperature;
		this.humidity = humidity;
		this.co2 = co2;
		this.sound = sound;
		this.light = light;
		this.code = code;
		this.timeReceived = timeReceived;
	}

	public SensorReading(String jsonString) {
		JSONObject dataJson = new JSONObject(jsonString);
		JSONObject idJson = dataJson.getJSONObject("_id");
		this.id=idJson.getString("$oid");
		this.pir=dataJson.getBoolean("pir");
		this.temperature = dataJson.getDouble("temperature");
		this.humidity = dataJson.getInt("humidity");
		this.co2 = dataJson.getInt("co2");
		this.sound = dataJson.getInt("sound");
		this.light = dataJson.getInt("light");
		this.code = dataJson.getInt("code");
		JSONObject dateJson = dataJson.getJSONObject("time");
		JSONObject timeJson = new JSONObject(dateJson.toString());

		String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
		DateTimeFormatter format = DateTimeFormatter.ofPattern(pattern);
		this.timeReceived=Timestamp.valueOf(LocalDateTime.parse(timeJson.getString("$date"), format));
	}

	@Override public String toString() {
		return String.format("Reading[pir=%s, temperature=%s, humidity=%s, co2=%s, sound=%s, light=%s, code=%s, timeReceived=%s]", pir, temperature, humidity, co2, sound, light, code, timeReceived);
	}
}