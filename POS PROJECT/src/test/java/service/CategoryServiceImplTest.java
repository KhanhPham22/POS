package service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.hibernate.query.Query; // ✅ Đúng

import org.hibernate.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import model.Category;
import model.Product;
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

	// Existing test cases...

	// Test createCategory when the category already exists
	@Test
	public void testCreateCategory_AlreadyExists() throws Exception {
		Category category = new Category();
		category.setName("Coffee");
		when(mockCategoryDao.getCategoryByName("Coffee")).thenReturn(new Category()); // Simulate category already
																						// exists

		Exception exception = assertThrows(RuntimeException.class, () -> {
			categoryService.createCategory(category);
		});

		assertEquals("Failed to create category: Category with name 'Coffee' already exists.", exception.getMessage());
		verify(mockCategoryDao, never()).create(category); // Ensure create is not called
	}

	// Test updateCategory when there is a name conflict
	@Test
	public void testUpdateCategory_NameConflict() throws Exception {
		Category category = new Category();
		category.setName("Coffee");
		category.setId(1L);

		// Mock the behavior of getCategoryByName to simulate the conflict
		Category existingCategory = new Category();
		existingCategory.setName("Coffee");
		existingCategory.setId(2L); // Different ID but same name

		when(mockCategoryDao.getCategoryByName("Coffee")).thenReturn(existingCategory);

		Exception exception = assertThrows(RuntimeException.class, () -> {
			categoryService.updateCategory(category);
		});

		assertEquals("Failed to update category: Category with name 'Coffee' already exists.", exception.getMessage());

		verify(mockCategoryDao, never()).update(category); // Ensure update was not called
	}

	// Test deleteCategoryById when deletion fails
	@Test
	public void testDeleteCategoryById_Failure() throws Exception {
		when(mockCategoryDao.deleteById(1L)).thenThrow(new RuntimeException("Deletion failed"));

		boolean result = categoryService.deleteCategoryById(1L);

		assertFalse(result); // Should return false since deletion failed
		verify(mockCategoryDao).deleteById(1L); // Verify delete was attempted
	}

	// Test deleteCategory when deletion fails
	@Test
	public void testDeleteCategory_Failure() throws Exception {
		Category category = new Category();
		category.setId(1L);

		when(mockCategoryDao.getSessionFactory()).thenThrow(new RuntimeException("SessionFactory error"));

		boolean result = categoryService.deleteCategory(category);

		assertFalse(result); // Kỳ vọng trả về false
		verify(mockCategoryDao).getSessionFactory(); // Xác minh đã gọi
	}

	// Test addProductToCategory when adding a product fails
	@Test
	public void testAddProductToCategory_Failure() throws Exception {
		Category category = new Category();
		Product product = new Product();
		when(mockCategoryDao.update(category)).thenThrow(new RuntimeException("Update failed"));

		Exception exception = assertThrows(RuntimeException.class, () -> {
			categoryService.addProductToCategory(category, product);
		});

		assertEquals("Update failed", exception.getMessage());
		verify(mockCategoryDao).update(category); // Ensure update was attempted
	}

	// Test removeProductFromCategory when removing a product fails
	@Test
	public void testRemoveProductFromCategory_Failure() throws Exception {
		Category category = new Category();
		category.setId(1L);
		Product product = new Product();

		// Mock các đối tượng Hibernate
		SessionFactory mockSessionFactory = mock(SessionFactory.class);
		Session mockSession = mock(Session.class);
		Transaction mockTransaction = mock(Transaction.class);

		// Cấu hình mock để tránh NullPointerException
		when(mockCategoryDao.getSessionFactory()).thenReturn(mockSessionFactory);
		when(mockSessionFactory.openSession()).thenReturn(mockSession);
		when(mockSession.beginTransaction()).thenReturn(mockTransaction);

		// Giả lập category có product và sẽ lỗi khi update
		Category managedCategory = new Category();
		managedCategory.setId(1L);
		category.setProducts(Set.of(product)); // nếu dùng Java 9+

		Query<Category> mockQuery = mock(Query.class);
		when(mockSession.createQuery(anyString(), eq(Category.class))).thenReturn(mockQuery);
		when(mockQuery.setParameter(eq("id"), eq(1L))).thenReturn(mockQuery);
		when(mockQuery.uniqueResult()).thenReturn(managedCategory);

		// Gây lỗi tại session.update
		doThrow(new RuntimeException("Remove failed")).when(mockSession).update(any());

		// Thực thi
		Exception exception = assertThrows(RuntimeException.class, () -> {
			categoryService.removeProductFromCategory(category, product);
		});

		// Kiểm tra kết quả
		assertEquals("Remove failed", exception.getMessage());
		verify(mockSession).update(any());
		verify(mockTransaction).rollback();
		verify(mockSession).close();
	}

}
