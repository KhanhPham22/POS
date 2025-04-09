package service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.StoreDao;
import model.Store;

public class StoreServiceImpl implements StoreService {

    private static final Logger Log = LogManager.getLogger(StoreServiceImpl.class);
    private final StoreDao storeDao;

    public StoreServiceImpl(StoreDao storeDao) {
        this.storeDao = new StoreDao();
    }

    @Override
    public boolean createStore(Store store) {
        try {
            return storeDao.create(store);
        } catch (Exception e) {
            Log.error("Failed to create store", e);
            return false;
        }
    }

    @Override
    public boolean updateStore(Store store) {
        try {
            return storeDao.update(store);
        } catch (Exception e) {
            Log.error("Failed to update store", e);
            return false;
        }
    }

    @Override
    public boolean deleteStoreById(Long storeId) {
        try {
            return storeDao.deleteById(storeId);
        } catch (Exception e) {
            Log.error("Failed to delete store with ID: " + storeId, e);
            return false;
        }
    }

    @Override
    public boolean deleteStore(Store store) {
        try {
            return storeDao.delete(store);
        } catch (Exception e) {
            Log.error("Failed to delete store", e);
            return false;
        }
    }

    @Override
    public Store getStoreById(Long storeId) {
        try {
            return storeDao.findById(storeId);
        } catch (Exception e) {
            Log.error("Failed to retrieve store with ID: " + storeId, e);
            return null;
        }
    }

    @Override
    public List<Store> getAllStores() {
        try {
            return storeDao.findAll();
        } catch (Exception e) {
            Log.error("Failed to retrieve all stores", e);
            return null;
        }
    }

    @Override
    public List<Store> getStoresByCity(String city) {
        try {
            return storeDao.findByCity(city);
        } catch (Exception e) {
            Log.error("Failed to retrieve stores by city: " + city, e);
            return null;
        }
    }

    @Override
    public List<Store> getStoresByName(String name) {
        try {
            return storeDao.findByName(name);
        } catch (Exception e) {
            Log.error("Failed to retrieve stores by name: " + name, e);
            return null;
        }
    }
}

