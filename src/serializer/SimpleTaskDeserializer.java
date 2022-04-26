package serializer;

import com.google.gson.*;
import model.SimpleTask;
import model.TaskStatus;

import java.lang.reflect.Type;

public class SimpleTaskDeserializer implements JsonDeserializer<SimpleTask> {
    @Override
    public SimpleTask deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        Integer taskId = null;
        if (!jsonObject.get("id").getAsString().equals("null")){
            taskId = jsonObject.get("id").getAsInt();
        }
        SimpleTask simpleTask = new SimpleTask(
                taskId,
                jsonObject.get("title").getAsString(),
                jsonObject.get("description").getAsString(),
                TaskStatus.valueOf(jsonObject.get("status").getAsString()),
                jsonObject.get("startTime").getAsString(),
                jsonObject.get("duration").getAsString()
        );
        return simpleTask;
    }
}
