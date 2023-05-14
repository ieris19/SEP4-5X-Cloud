package dk.via.sep4.cloud.data;

import lombok.Data;
import org.json.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class SensorReading {
	@Id
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
		JSONObject dataJson = new JSONObject(jsonString.toString());
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

		String pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
		this.timeReceived=Timestamp.valueOf(LocalDateTime.parse(timeJson.getString("$date"), dtf));
	}

	@Override public String toString() {
		return String.format("Reading[pir=%s, temperature=%s, humidity=%s, co2=%s, sound=%s, light=%s, code=%s, timeReceived=%s]", pir, temperature, humidity, co2, sound, light, code, timeReceived);
	}
}
