package manager;

import api.KVTaskClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Epic;
import model.SimpleTask;
import model.Subtask;
import serializer.*;

import java.io.IOException;


public class HTTPTaskManager extends FileBackedTasksManager {
    private Gson toGson = new GsonBuilder()
            .registerTypeAdapter(SimpleTask.class, new SimpleTaskSerializer())
            .registerTypeAdapter(Epic.class, new EpicSerializer())
            .registerTypeAdapter(Subtask.class, new SubtaskSerializer())
            .create();
    private KVTaskClient kvTaskClient;


    public HTTPTaskManager(String url) {
        kvTaskClient = new KVTaskClient(url);
        try {
            kvTaskClient.register();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void save() {
        if (!getAllTasks().isEmpty()) {
            String history = toString(historyManager);
            if (history.equals("")) {
                kvTaskClient.put("history", toGson.toJson("null"));
            } else {
                kvTaskClient.put("history", toGson.toJson(history));
            }
            kvTaskClient.put("tasks", toGson.toJson(taskMap));
        }
    }

    public String load(){
        String request = "";
        request = kvTaskClient.load("history") + kvTaskClient.load("tasks");
        return request;
    }
}
