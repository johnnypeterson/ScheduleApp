package dao;

import com.mysql.jdbc.Statement;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static util.DataBase.conn;

public class UserDao {

    private static final Logger logger = Logger.getLogger(UserDao.class.getName());

    public User getUser(String username, String password) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        List<User> users = new ArrayList<>();


        String sqlStatement = "SELECT * FROM user WHERE userName=" + "'" + username + "'" + "AND password=" + "'" + password + "'";
        System.out.println(sqlStatement);
        Statement statment = (Statement) conn.createStatement();
        ResultSet result = statment.executeQuery(sqlStatement);
        if(result.next()) {
            user.setUserName(result.getString("userName"));
            user.setPassword(result.getString("password"));
            user.setUserId(result.getInt("userId"));
        } else {
            return null;
        }
        return user;

    }
}
