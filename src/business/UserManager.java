package business;

import dao.UserDao;
import entity.User;

import java.sql.SQLException;

public class UserManager {
    private final UserDao userDao;

    public UserManager() throws SQLException {
        this.userDao = new UserDao();
    }
public User findByLogin(String user_name, String password) {
        //Farklı işlemler yapıbiliriz.
       return this.userDao.findByLogin(user_name,password);
}
}
