package dk.via.sep4.cloud.lorawan.websocket;

import dk.via.sep4.cloud.data.DataRepository;
import dk.via.sep4.cloud.data.dto.SensorLimits;
import dk.via.sep4.cloud.data.dto.SensorReading;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.NoSuchElementException;


@Slf4j
public class DataHandler {
    private final DataRepository repository;

    public DataHandler(DataRepository dataRepository) {
        this.repository = dataRepository;
    }

    public void save(SensorReading reading) {
        repository.insertReading(reading);
    }

    public SensorReading parsePayload(JSONObject jsonData) {
        String hexData = jsonData.getString("data");
        log.trace("Received payload: {}", hexData);

        String looseBitsHex, temperatureHex, humidityHex, co2Hex, soundHex, lightHex;
        try {
            // Separating the payload into its sections
            looseBitsHex = hexData.substring(0, 2);
            temperatureHex = hexData.substring(2, 6);
            humidityHex = hexData.substring(6, 8);
            co2Hex = hexData.substring(8, 12);
            soundHex = hexData.substring(12, 16);
            lightHex = hexData.substring(16, 19);
        } catch (StringIndexOutOfBoundsException e) {
            log.error("Payload was not long enough to be parsed! {}", e.getMessage());
            return null;
        }
        log.trace("Payload parsed into:\n Flags: {} Temperature: {} Humidity: {} CO2: {} Sound: {} Light: {}",
                looseBitsHex, temperatureHex, humidityHex, co2Hex, soundHex, lightHex);

        //Parsing the payload into its correct data types
        String binaryData = hexByteToBinaryString(looseBitsHex);
        log.trace("Binary Data: {}", binaryData);
        boolean pirTriggered = binaryData.charAt(0) == '1';
        boolean reserved = binaryData.charAt(1) == '1';
        int errorCode = Integer.parseInt(binaryData.substring(2), 2);
        double temperature = Integer.parseInt(temperatureHex, 16) / 10d;
        int humidity = Integer.parseInt(humidityHex, 16);
        int co2 = Integer.parseInt(co2Hex, 16);
        int sound = Integer.parseInt(soundHex, 16);
        int light = Integer.parseInt(lightHex, 16);
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now(ZoneId.of("Europe/Copenhagen")));

        return new SensorReading(pirTriggered, temperature, humidity, co2, sound, light, errorCode, timestamp);
    }

    public JSONObject createPayload(JSONObject jsonData) {
        SensorLimits limits = this.getLimits();
        if (limits == null) {
            return null;
        }
        String climateState = "0";
        if(repository.getState().isOn()){
            climateState = "1";
        }
        log.trace("Limits: {}", limits);
        StringBuilder hexData = new StringBuilder().append("0")
                .append(climateState)
                .append(String.format("%02X", limits.getMaxTemperature()))
                .append(String.format("%02X", limits.getMinTemperature()))
                .append(String.format("%02X", limits.getMaxHumidity()))
                .append(String.format("%02X", limits.getMinHumidity()))
                .append(String.format("%04X", limits.getMaxCo2()));

        log.trace("Raw Payload: {}", hexData);
        int portNumber = jsonData.getInt("port");
        String eui = jsonData.getString("EUI");
        JSONObject payload = new JSONObject();
        payload.put("cmd", "tx");
        payload.put("EUI", eui);
        payload.put("port", portNumber);
        payload.put("confirmed", true);
        payload.put("data", hexData);

        return payload;
    }

    public JSONObject createClimateTogglePayload(JSONObject jsonData) {
        StringBuilder hexData = new StringBuilder().append("00");

        log.trace("Raw Payload: {}", hexData);
        int portNumber = jsonData.getInt("port");
        String eui = jsonData.getString("EUI");
        JSONObject payload = new JSONObject();
        payload.put("cmd", "tx");
        payload.put("EUI", eui);
        payload.put("port", portNumber);
        payload.put("confirmed", true);
        payload.put("data", hexData);

        return payload;
    }

    private SensorLimits getLimits() {
        try {
            return repository.getLimits();
        } catch (NoSuchElementException e) {
            log.error("No limits found in database! {}", e.getMessage());
            return null;
        }
    }

    private String hexByteToBinaryString(String hexByte) {
        if (hexByte.length() != 2) {
            throw new IllegalArgumentException("Hex string must be exactly 2 characters long");
        }
        return String.format("%8s", Integer.toBinaryString(Integer.parseInt(hexByte, 16))).replace(' ', '0');
    }
}
