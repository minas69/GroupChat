package group.chat.server.routes;

import group.chat.server.models.Credentials;
import group.chat.server.models.TokenResponse;
import group.chat.server.models.User;
import group.chat.server.service.JWTService;
import group.chat.server.service.UserService;
import group.chat.server.util.Constants;
import group.chat.server.util.JsonConverter;
import group.chat.server.validators.NicknameValidator;
import group.chat.server.validators.PasswordValidator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Base64;
import java.util.logging.Logger;

/**
 * Сервлет для авторизации. При удачной авторизации возвращает идентификатор
 * пользователся и токен.
 */
@WebServlet("/login")
public class Login extends HttpServlet {

    private final Logger LOGGER = Logger.getLogger(getClass().getCanonicalName());

    private static final String BASIC = "Basic ";

    private JWTService jwtService = new JWTService();
    private UserService userService = new UserService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        final String nickname = request.getParameter("nickname");
        final String pass = request.getParameter("pass");

        Credentials credentials = new Credentials(nickname, pass);

        if (!NicknameValidator.validate(credentials.getNickname())) {
            request.setAttribute("code", Constants.Code.INCORRECT_NICKNAME);
            response.sendError(400, "Nickname is not correct");
            return;
        }
        if (!PasswordValidator.validate(credentials.getPassword())) {
            request.setAttribute("code", Constants.Code.INCORRECT_PASSWORD);
            response.sendError(400, "Password is not correct");
            return;
        }

        User user = userService.findUserByNickname(credentials.getNickname());

        if (user != null && user.passwordEquals(credentials.getPassword())) {
            String token = jwtService.generateToken(user);
            System.out.print("token = " + token + "\n");
            TokenResponse tokenResponse = new TokenResponse(user.getId(), token);

            String output = new JsonConverter().convertToJson(tokenResponse);

            response.setContentType("application/json;charset=UTF-8");

            PrintWriter out = response.getWriter();
            out.print(output);
            out.flush();
        } else {
            request.setAttribute("code", Constants.Code.INCORRECT_PASSWORD);
            response.sendError(400, "Password is not correct");
        }

    }

}
