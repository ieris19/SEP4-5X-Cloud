package dk.via.sep4.cloud.web.service;

import dk.via.sep4.cloud.Persistance.SensorReading;

public class WebJSONData {
	public static String getDataAsJSON() {
		StringBuilder data = new StringBuilder("{");
		data.append("\"tempReadings\":[");
		SensorReading[] readings = WebRepository.getReadings();
		for (int i = 0; i < readings.length; i++) {
			data.append(tempToJSON(i, readings[i]));
		}
		data.append("]");
		data.append("}");
		return data.toString();
	}

	public static String tempToJSON(int index, SensorReading reading) {
		StringBuilder data = new StringBuilder();
		if (index != 0) data.append(",");
		data.append("{");
		data.append("\"readingNumber\":").append(index).append(",");
		data.append("\"temperature\":").append(reading != null ? reading.getTemperature() : "null");
		data.append("}");
		return data.toString();
	}
}
