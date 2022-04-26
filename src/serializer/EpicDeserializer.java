package serializer;

import com.google.gson.*;
import model.Epic;
import model.TaskStatus;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class EpicDeserializer implements JsonDeserializer<Epic> {
    @Override
    public Epic deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        Integer epicId = null;
        if (!jsonObject.get("id").getAsString().equals("null")){
            epicId = jsonObject.get("id").getAsInt();
        }
        Epic epic = new Epic(
                epicId,
                jsonObject.get("title").getAsString(),
                jsonObject.get("description").getAsString(),
                TaskStatus.valueOf(jsonObject.get("status").getAsString()),
                jsonObject.get("startTime").getAsString(),
                jsonObject.get("endTime").getAsString()
        );

        JsonArray subtasks = jsonObject.getAsJsonArray("subtasks");
        List<Integer> subtasksId = new ArrayList<>();
        if(!subtasks.isEmpty()){
        for (JsonElement subtask : subtasks){
                subtasksId.add(subtask.getAsInt());
            }
        epic.setSubtasks(subtasksId);
        }
        return epic;
    }
}
