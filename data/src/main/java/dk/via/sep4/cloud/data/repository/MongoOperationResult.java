package dk.via.sep4.cloud.data.repository;


import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import lombok.Value;
import org.bson.BsonValue;

@Value
public class MongoOperationResult implements DataOperationResult {
    boolean isSuccessful;
    long affectedCount;
    String additionalInfo;

    public static MongoOperationResult from(InsertOneResult result) {
        BsonValue idValue = result.getInsertedId();
        return idValue != null ?
                new MongoOperationResult(result.wasAcknowledged(), 1, idValue.asString().getValue())
                : new MongoOperationResult(result.wasAcknowledged(), 0, "");
    }

    public static MongoOperationResult from(UpdateResult result) {
        return new MongoOperationResult(result.wasAcknowledged(), result.getModifiedCount(), "");
    }
}
