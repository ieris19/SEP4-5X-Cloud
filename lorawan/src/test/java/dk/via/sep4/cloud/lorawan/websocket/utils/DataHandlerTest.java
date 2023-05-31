package dk.via.sep4.cloud.lorawan.websocket.utils;

import dk.via.sep4.cloud.data.dto.SensorReading;
import dk.via.sep4.cloud.data.repository.MockRepository;
import dk.via.sep4.cloud.data.repository.mongo.MongoRepository;
import dk.via.sep4.cloud.data.utils.JsonComparator;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;

import static org.junit.jupiter.api.Assertions.*;

class DataHandlerTest {

    DataHandler dataHandler = new DataHandler(new MockRepository());

    @Test
    void parsePayload() {
        // Hex Payload:    BD-00FA-2E-2468-1616-1616
        String hexData = "BD"+"00FA"+"2E"+"2468"+"1616"+"1616";
        SensorReading expectedReading = new SensorReading(true, 25D, 46, 9320, 5654, 5654, "111101", null);
        JSONObject jsonData = new JSONObject();
        jsonData.put("data", hexData);
        SensorReading testReading = dataHandler.parsePayload(jsonData);
        assertEquals(expectedReading.pir(), testReading.pir());
        assertEquals(expectedReading.temperature(), testReading.temperature());
        assertEquals(expectedReading.humidity(), testReading.humidity());
        assertEquals(expectedReading.co2(), testReading.co2());
        assertEquals(expectedReading.sound(), testReading.sound());
        assertEquals(expectedReading.light(), testReading.light());
        assertEquals(expectedReading.code(), testReading.code());
    }

    @Test
    void createPayload() {
        JSONObject jsonData = new JSONObject();
        int port = 2;
        String EUI = "device-eui";
        jsonData.put("port", port);
        jsonData.put("EUI", EUI);
        JSONObject payload = dataHandler.createPayload(jsonData);
        String[] payloadPairs = new String[]{
                "\"cmd\":\"tx\"",
                "\"data\":\"80190F3C1E0320\"",
                "\"EUI\":\"" + EUI + '"',
                "\"port\":2",
                "\"confirmed\":true",
        };
        assertTrue(JsonComparator.contains(payload.toString(), payloadPairs));
    }
}