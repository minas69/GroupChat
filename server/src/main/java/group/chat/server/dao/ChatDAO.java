package group.chat.server.dao;

import group.chat.server.models.Chat;
import group.chat.server.models.Member;
import group.chat.server.models.Message;
import group.chat.server.models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO для объектов класса {@link Chat}.
 */
public class ChatDAO extends BaseDAO {

    public List<Chat> findAll() {

        List<Chat> chats = new ArrayList<>();

        Connection connection = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            connection = getConnection();

            pst = connection.prepareStatement("SELECT chats.id, " +
                    "chats.title, " +
                    "chats.description, " +
                    "chats.members_count, " +
                    "chats.color, " +
                    "chats.is_ephemeral, " +
                    "users.id, " +
                    "users.nickname, " +
                    "users.color, " +
                    "messages.id, " +
                    "messages.date, " +
                    "messages.text, " +
                    "messages.message_type, " +
                    "messages.custom_type, " +
                    "users.id, " +
                    "users.nickname, " +
                    "users.color " +
                    "FROM chats " +
                    "LEFT JOIN messages ON chats.last_message_id=messages.id " +
                    "LEFT JOIN users ON messages.user_id=users.id");
            rs = pst.executeQuery();

            while (rs.next()) {
                String messageType = rs.getString(13);

                Message message = null;
                int messageId = rs.getInt(10);
                if (messageId != 0) {
                    message = new Message();
                    message.setId(messageId);
                    message.setChatId(rs.getInt(1));
                    message.setDate(rs.getLong(11));
                    message.setBody(rs.getString(12));
                    message.setMessageType(messageType);
                    message.setCustomType(rs.getString(14));

                    if (messageType.equals(Message.TEXT)) {
                        Member author = new Member();
                        author.setId(rs.getInt(15));
                        author.setNickName(rs.getString(16));
                        author.setColor(rs.getString(17));

                        message.setAuthor(author);
                    }
                }

                Member admin = new Member();
                admin.setId(rs.getInt(7));
                admin.setNickName(rs.getString(8));
                admin.setColor(rs.getString(9));
                
                Chat chat = new Chat();
                chat.setId(rs.getInt(1));
                chat.setTitle(rs.getString(2));
                chat.setDescription(rs.getString(3));
                chat.setMembersCount(rs.getInt(4));
                chat.setColor(rs.getString(5));
                chat.setEphemeral(rs.getBoolean(6));
                chat.setLastMessage(message);
                chat.setAdmin(admin);
                
                chats.add(chat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(pst);
            close(connection);
        }

        return chats;

    }

    public Chat find(int id) {
        Chat chat = null;

        Connection connection = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            connection = getConnection();

            pst = connection.prepareStatement("SELECT " +
                    "chats.title, " +
                    "chats.description, " +
                    "chats.members_count, " +
                    "chats.color, " +
                    "chats.is_ephemeral, " +
                    "users.id, " +
                    "users.nickname ," +
                    "users.color, " +
                    "messages.id, " +
                    "messages.date, " +
                    "messages.text, " +
                    "messages.message_type, " +
                    "messages.custom_type, " +
                    "users.id, " +
                    "users.nickname, " +
                    "users.color " +
                    "FROM chats " +
                    "LEFT JOIN messages ON chats.last_message_id=messages.id " +
                    "LEFT JOIN users ON messages.user_id=users.id " +
                    "WHERE chats.id = (?)");
            pst.setInt(1, id);
            rs = pst.executeQuery();

            if (rs.next()) {
                String messageType = rs.getString(12);

                Message message = null;
                int messageId = rs.getInt(9);
                if (messageId != 0) {
                    message = new Message();
                    message.setId(messageId);
                    message.setChatId(id);
                    message.setDate(rs.getLong(10));
                    message.setBody(rs.getString(11));
                    message.setMessageType(messageType);
                    message.setCustomType(rs.getString(13));

                    if (messageType.equals(Message.TEXT)) {
                        Member author = new Member();
                        author.setId(rs.getInt(14));
                        author.setNickName(rs.getString(15));
                        author.setColor(rs.getString(16));

                        message.setAuthor(author);
                    }
                }

                Member admin = new Member();
                admin.setId(rs.getInt(6));
                admin.setNickName(rs.getString(7));
                admin.setColor(rs.getString(8));

                chat = new Chat();
                chat.setId(id);
                chat.setTitle(rs.getString(1));
                chat.setDescription(rs.getString(2));
                chat.setMembersCount(rs.getInt(3));
                chat.setColor(rs.getString(4));
                chat.setEphemeral(rs.getBoolean(5));
                chat.setLastMessage(message);
                chat.setAdmin(admin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(pst);
            close(connection);
        }

        return chat;
    }

    public Chat insert(Chat chat) {

        Chat resultChat = null;

        Connection connection = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            connection = getConnection();

            pst = connection.prepareStatement(
                    "INSERT INTO chats (title, description, last_message_id, members_count, " +
                            "color, is_ephemeral, admin_id) " +
                            "VALUES ((?), (?), (?), (?), (?), (?), (?))",
                    Statement.RETURN_GENERATED_KEYS);
            pst.setObject(1, chat.getTitle());
            pst.setObject(2, chat.getDescription());
            if (chat.getLastMessage() != null) {
                pst.setObject(3, chat.getLastMessage().getId());
            } else {
                pst.setObject(3, null);
            }
            pst.setObject(4, chat.getMembersCount());
            pst.setObject(5, chat.getColor());
            pst.setObject(6, chat.isEphemeral());
            pst.setObject(7, chat.getAdmin().getId());
            pst.executeUpdate();
            rs = pst.getGeneratedKeys();

            if (rs.next()) {
                resultChat = new Chat();
                resultChat.setId(rs.getInt(1));
                resultChat.setTitle(chat.getTitle());
                resultChat.setDescription(chat.getDescription());
                resultChat.setLastMessage(chat.getLastMessage());
                resultChat.setMembersCount(chat.getMembersCount());
                resultChat.setColor(chat.getColor());
                resultChat.setEphemeral(chat.isEphemeral());
                resultChat.setAdmin(chat.getAdmin());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(pst);
            close(connection);
        }

        return resultChat;

    }

    public void updateLastMessage(Message lastMessage) {

        Connection connection = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            connection = getConnection();

            pst = connection.prepareStatement(
                    "UPDATE chats SET last_message_id = ? WHERE id = ?");
            pst.setObject(1, lastMessage.getId());
            pst.setObject(2, lastMessage.getChatId());
            pst.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(pst);
            close(connection);
        }

    }

    public boolean addUserToChat(Chat chat, User user) {

        boolean result = false;

        Connection connection = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            connection = getConnection();

            pst = connection.prepareStatement(
                    "INSERT INTO members (chat_id, user_id) VALUES ((?), (?))",
                    Statement.RETURN_GENERATED_KEYS);
            pst.setObject(1, chat.getId());
            pst.setObject(2, user.getId());

            if (pst.executeUpdate() != 0) {
                result = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(pst);
            close(connection);
        }

        return result;

    }

    public boolean isUserJoined(Chat chat, User user) {

        boolean result = false;

        Connection connection = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            connection = getConnection();

            pst = connection.prepareStatement(
                    "SELECT members.id FROM members WHERE user_id = (?) AND chat_id = (?)");
            pst.setObject(1, user.getId());
            pst.setObject(2, chat.getId());
            rs = pst.executeQuery();

            if (rs.next()) {
                result = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(pst);
            close(connection);
        }

        return result;

    }

    public void setMembersCount(Chat chat, int membersCount) {

        Connection connection = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            connection = getConnection();

            pst = connection.prepareStatement(
                    "UPDATE chats SET members_count = ? WHERE id = ?");
            pst.setObject(1, membersCount);
            pst.setObject(2, chat.getId());
            pst.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(pst);
            close(connection);
        }

    }

}
