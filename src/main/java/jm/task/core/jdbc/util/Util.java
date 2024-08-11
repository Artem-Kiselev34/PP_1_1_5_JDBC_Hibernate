package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import java.util.Properties;

public class Util {
    // реализуйте настройку соеденения с БД
    private static final String DB_URL = "jdbc:mysql://localhost:3306/mydb";
    private static final String USER = "jpa_user";
    private static final String PWD = "jpa_pwd";
    private static final String DB_Driver = "com.mysql.cj.jdbc.Driver";


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
}