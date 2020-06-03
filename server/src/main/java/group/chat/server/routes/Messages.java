package group.chat.server.routes;

import group.chat.server.models.Chat;
import group.chat.server.models.Message;
import group.chat.server.service.ChatService;
import group.chat.server.service.MessageService;
import group.chat.server.util.JsonConverter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Logger;

/**
 * Сервлет для запроса списка сообщений.
 */
@WebServlet("/api/messages")
public class Messages extends HttpServlet {

    private final Logger LOGGER = Logger.getLogger(getClass().getCanonicalName());

    private ChatService chatService = new ChatService();
    private MessageService messageService = new MessageService();


    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json;charset=UTF-8");

        int chatId = Integer.parseInt(request.getParameter("chatId"));
        Chat chat = chatService.getChat(chatId);
        if (chat == null) {
            response.sendError(400, "There's no any chat with such id");
            return;
        }
        if (chat.isEphemeral()) {
            response.sendError(400, "This chat is ephemeral");
            return;
        }
        List<Message> messages = messageService.getMessages(chat);

        JsonConverter converter = new JsonConverter();
        String output = converter.convertMessagesToJson(messages);

        PrintWriter out = response.getWriter();
        out.print(output);
        out.flush();

    }

}
