package service;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.hibernate.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import model.Category;
import dao.CategoryDao;
import service.CategoryServiceImpl;

public class CategoryServiceImplTest {

	private CategoryDao mockCategoryDao;
	private CategoryServiceImpl categoryService;
	
	@BeforeEach
    public void setUp() {
		mockCategoryDao = mock(CategoryDao.class);
		categoryService = new CategoryServiceImpl(mockCategoryDao);
	}
	
	@Test
    public void testCreateCategory_Success() throws Exception {
        Category category = new Category();
        when(mockCategoryDao.create(category)).thenReturn(true);

        boolean result = categoryService.createCategory(category);

        assertTrue(result);
        verify(mockCategoryDao).create(category);
    }
	
	 @Test
	    public void testCreateCategory_Exception() throws Exception {
	        Category category = new Category();
	        when(mockCategoryDao.create(category)).thenThrow(new RuntimeException("Create failed"));

	        boolean result = categoryService.createCategory(category);

	        assertFalse(result);
	        verify(mockCategoryDao).create(category);
	    }

	    @Test
	    public void testUpdateCategory_Success() throws Exception {
	        Category category = new Category();
	        when(mockCategoryDao.update(category)).thenReturn(true);

	        boolean result = categoryService.updateCategory(category);

	        assertTrue(result);
	        verify(mockCategoryDao).update(category);
	    }
	    
	    @Test
	    public void testDeleteCategoryById_Success() throws Exception {
	        when(mockCategoryDao.deleteById(1L)).thenReturn(true);

	        boolean result = categoryService.deleteCategoryById(1L);

	        assertTrue(result);
	        verify(mockCategoryDao).deleteById(1L);
	    }

	    @Test
	    public void testDeleteCategory_Success() throws Exception {
	        Category category = new Category();
	        when(mockCategoryDao.delete(category)).thenReturn(true);

	        boolean result = categoryService.deleteCategory(category);

	        assertTrue(result);
	        verify(mockCategoryDao).delete(category);
	    }
	    
	    @Test
	    public void testGetCategoryById_Found() throws Exception {
	        Category category = new Category();
	        category.setId(1L);
	        when(mockCategoryDao.findById(1L)).thenReturn(category);

	        Category result = categoryService.getCategoryById(1L);

	        assertNotNull(result);
	        assertEquals(1L, result.getId());
	    }

	    @Test
	    public void testGetCategoryById_NotFound() throws Exception {
	        when(mockCategoryDao.findById(2L)).thenReturn(null);

	        Category result = categoryService.getCategoryById(2L);

	        assertNull(result);
	    }

	    @Test
	    public void testGetAllCategories_WithPagination() throws Exception {
	        List<Category> categories = Arrays.asList(new Category(), new Category());
	        when(mockCategoryDao.findAll(1, 10)).thenReturn(categories); // Giả lập DAO trả 2 category

	        List<Category> result = categoryService.getAllCategories(1, 10); // Gọi service với page 1, size 10

	        assertNotNull(result);
	        assertEquals(2, result.size());
	        verify(mockCategoryDao).findAll(1, 10); // Xác nhận DAO được gọi đúng
	    }



//	    @Test
//	    public void testGetCategoryByName_Found() throws Exception {
//	        Category category = new Category();
//	        when(mockCategoryDao.findByName("Coffee")).thenReturn(category);
//
//	        Category result = categoryService.getCategoryByName("Coffee");
//
//	        assertNotNull(result);
//	        verify(mockCategoryDao).findByName("Coffee");
//	    }
}
