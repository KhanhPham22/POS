package dao;

import jakarta.persistence.*;
import model.Payroll;

import java.util.List;

public class PayrollDao implements GenericDao<Payroll> {

    private Class<Payroll> payrollClass;
    private EntityManager entityManager;

    public PayrollDao(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.payrollClass = Payroll.class;
    }

    @Override
    public void setClass(Class<Payroll> classToSet) {
        this.payrollClass = classToSet;
    }

    @Override
    public Payroll findById(long id) throws Exception {
        // Truy vấn Payroll theo ID
        return entityManager.find(payrollClass, id);
    }

    @Override
    public List<Payroll> findAll() throws Exception {
        // Truy vấn tất cả Payroll
        String query = "SELECT p FROM Payroll p";
        TypedQuery<Payroll> typedQuery = entityManager.createQuery(query, payrollClass);
        return typedQuery.getResultList();
    }

    @Override
    public boolean create(Payroll entity) throws Exception {
        try {
            // Lưu Payroll mới
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
    public boolean update(Payroll entity) throws Exception {
        try {
            // Cập nhật Payroll
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
    public boolean delete(Payroll entity) throws Exception {
        try {
            // Xóa Payroll
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
            // Xóa Payroll theo ID
            Payroll payroll = findById(entityId);
            if (payroll != null) {
                return delete(payroll);
            }
            return false;
        } catch (Exception e) {
            throw e;
        }
    }

    // Tìm Payroll theo Employee ID
    public List<Payroll> findByEmployeeId(long employeeId) throws Exception {
        String query = "SELECT p FROM Payroll p WHERE p.employee.employeeId = :employeeId";
        TypedQuery<Payroll> typedQuery = entityManager.createQuery(query, payrollClass);
        typedQuery.setParameter("employeeId", employeeId);
        return typedQuery.getResultList();
    }

    // Tính tổng lương của tất cả nhân viên
    public Double getTotalSalary() throws Exception {
        String query = "SELECT SUM(p.totalSalary) FROM Payroll p";
        TypedQuery<Double> typedQuery = entityManager.createQuery(query, Double.class);
        return typedQuery.getSingleResult();
    }
}

