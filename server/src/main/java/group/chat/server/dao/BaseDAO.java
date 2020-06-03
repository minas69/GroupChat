package group.chat.server.dao;

import java.sql.*;

/**
 * Базовый Data Access Object (DAO) класс для работы с базой данных.
 */
class BaseDAO {

    private static final String URL_SOURCE =
                            "jdbc:mysql://172.17.0.2:3306/groupchat?" +
                            "useUnicode=true" +
                            "&useJDBCCompliantTimezoneShift=true" +
                            "&useLegacyDatetimeCode=false" +
                            "&serverTimezone=UTC" +
                            "&useSSL=false" +
                            "&allowPublicKeyRetrieval=true";
    private static final String USERNAME = "dmitry";
    private static final String PASSWORD = "password";

    BaseDAO() {
        String sqlChatsCreate =
                "CREATE TABLE IF NOT EXISTS chats (\n" +
                "  id int NOT NULL AUTO_INCREMENT,\n" +
                "  title char(255) NOT NULL,\n" +
                "  description char(255) NOT NULL,\n" +
                "  members_count int NOT NULL,\n" +
                "  color char(16) NOT NULL,\n" +
                "  is_ephemeral bit(1) NOT NULL,\n" +
                "  last_message_id int DEFAULT NULL,\n" +
                "  admin_id int NOT NULL,\n" +
                "  PRIMARY KEY (id),\n" +
                "  KEY last_message_id (last_message_id),\n" +
                "  KEY admin_id (admin_id),\n" +
                "  CONSTRAINT chats_constrain_1 FOREIGN KEY (admin_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE\n" +
                ") ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;";

        String sqlMembersCreate =
                "CREATE TABLE IF NOT EXISTS members (\n" +
                "  id int NOT NULL AUTO_INCREMENT,\n" +
                "  chat_id int NOT NULL,\n" +
                "  user_id int NOT NULL,\n" +
                "  PRIMARY KEY (id),\n" +
                "  KEY chat_id (chat_id),\n" +
                "  KEY user_id (user_id),\n" +
                "  CONSTRAINT members_constrain_1 FOREIGN KEY (chat_id) REFERENCES chats (id) ON DELETE CASCADE ON UPDATE CASCADE,\n" +
                "  CONSTRAINT members_constrain_2 FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE\n" +
                ") ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n";

        String sqlMessagesCreate =
                "CREATE TABLE IF NOT EXISTS messages (\n" +
                "  id int NOT NULL AUTO_INCREMENT,\n" +
                "  date bigint unsigned NOT NULL,\n" +
                "  chat_id int NOT NULL,\n" +
                "  user_id int DEFAULT NULL,\n" +
                "  text text NOT NULL,\n" +
                "  message_type char(16) NOT NULL,\n" +
                "  custom_type char(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,\n" +
                "  PRIMARY KEY (id),\n" +
                "  KEY chat_id (chat_id),\n" +
                "  KEY user_id (user_id),\n" +
                "  CONSTRAINT messages_constrain_1 FOREIGN KEY (chat_id) REFERENCES chats (id) ON DELETE CASCADE ON UPDATE CASCADE,\n" +
                "  CONSTRAINT messages_constrain_2 FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE\n" +
                ") ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n";

        String sqlUsersCreate =
                "CREATE TABLE IF NOT EXISTS users (\n" +
                "  id int NOT NULL AUTO_INCREMENT,\n" +
                "  nickname char(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,\n" +
                "  email char(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,\n" +
                "  password char(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,\n" +
                "  color char(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,\n" +
                "  PRIMARY KEY (id)\n" +
                ") ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;";

        String sqlAddConstrain = "ALTER TABLE chats ADD CONSTRAINT chats_constrain_1 FOREIGN KEY (last_message_id) REFERENCES messages (id) ON DELETE CASCADE ON UPDATE CASCADE;";

        Connection conn = null;
        Statement stmt = null;
        try {
            conn = getConnection();
            stmt = conn.createStatement();
            stmt.execute(sqlUsersCreate);
            stmt.execute(sqlChatsCreate);
            stmt.execute(sqlMembersCreate);
            stmt.execute(sqlMessagesCreate);
            stmt.execute(sqlAddConstrain);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn);
            close(stmt);
        }
    }

    /**
     * Инициализировать соединение с базой данных.
     * @return Connection объект, с помощью которого можно выполнять SQL запросы
     */
    Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(URL_SOURCE, USERNAME, PASSWORD);
    }

    void close(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (Exception exObj) {
            exObj.printStackTrace();
        }
    }

    void close(Statement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (Exception exObj) {
            exObj.printStackTrace();
        }
    }

    void close(ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (Exception exObj) {
            exObj.printStackTrace();
        }
    }

}
