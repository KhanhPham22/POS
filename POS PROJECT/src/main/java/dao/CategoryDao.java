package dao;

import model.Category;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import util.HibernateUtil;

import java.util.List;

public class CategoryDao implements GenericDao<Category> {

    private static final Logger Log = LogManager.getLogger(CategoryDao.class);

    private SessionFactory sessionFactory;
    private Class<Category> categoryClass; // Store the Category class reference

    // Constructor: initialize SessionFactory
    public CategoryDao() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    // Set the class type (required by GenericDao interface)
    @Override
    public void setClass(Class<Category> categoryClass) {
        this.categoryClass = categoryClass;
    }

    // Save a new Category to the database
    @Override
    public boolean create(Category category) throws Exception {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.save(category);
            transaction.commit();
            Log.info("Category persisted successfully: " + category.getName());
            return true;
        } catch (Exception e) {
            Log.error("Error while saving Category", e);
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

    // Find a Category by ID, including its products
    @Override
    public Category findById(long id) throws Exception {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Query<Category> query = session.createQuery(
                    "from Category c left join fetch c.products where c.id = :id", Category.class);
            query.setParameter("id", id);
            Category category = query.uniqueResult();
            if (category != null) {
                Log.info("Category with id: " + id + " retrieved successfully: " + category.getName());
            } else {
                Log.warn("Category with id: " + id + " not found");
            }
            return category;
        } catch (Exception e) {
            Log.error("Error while retrieving Category with id: " + id, e);
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

    // Get paginated list of all categories (including products)
    @Override
    public List<Category> findAll(int pageNumber, int pageSize) throws Exception {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            int offset = (pageNumber - 1) * pageSize;

            List<Category> categories = session.createQuery(
                    "from Category c left join fetch c.products", Category.class)
                    .setFirstResult(offset)
                    .setMaxResults(pageSize)
                    .list();

            transaction.commit();
            Log.info("All categories retrieved successfully with pagination. Total count: " + categories.size());
            return categories;
        } catch (Exception e) {
            Log.error("Error while retrieving all Categories with pagination", e);
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

    // Update an existing Category
    @Override
    public boolean update(Category category) throws Exception {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.update(category);
            transaction.commit();
            Log.info("Category updated successfully: " + category.getName());
            return true;
        } catch (Exception e) {
            Log.error("Error while updating Category", e);
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

    // Delete a Category by its ID
    @Override
    public boolean deleteById(long id) throws Exception {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            Category category = session.get(Category.class, id);
            if (category != null) {
                session.delete(category);
                transaction.commit();
                Log.info("Category with id: " + id + " deleted successfully");
            } else {
                Log.warn("Category with id: " + id + " not found for deletion");
            }
            return true;
        } catch (Exception e) {
            Log.error("Error while deleting Category with id: " + id, e);
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

    // Delete a Category directly by object
    @Override
    public boolean delete(Category category) throws Exception {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.delete(category);
            transaction.commit();
            Log.info("Category deleted successfully: " + category.getName());
            return true;
        } catch (Exception e) {
            Log.error("Error while deleting Category", e);
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

    // Find a Category by its name (including products)
    public Category getCategoryByName(String name) throws Exception {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            Query<Category> query = session.createQuery(
                    "from Category c left join fetch c.products where c.name = :name", Category.class);
            query.setParameter("name", name);
            query.setMaxResults(1);
            Category category = query.uniqueResult();
            transaction.commit();
            if (category != null) {
                Log.info("Category with name: " + name + " retrieved successfully: " + category.getId());
            } else {
                Log.warn("Category with name: " + name + " not found");
            }
            return category;
        } catch (Exception e) {
            Log.error("Error while retrieving Category with name: " + name, e);
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }
}
