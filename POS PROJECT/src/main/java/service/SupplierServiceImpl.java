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

	public SupplierServiceImpl(SupplierDao supplierDao) {
		this.supplierDao = supplierDao;
		this.supplierDao.setClass(Supplier.class);
	}

	//check phone and tax
	@Override
	public boolean createSupplier(Supplier supplier) {
	    try {
	        if (supplierDao.findByPhone(supplier.getPhone()) != null) {
	            Log.warn("Phone đã tồn tại: " + supplier.getPhone());
	            return false;
	        }
	        if (supplier.getTaxCode() != null && !supplier.getTaxCode().isBlank()
	            && supplierDao.findByTaxCode(supplier.getTaxCode()) != null) {
	            Log.warn("Tax code đã tồn tại: " + supplier.getTaxCode());
	            return false;
	        }
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
	public boolean deleteSupplierById(long supplierId) {
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
	public Supplier getSupplierById(long supplierId) {
		try {
			return supplierDao.findById(supplierId);
		} catch (Exception e) {
			Log.error("Failed to retrieve supplier with ID: " + supplierId, e);
			return null;
		}
	}

	@Override
	public List<Supplier> getAllSuppliers(int pageNumber, int pageSize) {
		try {
			List<Supplier> suppliers = supplierDao.findAll(pageNumber, pageSize);
			System.out.println("Số supplier lấy từ DB: " + (suppliers != null ? suppliers.size() : "null")
					+ ", pageNumber: " + pageNumber + ", pageSize: " + pageSize);
			return suppliers != null ? suppliers : Collections.emptyList();
		} catch (Exception e) {
			Log.error("Lỗi chi tiết: ", e); // Ghi log đầy đủ
			return Collections.emptyList();
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
