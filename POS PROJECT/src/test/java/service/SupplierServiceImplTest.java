package service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Supplier;
import dao.SupplierDao;
import service.SupplierServiceImpl;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SupplierServiceImplTest {

	 private SupplierDao mockSupplierDao;

	 private SupplierServiceImpl supplierService;
	    
	 @BeforeEach
	    public void setUp() {
	        mockSupplierDao = mock(SupplierDao.class);
	        supplierService = new SupplierServiceImpl(mockSupplierDao);
	    }
	 
	 @Test
	    public void testCreateSupplier() throws Exception {
	        Supplier supplier = new Supplier();
	        when(mockSupplierDao.create(supplier)).thenReturn(true);

	        boolean result = supplierService.createSupplier(supplier);

	        assertTrue(result);
	        verify(mockSupplierDao).create(supplier);
	    }
	 
	 @Test
	    public void testUpdateSupplier() throws Exception {
	        Supplier supplier = new Supplier();
	        when(mockSupplierDao.update(supplier)).thenReturn(true);

	        boolean result = supplierService.updateSupplier(supplier);

	        assertTrue(result);
	        verify(mockSupplierDao).update(supplier);
	    }
	 
	 @Test
	    public void testDeleteSupplierById() throws Exception {
	        Long supplierId = 1L;
	        when(mockSupplierDao.deleteById(supplierId)).thenReturn(true);

	        boolean result = supplierService.deleteSupplierById(supplierId);

	        assertTrue(result);
	        verify(mockSupplierDao).deleteById(supplierId);
	    }

	    @Test
	    public void testDeleteSupplier() throws Exception {
	        Supplier supplier = new Supplier();
	        when(mockSupplierDao.delete(supplier)).thenReturn(true);

	        boolean result = supplierService.deleteSupplier(supplier);

	        assertTrue(result);
	        verify(mockSupplierDao).delete(supplier);
	    }

	    @Test
	    public void testGetSupplierById() throws Exception {
	        Long supplierId = 1L;
	        Supplier supplier = new Supplier();
	        when(mockSupplierDao.findById(supplierId)).thenReturn(supplier);

	        Supplier result = supplierService.getSupplierById(supplierId);

	        assertNotNull(result);
	        assertEquals(supplier, result);
	        verify(mockSupplierDao).findById(supplierId);
	    }

	    @Test
	    public void testGetAllSuppliers() throws Exception {
	        List<Supplier> suppliers = Arrays.asList(new Supplier(), new Supplier());
	        when(mockSupplierDao.findAll()).thenReturn(suppliers);

	        List<Supplier> result = supplierService.getAllSuppliers();

	        assertNotNull(result);
	        assertEquals(2, result.size());
	        verify(mockSupplierDao).findAll();
	    }

	    @Test
	    public void testGetSuppliersByName() throws Exception {
	        String name = "SupplierName";
	        List<Supplier> suppliers = Arrays.asList(new Supplier(), new Supplier());
	        when(mockSupplierDao.findByName(name)).thenReturn(suppliers);

	        List<Supplier> result = supplierService.getSuppliersByName(name);

	        assertNotNull(result);
	        assertEquals(2, result.size());
	        verify(mockSupplierDao).findByName(name);
	    }

	    @Test
	    public void testCreateSupplierException() throws Exception {
	        Supplier supplier = new Supplier();
	        when(mockSupplierDao.create(supplier)).thenThrow(new RuntimeException("Database error"));

	        boolean result = supplierService.createSupplier(supplier);

	        assertFalse(result);
	        verify(mockSupplierDao).create(supplier);
	    }

	    @Test
	    public void testUpdateSupplierException() throws Exception {
	        Supplier supplier = new Supplier();
	        when(mockSupplierDao.update(supplier)).thenThrow(new RuntimeException("Database error"));

	        boolean result = supplierService.updateSupplier(supplier);

	        assertFalse(result);
	        verify(mockSupplierDao).update(supplier);
	    }

	    @Test
	    public void testDeleteSupplierByIdException() throws Exception {
	        Long supplierId = 1L;
	        when(mockSupplierDao.deleteById(supplierId)).thenThrow(new RuntimeException("Database error"));

	        boolean result = supplierService.deleteSupplierById(supplierId);

	        assertFalse(result);
	        verify(mockSupplierDao).deleteById(supplierId);
	    }

	    @Test
	    public void testDeleteSupplierException() throws Exception {
	        Supplier supplier = new Supplier();
	        when(mockSupplierDao.delete(supplier)).thenThrow(new RuntimeException("Database error"));

	        boolean result = supplierService.deleteSupplier(supplier);

	        assertFalse(result);
	        verify(mockSupplierDao).delete(supplier);
	    }

	    @Test
	    public void testGetSupplierByIdException() throws Exception {
	        Long supplierId = 1L;
	        when(mockSupplierDao.findById(supplierId)).thenThrow(new RuntimeException("Database error"));

	        Supplier result = supplierService.getSupplierById(supplierId);

	        assertNull(result);
	        verify(mockSupplierDao).findById(supplierId);
	    }

	    @Test
	    public void testGetAllSuppliersException() throws Exception {
	        when(mockSupplierDao.findAll()).thenThrow(new RuntimeException("Database error"));

	        List<Supplier> result = supplierService.getAllSuppliers();

	        assertNull(result);
	        verify(mockSupplierDao).findAll();
	    }

	    @Test
	    public void testGetSuppliersByNameException() throws Exception {
	        String name = "SupplierName";
	        when(mockSupplierDao.findByName(name)).thenThrow(new RuntimeException("Database error"));

	        List<Supplier> result = supplierService.getSuppliersByName(name);

	        assertNull(result);
	        verify(mockSupplierDao).findByName(name);
	    }
}
