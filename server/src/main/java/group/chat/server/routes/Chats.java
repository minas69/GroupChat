package group.chat.server.routes;

import group.chat.server.models.Chat;
import group.chat.server.service.ChatService;
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
 * Сервлет для запроса списка чатов.
 */
@WebServlet("/api/chats")
public class Chats extends HttpServlet {

    private final Logger LOGGER = Logger.getLogger(getClass().getCanonicalName());

    private ChatService chatService = new ChatService();


    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json;charset=UTF-8");

        List<Chat> chats = chatService.getChats();

        JsonConverter converter = new JsonConverter();
        String output = converter.convertChatsToJson(chats);

        PrintWriter out = response.getWriter();
        out.print(output);
        out.flush();

    }

}
