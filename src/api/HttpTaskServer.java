package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import manager.TaskManager;
import model.*;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import serializer.*;


import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;


public class HttpTaskServer {
    private static Gson toGson = new GsonBuilder()
            .registerTypeAdapter(SimpleTask.class, new SimpleTaskSerializer())
            .registerTypeAdapter(Epic.class, new EpicSerializer())
            .registerTypeAdapter(Subtask.class, new SubtaskSerializer())
            .create();
    private static Gson fromGson = new GsonBuilder()
            .registerTypeAdapter(SimpleTask.class, new SimpleTaskDeserializer())
            .registerTypeAdapter(Epic.class, new EpicDeserializer())
            .registerTypeAdapter(Subtask.class, new SubtaskDeserializer())
            .create();
    private static final int PORT = 8080;
    private final HttpServer httpServer;
    private TaskManager taskManager;


    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        this.httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TasksHandler());
        httpServer.createContext("/tasks/task", new SimpleTasksHandler());
        httpServer.createContext("/tasks/epic", new EpicTasksHandler());
        httpServer.createContext("/tasks/subtask", new SubTasksHandler());
        httpServer.start();
        System.out.println("HTTP-сервер запущен. Адрес http://localhost:" + PORT);
    }


    private class TasksHandler implements HttpHandler {
        HttpExchange exchange;

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            this.exchange = exchange;
            String response = "";
            String method = exchange.getRequestMethod(); // извлечь метод из запроса
            String[] path = exchange.getRequestURI().getPath().split("/");

            switch (method) {
                case "GET":
                    if (path.length == 2) {
                        getPrioritizedTasks();
                        break;
                    }
                    if (path.length > 2) {
                        if (path[2].equals("history")) {
                            getHistory();
                            break;
                        }
                    }
                    break;
                case "DELETE":
                    taskManager.deleteAllTasks();
                    if (taskManager.getPrioritizedTasks().size() == 0) {
                        response = "Все задачи удалены";
                        sendResponse(response);
                        break;
                    }
                    response = "Во время удаления задач произошла ошибка";
                    break;
                default:
                    response = "Что то пошло не так";
                    break;
            }
            sendErrorResponse(response);
        }


        private void getPrioritizedTasks() throws IOException {
            String response;
            Set<Task> tasks = taskManager.getPrioritizedTasks();
            if (!tasks.isEmpty()) {
                response = toGson.toJson(tasks);
            } else {
                response = "Список задач пуст";
            }
            sendResponse(response);
        }

        private void getHistory() throws IOException {
            String response = "История просмотров пуста";
            StringBuilder history = new StringBuilder();
            for (Task task : taskManager.history()) {
                history.append(toGson.toJson(task));
            }
            if (history.length() > 0) {
                response = history.toString();
            }
            sendResponse(response);
        }

        private void sendResponse(String response) throws IOException {
            exchange.sendResponseHeaders(200, 0);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }

        private void sendErrorResponse(String response) throws IOException {
            exchange.sendResponseHeaders(400, 0);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    private class SimpleTasksHandler implements HttpHandler {
        HttpExchange exchange;

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            this.exchange = exchange;
            String response = "";
            String method = exchange.getRequestMethod();
            String parameters = exchange.getRequestURI().getQuery();
            Map<Integer, Task> allTasks = taskManager.getAllTasks();
            Integer iD = null;
            if (parameters != null) {
                String[] param = exchange.getRequestURI().getQuery().split("=");
                if (param.length == 2 && param[0].equals("id")) {
                    iD = Integer.valueOf(param[1]);
                }
            }
            switch (method) {
                case "POST":
                    if (iD != null) {
                        if (allTasks.containsKey(iD) && allTasks.get(iD).getTypeOfTask().equals(TypeOfTask.SIMPLE_TASK)) {
                            SimpleTask task = fromGson.fromJson(getBody(), SimpleTask.class);
                            taskManager.updateSimpleTaskById(task);
                            response = "Задача id=" + iD + " успешно обновлена";
                            sendResponse(response);
                            break;
                        }
                    } else {
                        SimpleTask task = fromGson.fromJson(getBody(), SimpleTask.class);
                        task.setId(null);
                        int size = taskManager.getPrioritizedTasks().size();
                        taskManager.addSimpleTask(task);
                        if ((size + 1) == taskManager.getPrioritizedTasks().size()) {
                            response = "Задача добавлена";
                            sendResponse(response);
                            break;
                        }
                    }
                    response = "Ошибка при добавлении/обновлении задачи";
                    sendErrorResponse(response);
                    break;
                case "GET":
                    if (iD != null) {
                        if (allTasks.containsKey(iD) && allTasks.get(iD).getTypeOfTask().equals(TypeOfTask.SIMPLE_TASK)) {
                            Optional<SimpleTask> task = taskManager.getSimpleTask(iD);
                            if (task != null) {
                                response = toGson.toJson(task.get());
                                sendResponse(response);
                                break;
                            }
                        }
                    }
                    response = "Произошла ошибка во время получения задачи";
                    sendErrorResponse(response);
                    break;
                case "DELETE":
                    if (iD != null) {
                        if (allTasks.containsKey(iD) && allTasks.get(iD).getTypeOfTask().equals(TypeOfTask.SIMPLE_TASK)) {
                            taskManager.deleteSimpleTaskById(iD);
                            if (!allTasks.containsKey(iD)) {
                                response = "Задача id=" + iD + " успешно удалена";
                                sendResponse(response);
                                break;
                            }
                        }
                    }
                    response = "Произошла ошибка во время удаления задачи";
                    sendErrorResponse(response);
                    break;
                default:
                    response = "Что то пошло не так при работе с задачами";
                    break;
            }
            sendErrorResponse(response);
        }

        private String getBody() throws IOException {
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            return body;
        }

        private void sendResponse(String response) throws IOException {
            exchange.sendResponseHeaders(200, 0);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }

        private void sendErrorResponse(String response) throws IOException {
            exchange.sendResponseHeaders(400, 0);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    private class EpicTasksHandler implements HttpHandler {
        HttpExchange exchange;

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            this.exchange = exchange;
            String response = "";
            String method = exchange.getRequestMethod();
            String[] path = exchange.getRequestURI().getPath().split("/");
            String parameters = exchange.getRequestURI().getQuery();
            Map<Integer, Task> allTasks = taskManager.getAllTasks();
            Integer iD = null;
            if (parameters != null) {
                String[] param = exchange.getRequestURI().getQuery().split("=");
                if (param.length == 2 && param[0].equals("id")) {
                    iD = Integer.valueOf(param[1]);
                }
            }
            switch (method) {
                case "POST":
                    if (iD != null) {
                        if (allTasks.containsKey(iD) && allTasks.get(iD).getTypeOfTask().equals(TypeOfTask.EPIC)) {
                            Epic epic = fromGson.fromJson(getBody(), Epic.class);
                            taskManager.updateEpicById(epic);
                            response = "Эпик id=" + iD + " успешно обновлён";
                            sendResponse(response);
                            break;
                        }
                    } else {
                        Epic epic = fromGson.fromJson(getBody(), Epic.class);
                        epic.setId(null);
                        int size = allTasks.size();
                        taskManager.addEpic(epic);
                        if ((size + 1) == allTasks.size()) {
                            response = "Эпик добавлен";
                            sendResponse(response);
                            break;
                        }
                    }
                    response = "Ошибка при добавлении/обновлении эпика";
                    sendErrorResponse(response);
                    break;
                case "GET":
                    if (path.length == 3) {
                        if (iD != null) {
                            if (allTasks.containsKey(iD) && allTasks.get(iD).getTypeOfTask().equals(TypeOfTask.EPIC)) {
                                Optional<Epic> epic = taskManager.getEpic(iD);
                                if (epic != null) {
                                    response = toGson.toJson(epic.get());
                                    sendResponse(response);
                                    break;
                                }
                            }
                        }
                    }
                    if (path.length > 3) {
                        if (path[3].equals("subtasks")) {
                            Optional<List<Integer>> subtasksList = Optional.ofNullable(taskManager.getSubtasksIdOfEpic(iD));
                            if (subtasksList.get().isEmpty()) {
                                response = "У эпика id=" + iD + " нет подзадач";
                                sendResponse(response);
                                break;
                            }
                            List<Subtask> subtasks = new ArrayList<>();
                            for (Integer subtaskId : subtasksList.get()) {
                                subtasks.add((Subtask) allTasks.get(subtaskId));
                            }
                            response = toGson.toJson(subtasks);
                            sendResponse(response);
                        }
                    }
                    response = "Произошла ошибка во время получения эпика";
                    sendErrorResponse(response);
                    break;
                case "DELETE":
                    if (iD != null) {
                        if (allTasks.containsKey(iD) && allTasks.get(iD).getTypeOfTask().equals(TypeOfTask.EPIC)) {
                            taskManager.deleteEpicById(iD);
                            if (!allTasks.containsKey(iD)) {
                                response = "Эпик id=" + iD + " успешно удалён";
                                sendResponse(response);
                                break;
                            }
                        }
                    }
                    response = "Произошла ошибка во время удаления эпика";
                    sendErrorResponse(response);
                    break;
                default:
                    response = "Что то пошло не так при работе с эпиками";
                    break;
            }
            sendErrorResponse(response);
        }

        private String getBody() throws IOException {
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            return body;
        }

        private void sendResponse(String response) throws IOException {
            exchange.sendResponseHeaders(200, 0);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }

        private void sendErrorResponse(String response) throws IOException {
            exchange.sendResponseHeaders(400, 0);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    private class SubTasksHandler implements HttpHandler {
        HttpExchange exchange;

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            this.exchange = exchange;
            String response = "";
            String method = exchange.getRequestMethod();
            String parameters = exchange.getRequestURI().getQuery();
            Map<Integer, Task> allTasks = taskManager.getAllTasks();
            Integer iD = null;
            if (parameters != null) {
                String[] param = exchange.getRequestURI().getQuery().split("=");
                if (param.length == 2 && param[0].equals("id")) {
                    iD = Integer.valueOf(param[1]);
                }
            }
            switch (method) {
                case "POST":
                    if (iD != null) {
                        if (allTasks.containsKey(iD) && allTasks.get(iD).getTypeOfTask().equals(TypeOfTask.SUBTASK)) {
                            Subtask subtask = fromGson.fromJson(getBody(), Subtask.class);
                            taskManager.updateSubtaskById(subtask);
                            response = "Подадача id=" + iD + " успешно обновлена";
                            sendResponse(response);
                            break;
                        }
                    } else {
                        Subtask subtask = fromGson.fromJson(getBody(), Subtask.class);
                        subtask.setId(null);
                        int size = taskManager.getPrioritizedTasks().size();
                        taskManager.addSubtask(subtask.getEpicId(), subtask);
                        if ((size + 1) == taskManager.getPrioritizedTasks().size()) {
                            response = "Подзадача добавлена";
                            sendResponse(response);
                            break;
                        }
                    }
                    response = "Ошибка при добавлении/обновлении подадачи";
                    sendErrorResponse(response);
                    break;
                case "GET":
                    if (iD != null) {
                        if (allTasks.containsKey(iD) && allTasks.get(iD).getTypeOfTask().equals(TypeOfTask.SUBTASK)) {
                            Optional<Subtask> subtask = taskManager.getSubtask(iD);
                            if (subtask != null) {
                                response = toGson.toJson(subtask.get());
                                sendResponse(response);
                                break;
                            }
                        }
                    }
                    response = "Произошла ошибка во время получения подзадачи";
                    sendErrorResponse(response);
                    break;
                case "DELETE":
                    if (iD != null) {
                        if (allTasks.containsKey(iD) && allTasks.get(iD).getTypeOfTask().equals(TypeOfTask.SUBTASK)) {
                            Optional<Subtask> subtask = taskManager.getSubtask(iD);
                            if (subtask != null) {
                                int size = taskManager.getPrioritizedTasks().size();
                                taskManager.deleteSubtaskById(iD);
                                if ((size - 1) == taskManager.getPrioritizedTasks().size()) {
                                    response = "Подзадача id=" + iD + " успешно удалена";
                                    sendResponse(response);
                                    break;
                                }
                            }
                        }
                    }
                    response = "Произошла ошибка во время удаления подзадачи";
                    sendErrorResponse(response);
                    break;
                default:
                    response = "Что то пошло не так при работе с подзадачей";
                    break;
            }
            sendErrorResponse(response);
        }

        private String getBody() throws IOException {
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            return body;
        }

        private void sendResponse(String response) throws IOException {
            exchange.sendResponseHeaders(200, 0);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }

        private void sendErrorResponse(String response) throws IOException {
            exchange.sendResponseHeaders(400, 0);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    public void stop() {
        httpServer.stop(0);
    }
}