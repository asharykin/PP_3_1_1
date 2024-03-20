package web.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import web.model.User;

import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public void addUser(User user) {
        TypedQuery<Integer> maxIdQuery = entityManager.createQuery("SELECT MAX(u.id) FROM User u", Integer.class);
        Integer maxId = maxIdQuery.getSingleResult();
        User newUser = new User(user.getName(), user.getLastName(), user.getAge());
        newUser.setId(maxId == null ? 1 : maxId + 1);
        entityManager.persist(newUser);
    }

    @Transactional
    @Override
    public void updateUser(User user) {
        entityManager.merge(user);
    }

    @Transactional
    @Override
    public void deleteUser(int id) {
        User user = entityManager.find(User.class, id);
        if (user != null) {
            entityManager.remove(user);
            entityManager.createNativeQuery("UPDATE users SET id = id - 1 WHERE id > :id").setParameter("id", id).executeUpdate();
        }
    }

    @Transactional
    @Override
    public List<User> getAllUsers() {
        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u", User.class);
        return query.getResultList();
    }

    @Transactional
    @Override
    public User getUserById(int id) {
        return entityManager.find(User.class, id);
    }
}
