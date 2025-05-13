package service;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.SupplierDao;
import model.Item;
import model.Supplier;
import model.Warehouse;

public class SupplierServiceImpl implements SupplierService {

    private static final Logger Log = LogManager.getLogger(SupplierServiceImpl.class);
    private final SupplierDao supplierDao;

    // Constructor to initialize the SupplierDao and set the class type for DAO operations
    public SupplierServiceImpl(SupplierDao supplierDao) {
        this.supplierDao = supplierDao;
        this.supplierDao.setClass(Supplier.class);
    }

    // Method to create a new supplier, with checks to avoid duplicates by phone, tax code, and name
    @Override
    public boolean createSupplier(Supplier supplier) {
        try {
            // Check if the phone number already exists in the database
            if (supplierDao.findByPhone(supplier.getPhone()) != null) {
                Log.warn("Phone already exists: " + supplier.getPhone());
                return false;
            }
            // Check if the tax code already exists
            if (supplier.getTaxCode() != null && !supplier.getTaxCode().isBlank()
                && supplierDao.findByTaxCode(supplier.getTaxCode()) != null) {
                Log.warn("Tax code already exists: " + supplier.getTaxCode());
                return false;
            }
            // Check if the exact name already exists
            if (supplierDao.findByNameExact(supplier.getName()) != null) {
                Log.warn("Supplier name already exists: " + supplier.getName());
                return false;
            }
            // If no duplicates, create the supplier
            return supplierDao.create(supplier);
        } catch (Exception e) {
            Log.error("Failed to create supplier", e);
            return false;
        }
    }

    // Method to update an existing supplier
    @Override
    public boolean updateSupplier(Supplier supplier) {
        try {
            return supplierDao.update(supplier);
        } catch (Exception e) {
            Log.error("Failed to update supplier", e);
            return false;
        }
    }

    // Method to delete a supplier by its ID
    @Override
    public boolean deleteSupplierById(long supplierId) {
        try {
            return supplierDao.deleteById(supplierId);
        } catch (Exception e) {
            Log.error("Failed to delete supplier with ID: " + supplierId, e);
            return false;
        }
    }

    // Method to delete a supplier by the supplier object
    @Override
    public boolean deleteSupplier(Supplier supplier) {
        try {
            return supplierDao.delete(supplier);
        } catch (Exception e) {
            Log.error("Failed to delete supplier", e);
            return false;
        }
    }

    // Method to retrieve a supplier by its ID
    @Override
    public Supplier getSupplierById(long supplierId) {
        try {
            return supplierDao.findById(supplierId);
        } catch (Exception e) {
            Log.error("Failed to retrieve supplier with ID: " + supplierId, e);
            return null;
        }
    }

    // Method to retrieve all suppliers with pagination
    @Override
    public List<Supplier> getAllSuppliers(int pageNumber, int pageSize) {
        try {
            // Retrieve a list of suppliers with pagination parameters
            List<Supplier> suppliers = supplierDao.findAll(pageNumber, pageSize);
            System.out.println("Number of suppliers retrieved from DB: " + (suppliers != null ? suppliers.size() : "null")
                    + ", pageNumber: " + pageNumber + ", pageSize: " + pageSize);
            return suppliers != null ? suppliers : Collections.emptyList();
        } catch (Exception e) {
            Log.error("Error details: ", e); // Log full error details
            return Collections.emptyList();
        }
    }

    // Method to retrieve suppliers by their name
    @Override
    public List<Supplier> getSuppliersByName(String name) {
        try {
            return supplierDao.findByName(name);
        } catch (Exception e) {
            Log.error("Failed to retrieve suppliers with name: " + name, e);
            return null;
        }
    }

    // Method to retrieve all items associated with a supplier by its ID
    @Override
    public Set<Item> getItemsBySupplierId(long supplierId) {
        try {
            Supplier supplier = supplierDao.findById(supplierId);
            return supplier != null ? supplier.getItems() : Collections.emptySet();
        } catch (Exception e) {
            Log.error("Failed to get items for supplier ID: " + supplierId, e);
            return Collections.emptySet();
        }
    }

    // Method to retrieve all warehouses associated with a supplier by its ID
    @Override
    public Set<Warehouse> getWarehouseBySupplierId(long supplierId) {
        try {
            Supplier supplier = supplierDao.findById(supplierId);
            return supplier != null ? supplier.getWarehouseImports() : Collections.emptySet();
        } catch (Exception e) {
            Log.error("Failed to get warehouses for supplier ID: " + supplierId, e);
            return Collections.emptySet();
        }
    }
}
