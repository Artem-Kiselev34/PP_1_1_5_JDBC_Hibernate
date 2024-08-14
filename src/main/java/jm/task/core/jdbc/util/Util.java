package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Util {
    // реализуйте настройку соеденения с БД
    private static final String DB_URL = "jdbc:mysql://localhost:3306/mydb";
    private static final String USER = "jpa_user";
    private static final String PWD = "jpa_pwd";
    private static final String DB_Driver = "com.mysql.cj.jdbc.Driver";

    private static Connection connection = null;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName(DB_Driver).getDeclaredConstructor().newInstance();
                connection = DriverManager.getConnection(DB_URL, USER, PWD);
                System.out.println("Успешное соединение с БД");
            } catch (ClassNotFoundException | InstantiationException |
                     IllegalAccessException | NoSuchMethodException | SQLException e) {
                System.out.println("Ошибка при установке соединения с БД: " + e.getMessage());
            } catch (InvocationTargetException e) {
                System.out.println("Ошибка при создании драйвера: " + e.getCause().getMessage());
            }
        }
        return connection;
    }


    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();
                Properties properties = new Properties();
                properties.setProperty(Environment.URL, DB_URL);
                properties.setProperty(Environment.DRIVER, DB_Driver);
                properties.setProperty(Environment.USER, USER);
                properties.setProperty(Environment.PASS, PWD);
                properties.setProperty(Environment.DIALECT, "org.hibernate.dialect.MySQLDialect");
                properties.setProperty(Environment.SHOW_SQL, "true");
                properties.setProperty(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");

                configuration.setProperties(properties);
                configuration.addAnnotatedClass(User.class);

                sessionFactory = configuration.buildSessionFactory();
                System.out.println("SessionFactory успешно создан");
            } catch (Exception e) {
                System.out.println("Ошибка создания SessionFactory: " + e.getMessage());
            }
        }
        return sessionFactory;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Соединение с базой данных успешно закрыто");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public static void closeSessionFactory() {
        sessionFactory.close();
        System.out.println("Закрытие SessionFactory");
    }
}