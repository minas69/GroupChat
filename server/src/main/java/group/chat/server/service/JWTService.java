package group.chat.server.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import group.chat.server.dao.UserDAO;
import group.chat.server.models.User;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Сервис для работы с токенами.
 */
public class JWTService {

    private final String SECRET = "08e4e064ec5af8a33fefff4ce93764f16da9c45e";

    private UserDAO userDAO;

    /**
     * Конструктор, инициализирующий DAO объект.
     */
    public JWTService() {
        userDAO = new UserDAO();
    }

    /**
     * Сгенерировать токен.
     * @param user пользователь, для которого создаем токен
     * @return токен
     */
    public String generateToken(final User user) throws JWTCreationException {

        final Instant CURRENT_TIME = Instant.now();
        final Instant EXPIRED_TIME = CURRENT_TIME.plus(30, ChronoUnit.DAYS);

        Algorithm algorithm = Algorithm.HMAC256(SECRET);

        return JWT.create()
                .withSubject(String.valueOf(user.getId()))
                .withIssuedAt(Date.from(CURRENT_TIME))
                .withExpiresAt(Date.from(EXPIRED_TIME))
                .withIssuer("GroupChatServer")
                .sign(algorithm);

    }

    /**
     * Получить информацию о пользователе из токена.
     * @param token токен
     * @return пользователь, с помощью которого был сгенерирован токен
     */
    public User getCredentials(String token) {

        User user = null;

        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("GroupChatServer")
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            int userId = Integer.parseInt(jwt.getSubject());
            user = userDAO.find(userId);

        } catch (JWTVerificationException exception){
            System.out.println(exception.getMessage());
        }

        return user;

    }

}
