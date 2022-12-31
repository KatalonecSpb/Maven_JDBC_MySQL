package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private final Connection connection = Util.getConnection();
    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        try (Statement st = connection.createStatement()) {
            st.executeUpdate("CREATE TABLE IF NOT EXISTS mydbtest" +
                    "(id INT NOT NULL AUTO_INCREMENT," +
                    "name VARCHAR(50)," +
                    "lastname VARCHAR(50)," +
                    "age INT," +
                    "PRIMARY KEY (id))");
            System.out.println("Таблица создана");
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dropUsersTable() {
        try (Statement st = connection.createStatement()) {
            st.executeUpdate("DROP TABLE IF EXISTS  mydbtest");
            System.out.println("Таблица удалена");
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String sql = "INSERT INTO mydbtest(name, lastname, age) VALUES(?, ?, ?)";
        try (PreparedStatement pS = connection.prepareStatement(sql)) {
            pS.setString(1, name);
            pS.setString(2, lastName);
            pS.setByte(3, age);
            pS.executeUpdate();
            System.out.println("User с именем - " + name + " добавлен в базу данных");
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public void removeUserById(long id) {
        String sql = "DELETE FROM mydbtest(id) VALUES(?)";
        try (PreparedStatement pS = connection.prepareStatement(sql)) {
            pS.setLong(1, id);
            System.out.println("User удален");
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }

    }

    public List<User> getAllUsers() {
        List<User> allUsers = new ArrayList<>();
        try (Statement st = connection.createStatement()) {
            ResultSet rS = st.executeQuery("SELECT  id, name, lastName, age FROM mydbtest");
            connection.commit();
            while (rS.next()) {
                User user = new User();
                user.setId(rS.getLong("id"));
                user.setName(rS.getString("name"));
                user.setLastName(rS.getString("lastName"));
                user.setAge(rS.getByte("age"));
                allUsers.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
        return allUsers;
    }

    public void cleanUsersTable() {
        try (Statement st = connection.createStatement()) {
            st.executeUpdate("TRUNCATE mydbtest");
            System.out.println("Таблица очищена");
            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Не удалось очисить");
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
