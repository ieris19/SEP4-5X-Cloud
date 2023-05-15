package dk.via.sep4.cloud.web.service;

import dk.via.sep4.cloud.data.SensorLimits;
import dk.via.sep4.cloud.data.SensorReading;
import org.json.JSONArray;
import org.json.JSONObject;

public class WebJSONData {
	public static String getReadingsAsJSON(SensorReading[] readings) {
		JSONArray array=new JSONArray();
		for (SensorReading reading:readings)
		{
			JSONObject object=new JSONObject();
			object.put("pir", reading.isPir());
			object.put("temperature", reading.getTemperature());
			object.put("humidity", reading.getHumidity());
			object.put("co2", reading.getCo2());
			object.put("sound", reading.getSound());
			object.put("light", reading.getLight());
			object.put("code", reading.getCode());
			object.put("timeReceived", reading.getTimeReceived());

			array.put(object);
		}

		return array.toString();
	}
	public static String getLimitsAsJSON(SensorLimits limits) {
		JSONObject object=new JSONObject();
		object.put("temperature", limits.getTemperature());
		object.put("humidity", limits.getHumidity());
		object.put("co2", limits.getCo2());
		object.put("sound", limits.getSound());
		object.put("light", limits.getLight());

		return object.toString();
	}
}
