package dk.via.sep4.cloud.lorawan;

import dk.via.sep4.cloud.data.DBrepository;
import dk.via.sep4.cloud.data.SensorReading;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class LorawanSocketListener implements WebSocket.Listener {
	private final WebSocket server;
	private final Logger logger = LoggerFactory.getLogger(LorawanSocketListener.class);
	private static final DBrepository sensorRepository = new DBrepository();

	public LorawanSocketListener(String url) {
		HttpClient client = HttpClient.newHttpClient();
		CompletableFuture<WebSocket> ws = client.newWebSocketBuilder().buildAsync(URI.create(url), this);

		server = ws.join();
	}

	public void onOpen(WebSocket webSocket) {
		// This WebSocket will invoke onText, onBinary, onPing, onPong or onClose methods on the associated listener (i.e. receive methods) up to n more times
		webSocket.request(1);
		logger.info("WebSocket Listener successfully opened for requests.");
	}

	public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
		logger.error("WebSocket closed!");
		logger.debug("Status:" + statusCode + " Reason: " + reason);
		return CompletableFuture.completedFuture("onClose() completed.").thenAccept(logger::trace);
	}

	public void onError(WebSocket webSocket, Throwable error) {
		logger.error("A " + error.getClass().getSimpleName() + " exception was thrown.");
		logger.debug("Message: " + error.getMessage());
		if (logger.isTraceEnabled()) {
			error.printStackTrace();
		}
		webSocket.abort();
		logger.error("Websocket Aborted!");
	}

	public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
		logger.trace("OnText entered");

		JSONObject dataJson = new JSONObject(data.toString());
		String command = dataJson.getString("cmd");

		switch (command) {
			case "rx" -> parsePayload(dataJson);
			case "gw" -> logger.info("Received gateway status message");
			default -> unknownCommandReceived(dataJson);
		}

		webSocket.request(1);

		return CompletableFuture.completedFuture("onText() completed.").thenAccept(logger::trace);
	}

	private void parsePayload(JSONObject dataJson) {
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
	public CompletionStage<?> onPing(WebSocket webSocket, ByteBuffer message) {
		webSocket.request(1);

		logger.debug("Ping: Client ---> Server");
		logger.debug(message.asCharBuffer().toString());
		return CompletableFuture.completedFuture("Ping completed.").thenAccept(logger::trace);
	}

	private static String hexByteToBinaryString(String hexByte) {
		if (hexByte.length() != 2) {
			throw new IllegalArgumentException("Hex string must be exactly 2 characters long");
		}
		return String.format("%8s", Integer.toBinaryString(Integer.parseInt(hexByte, 16))).replace(' ', '0');
	public CompletionStage<?> onPong(WebSocket webSocket, ByteBuffer message) {
		webSocket.request(1);

		logger.debug("Pong: Client ---> Server");
		logger.debug(message.asCharBuffer().toString());
		return CompletableFuture.completedFuture("Pong completed.").thenAccept(logger::trace);
	}

	// Send down-link message to device
	// Must be in Json format, according to https://github.com/ihavn/IoT_Semester_project/blob/master/LORA_NETWORK_SERVER.md
	public void sendDownLink(String jsonTelegram) {
		server.sendText(jsonTelegram, true);
	}

	private void unknownCommandReceived(JSONObject dataJson) {
		String command = dataJson.getString("cmd");
		logger.error("Unknown Lorawan command received");
		logger.debug("Command value: " + command);
		logger.debug("Contents: " + dataJson.toString(4));
	}
}