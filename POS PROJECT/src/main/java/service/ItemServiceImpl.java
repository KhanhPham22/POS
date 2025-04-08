package service;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.ItemDao;
import model.Item;

public class ItemServiceImpl implements ItemService{
	private static final Logger Log = LogManager.getLogger(ItemServiceImpl.class);

	private ItemDao itemDao;
	
	public ItemServiceImpl(ItemDao itemDao) {
		this.itemDao = itemDao;
	}
	
	@Override
	public boolean createItem(Item item)
			 {
		try {
			itemDao.create(item);
			Log.info("Item created successfully");
			return true;
		} catch (Exception e) {
			Log.error("Error while creating item", e);
			return false;
		}
	}

	@Override
	public boolean deleteItem(long itemId)  {
		try {
			
			Item item = itemDao.findById(itemId);
			itemDao.delete(item);
			Log.info("Item with id: " + itemId + " deleted successfully");
			return true;
		} catch (Exception e) {
			Log.error("Error while deleting item with id: " + itemId, e);
			return false;
		}
	}

	@Override
	public boolean updateItem(Item newItem)  {
		try {
			
			itemDao.update(newItem);
			Log.info("Item with id: " + newItem.getItemId() + " updated successfully");
			return true;
		} catch (Exception e) {
			Log.error("Error while updating item with id: " + newItem.getItemId(), e);
			return false;
		}
	}

	@Override
	public boolean updateListItem(Item[] items)  {
		try {
			for (Item item : items) {
				itemDao.update(item);
			}
			Log.info("Items updated successfully");
			return true;
		} catch (Exception e) {
			Log.error("Error while updating items", e);
			return false;
		}
	}

	@Override
	public Item getItem(long itemId)  {
		try {
			
			Item item = itemDao.findById(itemId);
			Log.info("Item with id: " + itemId + " retrieved successfully");
			return item;
		} catch (Exception e) {
			Log.error("Error while retrieving item with id: " + itemId, e);
			return null;
		}
	}

	@Override
	public List<Item> getAllItems()  {
		try {
			
			List<Item> items = itemDao.findAll();
			Log.info("All items retrieved successfully");
			return items;
		} catch (Exception e) {
			Log.error("Error while retrieving all items", e);
			return new ArrayList<Item>();
		}

	}
	@Override
	public List<Item> getItemByName(String input)  {
		try {
			
			List<Item> items = itemDao.findByName(input);
			Log.info("Items get by Name retrieved successfully");
			return items;
		} catch (Exception e) {
			Log.error("Error while retrieving Items get by name", e);
			return new ArrayList<Item>();
		}

	}

	@Override
	public Item findbyEAN13(String ean13) {
		try {
			
			Item item =  itemDao.findByEAN13(ean13);
			Log.info("Find Item by EAN-13 successfully");
			return item;
		} catch (Exception e) {
			Log.error("Error while finding Item by EAN-13", e);
			return null;
		}
	}
	@Override
	public List<Item> findItem(String input) {
		try {

			List<Item> items = itemDao.findItem(input);
			Log.info("Find Item successfully");
			return items;
		} catch (Exception e) {
			Log.error("Error while finding Item", e);
			return new ArrayList<Item>();
		}
	}
	
}
