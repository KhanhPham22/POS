package service;
import dao.ItemDao;
import model.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ItemServiceImpl;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ItemServiceImplTest {

    private ItemDao mockItemDao;
    private ItemServiceImpl itemService;

    @BeforeEach
    public void setUp() {
        mockItemDao = mock(ItemDao.class);
        itemService = new ItemServiceImpl(mockItemDao);
    }

    @Test
    public void testCreateItem_Success() throws Exception {
        Item item = new Item();
        when(mockItemDao.create(item)).thenReturn(true);

        boolean result = itemService.createItem(item);

        assertTrue(result);
        verify(mockItemDao).create(item);
    }

    @Test
    public void testCreateItem_Exception() throws Exception {
        Item item = new Item();
        when(mockItemDao.create(item)).thenThrow(new RuntimeException("Create failed"));

        boolean result = itemService.createItem(item);

        assertFalse(result);
        verify(mockItemDao).create(item);
    }

    @Test
    public void testDeleteItem_Success() throws Exception {
        Item item = new Item();
        item.setId(1L);
        when(mockItemDao.findById(1L)).thenReturn(item);
        when(mockItemDao.delete(item)).thenReturn(true);

        boolean result = itemService.deleteItem(1L);

        assertTrue(result);
        verify(mockItemDao).delete(item);
    }

    @Test
    public void testDeleteItem_Exception() throws Exception {
        when(mockItemDao.findById(1L)).thenThrow(new RuntimeException("Item not found"));

        boolean result = itemService.deleteItem(1L);

        assertFalse(result);
        verify(mockItemDao).findById(1L);
    }

    @Test
    public void testUpdateItem_Success() throws Exception {
        Item item = new Item();
        item.setId(1L);
        when(mockItemDao.update(item)).thenReturn(true);

        boolean result = itemService.updateItem(item);

        assertTrue(result);
        verify(mockItemDao).update(item);
    }

    @Test
    public void testUpdateItem_Exception() throws Exception {
        Item item = new Item();
        item.setId(1L);
        when(mockItemDao.update(item)).thenThrow(new RuntimeException("Update failed"));

        boolean result = itemService.updateItem(item);

        assertFalse(result);
        verify(mockItemDao).update(item);
    }

    @Test
    public void testUpdateListItem_Success() throws Exception {
        Item[] items = {new Item(), new Item()};

        // Mocking update method to return true for each item
        when(mockItemDao.update(any(Item.class))).thenReturn(true);  // Assuming update returns a boolean

        boolean result = itemService.updateListItem(items);

        assertTrue(result);
        verify(mockItemDao, times(items.length)).update(any(Item.class));
    }

    @Test
    public void testUpdateListItem_Exception() throws Exception {
        Item[] items = {new Item(), new Item()};

     // Mocking update method to throw an exception for the first item and return true for the second item
        when(mockItemDao.update(any(Item.class)))
            .thenThrow(new RuntimeException("Update failed")) // First call throws an exception
            .thenReturn(true); // Second call returns true
        boolean result = itemService.updateListItem(items);

        assertFalse(result);
        verify(mockItemDao, times(2)).update(any(Item.class));
    }


    

    @Test
    public void testGetItem_Success() throws Exception {
        Item item = new Item();
        item.setId(1L);
        when(mockItemDao.findById(1L)).thenReturn(item);

        Item result = itemService.getItem(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    public void testGetItem_NotFound() throws Exception {
        when(mockItemDao.findById(2L)).thenReturn(null);

        Item result = itemService.getItem(2L);

        assertNull(result);
    }

//    @Test
//    public void testGetAllItems() throws Exception {
//        List<Item> items = Arrays.asList(new Item(), new Item());
//        when(mockItemDao.findAll()).thenReturn(items);
//
//        List<Item> result = itemService.getAllItems();
//
//        assertNotNull(result);
//        assertEquals(2, result.size());
//    }

    @Test
    public void testGetItemByName() throws Exception {
        List<Item> items = Arrays.asList(new Item(), new Item());
        when(mockItemDao.findByName("Sugar")).thenReturn(items);

        List<Item> result = itemService.getItemByName("Sugar");

        assertNotNull(result);
        verify(mockItemDao).findByName("Sugar");
    }

    
}
