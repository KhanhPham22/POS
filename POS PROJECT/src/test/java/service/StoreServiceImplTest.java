package service;
import dao.StoreDao;
import model.Store;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.StoreServiceImpl;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StoreServiceImplTest {

    private StoreDao mockStoreDao;
    private StoreServiceImpl storeService;

    @BeforeEach
    public void setUp() {
        mockStoreDao = mock(StoreDao.class);
        storeService = new StoreServiceImpl(mockStoreDao);
    }

    @Test
    public void testCreateStore_Success() throws Exception {
        Store store = new Store();
        when(mockStoreDao.create(store)).thenReturn(true);

        boolean result = storeService.createStore(store);

        assertTrue(result);
        verify(mockStoreDao).create(store);
    }

    @Test
    public void testCreateStore_Exception() throws Exception {
        Store store = new Store();
        when(mockStoreDao.create(store)).thenThrow(new RuntimeException("Create failed"));

        boolean result = storeService.createStore(store);

        assertFalse(result);
        verify(mockStoreDao).create(store);
    }

    @Test
    public void testUpdateStore_Success() throws Exception {
        Store store = new Store();
        when(mockStoreDao.update(store)).thenReturn(true);

        boolean result = storeService.updateStore(store);

        assertTrue(result);
        verify(mockStoreDao).update(store);
    }

    @Test
    public void testDeleteStoreById_Success() throws Exception {
        when(mockStoreDao.deleteById(1L)).thenReturn(true);

        boolean result = storeService.deleteStoreById(1L);

        assertTrue(result);
        verify(mockStoreDao).deleteById(1L);
    }

    @Test
    public void testDeleteStore_Success() throws Exception {
        Store store = new Store();
        when(mockStoreDao.delete(store)).thenReturn(true);

        boolean result = storeService.deleteStore(store);

        assertTrue(result);
        verify(mockStoreDao).delete(store);
    }

    @Test
    public void testGetStoreById_Found() throws Exception {
        Store store = new Store();
        store.setId(1L);
        when(mockStoreDao.findById(1L)).thenReturn(store);

        Store result = storeService.getStoreById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    public void testGetStoreById_NotFound() throws Exception {
        when(mockStoreDao.findById(2L)).thenReturn(null);

        Store result = storeService.getStoreById(2L);

        assertNull(result);
    }

//    @Test
//    public void testGetAllStores() throws Exception {
//        List<Store> stores = Arrays.asList(new Store(), new Store());
//        when(mockStoreDao.findAll()).thenReturn(stores);
//
//        List<Store> result = storeService.getAllStores();
//
//        assertNotNull(result);
//        assertEquals(2, result.size());
//    }

    @Test
    public void testGetStoresByCity() throws Exception {
        List<Store> stores = Arrays.asList(new Store(), new Store());
        when(mockStoreDao.findByCity("Hanoi")).thenReturn(stores);

        List<Store> result = storeService.getStoresByCity("Hanoi");

        assertNotNull(result);
        verify(mockStoreDao).findByCity("Hanoi");
    }

    @Test
    public void testGetStoresByName() throws Exception {
        List<Store> stores = Arrays.asList(new Store());
        when(mockStoreDao.findByName("Circle K")).thenReturn(stores);

        List<Store> result = storeService.getStoresByName("Circle K");

        assertNotNull(result);
        verify(mockStoreDao).findByName("Circle K");
    }
}
