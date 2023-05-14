package dk.via.sep4.cloud.lorawan;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.nio.ByteBuffer;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class LorawanSocketListener implements WebSocket.Listener {
	private final WebSocket server;
	private final Logger logger = LoggerFactory.getLogger(LorawanSocketListener.class);
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
			case "rx" -> PayloadHandler.parsePayload(dataJson);
			case "gw" -> logger.info("Received gateway status message");
			default -> unknownCommandReceived(dataJson);
		}

		webSocket.request(1);

		return CompletableFuture.completedFuture("onText() completed.").thenAccept(logger::trace);
	}

	public CompletionStage<?> onPing(WebSocket webSocket, ByteBuffer message) {
		webSocket.request(1);

		logger.debug("Ping: Client ---> Server");
		logger.debug(message.asCharBuffer().toString());
		return CompletableFuture.completedFuture("Ping completed.").thenAccept(logger::trace);
	}

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