package com.example.spotdifference.repository;

import com.example.spotdifference.model.Difference;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
@Transactional
public class DifferenceRepository {
    
    @Autowired
    private SessionFactory sessionFactory;
    
    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }
    
    public void save(Difference difference) {
        getSession().saveOrUpdate(difference);
    }
    
    // ✅ التصحيح: استخدام Query مع type parameter
    public Difference findByGameAndCoordinates(Long gameId, int x, int y, int tolerance) {
        try {
            String hql = "FROM Difference d WHERE d.game.id = :gameId " +
                        "AND ABS(d.x - :x) <= :tolerance " +
                        "AND ABS(d.y - :y) <= :tolerance";
            
            Query<Difference> query = getSession().createQuery(hql, Difference.class);
            query.setParameter("gameId", gameId);
            query.setParameter("x", x);
            query.setParameter("y", y);
            query.setParameter("tolerance", tolerance);
            query.setMaxResults(1);
            
            List<Difference> results = query.getResultList();
            return results.isEmpty() ? null : results.get(0);
        } catch (Exception e) {
            System.err.println("Error in findByGameAndCoordinates: " + e.getMessage());
            return null;
        }
    }
    
    // ✅ دالة للبحث عن جميع الاختلافات في لعبة معينة
    public List<Difference> findByGameId(Long gameId) {
        try {
            String hql = "FROM Difference d WHERE d.game.id = :gameId";
            Query<Difference> query = getSession().createQuery(hql, Difference.class);
            query.setParameter("gameId", gameId);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error in findByGameId: " + e.getMessage());
            return null;
        }
    }
    
    // ✅ حذف جميع الاختلافات (لإعادة التهيئة)
    public void deleteAll() {
        getSession().createQuery("DELETE FROM Difference").executeUpdate();
    }
}