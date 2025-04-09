package service;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.WarehouseImportDao;
import model.WarehouseImport;

public class WarehouseServiceImpl implements WarehouseService {
	private static final Logger Log = LogManager.getLogger(WarehouseServiceImpl.class);
	
private WarehouseImportDao warehouseDao ;
	
	public WarehouseServiceImpl(WarehouseImportDao warehouseDao) {
		this.warehouseDao = warehouseDao;
	}

	@Override
	public boolean createWarehouse(WarehouseImport warehouse) throws Exception {
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
	public boolean updateWarehouse(WarehouseImport warehouse) throws Exception {
		try {
			warehouseDao.update(warehouse);
			Log.info("Warehouse with id: " + warehouse.getWarehouseId() + " updated successfully");
			return true;
		} catch (Exception e) {
			Log.error("Error while updating warehouse with id: " + warehouse.getWarehouseId(), e);
			return false;
		}
	}

	@Override
	public WarehouseImport getWarehouse(long warehouseId) throws Exception {
		try {
			WarehouseImport warehouse = warehouseDao.findById(warehouseId);
			Log.info("Warehouse with id: " + warehouseId + " found successfully");
			return warehouse;
		} catch (Exception e) {
			Log.error("Error while finding warehouse with id: " + warehouseId, e);
			return null;
		}
	}

	@Override
	public List<WarehouseImport> getAllWarehouses() throws Exception {
		try {
			List<WarehouseImport> warehouses = warehouseDao.findAll();
			Log.info("All warehouses retrieved successfully");
			return warehouses;
		} catch (Exception e) {
			Log.error("Error while retrieving all warehouses", e);
			return null;
		}
	}
	
	@Override
	public WarehouseImport getWarehouseByName(String name) throws Exception {
		try {
			WarehouseImport warehouse = warehouseDao.findByName(name);
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
	public WarehouseImport getWarehouseByShortName(String shortName) throws Exception {
		try {
			WarehouseImport warehouse = warehouseDao.findByShortName(shortName);
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
