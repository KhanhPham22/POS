package service;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.SupplierDao;
import model.Supplier;

public class SupplierServiceImpl implements SupplierService {

    private static final Logger Log = LogManager.getLogger(SupplierServiceImpl.class);
    private final SupplierDao supplierDao;

    public SupplierServiceImpl() {
        this.supplierDao = new SupplierDao();
    }

    @Override
    public boolean createSupplier(Supplier supplier) {
        try {
            return supplierDao.create(supplier);
        } catch (Exception e) {
            Log.error("Failed to create supplier", e);
            return false;
        }
    }

    @Override
    public boolean updateSupplier(Supplier supplier) {
        try {
            return supplierDao.update(supplier);
        } catch (Exception e) {
            Log.error("Failed to update supplier", e);
            return false;
        }
    }

    @Override
    public boolean deleteSupplierById(Long supplierId) {
        try {
            return supplierDao.deleteById(supplierId);
        } catch (Exception e) {
            Log.error("Failed to delete supplier with ID: " + supplierId, e);
            return false;
        }
    }

    @Override
    public boolean deleteSupplier(Supplier supplier) {
        try {
            return supplierDao.delete(supplier);
        } catch (Exception e) {
            Log.error("Failed to delete supplier", e);
            return false;
        }
    }

    @Override
    public Supplier getSupplierById(Long supplierId) {
        try {
            return supplierDao.findById(supplierId);
        } catch (Exception e) {
            Log.error("Failed to retrieve supplier with ID: " + supplierId, e);
            return null;
        }
    }

    @Override
    public List<Supplier> getAllSuppliers() {
        try {
            return supplierDao.findAll();
        } catch (Exception e) {
            Log.error("Failed to retrieve all suppliers", e);
            return null;
        }
    }
    
    @Override
    public List<Supplier> getSuppliersByName(String name) {
        try {
            return supplierDao.findByName(name);
        } catch (Exception e) {
            Log.error("Failed to retrieve suppliers with name: " + name, e);
            return null;
        }
    }

}

