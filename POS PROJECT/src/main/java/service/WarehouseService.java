package service;
import model.WarehouseImport;
import java.util.*;


public interface WarehouseService {
	boolean createWarehouse(WarehouseImport warehouse) throws Exception;

	boolean deleteWarehouse(long warehouseId) throws Exception;

	boolean updateWarehouse(WarehouseImport warehouse) throws Exception;

	WarehouseImport getWarehouse(long warehouseId) throws Exception;

	List<WarehouseImport> getAllWarehouses() throws Exception;
}
