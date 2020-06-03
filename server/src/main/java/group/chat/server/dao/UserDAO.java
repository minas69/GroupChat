package group.chat.server.dao;

import group.chat.server.models.User;

import java.sql.*;

/**
 * DAO для объектов класса {@link User}.
 */
public class UserDAO extends BaseDAO {

    public User find(int id) {

        User user = null;

        Connection connection = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            connection = getConnection();

            pst = connection.prepareStatement("SELECT * FROM users WHERE id = (?)");
            pst.setInt(1, id);
            rs = pst.executeQuery();

            if (rs.next()) {
                user = new User(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(pst);
            close(connection);
        }

        return user;

    }

    public User findByNickname(String nickname) {

        User user = null;

        Connection connection = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            connection = getConnection();

            pst = connection.prepareStatement("SELECT * FROM users WHERE nickname = (?)");
            pst.setString(1, nickname);
            rs = pst.executeQuery();

            if (rs.next()) {
                user = new User(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(pst);
            close(connection);
        }

        return user;

    }

    public User insert(User user) {

        User resultUser = null;

        Connection connection = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            connection = getConnection();

            pst = connection.prepareStatement(
                    "INSERT INTO users (nickname, email,  password, color)" +
                            " VALUES ((?), (?), (?), (?))",
                    Statement.RETURN_GENERATED_KEYS);
            pst.setObject(1, user.getCredentials().getNickname());
            pst.setObject(2, user.getEmail());
            pst.setObject(3, user.getCredentials().getPassword());
            pst.setObject(4, user.getColor());
            pst.executeUpdate();
            rs = pst.getGeneratedKeys();

            if (rs.next()) {
                resultUser = new User(
                        rs.getInt(1),
                        user.getCredentials().getNickname(),
                        user.getEmail(),
                        user.getCredentials().getPassword(),
                        user.getColor());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(pst);
            close(connection);
        }

        return resultUser;

    }

}
