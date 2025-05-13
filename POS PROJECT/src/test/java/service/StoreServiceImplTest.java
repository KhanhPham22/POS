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

    private StoreDao mockStoreDao;  // Mocking the StoreDao dependency
    private StoreServiceImpl storeService;  // The service under test

    // Set up the test environment by creating a mock StoreDao and initializing the service
    @BeforeEach
    public void setUp() {
        mockStoreDao = mock(StoreDao.class);  // Mock StoreDao
        storeService = new StoreServiceImpl(mockStoreDao);  // Initialize StoreServiceImpl with mockStoreDao
    }

    // Test case for creating a store successfully
    @Test
    public void testCreateStore_Success() throws Exception {
        Store store = new Store();  // Create a new store object
        when(mockStoreDao.create(store)).thenReturn(true);  // Mock the create method to return true

        boolean result = storeService.createStore(store);  // Call the service method

        assertTrue(result);  // Assert that the result is true
        verify(mockStoreDao).create(store);  // Verify that the create method of mockStoreDao was called with the store
    }

    // Test case for creating a store when an exception occurs
    @Test
    public void testCreateStore_Exception() throws Exception {
        Store store = new Store();  // Create a new store object
        when(mockStoreDao.create(store)).thenThrow(new RuntimeException("Create failed"));  // Mock an exception

        boolean result = storeService.createStore(store);  // Call the service method

        assertFalse(result);  // Assert that the result is false
        verify(mockStoreDao).create(store);  // Verify that the create method of mockStoreDao was called with the store
    }

    // Test case for updating a store successfully
    @Test
    public void testUpdateStore_Success() throws Exception {
        Store store = new Store();  // Create a new store object
        when(mockStoreDao.update(store)).thenReturn(true);  // Mock the update method to return true

        boolean result = storeService.updateStore(store);  // Call the service method

        assertTrue(result);  // Assert that the result is true
        verify(mockStoreDao).update(store);  // Verify that the update method of mockStoreDao was called with the store
    }

    // Test case for updating a store when an exception occurs
    @Test
    public void testUpdateStore_Exception() throws Exception {
        Store store = new Store();  // Create a new store object
        when(mockStoreDao.update(store)).thenThrow(new RuntimeException("Update failed"));  // Mock an exception

        boolean result = storeService.updateStore(store);  // Call the service method

        assertFalse(result);  // Assert that the result is false
        verify(mockStoreDao).update(store);  // Verify that the update method of mockStoreDao was called with the store
    }

    // Test case for deleting a store by ID successfully
    @Test
    public void testDeleteStoreById_Success() throws Exception {
        when(mockStoreDao.deleteById(1L)).thenReturn(true);  // Mock the deleteById method to return true

        boolean result = storeService.deleteStoreById(1L);  // Call the service method

        assertTrue(result);  // Assert that the result is true
        verify(mockStoreDao).deleteById(1L);  // Verify that the deleteById method of mockStoreDao was called with the correct ID
    }

    // Test case for deleting a store by ID when an exception occurs
    @Test
    public void testDeleteStoreById_Exception() throws Exception {
        when(mockStoreDao.deleteById(1L)).thenThrow(new RuntimeException("Delete by ID failed"));  // Mock an exception

        boolean result = storeService.deleteStoreById(1L);  // Call the service method

        assertFalse(result);  // Assert that the result is false
        verify(mockStoreDao).deleteById(1L);  // Verify that the deleteById method of mockStoreDao was called with the correct ID
    }

    // Test case for deleting a store successfully
    @Test
    public void testDeleteStore_Success() throws Exception {
        Store store = new Store();  // Create a new store object
        when(mockStoreDao.delete(store)).thenReturn(true);  // Mock the delete method to return true

        boolean result = storeService.deleteStore(store);  // Call the service method

        assertTrue(result);  // Assert that the result is true
        verify(mockStoreDao).delete(store);  // Verify that the delete method of mockStoreDao was called with the store
    }

    // Test case for deleting a store when an exception occurs
    @Test
    public void testDeleteStore_Exception() throws Exception {
        Store store = new Store();  // Create a new store object
        when(mockStoreDao.delete(store)).thenThrow(new RuntimeException("Delete failed"));  // Mock an exception

        boolean result = storeService.deleteStore(store);  // Call the service method

        assertFalse(result);  // Assert that the result is false
        verify(mockStoreDao).delete(store);  // Verify that the delete method of mockStoreDao was called with the store
    }

    // Test case for getting a store by ID when the store is found
    @Test
    public void testGetStoreById_Found() throws Exception {
        Store store = new Store();  // Create a new store object
        store.setId(1L);  // Set the ID of the store
        when(mockStoreDao.findById(1L)).thenReturn(store);  // Mock the findById method to return the store

        Store result = storeService.getStoreById(1L);  // Call the service method

        assertNotNull(result);  // Assert that the result is not null
        assertEquals(1L, result.getId());  // Assert that the store ID matches the expected ID
    }

    // Test case for getting a store by ID when the store is not found
    @Test
    public void testGetStoreById_NotFound() throws Exception {
        when(mockStoreDao.findById(2L)).thenReturn(null);  // Mock the findById method to return null

        Store result = storeService.getStoreById(2L);  // Call the service method

        assertNull(result);  // Assert that the result is null, meaning the store was not found
    }

    // Test case for getting all stores successfully
    @Test
    public void testGetAllStores_Success() throws Exception {
        List<Store> stores = Arrays.asList(new Store(), new Store());  // Create a list of stores
        when(mockStoreDao.findAll(1, 10)).thenReturn(stores);  // Mock the findAll method to return the list of stores

        List<Store> result = storeService.getAllStores(1, 10);  // Call the service method

        assertNotNull(result);  // Assert that the result is not null
        assertEquals(2, result.size());  // Assert that the size of the result list is 2
        verify(mockStoreDao).findAll(1, 10);  // Verify that the findAll method of mockStoreDao was called with correct parameters
    }

    // Test case for getting all stores when an exception occurs
    @Test
    public void testGetAllStores_Exception() throws Exception {
        when(mockStoreDao.findAll(1, 10)).thenThrow(new RuntimeException("Get all stores failed"));  // Mock an exception

        List<Store> result = storeService.getAllStores(1, 10);  // Call the service method

        assertNull(result);  // Assert that the result is null because an exception was thrown
        verify(mockStoreDao).findAll(1, 10);  // Verify that the findAll method of mockStoreDao was called with correct parameters
    }

    // Test case for getting stores by city successfully
    @Test
    public void testGetStoresByCity_Success() throws Exception {
        List<Store> stores = Arrays.asList(new Store(), new Store());  // Create a list of stores
        when(mockStoreDao.findByCity("Hanoi")).thenReturn(stores);  // Mock the findByCity method to return the list of stores

        List<Store> result = storeService.getStoresByCity("Hanoi");  // Call the service method

        assertNotNull(result);  // Assert that the result is not null
        verify(mockStoreDao).findByCity("Hanoi");  // Verify that the findByCity method of mockStoreDao was called with the correct city
    }

    // Test case for getting stores by city when an exception occurs
    @Test
    public void testGetStoresByCity_Exception() throws Exception {
        when(mockStoreDao.findByCity("Hanoi")).thenThrow(new RuntimeException("Get stores by city failed"));  // Mock an exception

        List<Store> result = storeService.getStoresByCity("Hanoi");  // Call the service method

        assertNull(result);  // Assert that the result is null because an exception was thrown
        verify(mockStoreDao).findByCity("Hanoi");  // Verify that the findByCity method of mockStoreDao was called with the correct city
    }

    // Test case for getting stores by name successfully
    @Test
    public void testGetStoresByName_Success() throws Exception {
        List<Store> stores = Arrays.asList(new Store());  // Create a list of stores
        when(mockStoreDao.findByName("Circle K")).thenReturn(stores);  // Mock the findByName method to return the list of stores

        List<Store> result = storeService.getStoresByName("Circle K");  // Call the service method

        assertNotNull(result);  // Assert that the result is not null
        verify(mockStoreDao).findByName("Circle K");  // Verify that the findByName method of mockStoreDao was called with the correct name
    }

    // Test case for getting stores by name when an exception occurs
    @Test
    public void testGetStoresByName_Exception() throws Exception {
        when(mockStoreDao.findByName("Circle K")).thenThrow(new RuntimeException("Get stores by name failed"));  // Mock an exception

        List<Store> result = storeService.getStoresByName("Circle K");  // Call the service method

        assertNull(result);  // Assert that the result is null because an exception was thrown
        verify(mockStoreDao).findByName("Circle K");  // Verify that the findByName method of mockStoreDao was called with the correct name
    }
}