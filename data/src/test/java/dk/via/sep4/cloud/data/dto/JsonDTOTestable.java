package dk.via.sep4.cloud.data.dto;

import dk.via.sep4.cloud.data.utils.JsonComparator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

public abstract class JsonDTOTestable<T extends JsonTransformable> {
    protected String dataJSON;
    protected String[] webJSONPairs;
    protected T sample;

    @BeforeEach
    abstract void setUp();

    @Test
    public void fromJson() {
        try {
            Object fromJson = sample.getClass().getDeclaredMethod("fromJson", String.class)
                    .invoke(null, dataJSON);
            assertEquals(fromJson, sample, "fromJson() does not return equal object");
        } catch (NullPointerException e) {
            fail("fromJson() is not static");
        } catch (ClassCastException e) {
            fail("fromJson() does not return correct type (should be " + sample.getClass().getName() + ")");
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            fail("fromJson() not implemented by " + sample.getClass().getName());
        }
    }

    @Test
    public void toJSON() {
        String json = sample.toJSON().toString();
        assertTrue(JsonComparator.contains(json, webJSONPairs), "toJSON() does not return correct JSON");
    }
}
