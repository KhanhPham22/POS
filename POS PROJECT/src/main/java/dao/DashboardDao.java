package dao;

import model.Dashboard;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

public class DashboardDao implements GenericDao<Dashboard> {

    private Class<Dashboard> dashboardClass;
    private EntityManager entityManager;

    public DashboardDao(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.dashboardClass = Dashboard.class;
    }

    @Override
    public void setClass(Class<Dashboard> classToSet) {
        this.dashboardClass = classToSet;
    }

    @Override
    public Dashboard findById(long id) throws Exception {
        // Truy vấn Dashboard theo ID
        return entityManager.find(dashboardClass, id);
    }

    @Override
    public List<Dashboard> findAll() throws Exception {
        // Truy vấn tất cả Dashboard
        String query = "SELECT d FROM Dashboard d";
        TypedQuery<Dashboard> typedQuery = entityManager.createQuery(query, dashboardClass);
        return typedQuery.getResultList();
    }

    @Override
    public boolean create(Dashboard entity) throws Exception {
        try {
            // Lưu Dashboard mới
            entityManager.getTransaction().begin();
            entityManager.persist(entity);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e; // Hoặc xử lý lỗi theo cách của bạn
        }
    }

    @Override
    public boolean update(Dashboard entity) throws Exception {
        try {
            // Cập nhật Dashboard
            entityManager.getTransaction().begin();
            entityManager.merge(entity);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public boolean delete(Dashboard entity) throws Exception {
        try {
            // Xóa Dashboard
            entityManager.getTransaction().begin();
            entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public boolean deleteById(long entityId) throws Exception {
        try {
            // Xóa Dashboard theo ID
            Dashboard dashboard = findById(entityId);
            if (dashboard != null) {
                return delete(dashboard);
            }
            return false;
        } catch (Exception e) {
            throw e;
        }
    }

    // Tìm Dashboard theo các chỉ số (ví dụ: tổng doanh thu theo tháng)
    public List<Dashboard> findByMonthRevenue(Double monthRevenue) throws Exception {
        String query = "SELECT d FROM Dashboard d WHERE d.monthRevenue = :monthRevenue";
        TypedQuery<Dashboard> typedQuery = entityManager.createQuery(query, dashboardClass);
        typedQuery.setParameter("monthRevenue", monthRevenue);
        return typedQuery.getResultList();
    }

    // Tìm Dashboard theo thời gian (timestamp)
    public List<Dashboard> findByTimestamp(Date timestamp) throws Exception {
        String query = "SELECT d FROM Dashboard d WHERE d.timestamp = :timestamp";
        TypedQuery<Dashboard> typedQuery = entityManager.createQuery(query, dashboardClass);
        typedQuery.setParameter("timestamp", timestamp);
        return typedQuery.getResultList();
    }
}

