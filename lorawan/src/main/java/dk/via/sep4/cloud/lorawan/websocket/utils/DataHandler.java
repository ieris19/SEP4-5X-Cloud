package dk.via.sep4.cloud.lorawan.websocket.utils;

import dk.via.sep4.cloud.data.dto.SensorLimits;
import dk.via.sep4.cloud.data.dto.SensorReading;
import dk.via.sep4.cloud.data.repository.DataRepository;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.NoSuchElementException;


@Slf4j
@Component
public class DataHandler {
    private final DataRepository repository;

    public DataHandler(@Autowired DataRepository dataRepository) {
        this.repository = dataRepository;
    }

    public void save(SensorReading reading) {
        repository.insertReading(reading);
    }

    public SensorReading parsePayload(JSONObject jsonData) {
        String hexData = jsonData.getString("data");
        log.trace("Up-link payload: {}", hexData);

        String looseBitsHex, temperatureHex, humidityHex, co2Hex, soundHex, lightHex;
        try {
            // Separating the payload into its sections
            looseBitsHex = hexData.substring(0, 2);
            temperatureHex = hexData.substring(2, 6);
            humidityHex = hexData.substring(6, 8);
            co2Hex = hexData.substring(8, 12);
            soundHex = hexData.substring(12, 16);
            lightHex = hexData.substring(16, 20);
        } catch (StringIndexOutOfBoundsException e) {
            log.error("Payload was not long enough to be parsed! {}", e.getMessage());
            return null;
        }
        log.trace("Payload parsed into:\n Flags: {} Temperature: {} Humidity: {} CO2: {} Sound: {} Light: {}",
                looseBitsHex, temperatureHex, humidityHex, co2Hex, soundHex, lightHex);

        //Parsing the payload into its correct data types
        String errorCode;
        Timestamp timestamp;
        double temperature;
        boolean pirTriggered, reserved;
        int humidity, co2, sound, light;
        try {
            String binaryData = hexByteToBinaryString(looseBitsHex);
            log.trace("Binary Data: {}", binaryData);
            pirTriggered = binaryData.charAt(0) == '1';
            reserved = binaryData.charAt(1) == '1';
            errorCode = binaryData.substring(2);
            temperature = Integer.parseInt(temperatureHex, 16) / 10d;
            humidity = Integer.parseInt(humidityHex, 16);
            co2 = Integer.parseInt(co2Hex, 16);
            sound = Integer.parseInt(soundHex, 16);
            light = Integer.parseInt(lightHex, 16);
            timestamp = Timestamp.valueOf(LocalDateTime.now(ZoneId.of("Europe/Copenhagen")));
        } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
            log.error("Could not parse payload into correct data types!", e);
            return null;
        }
        return new SensorReading(pirTriggered, temperature, humidity, co2, sound, light, errorCode, timestamp);
    }

    public JSONObject createPayload(JSONObject jsonData) {
        SensorLimits limits = this.getLimits();
        if (limits == null) {
            return null;
        }
        /*
        Since the payload is hexadecimals, in order to achieve the first bit being the on/off state, meaning the
        payload starting with 1000 or 0000, the first hex digit has to be either 8 or 0.
        */
        String climateState = repository.getState().isOn() ? "8" : "0";
        String reserved = "0";
        log.trace("Limits: {}", limits);
        StringBuilder hexData = new StringBuilder()
                .append(climateState)
                .append(reserved)
                .append(String.format("%02X", limits.maxTemperature()))
                .append(String.format("%02X", limits.minTemperature()))
                .append(String.format("%02X", limits.maxHumidity()))
                .append(String.format("%02X", limits.minHumidity()))
                .append(String.format("%04X", limits.maxCo2()));

        log.trace("Down-link Payload: {}", hexData);

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
