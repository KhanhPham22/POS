package service;

import dao.CategoryDao;
import model.Category;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class CategoryServiceImpl implements CategoryService {

    private static final Logger Log = LogManager.getLogger(CategoryServiceImpl.class);
    private final CategoryDao categoryDao;

    public CategoryServiceImpl(CategoryDao categoryDao) {
        this.categoryDao =  categoryDao;
        this.categoryDao.setClass(Category.class);
    }

    @Override
    public boolean createCategory(Category category) {
        try {
            return categoryDao.create(category);
        } catch (Exception e) {
            Log.error("Failed to create category", e);
            return false;
        }
    }

    @Override
    public boolean updateCategory(Category category) {
        try {
            return categoryDao.update(category);
        } catch (Exception e) {
            Log.error("Failed to update category", e);
            return false;
        }
    }

    @Override
    public boolean deleteCategoryById(long categoryId) {
        try {
            return categoryDao.deleteById(categoryId);
        } catch (Exception e) {
            Log.error("Failed to delete category with ID: " + categoryId, e);
            return false;
        }
    }

    @Override
    public boolean deleteCategory(Category category) {
        try {
            return categoryDao.delete(category);
        } catch (Exception e) {
            Log.error("Failed to delete category", e);
            return false;
        }
    }

    @Override
    public Category getCategoryById(long categoryId) {
        try {
            return categoryDao.findById(categoryId);
        } catch (Exception e) {
            Log.error("Failed to retrieve category with ID: " + categoryId, e);
            return null;
        }
    }

    @Override
    public List<Category> getAllCategories() {
        try {
            return categoryDao.findAll();
        } catch (Exception e) {
            Log.error("Failed to retrieve all categories", e);
            return null;
        }
    }
    
    @Override
    public Category getCategoryByName(String name) {
        try {
            return categoryDao.findByName(name);
        } catch (Exception e) {
            Log.error("Failed to retrieve category with name: " + name, e);
            return null;
        }
    }

}

