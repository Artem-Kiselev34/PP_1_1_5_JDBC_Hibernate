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

    //Создание таблицы для User(ов) — не должно приводить к исключению, если такая таблица уже существует
    public void createUsersTable() {
        String sql = """
                CREATE TABLE IF NOT Exists Users
                (
                    Id INT AUTO_INCREMENT PRIMARY KEY,
                    Name VARCHAR(20),
                    LastName VARCHAR(20),
                    Age TINYINT
                );""";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println("Ошибка при выполнении SQL-запроса для создания таблицы Users " + e.getMessage());
        }
    }

    //Удаление таблицы User(ов) — не должно приводить к исключению, если таблицы не существует -
    public void dropUsersTable() {
        String sql = "DROP TABLE IF EXISTS Users;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println("Ошибка при выполнении SQL-запроса для удаления таблицы Users " + e.getMessage());
        }
    }

    //    Добавление User в таблицу
    public void saveUser(String name, String lastName, byte age) {
        String sql = "INSERT INTO Users ( Name, LastName, Age) VALUES (?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
            connection.setAutoCommit(false);
            connection.commit();
            System.out.printf("User с именем — %s добавлен(а) в базу данных", name);
            System.out.println();

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            System.out.println("Ошибка при выполнении SQL-запроса для сохранения пользователя " + e.getMessage());
        }
    }

    // Удаление User из таблицы (по id)
    public void removeUserById(long id) {
        String sql = "DELETE FROM Users WHERE Id=?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            connection.setAutoCommit(false);
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            System.out.println("Ошибка при выполнении SQL-запроса для удаления пользователя " + e.getMessage());
        }
    }

    //Получение всех User(ов) из таблицы +
    public List<User> getAllUsers() {
        List<User> usersList = new ArrayList<>();
        String sql = "SELECT Id,Name,LastName,Age FROM Users";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("Id"));
                user.setName(resultSet.getString("Name"));
                user.setLastName(resultSet.getString("LastName"));
                user.setAge(resultSet.getByte("Age"));
                usersList.add(user);
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при выполнении SQL-запроса для получения всех пользователей " + e.getMessage());
        }
        return usersList;
    }

    //Очистка содержания таблицы
    public void cleanUsersTable() {
        String sql = "TRUNCATE TABLE Users;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println("Ошибка при выполнении SQL-запроса для очистки таблицы Users " + e.getMessage());
        }
    }
}













//package jm.task.core.jdbc.dao;
//
//import jm.task.core.jdbc.model.User;
//import jm.task.core.jdbc.util.Util;
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//
//public class UserDaoJDBCImpl implements UserDao {
//    private final Connection connection = Util.getConnection();
//
//    public UserDaoJDBCImpl() {
//    }
//
//    //Создание таблицы для User(ов) — не должно приводить к исключению, если такая таблица уже существует
//    @Override
//    public void createUsersTable() {
//        String sql = """
//                CREATE TABLE IF NOT Exists Users
//                (
//                    Id INT AUTO_INCREMENT PRIMARY KEY,
//                    Name VARCHAR(20),
//                    LastName VARCHAR(20),
//                    Age TINYINT
//                );""";
//        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//            preparedStatement.execute();
//        } catch (SQLException e) {
//            System.out.println("Ошибка при выполнении SQL-запроса для создания таблицы Users " + e.getMessage());
//        }
//    }
//
//    //Удаление таблицы User(ов) — не должно приводить к исключению, если таблицы не существует
//    @Override
//    public void dropUsersTable() {
//        String sql = "DROP TABLE IF EXISTS Users;";
//        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//            preparedStatement.execute();
//        } catch (SQLException e) {
//            System.out.println("Ошибка при выполнении SQL-запроса для удаления таблицы Users " + e.getMessage());
//        }
//    }
//
//    //Добавление User в таблицу
//    @Override
//    public void saveUser(String name, String lastName, byte age) {
//        String sql = "INSERT INTO Users ( Name, LastName, Age) VALUES (?,?,?)";
//        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//            preparedStatement.setString(1, name);
//            preparedStatement.setString(2, lastName);
//            preparedStatement.setByte(3, age);
//            preparedStatement.executeUpdate();
//            System.out.printf("User с именем — %s добавлен(а) в базу данных", name);
//            System.out.println();
//
//        } catch (SQLException e) {
//            System.out.println("Ошибка при выполнении SQL-запроса для сохранения пользователя " + e.getMessage());
//
//        }
//    }
//
//    // Удаление User из таблицы (по id)
//    @Override
//    public void removeUserById(long id) {
//        String sql = "DELETE FROM Users WHERE Id=?;";
//        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//            preparedStatement.setLong(1, id);
//            preparedStatement.executeUpdate();
//        } catch (SQLException e) {
//            System.out.println("Ошибка при выполнении SQL-запроса для удаления пользователя " + e.getMessage());
//        }
//    }
//
//    //Получение всех User(ов) из таблицы
//    @Override
//    public List<User> getAllUsers() {
//        List<User> usersList = new ArrayList<>();
//        String sql = "SELECT Id,Name,LastName,Age FROM Users";
//        try (Statement statement = connection.createStatement();
//             ResultSet resultSet = statement.executeQuery(sql)) {
//            while (resultSet.next()) {
//                User user = new User();
//                user.setId(resultSet.getLong("Id"));
//                user.setName(resultSet.getString("Name"));
//                user.setLastName(resultSet.getString("LastName"));
//                user.setAge(resultSet.getByte("Age"));
//                usersList.add(user);
//            }
//        } catch (SQLException e) {
//            System.out.println("Ошибка при выполнении SQL-запроса для получения всех пользователей " + e.getMessage());
//        }
//        return usersList;
//    }
//
//    //Очистка содержания таблицы
//    @Override
//    public void cleanUsersTable() {
//        String sql = "TRUNCATE TABLE Users;";
//        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//            preparedStatement.execute();
//        } catch (SQLException e) {
//            System.out.println("Ошибка при выполнении SQL-запроса для очистки таблицы Users " + e.getMessage());
//        }
//    }
//}

