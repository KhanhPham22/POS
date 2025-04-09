package service;

import java.util.List;

import model.Supplier;

public interface SupplierService {

    boolean createSupplier(Supplier supplier);

    boolean updateSupplier(Supplier supplier);

    boolean deleteSupplierById(Long supplierId);

    boolean deleteSupplier(Supplier supplier);

    Supplier getSupplierById(Long supplierId);

    List<Supplier> getAllSuppliers();
    
    List<Supplier> getSuppliersByName(String name);

}

