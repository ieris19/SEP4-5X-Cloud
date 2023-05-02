package dk.via.sep4.cloud.web.service;

import dk.via.sep4.cloud.Persistance.SensorReading;

public class WebJSONData {
	public static String getDataAsJSON() {
		StringBuilder data = new StringBuilder("{");
		data.append("\"tempReadings\":[");
		SensorReading[] readings = WebRepository.getReadings();
		for (int i = 0; i < readings.length; i++) {
			if (i != 0) data.append(",");
			data.append("{");
			data.append("\"readingNumber\":").append(i).append(",");
			data.append("\"temperature\":").append(readings[i].getTemperature()).append(",");
			data.append("}");
		}
		data.append("]");
		data.append("}");
		return data.toString();
	}
}
