package dao;

import model.Employee;
import jakarta.persistence.*;
import java.util.List;

public class EmployeeDao implements GenericDao<Employee> {

    private Class<Employee> employeeClass;
    private EntityManager entityManager;

    public EmployeeDao(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.employeeClass = Employee.class;
    }

    @Override
    public void setClass(Class<Employee> classToSet) {
        this.employeeClass = classToSet;
    }

    @Override
    public Employee findById(long id) throws Exception {
        // Truy vấn Employee theo ID
        return entityManager.find(employeeClass, id);
    }

    @Override
    public List<Employee> findAll() throws Exception {
        // Truy vấn tất cả Employee
        String query = "SELECT e FROM Employee e";
        TypedQuery<Employee> typedQuery = entityManager.createQuery(query, employeeClass);
        return typedQuery.getResultList();
    }

    @Override
    public boolean create(Employee entity) throws Exception {
        try {
            // Lưu Employee mới
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
    public boolean update(Employee entity) throws Exception {
        try {
            // Cập nhật Employee
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
    public boolean delete(Employee entity) throws Exception {
        try {
            // Xóa Employee
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
            // Xóa Employee theo ID
            Employee employee = findById(entityId);
            if (employee != null) {
                return delete(employee);
            }
            return false;
        } catch (Exception e) {
            throw e;
        }
    }

    // Tìm Employee theo email (unique)
    public Employee findByEmail(String email) throws Exception {
        String query = "SELECT e FROM Employee e WHERE e.email = :email";
        TypedQuery<Employee> typedQuery = entityManager.createQuery(query, employeeClass);
        typedQuery.setParameter("email", email);
        List<Employee> results = typedQuery.getResultList();
        return results.isEmpty() ? null : results.get(0); // Trả về Employee đầu tiên nếu có
    }
}

