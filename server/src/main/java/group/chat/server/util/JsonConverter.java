package group.chat.server.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import group.chat.server.models.Chat;
import group.chat.server.models.Message;
import group.chat.server.models.TokenResponse;

import java.util.List;

/**
 * Утилита, конвертирующая объекты в json строки.
 */
public class JsonConverter {

    private final Gson gson;

    /**
     * Конструктор, инициализирующий объект класса {@link Gson}
     */
    public JsonConverter() {
        gson = new GsonBuilder().create();
    }

    /**
     * Конвертировать список чатов в json строку.
     */
    public String convertChatsToJson(List<Chat> chats) {
        JsonArray jsonArray = gson.toJsonTree(chats).getAsJsonArray();
        return jsonArray.toString();
    }

    public String convertChatToJson(Chat chat) {
        JsonObject jsonObject = gson.toJsonTree(chat).getAsJsonObject();
        return jsonObject.toString();
    }

    /**
     * Конвертировать ответ с токеном в json строку.
     */
    public String convertToJson(TokenResponse tokenResponse) {
        JsonObject jsonObject = gson.toJsonTree(tokenResponse).getAsJsonObject();
        return jsonObject.toString();
    }

    public String convertMessagesToJson(List<Message> messages) {
        JsonArray jsonArray = gson.toJsonTree(messages).getAsJsonArray();
        return jsonArray.toString();
    }

    public String convertIsMemberToJson(boolean isMember) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("isMember", isMember);
        return jsonObject.toString();
    }
}