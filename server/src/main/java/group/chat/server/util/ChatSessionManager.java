package group.chat.server.util;

import group.chat.server.models.Chat;
import group.chat.server.models.Member;
import group.chat.server.models.Message;
import group.chat.server.models.User;
import group.chat.server.service.MessageService;

import javax.websocket.CloseReason;
import javax.websocket.EncodeException;
import javax.websocket.Session;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class ChatSessionManager {

    private static final Lock LOCK = new ReentrantLock();
    private static final Set<Session> SESSIONS = new CopyOnWriteArraySet<>();

    private static MessageService messageService = new MessageService();

    private ChatSessionManager() {
        throw new IllegalStateException(Constants.INSTANTIATION_NOT_ALLOWED);
    }

    public static void publish(final Chat chat, final Message message) {
        assert !Objects.isNull(message);

        final long messageDate = System.currentTimeMillis();
        message.setDate(messageDate);

        Message newMessage;
        if (!chat.isEphemeral()) {
            newMessage = messageService.addMessage(message);
        } else {
            newMessage = message;
        }

        SESSIONS.forEach(session -> {
            try {
                session.getBasicRemote().sendObject(newMessage);
            } catch (IOException | EncodeException e) {
                e.printStackTrace();
            }
        });
    }

    public static boolean register(final Session session) {
        assert !Objects.isNull(session);

        boolean result;
        try {
            LOCK.lock();

            result = SESSIONS.add(session);
        } finally {
            LOCK.unlock();
        }

        return result;
    }

    public static void close(final Session session, final CloseReason.CloseCodes closeCode, final String message) {
        assert !Objects.isNull(session) && !Objects.isNull(closeCode);

        try {
            session.close(new CloseReason(closeCode, message));
        } catch (IOException e) {
            throw new RuntimeException("Unable to close session", e);
        }
    }

    public static boolean remove(final Session session) {
        assert !Objects.isNull(session);

        return SESSIONS.remove(session);
    }
}