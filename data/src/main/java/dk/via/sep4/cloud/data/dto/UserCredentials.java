package dk.via.sep4.cloud.data.dto;

import lombok.Data;
import org.json.JSONObject;
/**
 * This class is used to store the user credentials data in a Java environment.
 */
@Data
public class UserCredentials {
    private String username;
    private String password;

    public UserCredentials() {

    }

    public UserCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserCredentials(String jsonString) {
        JSONObject dataJson = new JSONObject(jsonString);
        this.username = dataJson.getString("username");
        this.password = dataJson.getString("password");
    }
}
