package service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dao.ProductDao;
import model.Product;

public class ProductServiceImplTest {

	private ProductDao mockProductDao;
	private ProductServiceImpl productService;

	@BeforeEach
	public void setUp() {
		mockProductDao = mock(ProductDao.class);
		productService = new ProductServiceImpl(mockProductDao);
	}

	// Other existing tests...

	@Test
	public void testGetAllProducts_Success() throws Exception {
		// Prepare mock return
		List<Product> mockList = Arrays.asList(new Product(), new Product());
		when(mockProductDao.findAll(1, 10)).thenReturn(mockList);

		// Call service method
		List<Product> result = productService.getAllProducts(1, 10);

		// Verify and assert
		assertNotNull(result);
		assertEquals(2, result.size());
		verify(mockProductDao).findAll(1, 10);
	}

	@Test
	public void testExistsByNameAndSize_ProductExists() throws Exception {
		// Mock product found
		Product mockProduct = new Product();
		when(mockProductDao.findByNameAndSize("Coca", "500ml")).thenReturn(mockProduct);

		boolean result = productService.existsByNameAndSize("Coca", "500ml");

		assertTrue(result);
		verify(mockProductDao).findByNameAndSize("Coca", "500ml");
	}

	@Test
	public void testExistsByNameAndSize_ProductNotFound() throws Exception {
		// Mock product not found
		when(mockProductDao.findByNameAndSize("Pepsi", "330ml")).thenReturn(null);

		boolean result = productService.existsByNameAndSize("Pepsi", "330ml");

		assertFalse(result);
		verify(mockProductDao).findByNameAndSize("Pepsi", "330ml");
	}

	@Test
	public void testGetProductByNameAndSize_Found() throws Exception {
		// Mock found
		Product product = new Product();
		when(mockProductDao.findByNameAndSize("Water", "1L")).thenReturn(product);

		Product result = productService.getProductByNameAndSize("Water", "1L");

		assertNotNull(result);
		verify(mockProductDao).findByNameAndSize("Water", "1L");
	}

	@Test
	public void testGetProductByNameAndSize_NotFound() throws Exception {
		// Mock not found
		when(mockProductDao.findByNameAndSize("Juice", "250ml")).thenReturn(null);

		Product result = productService.getProductByNameAndSize("Juice", "250ml");

		assertNull(result);
		verify(mockProductDao).findByNameAndSize("Juice", "250ml");
	}

	@Test
	public void testPopulateEAN13ForExistingProducts_PopulatesMissingEAN13() throws Exception {
		// Prepare product without EAN13
		Product p1 = new Product();
		p1.setId(1L);
		p1.setEan13(null);

		Product p2 = new Product();
		p2.setId(2L);
		p2.setEan13("1234567890123"); // Already valid

		List<Product> mockProducts = Arrays.asList(p1, p2);

		when(mockProductDao.findAll(1, Integer.MAX_VALUE)).thenReturn(mockProducts);

		// Spy on service to verify internal call
		ProductServiceImpl spyService = spy(productService);
		doReturn(true).when(spyService).updateProduct(any(Product.class));

		// Call method
		spyService.populateEAN13ForExistingProducts();

		// Verify update is only called on p1
		verify(spyService, times(1)).updateProduct(argThat(product -> product.getId() == 1L));
		verify(spyService, never()).updateProduct(argThat(product -> product.getId() == 2L));
	}
}
