package group.chat.server.routes;

import group.chat.server.models.Chat;
import group.chat.server.models.Member;
import group.chat.server.models.Message;
import group.chat.server.models.User;
import group.chat.server.service.ChatService;
import group.chat.server.service.JWTService;
import group.chat.server.util.*;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.logging.Logger;

@ServerEndpoint(value = "/connect/{accessToken}",
        encoders = MessageEncoder.class,
        decoders = MessageDecoder.class)
public final class ChatEndpoint {

    private final Logger LOGGER = Logger.getLogger(getClass().getCanonicalName());

    private JWTService jwtService = new JWTService();
    private ChatService chatService = new ChatService();

    @OnOpen
    public void onOpen(@PathParam(Constants.ACCESS_TOKEN_KEY) final String accessToken,
                       final Session session) {

        User user = jwtService.getCredentials(accessToken);
        if (user != null) {
            session.getUserProperties().put(Constants.USER_KEY, user);

            if (!ChatSessionManager.register(session)) {
                throw new RegistrationFailedException("Unable to register, user already connected");
            } else {
                System.out.print(user.getCredentials().getNickname() + " connected" + "\n");
            }
        }
    }

    @OnError
    public void onError(final Session session, final Throwable throwable) {
        if (throwable instanceof RegistrationFailedException) {
            ChatSessionManager.close(session, CloseReason.CloseCodes.VIOLATED_POLICY, throwable.getMessage());
        }
    }

    @OnMessage
    public void onMessage(final Message message, final Session session) {
        System.out.print(message.getBody() + "\n");
        if (message.getMessageType().equals(Message.TEXT)) {
            Chat chat = chatService.getChat(message.getChatId());
            if (chat == null) {
                return;
            }
            User user = (User) session.getUserProperties().get(Constants.USER_KEY);
            if (!chatService.isUserJoined(chat, user)) {
                return;
            }

            Member member = new Member();
            member.setId(user.getId());
            member.setNickName(user.getCredentials().getNickname());
            member.setColor(user.getColor());
            message.setAuthor(member);

            ChatSessionManager.publish(chat, message);
        }
    }

    @OnClose
    public void onClose(final Session session) {
        if (ChatSessionManager.remove(session)) {
            User user = (User) session.getUserProperties().get(Constants.USER_KEY);
            System.out.print(user.getCredentials().getNickname() + " disconnected" + "\n");
        }
    }

    private static final class RegistrationFailedException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        RegistrationFailedException(final String message) {
            super(message);
        }
    }

}
