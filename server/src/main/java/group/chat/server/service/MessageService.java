package group.chat.server.service;

import group.chat.server.dao.ChatDAO;
import group.chat.server.dao.MessageDAO;
import group.chat.server.models.Chat;
import group.chat.server.models.Message;

import java.util.List;

/**
 * Сервис для работы с сообщениями.
 */
public class MessageService {

    private MessageDAO messageDAO;
    private ChatDAO chatDAO;

    public MessageService() {
        messageDAO = new MessageDAO();
        chatDAO = new ChatDAO();
    }

    public List<Message> getMessages(Chat chat) {
        return messageDAO.findAll(chat);
    }

    public Message addMessage(Message message) {
        Message addedMessage = messageDAO.insert(message);
        chatDAO.updateLastMessage(addedMessage);
        return addedMessage;
    }

}

