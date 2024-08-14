package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import org.hibernate.*;

import java.util.Collections;
import java.util.List;


public class UserDaoHibernateImpl implements UserDao {
    private final SessionFactory sessionFactory = Util.getSessionFactory();

    public UserDaoHibernateImpl() {
    }

    public void createUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            Query query = session
                    .createSQLQuery("CREATE TABLE IF NOT EXISTS users " +
                                    "(id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                                    "name VARCHAR(255) NOT NULL, lastName VARCHAR(255) NOT NULL, " +
                                    "age TINYINT NOT NULL) " +
                                    "AUTO_INCREMENT=1")
                    .addEntity(User.class);
            query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            System.out.println("Ошибка при создании таблицы пользователей: " + e.getMessage());
        }
    }

    public void dropUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Query query = session.createSQLQuery("DROP TABLE IF EXISTS users").addEntity(User.class);
            query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            System.out.println("Ошибка при удалении таблицы пользователей: " + e.getMessage());
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        Transaction userTransaction = null;
        try (Session session = sessionFactory.openSession()) {
            userTransaction = session.beginTransaction();
            User newUser = new User(name, lastName, age);
            session.persist(newUser);
            userTransaction.commit();
        } catch (Exception e) {
            System.out.println("Ошибка при сохранении пользователя : " + e.getMessage());
            if (userTransaction != null && userTransaction.isActive()) {
            userTransaction.rollback();
            }
        }
    }

    public void removeUserById(long id) {
        Transaction userTransaction = null;
        try (Session session = sessionFactory.openSession()) {
            userTransaction = session.beginTransaction();
            User userToRemove = session.get(User.class, id);
            if (userToRemove != null) {
                session.remove(userToRemove);
            }
            userTransaction.commit();
        } catch (Exception e) {
            System.out.println("Ошибка при удалении пользователя с id " + id + ": " + e.getMessage());
            if (userTransaction != null && userTransaction.isActive()) {
                userTransaction.rollback();
            }
        }
    }

//    public void removeUserById(long id) {
//        Transaction transaction = null;
//        try (Session session = sessionFactory.openSession()) {
//            Transaction userTransaction = session.beginTransaction();
//            User userToRemove = session.get(User.class, id);
//            if (userToRemove != null) {
//                session.remove(userToRemove);
//            }
//            userTransaction.commit();
//        } catch (Exception e) {
//            System.out.println("Ошибка при удалении пользователя с id " + id + ": " + e.getMessage());
//            if (transaction != null && transaction.isActive()) {
//                transaction.rollback();
//            }
//        }
//    }

    public List<User> getAllUsers() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from User", User.class)
                    .getResultList();
        } catch (Exception e) {
            System.out.println("Ошибка при получении списка пользователей: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public void cleanUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createQuery("delete User").executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Ошибка при очистке таблицы пользователей: " + e.getMessage());
        }
    }
}
