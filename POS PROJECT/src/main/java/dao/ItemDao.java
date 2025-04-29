package dao;

import java.util.List;

import model.Item;
import model.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import util.HibernateUtil;

public class ItemDao implements GenericDao<Item> {

	private static final Logger Log = LogManager.getLogger(ItemDao.class);

	private SessionFactory sessionFactory;
	private Class<Item> Item;

	public ItemDao() {
		sessionFactory = HibernateUtil.getSessionFactory();
	}
	
	@Override
	public void setClass(Class<Item> Item) {
		this.Item = Item;
	}

	@Override
	public boolean create(Item item) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			session.save(item);
			transaction.commit();
			Log.info("Item persisted successfully");
			return true;
		} catch (Exception e) {
			Log.error("Error while saving Item", e);
			if (transaction != null)
				transaction.rollback();
			throw e;
		} finally {
			session.close();
		}
	}

	@Override
	public Item findById(long id) throws Exception {
		Session session = sessionFactory.openSession();
		try {
			Item item = session.get(Item.class, id);
			Log.info("Item with id: " + id + " retrieved successfully");
			return item;
		} catch (Exception e) {
			Log.error("Error while retrieving Item", e);
			throw e;
		} finally {
			session.close();
		}
	}

	@Override
	public List<Item> findAll(int pageNumber, int pageSize) throws Exception {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			int offset = pageNumber * pageSize; // Tính toán offset dựa trên pageNumber và pageSize
			List<Item> items = session.createQuery("from Item", Item.class).setFirstResult(offset)
					.setMaxResults(pageSize) // Áp dụng phân trang
					.list();

			// Initialize các liên kết (nếu có) cho từng item ngay khi session còn mở
			for (Item item : items) {
				Hibernate.initialize(item.getSupplier()); // Giả sử Item có liên kết đến Supplier
			}

			Log.info("All items retrieved successfully. Total count: " + items.size());
			return items;
		} catch (Exception e) {
			Log.error("Error while retrieving all Items with pagination", e);
			throw e;
		} finally {
			if (session != null)
				session.close();
		}
	}

	@Override
	public boolean update(Item item) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			session.update(item);
			transaction.commit();
			Log.info("Item updated successfully");
			return true;
		} catch (Exception e) {
			Log.error("Error while updating Item", e);
			if (transaction != null)
				transaction.rollback();
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
			Item item = session.get(Item.class, id);
			session.delete(item);
			transaction.commit();
			Log.info("Item deleted successfully");
			return true;
		} catch (Exception e) {
			Log.error("Error while deleting Item by id", e);
			if (transaction != null)
				transaction.rollback();
			throw e;
		} finally {
			session.close();
		}
	}

	@Override
	public boolean delete(Item item) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			session.delete(item);
			transaction.commit();
			Log.info("Item deleted successfully");
			return true;
		} catch (Exception e) {
			Log.error("Error while deleting Item", e);
			if (transaction != null)
				transaction.rollback();
			throw e;
		} finally {
			session.close();
		}
	}

	public List<Item> findByName(String name) throws Exception {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			 // Tính toán offset
			String hql = "FROM Item WHERE lower(name) LIKE :name";
			List<Item> items = session.createQuery(hql, Item.class).setParameter("name", "%" + name.toLowerCase() + "%")
				
					.list();

			// Initialize các liên kết (nếu có) cho từng item ngay khi session còn mở
			for (Item item : items) {
				Hibernate.initialize(item.getSupplier()); // Giả sử Item có liên kết đến Supplier
			}

			Log.info("Items retrieved with name like: " + name);
			return items;
		} catch (Exception e) {
			Log.error("Error while retrieving items by name with pagination", e);
			throw e;
		} finally {
			if (session != null)
				session.close();
		}
	}

	public List<Item> findBySupplierId(long supplierId, int pageNumber, int pageSize) throws Exception {
	    Session session = null;
	    try {
	        session = sessionFactory.openSession();
	        
	        // Tính toán offset dựa trên pageNumber và pageSize
	        int offset = (pageNumber - 1) * pageSize;
	        System.out.println("Fetching items for supplierId=" + supplierId + ", pageNumber=" + pageNumber + ", pageSize=" + pageSize + ", offset=" + offset);
	        
	        String hql = "FROM Item WHERE supplier.id = :supplierId";
	        List<Item> items = session.createQuery(hql, Item.class)
	                                  .setParameter("supplierId", supplierId)
	                                  .setFirstResult(offset)
	                                  .setMaxResults(pageSize)
	                                  .list();

	        // Log số lượng items trả về
	        System.out.println("Items retrieved: " + items.size());

	        for (Item item : items) {
	            Hibernate.initialize(item.getSupplier());
	        }

	        Log.info("Items for supplier with ID: " + supplierId + " retrieved successfully.");
	        return items;
	    } catch (Exception e) {
	        Log.error("Error while retrieving items by supplierId", e);
	        throw e;
	    } finally {
	        if (session != null)
	            session.close();
	    }
	}


}
