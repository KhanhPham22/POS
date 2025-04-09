package dao;

import java.util.List;

import model.GiftVoucher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import util.HibernateUtil;

public class GiftVoucherDao implements GenericDao<GiftVoucher> {

    private static final Logger Log = LogManager.getLogger(GiftVoucherDao.class);

    private SessionFactory sessionFactory;
    private Class<GiftVoucher> giftVoucherClass;

    public GiftVoucherDao() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    public void setClass(Class<GiftVoucher> giftVoucherClass) {
        this.giftVoucherClass = giftVoucherClass;
    }

    @Override
    public boolean create(GiftVoucher giftVoucher) throws Exception {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(giftVoucher);
            transaction.commit();
            Log.info("GiftVoucher persisted successfully");
            return true;
        } catch (Exception e) {
            Log.error("Error while saving GiftVoucher", e);
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public GiftVoucher findById(long id) throws Exception {
        Session session = sessionFactory.openSession();
        try {
            GiftVoucher giftVoucher = session.get(GiftVoucher.class, id);
            Log.info("GiftVoucher with id: " + id + " retrieved successfully");
            return giftVoucher;
        } catch (Exception e) {
            Log.error("Error while retrieving GiftVoucher", e);
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public List<GiftVoucher> findAll() throws Exception {
        Session session = sessionFactory.openSession();
        try {
            List<GiftVoucher> giftVouchers = session.createQuery("from GiftVoucher", GiftVoucher.class).list();
            Log.info("All GiftVouchers retrieved successfully");
            return giftVouchers;
        } catch (Exception e) {
            Log.error("Error while retrieving all GiftVouchers", e);
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean update(GiftVoucher giftVoucher) throws Exception {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(giftVoucher);
            transaction.commit();
            Log.info("GiftVoucher updated successfully");
            return true;
        } catch (Exception e) {
            Log.error("Error while updating GiftVoucher", e);
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
            GiftVoucher giftVoucher = session.get(GiftVoucher.class, id);
            session.delete(giftVoucher);
            transaction.commit();
            Log.info("GiftVoucher deleted successfully");
            return true;
        } catch (Exception e) {
            Log.error("Error while deleting GiftVoucher by id", e);
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean delete(GiftVoucher giftVoucher) throws Exception {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.delete(giftVoucher);
            transaction.commit();
            Log.info("GiftVoucher deleted successfully");
            return true;
        } catch (Exception e) {
            Log.error("Error while deleting GiftVoucher", e);
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }
    
    public GiftVoucher findByVoucherName(String voucherName) throws Exception {
        Session session = sessionFactory.openSession();
        try {
            Query<GiftVoucher> query = session.createQuery(
                "FROM GiftVoucher WHERE voucherName = :voucherName", GiftVoucher.class
            );
            query.setParameter("voucherName", voucherName);
            GiftVoucher result = query.uniqueResult();
            Log.info("GiftVoucher with voucherName: " + voucherName + " retrieved successfully");
            return result;
        } catch (Exception e) {
            Log.error("Error while retrieving GiftVoucher by voucherName", e);
            throw e;
        } finally {
            session.close();
        }
    }

}
