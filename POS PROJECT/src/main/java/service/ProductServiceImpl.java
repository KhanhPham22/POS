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
    private final ProductDao productDao; // Data access object for Product entity

    // Constructor to initialize ProductDao
    public ProductServiceImpl(ProductDao productDao) {
        this.productDao = productDao;
        this.productDao.setClass(Product.class); // Setting the class for DAO operations
    }

    // Method to create a new product in the database
    @Override
    public boolean createProduct(Product product) {
        try {
            return productDao.create(product); // Attempt to create the product
        } catch (Exception e) {
            Log.error("Failed to create product", e); // Log any errors
            return false; // Return false if an error occurs
        }
    }

    // Method to update an existing product
    @Override
    public boolean updateProduct(Product product) {
        try {
            Session session = productDao.getSessionFactory().openSession();
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction(); // Start a new transaction
                // Ensure the product's category is managed (if not null)
                if (product.getCategory() != null) {
                    Category managedCategory = session.get(Category.class, product.getCategory().getId());
                    product.setCategory(managedCategory); // Set the category to a managed entity
                }
                productDao.update(product); // Update the product
                transaction.commit(); // Commit the transaction
                return true; // Return true if the update is successful
            } catch (Exception e) {
                if (transaction != null) transaction.rollback(); // Rollback transaction in case of error
                throw e; // Rethrow the exception
            } finally {
                session.close(); // Close the session
            }
        } catch (Exception e) {
            Log.error("Failed to update product", e); // Log any errors
            return false; // Return false if an error occurs
        }
    }

    // Method to delete a product by its ID
    @Override
    public boolean deleteProductById(long productId) {
        try {
            return productDao.deleteById(productId); // Attempt to delete the product by ID
        } catch (Exception e) {
            Log.error("Failed to delete product with ID: " + productId, e); // Log any errors
            return false; // Return false if an error occurs
        }
    }

    // Method to delete a product
    @Override
    public boolean deleteProduct(Product product) {
        try {
            return productDao.delete(product); // Attempt to delete the product
        } catch (Exception e) {
            Log.error("Failed to delete product", e); // Log any errors
            return false; // Return false if an error occurs
        }
    }

    // Method to retrieve a product by its ID
    @Override
    public Product getProductById(long productId) {
        try {
            return productDao.findById(productId); // Retrieve the product by ID
        } catch (Exception e) {
            Log.error("Failed to retrieve product with ID: " + productId, e); // Log any errors
            return null; // Return null if an error occurs
        }
    }

    // Method to retrieve all products with pagination
    @Override
    public List<Product> getAllProducts(int pageNumber, int pageSize) {
        try {
            return productDao.findAll(pageNumber, pageSize); // Retrieve all products with pagination
        } catch (Exception e) {
            Log.error("Failed to retrieve all products", e); // Log any errors
            return null; // Return null if an error occurs
        }
    }

    // Method to retrieve a product by its name
    @Override
    public Product getProductByName(String name) {
        try {
            return productDao.getProductByName(name); // Retrieve the product by name
        } catch (Exception e) {
            Log.error("Failed to retrieve product with name: " + name, e); // Log any errors
            return null; // Return null if an error occurs
        }
    }

    // Method to retrieve a product by its EAN13 code
    @Override
    public Product findbyEAN13(String ean13) {
        try {
            return productDao.findByEAN13(ean13); // Retrieve the product by its EAN13 code
        } catch (Exception e) {
            Log.error("Failed to retrieve product with EAN13: " + ean13, e); // Log any errors
            return null; // Return null if an error occurs
        }
    }

    // Method to check if a product exists by its name and size
    @Override
    public boolean existsByNameAndSize(String name, String size) {
        try {
            Product product = productDao.findByNameAndSize(name, size); // Find product by name and size
            return product != null; // Return true if the product exists
        } catch (Exception e) {
            Log.error("Failed to check for existing product with name: " + name + " and size: " + size, e); // Log any errors
            return false; // Return false if an error occurs
        }
    }

    // Method to populate the EAN13 field for existing products that do not have a valid EAN13
    @Override
    public void populateEAN13ForExistingProducts() {
        try {
            List<Product> products = productDao.findAll(1, Integer.MAX_VALUE); // Retrieve all products
            for (Product product : products) {
                // If the product does not have a valid EAN13, assign one
                if (product.getEan13() == null || !product.getEan13().matches("\\d{12,13}")) {
                    String ean13 = String.format("%012d", product.getId()); // Generate EAN13 based on product ID
                    product.setEan13(ean13); // Set the generated EAN13
                    updateProduct(product); // Update the product with the new EAN13
                }
            }
        } catch (Exception e) {
            Log.error("Failed to populate EAN13 for existing products", e); // Log any errors
        }
    }

    // Method to retrieve a product by its name and size
    @Override
    public Product getProductByNameAndSize(String name, String size) {
        try {
            return productDao.findByNameAndSize(name, size); // Retrieve product by name and size
        } catch (Exception e) {
            Log.error("Failed to retrieve product with name: " + name + " and size: " + size, e); // Log any errors
            return null; // Return null if an error occurs
        }
    }
}
