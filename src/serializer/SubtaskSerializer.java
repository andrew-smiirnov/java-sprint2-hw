package serializer;

import com.google.gson.*;
import model.Subtask;

import java.lang.reflect.Type;

public class SubtaskSerializer implements JsonSerializer<Subtask> {
    @Override
    public JsonElement serialize(Subtask subtask, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject result = new JsonObject();
        result.addProperty("id", subtask.getId());
        result.addProperty("title", subtask.getTitle());
        result.addProperty("description", subtask.getDescription());
        result.addProperty("status", String.valueOf(subtask.getStatus()));
        result.addProperty("startTime", subtask.getStartTimeToStringForBackup());
        result.addProperty("duration", subtask.getDurationToString());
        result.addProperty("epicId", subtask.getEpicId());

        return result;
    }
}
