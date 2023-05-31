package dk.via.sep4.cloud.data.utils;

public class JsonComparator {
    public static boolean contains(String json, String[] keyValuePairs) {
        for (String pair : keyValuePairs) {
            if (!json.contains(pair)) {
                return false;
            }
        }
        return true;
    }
}
