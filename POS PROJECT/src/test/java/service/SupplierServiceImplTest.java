package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Item;
import model.Supplier;
import model.Warehouse;
import dao.SupplierDao;
import service.SupplierServiceImpl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SupplierServiceImplTest {

	private SupplierDao mockSupplierDao;
	private SupplierServiceImpl supplierService;

	// Setup method to initialize mock objects before each test
	@BeforeEach
	public void setUp() {
		mockSupplierDao = mock(SupplierDao.class); // Mocking the SupplierDao
		supplierService = new SupplierServiceImpl(mockSupplierDao); // Initializing the SupplierServiceImpl with the
																	// mocked DAO
	}

	// Test case for the createSupplier method, ensuring it works without exceptions
	@Test
	public void testCreateSupplier() throws Exception {
		Supplier supplier = new Supplier();
		when(mockSupplierDao.create(supplier)).thenReturn(true); // Mocking the successful creation of a supplier

		boolean result = supplierService.createSupplier(supplier); // Calling the method

		assertTrue(result); // Asserting that the result is true
		verify(mockSupplierDao).create(supplier); // Verifying that the DAO method was called
	}

	// Test case for the updateSupplier method, ensuring it works without exceptions
	@Test
	public void testUpdateSupplier() throws Exception {
		Supplier supplier = new Supplier();
		when(mockSupplierDao.update(supplier)).thenReturn(true); // Mocking the successful update of a supplier

		boolean result = supplierService.updateSupplier(supplier); // Calling the method

		assertTrue(result); // Asserting that the result is true
		verify(mockSupplierDao).update(supplier); // Verifying that the DAO method was called
	}

	// Test case for the deleteSupplierById method, ensuring it works without
	// exceptions
	@Test
	public void testDeleteSupplierById() throws Exception {
		Long supplierId = 1L;
		when(mockSupplierDao.deleteById(supplierId)).thenReturn(true); // Mocking the successful deletion by ID

		boolean result = supplierService.deleteSupplierById(supplierId); // Calling the method

		assertTrue(result); // Asserting that the result is true
		verify(mockSupplierDao).deleteById(supplierId); // Verifying that the DAO method was called
	}

	// Test case for the deleteSupplier method, ensuring it works without exceptions
	@Test
	public void testDeleteSupplier() throws Exception {
		Supplier supplier = new Supplier();
		when(mockSupplierDao.delete(supplier)).thenReturn(true); // Mocking the successful deletion of a supplier

		boolean result = supplierService.deleteSupplier(supplier); // Calling the method

		assertTrue(result); // Asserting that the result is true
		verify(mockSupplierDao).delete(supplier); // Verifying that the DAO method was called
	}

	// Test case for the getSupplierById method, ensuring it returns a supplier when
	// found
	@Test
	public void testGetSupplierById() throws Exception {
		Long supplierId = 1L;
		Supplier supplier = new Supplier();
		when(mockSupplierDao.findById(supplierId)).thenReturn(supplier); // Mocking the retrieval of a supplier by ID

		Supplier result = supplierService.getSupplierById(supplierId); // Calling the method

		assertNotNull(result); // Asserting that the result is not null
		assertEquals(supplier, result); // Asserting that the returned supplier matches the expected supplier
		verify(mockSupplierDao).findById(supplierId); // Verifying that the DAO method was called
	}

	// Test case for the getSuppliersByName method, ensuring it returns a list of
	// suppliers
	@Test
	public void testGetSuppliersByName() throws Exception {
		String name = "SupplierName";
		List<Supplier> suppliers = Arrays.asList(new Supplier(), new Supplier()); // Mocking a list of suppliers with
																					// the given name
		when(mockSupplierDao.findByName(name)).thenReturn(suppliers); // Mocking the DAO method to return the list

		List<Supplier> result = supplierService.getSuppliersByName(name); // Calling the method

		assertNotNull(result); // Asserting that the result is not null
		assertEquals(2, result.size()); // Asserting that the result contains exactly 2 suppliers
		verify(mockSupplierDao).findByName(name); // Verifying that the DAO method was called
	}

	// Test case for the createSupplier method, ensuring it handles exceptions
	// properly
	@Test
	public void testCreateSupplierException() throws Exception {
		Supplier supplier = new Supplier();
		when(mockSupplierDao.create(supplier)).thenThrow(new RuntimeException("Database error")); // Mocking an
																									// exception

		boolean result = supplierService.createSupplier(supplier); // Calling the method

		assertFalse(result); // Asserting that the result is false due to the exception
		verify(mockSupplierDao).create(supplier); // Verifying that the DAO method was called
	}

	// Test case for the updateSupplier method, ensuring it handles exceptions
	// properly
	@Test
	public void testUpdateSupplierException() throws Exception {
		Supplier supplier = new Supplier();
		when(mockSupplierDao.update(supplier)).thenThrow(new RuntimeException("Database error")); // Mocking an
																									// exception

		boolean result = supplierService.updateSupplier(supplier); // Calling the method

		assertFalse(result); // Asserting that the result is false due to the exception
		verify(mockSupplierDao).update(supplier); // Verifying that the DAO method was called
	}

	// Test case for the deleteSupplierById method, ensuring it handles exceptions
	// properly
	@Test
	public void testDeleteSupplierByIdException() throws Exception {
		Long supplierId = 1L;
		when(mockSupplierDao.deleteById(supplierId)).thenThrow(new RuntimeException("Database error")); // Mocking an
																										// exception

		boolean result = supplierService.deleteSupplierById(supplierId); // Calling the method

		assertFalse(result); // Asserting that the result is false due to the exception
		verify(mockSupplierDao).deleteById(supplierId); // Verifying that the DAO method was called
	}

	// Test case for the deleteSupplier method, ensuring it handles exceptions
	// properly
	@Test
	public void testDeleteSupplierException() throws Exception {
		Supplier supplier = new Supplier();
		when(mockSupplierDao.delete(supplier)).thenThrow(new RuntimeException("Database error")); // Mocking an
																									// exception

		boolean result = supplierService.deleteSupplier(supplier); // Calling the method

		assertFalse(result); // Asserting that the result is false due to the exception
		verify(mockSupplierDao).delete(supplier); // Verifying that the DAO method was called
	}

	// Test case for the getSupplierById method, ensuring it handles exceptions
	// properly
	@Test
	public void testGetSupplierByIdException() throws Exception {
		Long supplierId = 1L;
		when(mockSupplierDao.findById(supplierId)).thenThrow(new RuntimeException("Database error")); // Mocking an
																										// exception

		Supplier result = supplierService.getSupplierById(supplierId); // Calling the method

		assertNull(result); // Asserting that the result is null due to the exception
		verify(mockSupplierDao).findById(supplierId); // Verifying that the DAO method was called
	}

	// Test case for the getSuppliersByName method, ensuring it handles exceptions
	// properly
	@Test
	public void testGetSuppliersByNameException() throws Exception {
		String name = "SupplierName";
		when(mockSupplierDao.findByName(name)).thenThrow(new RuntimeException("Database error")); // Mocking an
																									// exception

		List<Supplier> result = supplierService.getSuppliersByName(name); // Calling the method

		assertNull(result); // Asserting that the result is null due to the exception
		verify(mockSupplierDao).findByName(name); // Verifying that the DAO method was called
	}

	// Test case for the getItemsBySupplierId method, ensuring it returns the items
	// for a supplier
	@Test
	public void testGetItemsBySupplierId() throws Exception {
		Long supplierId = 1L;
		Supplier supplier = new Supplier();
		Set<Item> items = new HashSet<>();
		supplier.setItems(items); // Mocking items for the supplier
		when(mockSupplierDao.findById(supplierId)).thenReturn(supplier); // Mocking the retrieval of the supplier

		Set<Item> result = supplierService.getItemsBySupplierId(supplierId); // Calling the method

		assertNotNull(result); // Asserting that the result is not null
		assertEquals(items, result); // Asserting that the items match the mocked items
		verify(mockSupplierDao).findById(supplierId); // Verifying that the DAO method was called
	}

	// Test case for the getWarehouseBySupplierId method, ensuring it returns the
	// warehouses for a supplier
	@Test
	public void testGetWarehouseBySupplierId() throws Exception {
		Long supplierId = 1L;
		Supplier supplier = new Supplier();
		Set<Warehouse> warehouses = new HashSet<>();
		supplier.setWarehouseImports(warehouses); // Mocking warehouses for the supplier
		when(mockSupplierDao.findById(supplierId)).thenReturn(supplier); // Mocking the retrieval of the supplier

		Set<Warehouse> result = supplierService.getWarehouseBySupplierId(supplierId); // Calling the method

		assertNotNull(result); // Asserting that the result is not null
		assertEquals(warehouses, result); // Asserting that the warehouses match the mocked warehouses
		verify(mockSupplierDao).findById(supplierId); // Verifying that the DAO method was called
	}
}
