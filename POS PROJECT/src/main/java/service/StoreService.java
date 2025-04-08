package service;

import java.util.List;

import model.Store;

public interface StoreService {

    boolean createStore(Store store);

    boolean updateStore(Store store);

    boolean deleteStoreById(Long storeId);

    boolean deleteStore(Store store);

    Store getStoreById(Long storeId);

    List<Store> getAllStores();

    List<Store> getStoresByCity(String city);

    List<Store> getStoresByName(String name); 
}

