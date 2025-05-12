package service;

import dao.CategoryDao;
import model.Category;
import model.Product;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Collections;
import java.util.List;

public class CategoryServiceImpl implements CategoryService {

    private static final Logger Log = LogManager.getLogger(CategoryServiceImpl.class);
    private final CategoryDao categoryDao;

    // Constructor - inject CategoryDao and set class type
    public CategoryServiceImpl(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
        this.categoryDao.setClass(Category.class);
    }

    // Create a new category if it does not already exist
    @Override
    public boolean createCategory(Category category) {
        try {
            Category existing = categoryDao.getCategoryByName(category.getName());
            if (existing != null) {
                throw new Exception("Category with name '" + category.getName() + "' already exists.");
            }
            return categoryDao.create(category);
        } catch (Exception e) {
            Log.error("Failed to create category", e);
            throw new RuntimeException("Failed to create category: " + e.getMessage(), e);
        }
    }

    // Update an existing category, check for name conflicts
    @Override
    public boolean updateCategory(Category category) {
        try {
            Category existing = categoryDao.getCategoryByName(category.getName());
            if (existing != null && existing.getId() != category.getId()) {
                throw new Exception("Category with name '" + category.getName() + "' already exists.");
            }
            return categoryDao.update(category);
        } catch (Exception e) {
            Log.error("Failed to update category", e);
            throw new RuntimeException("Failed to update category: " + e.getMessage(), e);
        }
    }

    // Delete category by its ID
    @Override
    public boolean deleteCategoryById(long categoryId) {
        try {
            return categoryDao.deleteById(categoryId);
        } catch (Exception e) {
            Log.error("Failed to delete category with ID: " + categoryId, e);
            return false;
        }
    }

    // Delete category with disassociation of products (break foreign key before delete)
    @Override
    public boolean deleteCategory(Category category) {
        try {
            Session session = categoryDao.getSessionFactory().openSession();
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();

                // Fetch category and its associated products
                Category managedCategory = session.createQuery(
                        "from Category c left join fetch c.products where c.id = :id", Category.class)
                        .setParameter("id", category.getId())
                        .uniqueResult();

                if (managedCategory != null) {
                    // Remove association with products
                    if (managedCategory.getProducts() != null) {
                        for (Product product : managedCategory.getProducts()) {
                            product.setCategory(null);
                            session.update(product);
                        }
                    }

                    // Delete category
                    session.delete(managedCategory);
                    transaction.commit();
                    Log.info("Category deleted successfully: " + managedCategory.getName());
                    return true;
                } else {
                    Log.warn("Category not found: " + category.getName());
                    return false;
                }
            } catch (Exception e) {
                if (transaction != null) transaction.rollback();
                Log.error("Failed to delete category", e);
                throw e;
            } finally {
                session.close();
            }
        } catch (Exception e) {
            Log.error("Failed to delete category", e);
            return false;
        }
    }

    // Get a category by ID (including products)
    @Override
    public Category getCategoryById(long categoryId) {
        try {
            return categoryDao.findById(categoryId);
        } catch (Exception e) {
            Log.error("Failed to retrieve category with ID: " + categoryId, e);
            return null;
        }
    }

    // Get paginated list of all categories
    @Override
    public List<Category> getAllCategories(int pageNumber, int pageSize) {
        try {
            return categoryDao.findAll(pageNumber, pageSize);
        } catch (Exception e) {
            Log.error("Failed to retrieve all categories", e);
            return Collections.emptyList();
        }
    }

    // Find a category by name
    @Override
    public Category getCategoryByName(String name) {
        try {
            return categoryDao.getCategoryByName(name);
        } catch (Exception e) {
            Log.error("Failed to retrieve category with name: " + name, e);
            return null;
        }
    }

    // Add a product to a category
    @Override
    public void addProductToCategory(Category category, Product product) throws Exception {
        try {
            // Initialize product list if null
            if (category.getProducts() == null) {
                category.setProducts(new java.util.HashSet<>());
            }

            // Add product and update
            category.getProducts().add(product);
            categoryDao.update(category);
        } catch (Exception e) {
            Log.error("Failed to add product to category", e);
            throw e;
        }
    }

    // Remove a product from a category
    @Override
    public void removeProductFromCategory(Category category, Product product) throws Exception {
        Session session = null;
        Transaction transaction = null;
        try {
            session = categoryDao.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            // Load full category with products to ensure lazy list is initialized
            Category managedCategory = session.createQuery(
                    "from Category c left join fetch c.products where c.id = :id", Category.class)
                    .setParameter("id", category.getId())
                    .uniqueResult();

            if (managedCategory != null && managedCategory.getProducts() != null) {
                managedCategory.getProducts().remove(product);
                session.update(managedCategory);
            }

            transaction.commit();
        } catch (Exception e) {
            Log.error("Failed to remove product from category", e);
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

}


