package ru.urfu.studyassistant.infrastructure.vk;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ru.urfu.studyassistant.core.model.UserId;
import ru.urfu.studyassistant.core.port.ChatPlatform;
import ru.urfu.studyassistant.core.port.Message;
import ru.urfu.studyassistant.core.port.MessageHandler;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class VkChatPlatform implements ChatPlatform {
    private final String groupId;
    private final String accessToken;
    private final HttpClient httpClient;
    private final Gson gson;
    private MessageHandler handler;

    public VkChatPlatform(String groupId, String accessToken) {
        this.groupId = groupId;
        this.accessToken = accessToken;
        this.gson = new Gson();
        
        // HTTP-клиент
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    @Override
    public void setHandler(MessageHandler handler) {
        this.handler = handler;
    }

    @Override
    public void send(UserId to, String text) {
        try {
            var vkUserId = to.value().replace("vk-", "");
            var encodedText = URLEncoder.encode(text, StandardCharsets.UTF_8);
            var randomId = (int) (Math.random() * Integer.MAX_VALUE);

            var url = String.format(
                    "https://api.vk.com/method/messages.send?user_id=%s&message=%s&random_id=%d&v=5.199&access_token=%s",
                    vkUserId, encodedText, randomId, accessToken
            );

            // POST
            var request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            System.err.println("[VK] Ошибка при отправке сообщения:");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("[VK] Запуск Long Poll...");
        try {
            var lpData = getLongPollServer();
            var server = lpData.get("server").getAsString();
            var key = lpData.get("key").getAsString();
            var ts = lpData.get("ts").getAsString();

            System.out.println("[VK] Успешно. Подключились к ВКонтакте. Ждем сообщения...");

            while (true) {
                var url = String.format("%s?act=a_check&key=%s&ts=%s&wait=25", server, key, ts);
                
                // GET
                var request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .GET()
                        .build();

                var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                var json = gson.fromJson(response.body(), JsonObject.class);

                if (json.has("failed")) {
                    System.out.println("[VK] токен устаревший, переподключаемся...");
                    lpData = getLongPollServer();
                    server = lpData.get("server").getAsString();
                    key = lpData.get("key").getAsString();
                    ts = lpData.get("ts").getAsString();
                    continue;
                }
                ts = json.get("ts").getAsString();

                var updates = json.getAsJsonArray("updates");
                for (JsonElement element : updates) {
                    var update = element.getAsJsonObject();
                    
                    if ("message_new".equals(update.get("type").getAsString())) {
                        var messageObj = update.getAsJsonObject("object").getAsJsonObject("message");
                        
                        var vkUserId = messageObj.get("from_id").getAsString();
                        var text = messageObj.get("text").getAsString();

                        var domainUserId = new UserId("vk-" + vkUserId);
                        var answer = handler.onMessage(new Message(domainUserId, text));

                        send(domainUserId, answer);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("[VK] Ошибка в работе платформы:");
            e.printStackTrace();
        }
    }

    private JsonObject getLongPollServer() throws Exception {
        var url = String.format(
                "https://api.vk.com/method/groups.getLongPollServer?group_id=%s&v=5.199&access_token=%s",
                groupId, accessToken
        );
        // GET
        var request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return gson.fromJson(response.body(), JsonObject.class).getAsJsonObject("response");
    }
}