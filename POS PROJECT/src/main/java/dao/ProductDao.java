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
    private Class<Product> productClass;

    public ProductDao() {
        // Initialize Hibernate SessionFactory
        sessionFactory = HibernateUtil.getSessionFactory();
    }
    
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    @Override
    public void setClass(Class<Product> productClass) {
        // Set the Product class for generic operations
        this.productClass = productClass;
    }

    @Override
    public boolean create(Product product) throws Exception {
        // Create a new Product entry in the database
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
        // Retrieve a Product by its ID
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
        // Retrieve a paginated list of Products with associated Category
        Session session = null;
        try {
            session = sessionFactory.openSession();

            // Calculate pagination offset
            int offset = (pageNumber - 1) * pageSize;

            // Use HQL to fetch Products and their Categories
            List<Product> products = session.createQuery(
                    "from Product p left join fetch p.category", Product.class)
                    .setFirstResult(offset)
                    .setMaxResults(pageSize)
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
        // Update an existing Product in the database
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
        // Delete a Product by its ID
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
        // Delete a Product using the Product object
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
        // Retrieve a Product by its name
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            Query<Product> query = session.createQuery(
                    "from Product p left join fetch p.category where p.name = :name", Product.class);
            query.setParameter("name", name);
            query.setMaxResults(1);
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
        // Retrieve a Product by its EAN13 code
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            Query<Product> query = session.createQuery(
                    "from Product p left join fetch p.category where p.ean13 = :ean13", Product.class);
            query.setParameter("ean13", ean13);
            Product product = query.uniqueResult();

            transaction.commit();

            if (product != null) {
                Log.info("Product with EAN13: " + ean13 + " retrieved successfully: " + product.getId());
            } else {
                Log.warn("Product with EAN13: " + ean13 + " not found");
            }
            return product;
        } catch (Exception e) {
            Log.error("Error while retrieving Product with EAN13: " + ean13, e);
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

    public Product findByNameAndSize(String name, String size) throws Exception {
        // Retrieve a Product by both name and size
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            Query<Product> query = session.createQuery(
                    "from Product p left join fetch p.category where p.name = :name and p.size = :size", Product.class);
            query.setParameter("name", name);
            query.setParameter("size", size);
            Product product = query.uniqueResult();

            transaction.commit();

            if (product != null) {
                Log.info("Product with name: " + name + " and size: " + size + " retrieved successfully: " + product.getId());
            } else {
                Log.warn("Product with name: " + name + " and size: " + size + " not found");
            }
            return product;
        } catch (Exception e) {
            Log.error("Error while retrieving Product with name: " + name + " and size: " + size, e);
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }
}
