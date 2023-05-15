package dk.via.sep4.cloud.lorawan;

import com.ieris19.lib.files.config.FileProperties;
import dk.via.sep4.cloud.data.SensorLimits;
import dk.via.sep4.cloud.data.MongoRepository;
import dk.via.sep4.cloud.data.SensorReading;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class PayloadHandler {
	private static final Logger logger = LoggerFactory.getLogger(PayloadHandler.class);
	private static final MongoRepository sensorRepository = new MongoRepository();

	static void parsePayload(JSONObject dataJson) {
		String payloadHex = dataJson.getString("data");
		logger.info("Received up-link message from device.");
		logger.trace("Raw Payload: " + payloadHex);

		// Parsing the payload into its sections
		String looseBitsHex = payloadHex.substring(0, 2);
		String temperatureHex = payloadHex.substring(2, 6);
		String humidityHex = payloadHex.substring(6, 8);
		String co2Hex = payloadHex.substring(8, 12);
		String soundHex = payloadHex.substring(12, 16);
		String lightHex = payloadHex.substring(16, 19);
		logger.trace(
				String.format("Flags: %s, Temperature: %s, Humidity: %s, CO2: %s, Sound: %s, Light: %s", looseBitsHex,
											temperatureHex, humidityHex, co2Hex, soundHex, lightHex));

		//Parsing the payload into its correct data types
		String binaryData = hexByteToBinaryString(looseBitsHex);
		logger.trace("Binary Data: " + binaryData);
		boolean pirTriggered = binaryData.charAt(0) == '1';
		boolean reserved = binaryData.charAt(1) == '1';
		int errorCode = Integer.parseInt(binaryData.substring(2), 2);
		double temperature = Integer.parseInt(temperatureHex, 16) / 10d;
		int humidity = Integer.parseInt(humidityHex, 16);
		int co2 = Integer.parseInt(co2Hex, 16);
		int sound = Integer.parseInt(soundHex, 16);
		int light = Integer.parseInt(lightHex, 16);
		Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now(ZoneId.of("Europe/Copenhagen")));
		logger.trace("Timestamp: " + timestamp);

		//print values for debugging
		logger.debug("PIR Triggered: " + pirTriggered);
		logger.debug("Reserved bit: " + reserved);
		logger.debug("Error Code: " + errorCode);
		logger.debug("Temperature Double: " + temperature);
		logger.debug("Humidity Double: " + humidity);
		logger.debug("CO2 Double: " + co2);
		logger.debug("Sound Double: " + sound);
		logger.debug("Light Double: " + light);

		SensorReading sensorReading = new SensorReading(pirTriggered, temperature, humidity, co2, sound, light, errorCode,
																										timestamp);
		sensorRepository.insertReading(sensorReading);
	}

	static String hexByteToBinaryString(String hexByte) {
		if (hexByte.length() != 2) {
			throw new IllegalArgumentException("Hex string must be exactly 2 characters long");
		}
		return String.format("%8s", Integer.toBinaryString(Integer.parseInt(hexByte, 16))).replace(' ', '0');
	}

	public static JSONObject createPayload(JSONObject dataJson)
	{
		SensorLimits limits=sensorRepository.getLimits();
		String flags="00";
		String maxTemperature=String.format("%04X", limits.getMaxTemperature());
		String minTemperature=String.format("%04X", limits.getMinTemperature());
		String maxHumidity=String.format("%04X", limits.getMaxHumidity());
		String minHumidity=String.format("%04X", limits.getMinHumidity());
		String maxCo2=String.format("%08X", limits.getMaxCo2());

		String hexString=flags+maxTemperature+minTemperature+maxHumidity+minHumidity+maxCo2;

		int portNumber= dataJson.getInt("port");
		JSONObject object=new JSONObject();
		object.put("cmd", "tx");
		object.put("EUI", FileProperties.getInstance("secrets").getProperty("lorawan.EUI_dev"));
		object.put("port", portNumber);
		object.put("confirmed", true);
		object.put("data", hexString);

		return object;
	}
}
