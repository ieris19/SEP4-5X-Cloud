package dk.via.sep4.cloud.lorawan.net;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.http.WebSocket;
import java.nio.ByteBuffer;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class LorawanSocketListener implements WebSocket.Listener {
	private final Logger logger = LoggerFactory.getLogger(LorawanSocketListener.class);
	private final LorawanClient client;

	public LorawanSocketListener(LorawanClient client) {
		this.client = client;
	}

	@Override
	public void onOpen(WebSocket webSocket) {
		// This WebSocket will invoke onText, onBinary, onPing, onPong or onClose methods on the associated listener (i.e. receive methods) up to n more times
		webSocket.request(1);
		logger.info("WebSocket Listener successfully opened for requests.");
	}

	@Override
	public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
		logger.error("WebSocket closed!");
		logger.debug("Status:" + statusCode + " Reason: " + reason);
		return CompletableFuture.completedFuture("onClose() completed.").thenAccept(logger::trace);
	}

	@Override
	public void onError(WebSocket webSocket, Throwable error) {
		logger.error("A " + error.getClass().getSimpleName() + " exception was thrown.");
		logger.debug("Message: " + error.getMessage());
		if (logger.isTraceEnabled()) {
			logger.trace("Detailed: ", error);
		}
		webSocket.abort();
		logger.error("Websocket Aborted!");
	}

	@Override
	public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
		logger.trace("OnText entered");

		JSONObject dataJson = new JSONObject(data.toString());
		String command = dataJson.getString("cmd");

		switch (command) {
			case "rx" -> client.uplinkMessage(dataJson);
			case "gw" -> client.downLinkMessageSent(dataJson);
			case "tx" -> client.downLinkMessageAcknowledgement(dataJson);
			case "txd" -> client.downLinkMessageConfirmation(dataJson);
			default -> unknownCommandReceived(dataJson);
		}

		webSocket.request(1);

		return CompletableFuture.completedFuture("onText() completed.").thenAccept(logger::trace);
	}

	@Override
	public CompletionStage<?> onPing(WebSocket webSocket, ByteBuffer message) {
		webSocket.request(1);

		logger.debug("Ping: Client <--- Server");
		logger.trace("Pong Sent:" + message.asCharBuffer().toString());
		return CompletableFuture.completedFuture("Ping completed.").thenAccept(logger::trace);
	}

	@Override
	public CompletionStage<?> onPong(WebSocket webSocket, ByteBuffer message) {
		webSocket.request(1);

		logger.debug("Ping: Client ---> Server");
		logger.trace("Pong Received" + message.asCharBuffer().toString());
		return CompletableFuture.completedFuture("Pong completed.").thenAccept(logger::trace);
	}

	private void unknownCommandReceived(JSONObject dataJson) {
		String command = dataJson.getString("cmd");
		logger.error("Unknown Lorawan command received");
		logger.debug("Command value: " + command);
		logger.debug("Contents: " + dataJson.toString(4));
	}
}