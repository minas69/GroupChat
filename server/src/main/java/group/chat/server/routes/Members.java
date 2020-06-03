package group.chat.server.routes;

import group.chat.server.models.Chat;
import group.chat.server.models.User;
import group.chat.server.service.ChatService;
import group.chat.server.util.JsonConverter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

@WebServlet("/api/chat/members")
public class Members extends HttpServlet {

    private final Logger LOGGER = Logger.getLogger(getClass().getCanonicalName());

    private ChatService chatService = new ChatService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");

        User user = (User) request.getAttribute("user");
        int chatId = Integer.parseInt(request.getParameter("chatId"));
        Chat chat = chatService.getChat(chatId);
        if (chat == null) {
            response.sendError(400, "There's no any chat with such id");
        } else {
            boolean isUserJoined = chatService.isUserJoined(chat, user);
            JsonConverter converter = new JsonConverter();
            String output = converter.convertIsMemberToJson(isUserJoined);

            PrintWriter out = response.getWriter();
            out.print(output);
            out.flush();
        }

    }

}
