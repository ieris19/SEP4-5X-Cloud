package dk.via.sep4.cloud.web.service;

import dk.via.sep4.cloud.Persistance.SensorReading;

import java.util.ArrayList;

public class WebRepository {
	private static final ArrayList<SensorReading> readings;

	static  {
		readings = new ArrayList<>();
	}

	public static SensorReading[] getReadings() {
		return readings.toArray(new SensorReading[0]);
	}

	public static void addReading(SensorReading reading) {
		readings.add(reading);
	}

	public static SensorReading getLastReading() {
		return readings.size() != 0 ? readings.get(readings.size() - 1) : null;
	}

	public static SensorReading getReadingIndex(int index) {
		return readings.get(index);
	}
}
