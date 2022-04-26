package serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import model.SimpleTask;

import java.lang.reflect.Type;

public class SimpleTaskSerializer implements JsonSerializer<SimpleTask> {
    @Override
    public JsonElement serialize(SimpleTask simpleTask, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject result = new JsonObject();
        result.addProperty("id", simpleTask.getId());
        result.addProperty("title", simpleTask.getTitle());
        result.addProperty("description", simpleTask.getDescription());
        result.addProperty("status", String.valueOf(simpleTask.getStatus()));
        result.addProperty("startTime", simpleTask.getStartTimeToStringForBackup());
        result.addProperty("duration", simpleTask.getDurationToString());
        return result;
    }
}
