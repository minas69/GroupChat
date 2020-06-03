package group.chat.server.routes;

import group.chat.server.models.TokenResponse;
import group.chat.server.models.User;
import group.chat.server.service.JWTService;
import group.chat.server.service.UserService;
import group.chat.server.util.ColorUtils;
import group.chat.server.util.Constants;
import group.chat.server.util.JsonConverter;
import group.chat.server.validators.EmailValidator;
import group.chat.server.validators.NicknameValidator;
import group.chat.server.validators.PasswordValidator;

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

/**
 * Сервлет для регистрации. При удачной регистрации возвращает идентификатор
 * созданного пользователся и токен.
 */
@WebServlet("/signUp")
public class SignUp extends HttpServlet {

    private final Logger LOGGER = Logger.getLogger(getClass().getCanonicalName());

    private JWTService jwtService = new JWTService();
    private UserService userService = new UserService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        final String nickname = request.getParameter("nickname");
        final String email = request.getParameter("email");
        final String pass = request.getParameter("pass");

        if (!NicknameValidator.validate(nickname)) {
            request.setAttribute("code", Constants.Code.INCORRECT_NICKNAME);
            response.sendError(400, "Nickname is not correct");
            return;
        }
        if (!EmailValidator.validate(email)) {
            request.setAttribute("code", Constants.Code.INCORRECT_EMAIL);
            response.sendError(400, "Email is not correct");
            return;
        }
        if (!PasswordValidator.validate(pass)) {
            request.setAttribute("code", Constants.Code.INCORRECT_PASSWORD);
            response.sendError(400, "Password is not correct");
            return;
        }

        User user = new User();
        user.getCredentials().setNickname(nickname);
        user.setEmail(email);
        user.getCredentials().setPassword(pass);
        user.setColor(ColorUtils.getRandomColor());

        User addedUser = userService.addUser(user);
        if (addedUser != null) {
            String token = jwtService.generateToken(addedUser);
            TokenResponse tokenResponse = new TokenResponse(addedUser.getId(), token);

            String output = new JsonConverter().convertToJson(tokenResponse);

            response.setContentType("application/json;charset=UTF-8");

            PrintWriter out = response.getWriter();
            out.print(output);
            out.flush();
        } else {
            response.sendError(400, "Such profile already exists");
        }

    }

}
