package api;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class KVTaskClient {
    private final HttpClient client;
    private final static String request = "%s/%s/%s/?API_KEY=%s";
    private final String url;
    private String apiKey = null;


    public KVTaskClient (String url) {
        this.client = HttpClient.newHttpClient();
        this.url = url;
    }

    public void put(String key, String jsonString) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(saveUrl(key)))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                .build();
        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String load(String key) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(loadUrl(key)))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                System.out.println("Что-то пошло не так. Сервер вернул код состояния: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса load возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
        return response.body();
    }

    public void stop() throws IOException {
        new KVServer().stop();
    }

    private String saveUrl(String key){
        if (apiKey == null){
            throw new IllegalArgumentException("API_KEY не получен");
        }
        return String.format(request, this.url, "save", key, apiKey);
    }

    private String loadUrl(String key){
        if (apiKey == null){
            throw new IllegalArgumentException("API_KEY не получен");
        }
        return String.format(request, this.url, "load", key, apiKey);
    }

    public void register() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url + "/register"))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        apiKey = response.body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            System.out.println("Ошибка произошла на этапе регистрации на KVServer-е");
            e.printStackTrace();
        }
    }
}
