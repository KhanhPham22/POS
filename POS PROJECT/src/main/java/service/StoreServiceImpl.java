package service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.StoreDao;
import model.Store;

public class StoreServiceImpl implements StoreService {

	private static final Logger Log = LogManager.getLogger(StoreServiceImpl.class);
	private final StoreDao storeDao;

	// Constructor to initialize the StoreDao
	public StoreServiceImpl(StoreDao storeDao) {
		this.storeDao = storeDao;
		this.storeDao.setClass(Store.class); // Set the class type for StoreDao
	}

	// Method to create a new store
	@Override
	public boolean createStore(Store store) {
		try {
			// Call the create method of StoreDao to save the store in the database
			return storeDao.create(store);
		} catch (Exception e) {
			Log.error("Failed to create store", e); // Log error if creation fails
			return false;
		}
	}

	// Method to update an existing store
	@Override
	public boolean updateStore(Store store) {
		try {
			// Call the update method of StoreDao to update the store in the database
			return storeDao.update(store);
		} catch (Exception e) {
			Log.error("Failed to update store", e); // Log error if update fails
			return false;
		}
	}

	// Method to delete a store by its ID
	@Override
	public boolean deleteStoreById(long storeId) {
		try {
			// Call the deleteById method of StoreDao to remove the store from the database
			return storeDao.deleteById(storeId);
		} catch (Exception e) {
			Log.error("Failed to delete store with ID: " + storeId, e); // Log error if deletion fails
			return false;
		}
	}

	// Method to delete a store object
	@Override
	public boolean deleteStore(Store store) {
		try {
			// Call the delete method of StoreDao to remove the store from the database
			return storeDao.delete(store);
		} catch (Exception e) {
			Log.error("Failed to delete store", e); // Log error if deletion fails
			return false;
		}
	}

	// Method to retrieve a store by its ID
	@Override
	public Store getStoreById(long storeId) {
		try {
			// Call the findById method of StoreDao to retrieve the store from the database
			return storeDao.findById(storeId);
		} catch (Exception e) {
			Log.error("Failed to retrieve store with ID: " + storeId, e); // Log error if retrieval fails
			return null;
		}
	}

	// Method to retrieve all stores with pagination
	@Override
	public List<Store> getAllStores(int pageNumber, int pageSize) {
		try {
			// Call the findAll method of StoreDao to retrieve the stores with pagination
			return storeDao.findAll(pageNumber, pageSize);
		} catch (Exception e) {
			Log.error("Failed to retrieve all stores", e); // Log error if retrieval fails
			return null;
		}
	}

	// Method to retrieve stores by city
	@Override
	public List<Store> getStoresByCity(String city) {
		try {
			// Call the findByCity method of StoreDao to retrieve stores based on the city
			return storeDao.findByCity(city);
		} catch (Exception e) {
			Log.error("Failed to retrieve stores by city: " + city, e); // Log error if retrieval fails
			return null;
		}
	}

	// Method to retrieve stores by name
	@Override
	public List<Store> getStoresByName(String name) {
		try {
			// Call the findByName method of StoreDao to retrieve stores based on the name
			return storeDao.findByName(name);
		} catch (Exception e) {
			Log.error("Failed to retrieve stores by name: " + name, e); // Log error if retrieval fails
			return null;
		}
	}
}
