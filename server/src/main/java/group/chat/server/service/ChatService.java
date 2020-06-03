package group.chat.server.service;

import group.chat.server.dao.ChatDAO;
import group.chat.server.models.Chat;
import group.chat.server.models.User;
import group.chat.server.util.Constants;

import java.util.List;

/**
 * Сервис для работы с чатами.
 */
public class ChatService {

    private ChatDAO chatDAO;

    public ChatService() {
        chatDAO = new ChatDAO();
    }

    public List<Chat> getChats() {
        return chatDAO.findAll();
    }

    public Chat getChat(int id) {
        return chatDAO.find(id);
    }

    public Chat addChat(Chat chat) {
        chat.setMembersCount(0);
        if (chat.isEphemeral()) {
            chat.setColor(Constants.Color.RED);
        } else {
            chat.setColor(Constants.Color.DEEP_PURPLE);
        }
        return chatDAO.insert(chat);
    }

    public boolean isUserJoined(Chat chat, User user) {
        return chatDAO.isUserJoined(chat, user);
    }

    public boolean joinChat(Chat chat, User user) {
        if (!isUserJoined(chat, user) && chatDAO.addUserToChat(chat, user)) {
            chatDAO.setMembersCount(chat, chat.getMembersCount() + 1);
            return true;
        }

        return false;
    }

}

