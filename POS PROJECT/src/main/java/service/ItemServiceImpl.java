package service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.ItemDao;
import model.Item;
import model.Supplier;

public class ItemServiceImpl implements ItemService {
	private static final Logger Log = LogManager.getLogger(ItemServiceImpl.class);

	private ItemDao itemDao;

	public ItemServiceImpl(ItemDao itemDao) {
		this.itemDao = itemDao;
		this.itemDao.setClass(Item.class);
	}

	@Override
	public boolean createItem(Item item) {
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
	public boolean deleteItem(long itemId) {
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
	public boolean updateItem(Item newItem) {
		try {

			itemDao.update(newItem);
			Log.info("Item with id: " + newItem.getId() + " updated successfully");
			return true;
		} catch (Exception e) {
			Log.error("Error while updating item with id: " + newItem.getId(), e);
			return false;
		}
	}

	@Override
	public boolean updateListItem(Item[] items) {
		boolean updateFailed = false;

		for (Item item : items) {
			try {
				itemDao.update(item);
			} catch (Exception e) {
				Log.error("Error while updating item", e);
				updateFailed = true; // Flag the update as failed
			}
		}

		if (updateFailed) {
			return false; // Return false if any update failed
		}

		Log.info("Items updated successfully");
		return true;
	}

	@Override
	public Item getItem(long itemId) {
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
	public List<Item> getAllItems(int pageNumber, int pageSize) {
		try {
			return itemDao.findAll(pageNumber, pageSize); // Gọi phương thức phân trang trong dao
		} catch (Exception e) {
			Log.error("Failed to retrieve all items", e);
			return Collections.emptyList();
		}
	}

	@Override
	public List<Item> getItemByName(String input) {
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
	public List<Item> getItemsBySupplierId(long supplierId, int pageNumber, int pageSize) {
	    try {
	        List<Item> items = itemDao.findBySupplierId(supplierId, pageNumber, pageSize);
	        System.out.println("Items retrieved for supplierId=" + supplierId + ", page=" + pageNumber + ": " + items.size());
	        Log.info("Items for supplier with ID: " + supplierId + " retrieved successfully");
	        return items;
	    } catch (Exception e) {
	        Log.error("Error while retrieving items for supplier with ID: " + supplierId, e);
	        return Collections.emptyList();
	    }
	}

}
