package dao;

import java.util.List;

import model.Dashboard;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import util.HibernateUtil;

public class DashboardDao implements GenericDao<Dashboard> {

    private static final Logger Log = LogManager.getLogger(DashboardDao.class);

    private SessionFactory sessionFactory;
    private Class<Dashboard> Dashboard;

    public DashboardDao() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public void setClass(Class<Dashboard> Dashboard) {
        this.Dashboard = Dashboard;
    }

    @Override
    public boolean create(Dashboard dashboard) throws Exception {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(dashboard);
            transaction.commit();
            Log.info("Dashboard persisted in database successfully");
            return true;
        } catch (Exception e) {
            Log.error("Database error while persisting Dashboard", e);
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public Dashboard findById(long id) throws Exception {
        Session session = sessionFactory.openSession();
        try {
            Dashboard dashboard = session.get(Dashboard.class, id);
            Log.info("Dashboard with id: " + id + " retrieved successfully from database");
            return dashboard;
        } catch (Exception e) {
            Log.error("Database error while retrieving Dashboard", e);
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public List<Dashboard> findAll() throws Exception {
        Session session = sessionFactory.openSession();
        try {
            List<Dashboard> dashboards = session.createQuery("from Dashboard", Dashboard.class).list();
            Log.info("All Dashboards retrieved successfully from database");
            return dashboards;
        } catch (Exception e) {
            Log.error("Database error while retrieving all Dashboards", e);
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean update(Dashboard dashboard) throws Exception {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(dashboard);
            transaction.commit();
            Log.info("Dashboard updated in database successfully");
            return true;
        } catch (Exception e) {
            Log.error("Database error while updating Dashboard", e);
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean deleteById(long id) throws Exception {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Dashboard dashboard = session.get(Dashboard.class, id);
            session.delete(dashboard);
            transaction.commit();
            Log.info("Dashboard deleted from database successfully");
            return true;
        } catch (Exception e) {
            Log.error("Database error while deleting Dashboard", e);
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean delete(Dashboard dashboard) throws Exception {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.delete(dashboard);
            transaction.commit();
            Log.info("Dashboard deleted from database successfully");
            return true;
        } catch (Exception e) {
            Log.error("Database error while deleting Dashboard", e);
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }
    
    public Dashboard findLatestDashboardByStoreId(Long storeId) throws Exception {
        Session session = sessionFactory.openSession();
        try {
            String hql = "FROM Dashboard WHERE storeId = :storeId ORDER BY timestamp DESC";
            Dashboard latestDashboard = session.createQuery(hql, Dashboard.class)
                    .setParameter("storeId", storeId)
                    .setMaxResults(1)
                    .uniqueResult();
            Log.info("Latest dashboard retrieved for storeId: " + storeId);
            return latestDashboard;
        } catch (Exception e) {
            Log.error("Error retrieving latest dashboard for storeId: " + storeId, e);
            throw e;
        } finally {
            session.close();
        }
    }

}
