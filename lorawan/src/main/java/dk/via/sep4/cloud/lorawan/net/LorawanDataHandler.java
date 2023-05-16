package dk.via.sep4.cloud.lorawan.net;

import com.ieris19.lib.files.config.FileProperties;
import dk.via.sep4.cloud.data.dto.SensorLimits;
import dk.via.sep4.cloud.data.dto.SensorReading;
import dk.via.sep4.cloud.data.DataRepository;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.NoSuchElementException;

public class LorawanDataHandler {
    private static final Logger logger = LoggerFactory.getLogger(LorawanDataHandler.class);

    private final DataRepository repository;

    public LorawanDataHandler(DataRepository repository) {
        this.repository = repository;
    }

    void parsePayload(JSONObject dataJson) {
        String payloadHex = dataJson.getString("data");
        logger.trace("Raw Payload: " + payloadHex);

        String looseBitsHex, temperatureHex, humidityHex, co2Hex, soundHex, lightHex;
        try {
        // Parsing the payload into its sections
        looseBitsHex = payloadHex.substring(0, 2);
        temperatureHex = payloadHex.substring(2, 6);
        humidityHex = payloadHex.substring(6, 8);
        co2Hex = payloadHex.substring(8, 12);
        soundHex = payloadHex.substring(12, 16);
        lightHex = payloadHex.substring(16, 19);
        } catch (StringIndexOutOfBoundsException e) {
            logger.error("Payload was not long enough to be parsed! " + e.getMessage());
            return;
        }
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
        repository.insertReading(sensorReading);
    }

    public String createLimitsPayload(JSONObject dataJson) {
        SensorLimits limits;
        try {
            limits = repository.getLimits();
        } catch (NoSuchElementException e) {
            logger.error("No limits found in database! " + e.getMessage());
            return "";
        }
        logger.trace("Limits: " + limits.toString());
        String flags, maxTemperature, minTemperature, maxHumidity, minHumidity, maxCo2;
        flags = "00";

        maxTemperature = String.format("%04X", limits.getMaxTemperature());
        minTemperature = String.format("%04X", limits.getMinTemperature());
        maxHumidity = String.format("%04X", limits.getMaxHumidity());
        minHumidity = String.format("%04X", limits.getMinHumidity());
        maxCo2 = String.format("%08X", limits.getMaxCo2());

        String hexData = flags + maxTemperature + minTemperature + maxHumidity + minHumidity + maxCo2;
        logger.trace("Raw Payload: " + hexData);
        int portNumber = dataJson.getInt("port");
        JSONObject payload = new JSONObject();
        payload.put("cmd", "tx");
        payload.put("EUI", FileProperties.getInstance("secrets").getProperty("lorawan.EUI_dev"));
        payload.put("port", portNumber);
        payload.put("confirmed", true);
        payload.put("data", hexData);

        return payload.toString();
    }

    static String hexByteToBinaryString(String hexByte) {
        if (hexByte.length() != 2) {
            throw new IllegalArgumentException("Hex string must be exactly 2 characters long");
        }
        return String.format("%8s", Integer.toBinaryString(Integer.parseInt(hexByte, 16))).replace(' ', '0');
    }
}
