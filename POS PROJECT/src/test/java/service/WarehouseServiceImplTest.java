package service;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;

import model.Warehouse;
import dao.WarehouseDao;
import service.WarehouseServiceImpl;

public class WarehouseServiceImplTest {
	
	private WarehouseDao mockWarehouseDao;

    private WarehouseServiceImpl warehouseService;

    @BeforeEach
    public void setUp() {
        mockWarehouseDao = mock(WarehouseDao.class);
        warehouseService = new WarehouseServiceImpl(mockWarehouseDao);
    }
    
    @Test
    public void testCreateWarehouse() throws Exception {
        Warehouse warehouse = new Warehouse();
        when(mockWarehouseDao.create(warehouse)).thenReturn(true);

        boolean result = warehouseService.createWarehouse(warehouse);

        assertTrue(result);
        verify(mockWarehouseDao).create(warehouse);
    }

    @Test
    public void testDeleteWarehouse() throws Exception {
        long warehouseId = 1L;
        when(mockWarehouseDao.deleteById(warehouseId)).thenReturn(true);

        boolean result = warehouseService.deleteWarehouse(warehouseId);

        assertTrue(result);
        verify(mockWarehouseDao).deleteById(warehouseId);
    }
    
    @Test
    public void testUpdateWarehouse() throws Exception {
        Warehouse warehouse = new Warehouse();
        when(mockWarehouseDao.update(warehouse)).thenReturn(true);

        boolean result = warehouseService.updateWarehouse(warehouse);

        assertTrue(result);
        verify(mockWarehouseDao).update(warehouse);
    }

    @Test
    public void testGetWarehouse() throws Exception {
        long warehouseId = 1L;
        Warehouse warehouse = new Warehouse();
        when(mockWarehouseDao.findById(warehouseId)).thenReturn(warehouse);

        Warehouse result = warehouseService.getWarehouse(warehouseId);

        assertNotNull(result);
        assertEquals(warehouse, result);
        verify(mockWarehouseDao).findById(warehouseId);
    }
    


    @Test
    public void testGetWarehouseByName() throws Exception {
        String name = "WarehouseName";
        Warehouse warehouse = new Warehouse();
        when(mockWarehouseDao.findByName(name)).thenReturn(warehouse);

        Warehouse result = warehouseService.getWarehouseByName(name);

        assertNotNull(result);
        assertEquals(warehouse, result);
        verify(mockWarehouseDao).findByName(name);
    }

    @Test
    public void testGetWarehouseByShortName() throws Exception {
        String shortName = "WH01";
        Warehouse warehouse = new Warehouse();
        when(mockWarehouseDao.findByShortName(shortName)).thenReturn(warehouse);

        Warehouse result = warehouseService.getWarehouseByShortName(shortName);

        assertNotNull(result);
        assertEquals(warehouse, result);
        verify(mockWarehouseDao).findByShortName(shortName);
    }

    @Test
    public void testCreateWarehouseException() throws Exception {
        Warehouse warehouse = new Warehouse();
        when(mockWarehouseDao.create(warehouse)).thenThrow(new RuntimeException("Database error"));

        boolean result = warehouseService.createWarehouse(warehouse);

        assertFalse(result);
        verify(mockWarehouseDao).create(warehouse);
    }

    @Test
    public void testDeleteWarehouseException() throws Exception {
        long warehouseId = 1L;
        when(mockWarehouseDao.deleteById(warehouseId)).thenThrow(new RuntimeException("Database error"));

        boolean result = warehouseService.deleteWarehouse(warehouseId);

        assertFalse(result);
        verify(mockWarehouseDao).deleteById(warehouseId);
    }

    @Test
    public void testUpdateWarehouseException() throws Exception {
        Warehouse warehouse = new Warehouse();
        when(mockWarehouseDao.update(warehouse)).thenThrow(new RuntimeException("Database error"));

        boolean result = warehouseService.updateWarehouse(warehouse);

        assertFalse(result);
        verify(mockWarehouseDao).update(warehouse);
    }

    @Test
    public void testGetWarehouseException() throws Exception {
        long warehouseId = 1L;
        when(mockWarehouseDao.findById(warehouseId)).thenThrow(new RuntimeException("Database error"));

        Warehouse result = warehouseService.getWarehouse(warehouseId);

        assertNull(result);
        verify(mockWarehouseDao).findById(warehouseId);
    }



    @Test
    public void testGetWarehouseByNameException() throws Exception {
        String name = "WarehouseName";
        when(mockWarehouseDao.findByName(name)).thenThrow(new RuntimeException("Database error"));

        Warehouse result = warehouseService.getWarehouseByName(name);

        assertNull(result);
        verify(mockWarehouseDao).findByName(name);
    }

    @Test
    public void testGetWarehouseByShortNameException() throws Exception {
        String shortName = "WH01";
        when(mockWarehouseDao.findByShortName(shortName)).thenThrow(new RuntimeException("Database error"));

        Warehouse result = warehouseService.getWarehouseByShortName(shortName);

        assertNull(result);
        verify(mockWarehouseDao).findByShortName(shortName);
    }
}
