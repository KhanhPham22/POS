package dao;

import model.Feedback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import util.HibernateUtil;

import java.util.List;

public class FeedbackDao implements GenericDao<Feedback> {

    private static final Logger Log = LogManager.getLogger(FeedbackDao.class);

    private SessionFactory sessionFactory;
    private Class<Feedback> feedbackClass;

    public FeedbackDao() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public void setClass(Class<Feedback> feedbackClass) {
        this.feedbackClass = feedbackClass; // Gán lớp Feedback
    }

    @Override
    public boolean create(Feedback feedback) throws Exception {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.save(feedback);
            transaction.commit();
            Log.info("Feedback persisted successfully for customer: " + feedback.getCustomer().getCustomerFirstName());
            return true;
        } catch (Exception e) {
            Log.error("Error while saving Feedback", e);
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public Feedback findById(long id) throws Exception {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Feedback feedback = session.get(Feedback.class, id);
            if (feedback != null) {
                Log.info("Feedback with id: " + id + " retrieved successfully");
            } else {
                Log.warn("Feedback with id: " + id + " not found");
            }
            return feedback;
        } catch (Exception e) {
            Log.error("Error while retrieving Feedback with id: " + id, e);
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public List<Feedback> findAll(int pageNumber, int pageSize) throws Exception {
        Session session = null;
        try {
            session = sessionFactory.openSession();

            // Tính toán offset dựa trên pageNumber và pageSize
            int offset = (pageNumber - 1) * pageSize; // Lưu ý pageNumber bắt đầu từ 1

            // Sử dụng HQL để lấy tất cả các Feedback, và áp dụng phân trang
            List<Feedback> feedbacks = session.createQuery("from Feedback", Feedback.class)
                                              .setFirstResult(offset)  // Thiết lập vị trí bắt đầu
                                              .setMaxResults(pageSize) // Thiết lập số lượng bản ghi mỗi trang
                                              .list();

            Log.info("All feedbacks retrieved successfully with pagination. Total count: " + feedbacks.size());
            return feedbacks;
        } catch (Exception e) {
            Log.error("Error while retrieving feedbacks with pagination", e);
            throw e;
        } finally {
            if (session != null)
                session.close();
        }
    }


    @Override
    public boolean update(Feedback feedback) throws Exception {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.update(feedback);
            transaction.commit();
            Log.info("Feedback updated successfully for customer: " + feedback.getCustomer().getCustomerFirstName());
            return true;
        } catch (Exception e) {
            Log.error("Error while updating Feedback", e);
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
            Feedback feedback = session.get(Feedback.class, id);
            if (feedback != null) {
                session.delete(feedback);
                transaction.commit();
                Log.info("Feedback with id: " + id + " deleted successfully");
            } else {
                Log.warn("Feedback with id: " + id + " not found for deletion");
            }
            return true;
        } catch (Exception e) {
            Log.error("Error while deleting Feedback with id: " + id, e);
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public boolean delete(Feedback feedback) throws Exception {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.delete(feedback);
            transaction.commit();
            Log.info("Feedback deleted successfully for customer: " + feedback.getCustomer().getCustomerFirstName());
            return true;
        } catch (Exception e) {
            Log.error("Error while deleting Feedback", e);
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }
    
 // Tìm kiếm feedback theo UserId
    public List<Feedback> findByPersonId(long personId) throws Exception {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Query<Feedback> query = session.createQuery("from Feedback where customer.id = :personId", Feedback.class);
            query.setParameter("personId", personId);
            List<Feedback> feedbacks = query.list();
            Log.info("Retrieved " + feedbacks.size() + " feedbacks for person with ID: " + personId);
            return feedbacks;
        } catch (Exception e) {
            Log.error("Error while retrieving feedback for user with ID: " + personId, e);
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

    // Tìm kiếm feedback theo ProductId
    public List<Feedback> findByProductId(long productId) throws Exception {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Query<Feedback> query = session.createQuery("from Feedback where product.id = :productId", Feedback.class);
            query.setParameter("productId", productId);
            List<Feedback> feedbacks = query.list();
            Log.info("Retrieved " + feedbacks.size() + " feedbacks for product with ID: " + productId);
            return feedbacks;
        } catch (Exception e) {
            Log.error("Error while retrieving feedback for product with ID: " + productId, e);
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }
}
