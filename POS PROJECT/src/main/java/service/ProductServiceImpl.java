package service;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.ProductDao;
import model.Product;

public class ProductServiceImpl implements ProductService {

    private static final Logger Log = LogManager.getLogger(ProductServiceImpl.class);
    private final ProductDao productDao;

    public ProductServiceImpl() {
        this.productDao = new ProductDao();
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
            return productDao.update(product);
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
    public List<Product> getAllProducts() {
        try {
            return productDao.findAll();
        } catch (Exception e) {
            Log.error("Failed to retrieve all products", e);
            return null;
        }
    }
}

