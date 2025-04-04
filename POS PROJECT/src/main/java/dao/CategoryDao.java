package dao;

import model.Category;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;

public class CategoryDao implements GenericDao<Category> {

    private static final Logger Log = LogManager.getLogger(CategoryDao.class);

    private SessionFactory sessionFactory;
    private Class<Category> categoryClass; // Biến để lưu lớp Category

    public CategoryDao() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public void setClass(Class<Category> categoryClass) {
        this.categoryClass = categoryClass; // Gán lớp Category
    }

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

    @Override
    public Category findById(long id) throws Exception {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Category category = session.get(Category.class, id);
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

    @Override
    public List<Category> findAll() throws Exception {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            List<Category> categories = session.createQuery("from Category", Category.class).list();
            Log.info("All categories retrieved successfully. Total count: " + categories.size());
            return categories;
        } catch (Exception e) {
            Log.error("Error while retrieving all Categories", e);
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

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
}
