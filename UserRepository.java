package com.example.spotdifference.repository;

import com.example.spotdifference.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class UserRepository {
    
    @Autowired
    private SessionFactory sessionFactory;
    
    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }
    
    public void save(User user) {
        getSession().saveOrUpdate(user);
    }
    
    public User findByUsername(String username) {
        Query<User> query = getSession().createQuery("FROM User WHERE username = :username", User.class);
        query.setParameter("username", username);
        try {
            return query.uniqueResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    public boolean existsByUsername(String username) {
        Query<Long> query = getSession().createQuery("SELECT COUNT(*) FROM User WHERE username = :username", Long.class);
        query.setParameter("username", username);
        return query.uniqueResult() > 0;
    }
    
    public void update(User user) {
        getSession().update(user);
    }
}