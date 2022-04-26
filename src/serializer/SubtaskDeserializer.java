package serializer;

import com.google.gson.*;
import model.Subtask;
import model.TaskStatus;

import java.lang.reflect.Type;

public class SubtaskDeserializer implements JsonDeserializer<Subtask> {
    @Override
    public Subtask deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        Integer subtaskId = null;
        if (!jsonObject.get("id").getAsString().equals("null")){
            subtaskId = jsonObject.get("id").getAsInt();
        }
        Subtask subtask = new Subtask(
                subtaskId,
                jsonObject.get("title").getAsString(),
                jsonObject.get("description").getAsString(),
                TaskStatus.valueOf(jsonObject.get("status").getAsString()),
                jsonObject.get("startTime").getAsString(),
                jsonObject.get("duration").getAsString()
        );
        subtask.setEpicId(jsonObject.get("epicId").getAsInt());
        return subtask;
    }
}
