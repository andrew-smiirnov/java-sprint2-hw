package serializer;

import com.google.gson.*;
import model.Epic;

import java.lang.reflect.Type;

public class EpicSerializer implements JsonSerializer<Epic> {
    @Override
    public JsonElement serialize(Epic epic, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject result = new JsonObject();
        result.addProperty("id", epic.getId());
        result.addProperty("title", epic.getTitle());
        result.addProperty("description", epic.getDescription());
        result.addProperty("status", String.valueOf(epic.getStatus()));
        result.addProperty("startTime", epic.getStartTimeToStringForBackup());
        result.addProperty("endTime", epic.getEndTimeToStringForBackup());

        JsonArray subtasks = new JsonArray();
        result.add("subtasks", subtasks);
        for (Integer subtaskId : epic.getSubtasks()){
            subtasks.add(subtaskId);
        }

        return result;
    }
}
