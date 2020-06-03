package group.chat.server.routes;

import group.chat.server.models.Chat;
import group.chat.server.models.Message;
import group.chat.server.models.User;
import group.chat.server.service.ChatService;
import group.chat.server.service.MessageService;
import group.chat.server.util.ChatSessionManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

@WebServlet("/api/chat/join")
public class ChatJoin extends HttpServlet {

    private final Logger LOGGER = Logger.getLogger(getClass().getCanonicalName());

    private ChatService chatService = new ChatService();

    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");

        User user = (User) request.getAttribute("user");
        int chatId = Integer.parseInt(request.getParameter("chatId"));
        Chat chat = chatService.getChat(chatId);
        if (chat == null) {
            response.sendError(400, "There's no any chat with such id");
        } else if (!chatService.joinChat(chat, user)) {
            response.sendError(400, "Error joining the chat");
        } else {
            Message userJoinedMessage = new Message();
            userJoinedMessage.setMessageType(Message.ADMIN);
            userJoinedMessage.setCustomType(Message.USER_JOINED);
            userJoinedMessage.setChatId(chat.getId());
            userJoinedMessage.setBody(user.getCredentials().getNickname() + " has joined the chat");

            ChatSessionManager.publish(chat, userJoinedMessage);
        }

    }

}
