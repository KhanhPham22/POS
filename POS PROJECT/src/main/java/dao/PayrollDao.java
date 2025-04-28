package dao;

import java.time.Month;
import java.time.Year;
import java.util.List;

import model.Payroll;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import util.HibernateUtil;

public class PayrollDao implements GenericDao<Payroll> {

    private static final Logger Log = LogManager.getLogger(PayrollDao.class);

    private SessionFactory sessionFactory;
    private Class<Payroll> Payroll;

    public PayrollDao() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    public void setClass(Class<Payroll> Payroll) {
        this.Payroll = Payroll;
    }

    @Override
    public boolean create(Payroll payroll) throws Exception {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(payroll);
            transaction.commit();
            Log.info("Payroll persisted successfully");
            return true;
        } catch (Exception e) {
            Log.error("Error while saving Payroll", e);
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public Payroll findById(long id) throws Exception {
        Session session = sessionFactory.openSession();
        try {
            Payroll payroll = session.get(Payroll.class, id);
            Log.info("Payroll with id: " + id + " retrieved successfully");
            return payroll;
        } catch (Exception e) {
            Log.error("Error while retrieving Payroll", e);
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public List<Payroll> findAll(int pageNumber, int pageSize) throws Exception {
        Session session = null;
        try {
            session = sessionFactory.openSession();

            // Tính toán offset dựa trên pageNumber và pageSize
            int offset = (pageNumber - 1) * pageSize; // Lưu ý pageNumber bắt đầu từ 1

            // Sử dụng HQL để lấy tất cả các Payroll, và áp dụng phân trang
            List<Payroll> payrolls = session.createQuery("from Payroll", Payroll.class)
                                            .setFirstResult(offset)  // Thiết lập vị trí bắt đầu
                                            .setMaxResults(pageSize) // Thiết lập số lượng bản ghi mỗi trang
                                            .list();

            Log.info("All Payrolls retrieved successfully with pagination");
            return payrolls;
        } catch (Exception e) {
            Log.error("Error while retrieving all Payrolls with pagination", e);
            throw e;
        } finally {
            if (session != null)
                session.close();
        }
    }


    @Override
    public boolean update(Payroll payroll) throws Exception {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(payroll);
            transaction.commit();
            Log.info("Payroll updated successfully");
            return true;
        } catch (Exception e) {
            Log.error("Error while updating Payroll", e);
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean deleteById(long id) throws Exception {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Payroll payroll = session.get(Payroll.class, id);
            session.delete(payroll);
            transaction.commit();
            Log.info("Payroll deleted successfully");
            return true;
        } catch (Exception e) {
            Log.error("Error while deleting Payroll by id", e);
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean delete(Payroll payroll) throws Exception {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.delete(payroll);
            transaction.commit();
            Log.info("Payroll deleted successfully");
            return true;
        } catch (Exception e) {
            Log.error("Error while deleting Payroll", e);
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }
    
    public List<Payroll> findByMonthAndYear(Month month, Year year) throws Exception {
        Session session = sessionFactory.openSession();
        try {
            String hql = "FROM Payroll p WHERE p.month = :month AND p.year = :year";
            List<Payroll> payrolls = session.createQuery(hql, Payroll.class)
                    .setParameter("month", month)
                    .setParameter("year", year)
                    .list();
            Log.info("Payrolls for month " + month + " and year " + year + " retrieved successfully");
            return payrolls;
        } catch (Exception e) {
            Log.error("Error while retrieving Payrolls by month and year", e);
            throw e;
        } finally {
            session.close();
        }
    }
}
