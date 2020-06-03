package group.chat.server.dao;

import group.chat.server.models.Chat;
import group.chat.server.models.Member;
import group.chat.server.models.Message;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO для объектов класса {@link Message}.
 */
public class MessageDAO extends BaseDAO {

    public List<Message> findAll(Chat chat) {

        List<Message> messages = new ArrayList<>();

        Connection connection = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            connection = getConnection();

            pst = connection.prepareStatement("SELECT messages.*, " +
                    "users.nickname, " +
                    "users.color " +
                    "FROM messages " +
                    "LEFT JOIN users ON messages.user_id=users.id " +
                    "WHERE chat_id = (?)");
            pst.setInt(1, chat.getId());
            rs = pst.executeQuery();

            while (rs.next()) {
                String messageType = rs.getString(6);

                Message message = new Message();
                message.setId(rs.getInt(1));
                message.setDate(rs.getLong(2));
                message.setChatId(rs.getInt(3));
                message.setBody(rs.getString(5));
                message.setMessageType(messageType);
                message.setCustomType(rs.getString(7));

                if (messageType.equals(Message.TEXT)) {
                    Member author = new Member();
                    author.setId(rs.getInt(4));
                    author.setNickName(rs.getString(8));
                    author.setColor(rs.getString(9));

                    message.setAuthor(author);
                }
                
                messages.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(pst);
            close(connection);
        }

        return messages;

    }

    public Message insert(Message message) {

        Message resultMessage = null;

        Connection connection = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            connection = getConnection();

            pst = connection.prepareStatement(
                    "INSERT INTO messages (date, chat_id, user_id, text, message_type, custom_type)" +
                            " VALUES ((?), (?), (?), (?), (?), (?))",
                    Statement.RETURN_GENERATED_KEYS);
            pst.setObject(1, message.getDate());
            pst.setObject(2, message.getChatId());
            if (message.getAuthor() != null) {
                pst.setObject(3, message.getAuthor().getId());
            } else {
                pst.setObject(3, null);
            }
            pst.setObject(4, message.getBody());
            pst.setObject(5, message.getMessageType());
            pst.setObject(6, message.getCustomType());
            pst.executeUpdate();
            rs = pst.getGeneratedKeys();

            if (rs.next()) {
                resultMessage = new Message(
                        rs.getInt(1),
                        message.getDate(),
                        message.getChatId(),
                        message.getAuthor(),
                        message.getBody(),
                        message.getMessageType(),
                        message.getCustomType());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(pst);
            close(connection);
        }

        return resultMessage;

    }

}
