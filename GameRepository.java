package com.example.spotdifference.repository;

import com.example.spotdifference.model.DifferenceGame;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class GameRepository {
    
    @Autowired
    private SessionFactory sessionFactory;
    
    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }
    
    public void deleteAll() {
        // حذف جميع الاختلافات أولاً (بسبب العلاقة)
        getSession().createQuery("DELETE FROM Difference").executeUpdate();
        // ثم حذف جميع الألعاب
        getSession().createQuery("DELETE FROM DifferenceGame").executeUpdate();
        System.out.println("🗑️ All games and differences deleted");
    }
    
    public void save(DifferenceGame game) {
        getSession().saveOrUpdate(game);
    }
    
    public DifferenceGame findByLevelAndGameNumber(Integer level, Integer gameNumber) {
        try {
            String hql = "FROM DifferenceGame WHERE level = :level AND gameNumber = :gameNumber";
            Query<DifferenceGame> query = getSession().createQuery(hql, DifferenceGame.class);
            query.setParameter("level", level);
            query.setParameter("gameNumber", gameNumber);
            query.setMaxResults(1);
            
            DifferenceGame result = query.uniqueResult();
            System.out.println("🔍 DB Query: level=" + level + ", gameNumber=" + gameNumber + " -> " + (result != null ? "found" : "not found"));
            return result;
        } catch (Exception e) {
            System.err.println("Error in findByLevelAndGameNumber: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    public long count() {
        Query<Long> query = getSession().createQuery("SELECT COUNT(*) FROM DifferenceGame", Long.class);
        return query.uniqueResult();
    }
}