package dk.via.sep4.cloud.data;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.sql.Timestamp;

@Data
public class SensorReading {
	@Id
	private String id;
	private boolean open;
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

	public SensorReading(boolean open, boolean pir, double temperature, int humidity, int co2, int sound, int light, int code, Timestamp timeReceived) {
		this.open = open;
		this.pir = pir;
		this.temperature = temperature;
		this.humidity = humidity;
		this.co2 = co2;
		this.sound = sound;
		this.light = light;
		this.code = code;
		this.timeReceived = timeReceived;
	}

	public String getId() {
		return id;
	}

	public boolean isOpen() {
		return open;
	}

	public boolean isPir() {
		return pir;
	}

	public double getTemperature() {
		return temperature;
	}

	public int getHumidity() {
		return humidity;
	}

	public int getCo2() {
		return co2;
	}

	public int getSound() {
		return sound;
	}

	public int getLight() {
		return light;
	}

	public int getCode() {
		return code;
	}

	public Timestamp getTimeReceived() {
		return timeReceived;
	}

	@Override public String toString() {
		return String.format("Reading[open=%s, pir=%s, temperature=%s, humidity=%s, co2=%s, sound=%s, light=%s, code=%s, timeReceived=%s]", open, pir, temperature, humidity, co2, sound, light, code, timeReceived);
	}
}
