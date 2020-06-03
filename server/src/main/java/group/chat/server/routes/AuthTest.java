package group.chat.server.routes;

import group.chat.server.models.User;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

@WebServlet("/api/testauth")
public class AuthTest extends HttpServlet {

    private final Logger LOGGER = Logger.getLogger(getClass().getCanonicalName());

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json");
        response.setStatus(200);

        JsonObjectBuilder builder = Json.createObjectBuilder();
        User user = (User) request.getAttribute("user");
        builder.add("userId", user.getId());
        builder.add("email", user.getEmail());
        builder.add("nickname", user.getCredentials().getNickname());
        JsonObject jo = builder.build();

        PrintWriter out = response.getWriter();
        out.print(jo.toString());
        out.flush();

    }

}
