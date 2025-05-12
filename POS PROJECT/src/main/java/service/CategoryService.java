package service;

import model.Category;
import model.Product;

import java.util.List;

public interface CategoryService {
    boolean createCategory(Category category);

    boolean updateCategory(Category category);

    boolean deleteCategoryById(long categoryId);

    boolean deleteCategory(Category category);

    Category getCategoryById(long categoryId);

    Category getCategoryByName(String name);
    
    List<Category> getAllCategories(int pageNumber, int pageSize);
    
    void addProductToCategory(Category category, Product product) throws Exception;
    
    void removeProductFromCategory(Category category, Product product) throws Exception;
}

