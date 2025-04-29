package dao;

import model.Product;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import util.HibernateUtil;

import java.util.List;

public class ProductDao implements GenericDao<Product> {

    private static final Logger Log = LogManager.getLogger(ProductDao.class);

    private SessionFactory sessionFactory;
    private Class<Product> productClass; // Thêm biến để lưu class của Product

    public ProductDao() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public void setClass(Class<Product> productClass) {
        this.productClass = productClass; // Gán giá trị cho productClass
    }

    @Override
    public boolean create(Product product) throws Exception {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.save(product);
            transaction.commit();
            Log.info("Product persisted successfully: " + product.getName());
            return true;
        } catch (Exception e) {
            Log.error("Error while saving Product", e);
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public Product findById(long id) throws Exception {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Product product = session.get(Product.class, id);
            if (product != null) {
                Log.info("Product with id: " + id + " retrieved successfully: " + product.getName());
            } else {
                Log.warn("Product with id: " + id + " not found");
            }
            return product;
        } catch (Exception e) {
            Log.error("Error while retrieving Product with id: " + id, e);
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public List<Product> findAll(int pageNumber, int pageSize) throws Exception {
        Session session = null;
        try {
            session = sessionFactory.openSession();

            // Tính toán offset dựa trên pageNumber và pageSize
            int offset = (pageNumber - 1) * pageSize; // Lưu ý pageNumber bắt đầu từ 1

            // Sử dụng HQL để lấy tất cả các Product, và áp dụng phân trang
            List<Product> products = session.createQuery("from Product", Product.class)
                                            .setFirstResult(offset)  // Thiết lập vị trí bắt đầu
                                            .setMaxResults(pageSize) // Thiết lập số lượng bản ghi mỗi trang
                                            .list();

            Log.info("All products retrieved successfully with pagination. Total count: " + products.size());
            return products;
        } catch (Exception e) {
            Log.error("Error while retrieving all Products with pagination", e);
            throw e;
        } finally {
            if (session != null)
                session.close();
        }
    }


    @Override
    public boolean update(Product product) throws Exception {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.update(product);
            transaction.commit();
            Log.info("Product updated successfully: " + product.getName());
            return true;
        } catch (Exception e) {
            Log.error("Error while updating Product", e);
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public boolean deleteById(long id) throws Exception {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            Product product = session.get(Product.class, id);
            if (product != null) {
                session.delete(product);
                transaction.commit();
                Log.info("Product with id: " + id + " deleted successfully");
            } else {
                Log.warn("Product with id: " + id + " not found for deletion");
            }
            return true;
        } catch (Exception e) {
            Log.error("Error while deleting Product with id: " + id, e);
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public boolean delete(Product product) throws Exception {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.delete(product);
            transaction.commit();
            Log.info("Product deleted successfully: " + product.getName());
            return true;
        } catch (Exception e) {
            Log.error("Error while deleting Product", e);
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }
    
    public Product getProductByName(String name) throws Exception {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            Query<Product> query = session.createQuery("from Product where name = :name", Product.class);
            query.setParameter("name", name);
            query.setMaxResults(1); // Lấy sản phẩm đầu tiên nếu trùng tên
            Product product = query.uniqueResult();

            transaction.commit();

            if (product != null) {
                Log.info("Product with name: " + name + " retrieved successfully: " + product.getId());
            } else {
                Log.warn("Product with name: " + name + " not found");
            }
            return product;
        } catch (Exception e) {
            Log.error("Error while retrieving Product with name: " + name, e);
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }
    
    public Product findByEAN13(String ean13) throws Exception {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Query<Product> query = session.createQuery("from Product where ean13 = :ean13", Product.class);
            query.setParameter("ean13", ean13);

            Product product = query.uniqueResult(); // Mã vạch là duy nhất

            if (product != null) {
                Log.info("Product with EAN13: " + ean13 + " retrieved successfully: " + product.getId());
            } else {
                Log.warn("Product with EAN13: " + ean13 + " not found");
            }
            return product;
        } catch (Exception e) {
            Log.error("Error while retrieving Product with EAN13: " + ean13, e);
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

}
