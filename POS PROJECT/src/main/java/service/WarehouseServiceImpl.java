package service;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.WarehouseDao;
import model.Warehouse;

public class WarehouseServiceImpl implements WarehouseService {
	private static final Logger Log = LogManager.getLogger(WarehouseServiceImpl.class);
	
private WarehouseDao warehouseDao ;
	
	public WarehouseServiceImpl(WarehouseDao warehouseDao) {
		this.warehouseDao = warehouseDao;
	}

	@Override
	public boolean createWarehouse(Warehouse warehouse) throws Exception {
		try {
			warehouseDao.create(warehouse);
			Log.info("Warehouse created successfully");
			return true;
		} catch (Exception e) {
			Log.error("Error while creating warehouse", e);
			return false;
		}
	}

	@Override
	public boolean deleteWarehouse(long warehouseId) throws Exception {
		try {
			warehouseDao.deleteById(warehouseId);
			Log.info("Warehouse with id: " + warehouseId + " deleted successfully");
			return true;
		} catch (Exception e) {
			Log.error("Error while deleting warehouse with id: " + warehouseId, e);
			return false;
		}
	}

	@Override
	public boolean updateWarehouse(Warehouse warehouse) throws Exception {
		try {
			warehouseDao.update(warehouse);
			Log.info("Warehouse with id: " + warehouse.getId() + " updated successfully");
			return true;
		} catch (Exception e) {
			Log.error("Error while updating warehouse with id: " + warehouse.getId(), e);
			return false;
		}
	}

	@Override
	public Warehouse getWarehouse(long warehouseId) throws Exception {
		try {
			Warehouse warehouse = warehouseDao.findById(warehouseId);
			Log.info("Warehouse with id: " + warehouseId + " found successfully");
			return warehouse;
		} catch (Exception e) {
			Log.error("Error while finding warehouse with id: " + warehouseId, e);
			return null;
		}
	}

	@Override
	public List<Warehouse> getAllWarehouses() throws Exception {
		try {
			List<Warehouse> warehouses = warehouseDao.findAll();
			Log.info("All warehouses retrieved successfully");
			return warehouses;
		} catch (Exception e) {
			Log.error("Error while retrieving all warehouses", e);
			return null;
		}
	}
	
	@Override
	public Warehouse getWarehouseByName(String name) throws Exception {
		try {
			Warehouse warehouse = warehouseDao.findByName(name);
			if (warehouse != null) {
				Log.info("Warehouse with name: " + name + " found successfully");
			} else {
				Log.warn("No warehouse found with name: " + name);
			}
			return warehouse;
		} catch (Exception e) {
			Log.error("Error while finding warehouse with name: " + name, e);
			return null;
		}
	}

	@Override
	public Warehouse getWarehouseByShortName(String shortName) throws Exception {
		try {
			Warehouse warehouse = warehouseDao.findByShortName(shortName);
			if (warehouse != null) {
				Log.info("Warehouse with shortName: " + shortName + " found successfully");
			} else {
				Log.warn("No warehouse found with shortName: " + shortName);
			}
			return warehouse;
		} catch (Exception e) {
			Log.error("Error while finding warehouse with shortName: " + shortName, e);
			return null;
		}
	}

}
