package dk.via.sep4.cloud.data.repository.mongo;


import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import dk.via.sep4.cloud.data.repository.DataOperationResult;
import org.bson.BsonValue;

public class MongoOperationResult extends DataOperationResult {
    MongoOperationResult(boolean isSuccessful, long affectedCount) {
        this.isSuccessful = isSuccessful;
        this.affectedCount = affectedCount;
    }

    public static MongoOperationResult from(InsertOneResult result) {
        BsonValue idValue = result.getInsertedId();
        return idValue != null ?
                new MongoOperationResult(result.wasAcknowledged(), 1)
                : new MongoOperationResult(result.wasAcknowledged(), 0);
    }

    public static MongoOperationResult from(UpdateResult result) {
        return new MongoOperationResult(result.wasAcknowledged(), result.getModifiedCount());
    }
}
