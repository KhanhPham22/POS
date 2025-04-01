package dao;

import jakarta.persistence.*;
import model.Category;
import model.Product;

import java.util.List;

public class ProductDao implements GenericDao<Product> {

    private Class<Product> productClass;
    private EntityManager entityManager;

    public ProductDao(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.productClass = Product.class;
    }

    @Override
    public void setClass(Class<Product> classToSet) {
        this.productClass = classToSet;
    }

    @Override
    public Product findById(long id) throws Exception {
        // Truy vấn Product theo ID
        return entityManager.find(productClass, id);
    }

    @Override
    public List<Product> findAll() throws Exception {
        // Truy vấn tất cả Product
        String query = "SELECT p FROM Product p";
        TypedQuery<Product> typedQuery = entityManager.createQuery(query, productClass);
        return typedQuery.getResultList();
    }

    @Override
    public boolean create(Product entity) throws Exception {
        try {
            // Lưu Product mới
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
    public boolean update(Product entity) throws Exception {
        try {
            // Cập nhật Product
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
    public boolean delete(Product entity) throws Exception {
        try {
            // Xóa Product
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
            // Xóa Product theo ID
            Product product = findById(entityId);
            if (product != null) {
                return delete(product);
            }
            return false;
        } catch (Exception e) {
            throw e;
        }
    }

    // Tìm Product theo tên (unique)
    public Product findByName(String name) throws Exception {
        String query = "SELECT p FROM Product p WHERE p.name = :name";
        TypedQuery<Product> typedQuery = entityManager.createQuery(query, productClass);
        typedQuery.setParameter("name", name);
        List<Product> results = typedQuery.getResultList();
        return results.isEmpty() ? null : results.get(0); // Trả về null nếu không tìm thấy
    }

    // Tìm tất cả sản phẩm theo danh mục
    public List<Product> findByCategory(Category category) throws Exception {
        String query = "SELECT p FROM Product p WHERE p.category = :category";
        TypedQuery<Product> typedQuery = entityManager.createQuery(query, productClass);
        typedQuery.setParameter("category", category);
        return typedQuery.getResultList();
    }

    // Lấy tất cả sản phẩm có sẵn (status = true)
    public List<Product> findAvailableProducts() throws Exception {
        String query = "SELECT p FROM Product p WHERE p.status = true";
        TypedQuery<Product> typedQuery = entityManager.createQuery(query, productClass);
        return typedQuery.getResultList();
    }

    // Cập nhật số lượng sản phẩm sau mỗi đơn hàng
    public boolean updateProductQuantity(Long productId, Integer quantitySold) throws Exception {
        Product product = findById(productId);
        if (product != null && product.getQuantity() >= quantitySold) {
            product.setQuantity(product.getQuantity() - quantitySold);
            update(product);
            return true;
        }
        return false; // Nếu không đủ số lượng sản phẩm
    }
}

