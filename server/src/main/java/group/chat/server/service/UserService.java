package group.chat.server.service;

import group.chat.server.dao.UserDAO;
import group.chat.server.models.User;

/**
 * Сервис для работы с пользователями.
 */
public class UserService {

    private UserDAO userDAO;

    public UserService() {
        userDAO = new UserDAO();
    }

    public User findUser(int id) {
        return userDAO.find(id);
    }

    public User findUserByNickname(String nickname) {
        return userDAO.findByNickname(nickname);
    }

    public User addUser(User user) {
        return userDAO.insert(user);
    }

}
