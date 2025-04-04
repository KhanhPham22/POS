package dao;

import model.Product;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
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
    public List<Product> findAll() throws Exception {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            List<Product> products = session.createQuery("from Product", Product.class).list();
            Log.info("All products retrieved successfully. Total count: " + products.size());
            return products;
        } catch (Exception e) {
            Log.error("Error while retrieving all Products", e);
            throw e;
        } finally {
            if (session != null) session.close();
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
}
