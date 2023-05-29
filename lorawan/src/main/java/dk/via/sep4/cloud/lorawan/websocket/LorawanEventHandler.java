package dk.via.sep4.cloud.lorawan.websocket;

import org.json.JSONObject;

/**
 * This interface is used to handle the events from the lorawan server.
 */
public interface LorawanEventHandler {
    /**
     * This method is used to handle the uplink command.
     * This is indicated by the value {@code cmd=rx}
     * This method will be called to process it.
     *
     * @param jsonData The json data received from the lorawan server.
     */
    void onUpLink(JSONObject jsonData); //rx

    /**
     * This method is used to handle the gateway status command.
     * This is indicated by the value {@code cmd=gw}
     * This method processes the gateway information.
     *
     * @param jsonData The json data received from the lorawan server.
     */
    void onGatewayStatus(JSONObject jsonData); //gw

    /**
     * This method is used to handle the downlink command.
     * This is indicated by the value {@code cmd=tx}
     * This method is called as an acknowledgement.
     *
     * @param jsonData The json data received from the lorawan server.
     */
    void onDownLink(JSONObject jsonData); //tx

    /**
     * This method is used to handle the downlink command.
     * This is indicated by the cmd value - txd
     * This method will be called as a confirmation.
     *
     * @param jsonData The json data received from the lorawan server.
     */
    void onDownLinkConfirmation(JSONObject jsonData); //txd

    /**
     * This method is used to handle the unknown command.
     * This is indicated by any unknown cmd value.
     * This method will be called to process it.
     *
     * @param dataJson The json data received from the lorawan server.
     */
    void unknownCommandReceived(JSONObject dataJson);

    /**
     * This method is used to determine the behaviour of the handler when the connection is refreshed.
     */
    void onRefresh();

    /**
     * This method is used to determine whether the handler is listening for events or not.
     *
     * @return {@code true} if the handler is listening for events, {@code false} otherwise.
     */
    boolean isListening();
}
