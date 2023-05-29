package dk.via.sep4.cloud.lorawan.websocket.utils;

import com.ieris19.lib.files.config.FileProperties;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;

public class WebSocketFactory {
    private static final URI targetURI;

    static {
        targetURI = getLorawanURI();
    }

    private static URI getLorawanURI() {
        try (FileProperties secret = FileProperties.getInstance("secrets")) {
            return URI.create(secret.getProperty("lorawan.url"));
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("At least one of the accessed properties in secrets.properties doesn't exist", e);
        } catch (IOException e) {
            throw new IllegalStateException("Error reading secrets.properties", e);
        }
    }

    public static WebSocket createWebSocket(WebSocket.Listener listener) {
        return HttpClient.newHttpClient()
                .newWebSocketBuilder()
                .buildAsync(targetURI, listener)
                .join();
    }
}
