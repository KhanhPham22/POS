package service;
import model.Warehouse;
import java.util.*;


public interface WarehouseService {
	boolean createWarehouse(Warehouse warehouse) throws Exception;

	boolean deleteWarehouse(long warehouseId) throws Exception;

	boolean updateWarehouse(Warehouse warehouse) throws Exception;

	Warehouse getWarehouse(long warehouseId) throws Exception;

	Warehouse getWarehouseByName(String name) throws Exception;

	Warehouse getWarehouseByShortName(String shortName) throws Exception;

	
	List<Warehouse> getAllWarehouses(int pageNumber, int pageSize) throws Exception;
}
