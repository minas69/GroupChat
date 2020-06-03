package group.chat.server.routes;

import group.chat.server.models.Chat;
import group.chat.server.models.Member;
import group.chat.server.models.Message;
import group.chat.server.models.User;
import group.chat.server.service.ChatService;
import group.chat.server.util.ChatSessionManager;
import group.chat.server.util.JsonConverter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

@WebServlet("/api/chat/new")
public class ChatNew extends HttpServlet {

    private final Logger LOGGER = Logger.getLogger(getClass().getCanonicalName());

    private ChatService chatService = new ChatService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");

        request.setCharacterEncoding("UTF-8");
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        boolean isEphemeral = Boolean.parseBoolean(request.getParameter("isEphemeral"));
        Chat chat = new Chat();
        chat.setTitle(title);
        chat.setDescription(description);
        chat.setEphemeral(isEphemeral);
        User user = (User) request.getAttribute("user");
        Member admin = new Member();
        admin.setId(user.getId());
        admin.setNickName(user.getCredentials().getNickname());
        admin.setColor(user.getColor());
        chat.setAdmin(admin);
        Chat createdChat = chatService.addChat(chat);

        JsonConverter converter = new JsonConverter();
        String output = converter.convertChatToJson(createdChat);
        PrintWriter out = response.getWriter();
        out.print(output);
        out.flush();

        Message userJoinedMessage = new Message();
        userJoinedMessage.setMessageType(Message.ADMIN);
        userJoinedMessage.setCustomType(Message.USER_JOINED);
        userJoinedMessage.setChatId(createdChat.getId());
        userJoinedMessage.setBody("Chat \"" + createdChat.getTitle() + "\" has been created");

        ChatSessionManager.publish(createdChat, userJoinedMessage);

    }

}
