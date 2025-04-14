package service;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import dao.ProductDao;
import model.Product;
import service.ProductServiceImpl;

public class ProductServiceImplTest {

	private ProductDao mockProductDao;
    private ProductServiceImpl productService;

    @BeforeEach
    public void setUp() {
        mockProductDao = mock(ProductDao.class);
        productService = new ProductServiceImpl(mockProductDao);
    }

    @Test
    public void testCreateProduct_Success() throws Exception {
        Product product = new Product();
        when(mockProductDao.create(product)).thenReturn(true);

        boolean result = productService.createProduct(product);

        assertTrue(result);
        verify(mockProductDao).create(product);
    }

    @Test
    public void testCreateProduct_Exception() throws Exception {
        Product product = new Product();
        when(mockProductDao.create(product)).thenThrow(new RuntimeException("Create failed"));

        boolean result = productService.createProduct(product);

        assertFalse(result);
        verify(mockProductDao).create(product);
    }

    @Test
    public void testUpdateProduct_Success() throws Exception {
        Product product = new Product();
        when(mockProductDao.update(product)).thenReturn(true);

        boolean result = productService.updateProduct(product);

        assertTrue(result);
        verify(mockProductDao).update(product);
    }
    @Test
    public void testDeleteProductById_Success() throws Exception {
        when(mockProductDao.deleteById(1L)).thenReturn(true);

        boolean result = productService.deleteProductById(1L);

        assertTrue(result);
        verify(mockProductDao).deleteById(1L);
    }

    @Test
    public void testDeleteProduct_Success() throws Exception {
        Product product = new Product();
        when(mockProductDao.delete(product)).thenReturn(true);

        boolean result = productService.deleteProduct(product);

        assertTrue(result);
        verify(mockProductDao).delete(product);
    }

    @Test
    public void testGetProductById_Found() throws Exception {
        Product product = new Product();
        product.setId(1L);
        when(mockProductDao.findById(1L)).thenReturn(product);

        Product result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    public void testGetProductById_NotFound() throws Exception {
        when(mockProductDao.findById(2L)).thenReturn(null);

        Product result = productService.getProductById(2L);

        assertNull(result);
    }

    @Test
    public void testGetAllProducts() throws Exception {
        List<Product> products = Arrays.asList(new Product(), new Product());
        when(mockProductDao.findAll()).thenReturn(products);

        List<Product> result = productService.getAllProducts();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testGetProductByName_Found() throws Exception {
        Product product = new Product();
        when(mockProductDao.getProductByName("Milk")).thenReturn(product);

        Product result = productService.getProductByName("Milk");

        assertNotNull(result);
        verify(mockProductDao).getProductByName("Milk");
    }

    @Test
    public void testFindbyEAN13_Found() throws Exception {
        Product product = new Product();
        when(mockProductDao.findByEAN13("1234567890123")).thenReturn(product);

        Product result = productService.findbyEAN13("1234567890123");

        assertNotNull(result);
        verify(mockProductDao).findByEAN13("1234567890123");
    }
}
