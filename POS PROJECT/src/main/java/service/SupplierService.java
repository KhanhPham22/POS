package service;

import java.util.List;
import java.util.Set;

import model.Item;
import model.Supplier;
import model.Warehouse;

public interface SupplierService {

    boolean createSupplier(Supplier supplier);

    boolean updateSupplier(Supplier supplier);

    boolean deleteSupplierById(long supplierId);

    boolean deleteSupplier(Supplier supplier);

    Supplier getSupplierById(long supplierId);

    List<Supplier> getAllSuppliers(int pageNumber, int pageSize);
    
    List<Supplier> getSuppliersByName(String name);
    
    Set<Item> getItemsBySupplierId(long supplierId);

    Set<Warehouse> getWarehouseBySupplierId(long supplierId);
}

