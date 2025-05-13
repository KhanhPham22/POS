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

	// Test case for successful item creation
	@Test
	public void testCreateItem_Success() throws Exception {
		Item item = new Item();
		when(mockItemDao.create(item)).thenReturn(true);

		boolean result = itemService.createItem(item);

		assertTrue(result);
		verify(mockItemDao).create(item);
	}

	// Test case for exception during item creation
	@Test
	public void testCreateItem_Exception() throws Exception {
		Item item = new Item();
		when(mockItemDao.create(item)).thenThrow(new RuntimeException("Create failed"));

		boolean result = itemService.createItem(item);

		assertFalse(result);
		verify(mockItemDao).create(item);
	}

	// Test case for successful item deletion
	@Test
	public void testDeleteItem_Success() throws Exception {
		Item item = new Item();
		item.setId(1L);
		when(mockItemDao.findById(1L)).thenReturn(item);
		when(mockItemDao.delete(item)).thenReturn(true);

		boolean result = itemService.deleteItem(1L);

		assertTrue(result);
		verify(mockItemDao).findById(1L);
		verify(mockItemDao).delete(item);
	}

	// Test case for exception during item deletion
	@Test
	public void testDeleteItem_Exception() throws Exception {
		when(mockItemDao.findById(1L)).thenThrow(new RuntimeException("Item not found"));

		boolean result = itemService.deleteItem(1L);

		assertFalse(result);
		verify(mockItemDao).findById(1L);
	}

	// Test case for successful item update
	@Test
	public void testUpdateItem_Success() throws Exception {
		Item item = new Item();
		item.setId(1L);
		when(mockItemDao.update(item)).thenReturn(true);

		boolean result = itemService.updateItem(item);

		assertTrue(result);
		verify(mockItemDao).update(item);
	}

	// Test case for exception during item update
	@Test
	public void testUpdateItem_Exception() throws Exception {
		Item item = new Item();
		item.setId(1L);
		when(mockItemDao.update(item)).thenThrow(new RuntimeException("Update failed"));

		boolean result = itemService.updateItem(item);

		assertFalse(result);
		verify(mockItemDao).update(item);
	}

	// Test case for successful update of multiple items
	@Test
	public void testUpdateListItem_Success() throws Exception {
		Item[] items = { new Item(), new Item() };
		when(mockItemDao.update(any(Item.class))).thenReturn(true);

		boolean result = itemService.updateListItem(items);

		assertTrue(result);
		verify(mockItemDao, times(items.length)).update(any(Item.class));
	}

	// Test case for partial failure during bulk item update
	@Test
	public void testUpdateListItem_Exception() throws Exception {
		Item[] items = { new Item(), new Item() };
		when(mockItemDao.update(any(Item.class))).thenThrow(new RuntimeException("Update failed")).thenReturn(true);

		boolean result = itemService.updateListItem(items);

		assertFalse(result);
		verify(mockItemDao, times(2)).update(any(Item.class));
	}

	// Test case for successful retrieval of item by ID
	@Test
	public void testGetItem_Success() throws Exception {
		Item item = new Item();
		item.setId(1L);
		when(mockItemDao.findById(1L)).thenReturn(item);

		Item result = itemService.getItem(1L);

		assertNotNull(result);
		assertEquals(1L, result.getId());
		verify(mockItemDao).findById(1L);
	}

	// Test case for item not found by ID
	@Test
	public void testGetItem_NotFound() throws Exception {
		when(mockItemDao.findById(2L)).thenReturn(null);

		Item result = itemService.getItem(2L);

		assertNull(result);
		verify(mockItemDao).findById(2L);
	}

	// ✅ Test for retrieving all items with pagination
	@Test
	public void testGetAllItems_Success() throws Exception {
		List<Item> items = Arrays.asList(new Item(), new Item());
		when(mockItemDao.findAll(1, 10)).thenReturn(items);

		List<Item> result = itemService.getAllItems(1, 10);

		assertNotNull(result);
		assertEquals(2, result.size());
		verify(mockItemDao).findAll(1, 10);
	}

	// ❌ Test for exception while retrieving all items
	@Test
	public void testGetAllItems_Exception() throws Exception {
		when(mockItemDao.findAll(1, 10)).thenThrow(new RuntimeException("DB error"));

		List<Item> result = itemService.getAllItems(1, 10);

		assertNotNull(result);
		assertTrue(result.isEmpty());
		verify(mockItemDao).findAll(1, 10);
	}

	// ✅ Test for retrieving items by name
	@Test
	public void testGetItemByName_Success() throws Exception {
		List<Item> items = Arrays.asList(new Item(), new Item());
		when(mockItemDao.findByName("Sugar")).thenReturn(items);

		List<Item> result = itemService.getItemByName("Sugar");

		assertNotNull(result);
		assertEquals(2, result.size());
		verify(mockItemDao).findByName("Sugar");
	}

	// ❌ Test for exception during retrieval by name
	@Test
	public void testGetItemByName_Exception() throws Exception {
		when(mockItemDao.findByName("Sugar")).thenThrow(new RuntimeException("Search failed"));

		List<Item> result = itemService.getItemByName("Sugar");

		assertNotNull(result);
		assertTrue(result.isEmpty());
		verify(mockItemDao).findByName("Sugar");
	}

	// ✅ Test for retrieving items by supplier ID
	@Test
	public void testGetItemsBySupplierId_Success() throws Exception {
		List<Item> items = Arrays.asList(new Item(), new Item());
		when(mockItemDao.findBySupplierId(1L, 0, 5)).thenReturn(items);

		List<Item> result = itemService.getItemsBySupplierId(1L, 0, 5);

		assertNotNull(result);
		assertEquals(2, result.size());
		verify(mockItemDao).findBySupplierId(1L, 0, 5);
	}

	// ❌ Test for exception while retrieving by supplier ID
	@Test
	public void testGetItemsBySupplierId_Exception() throws Exception {
		when(mockItemDao.findBySupplierId(1L, 0, 5)).thenThrow(new RuntimeException("DB error"));

		List<Item> result = itemService.getItemsBySupplierId(1L, 0, 5);

		assertNotNull(result);
		assertTrue(result.isEmpty());
		verify(mockItemDao).findBySupplierId(1L, 0, 5);
	}
}
