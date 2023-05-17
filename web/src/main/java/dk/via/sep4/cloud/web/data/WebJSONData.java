package dk.via.sep4.cloud.web.data;

import dk.via.sep4.cloud.data.dto.SensorLimits;
import dk.via.sep4.cloud.data.dto.SensorReading;
import dk.via.sep4.cloud.data.dto.UserCredentials;
import org.json.JSONArray;
import org.json.JSONObject;
/**
 * This class is used to convert the data received from the lorawan server to JSON format.
 */
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
		object.put("minTemperature", limits.getMinTemperature());
		object.put("maxTemperature", limits.getMaxTemperature());
		object.put("minHumidity", limits.getMinHumidity());
		object.put("maxHumidity", limits.getMaxHumidity());
		object.put("maxCo2", limits.getMaxCo2());

		return object.toString();
	}

	public static String getCredentialsAsJSON(UserCredentials credentials) {
		JSONObject object=new JSONObject();
		object.put("username", credentials.getUsername());
		object.put("password", credentials.getPassword());

		return object.toString();
	}
}
