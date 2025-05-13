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

    // Constructor to initialize ItemDao
    public ItemServiceImpl(ItemDao itemDao) {
        this.itemDao = itemDao;
        this.itemDao.setClass(Item.class);  // Set the class type for ItemDao
    }

    // Method to create a new item
    @Override
    public boolean createItem(Item item) {
        try {
            // Call the create method of ItemDao to save the item in the database
            itemDao.create(item);
            Log.info("Item created successfully");
            return true;
        } catch (Exception e) {
            Log.error("Error while creating item", e);  // Log error if creation fails
            return false;
        }
    }

    // Method to delete an item by its ID
    @Override
    public boolean deleteItem(long itemId) {
        try {
            // Retrieve the item by its ID before deletion
            Item item = itemDao.findById(itemId);
            itemDao.delete(item);
            Log.info("Item with id: " + itemId + " deleted successfully");
            return true;
        } catch (Exception e) {
            Log.error("Error while deleting item with id: " + itemId, e);  // Log error if deletion fails
            return false;
        }
    }

    // Method to update an existing item
    @Override
    public boolean updateItem(Item newItem) {
        try {
            // Call the update method of ItemDao to update the item in the database
            itemDao.update(newItem);
            Log.info("Item with id: " + newItem.getId() + " updated successfully");
            return true;
        } catch (Exception e) {
            Log.error("Error while updating item with id: " + newItem.getId(), e);  // Log error if update fails
            return false;
        }
    }

    // Method to update a list of items
    @Override
    public boolean updateListItem(Item[] items) {
        boolean updateFailed = false;

        // Loop through each item and try to update
        for (Item item : items) {
            try {
                itemDao.update(item);
            } catch (Exception e) {
                Log.error("Error while updating item", e);  // Log error for any failed update
                updateFailed = true; // Flag the update as failed
            }
        }

        if (updateFailed) {
            return false; // Return false if any update failed
        }

        Log.info("Items updated successfully");
        return true;
    }

    // Method to retrieve an item by its ID
    @Override
    public Item getItem(long itemId) {
        try {
            // Call the findById method of ItemDao to retrieve the item
            Item item = itemDao.findById(itemId);
            Log.info("Item with id: " + itemId + " retrieved successfully");
            return item;
        } catch (Exception e) {
            Log.error("Error while retrieving item with id: " + itemId, e);  // Log error if retrieval fails
            return null;
        }
    }

    // Method to retrieve all items with pagination
    @Override
    public List<Item> getAllItems(int pageNumber, int pageSize) {
        try {
            // Call the findAll method of ItemDao to retrieve all items with pagination
            return itemDao.findAll(pageNumber, pageSize);
        } catch (Exception e) {
            Log.error("Failed to retrieve all items", e);  // Log error if retrieval fails
            return Collections.emptyList();  // Return empty list in case of failure
        }
    }

    // Method to retrieve items by name
    @Override
    public List<Item> getItemByName(String input) {
        try {
            // Call the findByName method of ItemDao to retrieve items by name
            List<Item> items = itemDao.findByName(input);
            Log.info("Items retrieved by Name successfully");
            return items;
        } catch (Exception e) {
            Log.error("Error while retrieving items by name", e);  // Log error if retrieval fails
            return new ArrayList<Item>();  // Return empty list in case of failure
        }
    }

    // Method to retrieve items by supplier ID with pagination
    @Override
    public List<Item> getItemsBySupplierId(long supplierId, int pageNumber, int pageSize) {
        try {
            // Call the findBySupplierId method of ItemDao to retrieve items by supplier ID with pagination
            List<Item> items = itemDao.findBySupplierId(supplierId, pageNumber, pageSize);
            System.out.println("Items retrieved for supplierId=" + supplierId + ", page=" + pageNumber + ": " + items.size());
            Log.info("Items for supplier with ID: " + supplierId + " retrieved successfully");
            return items;
        } catch (Exception e) {
            Log.error("Error while retrieving items for supplier with ID: " + supplierId, e);  // Log error if retrieval fails
            return Collections.emptyList();  // Return empty list in case of failure
        }
    }
}
