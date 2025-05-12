package service;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import dao.ProductDao;
import model.Category;
import model.Product;

public class ProductServiceImpl implements ProductService {

	private static final Logger Log = LogManager.getLogger(ProductServiceImpl.class);
	private final ProductDao productDao;

	public ProductServiceImpl(ProductDao productDao) {
		this.productDao = productDao;
		this.productDao.setClass(Product.class);
	}

	@Override
	public boolean createProduct(Product product) {
		try {
			return productDao.create(product);
		} catch (Exception e) {
			Log.error("Failed to create product", e);
			return false;
		}
	}

	@Override
	public boolean updateProduct(Product product) {
	    try {
	        Session session = productDao.getSessionFactory().openSession();
	        Transaction transaction = null;
	        try {
	            transaction = session.beginTransaction();
	            // Ensure the product's category is managed (if not null)
	            if (product.getCategory() != null) {
	                Category managedCategory = session.get(Category.class, product.getCategory().getId());
	                product.setCategory(managedCategory);
	            }
	            productDao.update(product);
	            transaction.commit();
	            return true;
	        } catch (Exception e) {
	            if (transaction != null) transaction.rollback();
	            throw e;
	        } finally {
	            session.close();
	        }
	    } catch (Exception e) {
	        Log.error("Failed to update product", e);
	        return false;
	    }
	}

	@Override
	public boolean deleteProductById(long productId) {
		try {
			return productDao.deleteById(productId);
		} catch (Exception e) {
			Log.error("Failed to delete product with ID: " + productId, e);
			return false;
		}
	}

	@Override
	public boolean deleteProduct(Product product) {
		try {
			return productDao.delete(product);
		} catch (Exception e) {
			Log.error("Failed to delete product", e);
			return false;
		}
	}

	@Override
	public Product getProductById(long productId) {
		try {
			return productDao.findById(productId);
		} catch (Exception e) {
			Log.error("Failed to retrieve product with ID: " + productId, e);
			return null;
		}
	}

	@Override
	public List<Product> getAllProducts(int pageNumber, int pageSize) {
		try {
			return productDao.findAll(pageNumber, pageSize);
		} catch (Exception e) {
			Log.error("Failed to retrieve all products", e);
			return null;
		}
	}

	@Override
	public Product getProductByName(String name) {
		try {
			return productDao.getProductByName(name);
		} catch (Exception e) {
			Log.error("Failed to retrieve product with name: " + name, e);
			return null;
		}
	}

	@Override
	public Product findbyEAN13(String ean13) {
		try {
			return productDao.findByEAN13(ean13);
		} catch (Exception e) {
			Log.error("Failed to retrieve product with EAN13: " + ean13, e);
			return null;
		}
	}

	@Override
	public boolean existsByNameAndSize(String name, String size) {
		try {
			Product product = productDao.findByNameAndSize(name, size);
			return product != null;
		} catch (Exception e) {
			Log.error("Failed to check for existing product with name: " + name + " and size: " + size, e);
			return false;
		}
	}

	@Override
	public void populateEAN13ForExistingProducts() {
		try {
			List<Product> products = productDao.findAll(1, Integer.MAX_VALUE);
			for (Product product : products) {
				if (product.getEan13() == null || !product.getEan13().matches("\\d{12,13}")) {
					String ean13 = String.format("%012d", product.getId()); // Pad with leading zeros
					product.setEan13(ean13);
					updateProduct(product);
				}
			}
		} catch (Exception e) {
			Log.error("Failed to populate EAN13 for existing products", e);
		}
	}

	@Override
	public Product getProductByNameAndSize(String name, String size) {
		try {
			return productDao.findByNameAndSize(name, size);
		} catch (Exception e) {
			Log.error("Failed to retrieve product with name: " + name + " and size: " + size, e);
			return null;
		}
	}
}
