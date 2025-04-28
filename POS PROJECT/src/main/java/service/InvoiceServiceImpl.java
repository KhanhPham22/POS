package service;

import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.InvoiceDao;
import model.Invoice;

public class InvoiceServiceImpl implements InvoiceService {

    private static final Logger Log = LogManager.getLogger(InvoiceServiceImpl.class);
    private final InvoiceDao invoiceDao;

    public InvoiceServiceImpl(InvoiceDao invoiceDao) {
        this.invoiceDao =  invoiceDao;
        
    }

    @Override
    public boolean createInvoice(Invoice invoice) {
        try {
            return invoiceDao.create(invoice);
        } catch (Exception e) {
            Log.error("Failed to create invoice", e);
            return false;
        }
    }

    @Override
    public boolean updateInvoice(Invoice invoice) {
        try {
            return invoiceDao.update(invoice);
        } catch (Exception e) {
            Log.error("Failed to update invoice", e);
            return false;
        }
    }

    @Override
    public boolean deleteInvoiceById(long invoiceId) {
        try {
            return invoiceDao.deleteById(invoiceId);
        } catch (Exception e) {
            Log.error("Failed to delete invoice with ID: " + invoiceId, e);
            return false;
        }
    }

    @Override
    public boolean deleteInvoice(Invoice invoice) {
        try {
            return invoiceDao.delete(invoice);
        } catch (Exception e) {
            Log.error("Failed to delete invoice", e);
            return false;
        }
    }

    @Override
    public Invoice getInvoiceById(long invoiceId) {
        try {
            return invoiceDao.findById(invoiceId);
        } catch (Exception e) {
            Log.error("Failed to retrieve invoice with ID: " + invoiceId, e);
            return null;
        }
    }

    @Override
    public List<Invoice> getAllInvoices(int pageNumber, int pageSize) {
        try {
            return invoiceDao.findAll(pageNumber, pageSize);
        } catch (Exception e) {
            Log.error("Failed to retrieve all invoices", e);
            return null;
        }
    }

    @Override
    public List<Invoice> getInvoicesByPaymentMethod(String paymentMethod) {
        try {
            return invoiceDao.findByPaymentMethod(paymentMethod);
        } catch (Exception e) {
            Log.error("Failed to retrieve invoices with payment method: " + paymentMethod, e);
            return null;
        }
    }

    @Override
    public List<Invoice> getInvoicesByStatus(String status) {
        try {
            return invoiceDao.findByStatus(status);
        } catch (Exception e) {
            Log.error("Failed to retrieve invoices with status: " + status, e);
            return null;
        }
    }

    @Override
    public List<Invoice> getInvoicesByDate(Date date) {
        try {
            return invoiceDao.findByInvoiceDate(date);
        } catch (Exception e) {
            Log.error("Failed to retrieve invoices by date: " + date, e);
            return null;
        }
    }
    
    @Override
    public List<Invoice> getInvoicesByEmployeeName(String employeeName) {
        try {
            return invoiceDao.findByEmployeeName(employeeName);
        } catch (Exception e) {
            Log.error("Failed to retrieve invoices with employee name: " + employeeName, e);
            return null;
        }
    }

    @Override
    public List<Invoice> getInvoicesByCustomerName(String customerName) {
        try {
            return invoiceDao.findByCustomerName(customerName);
        } catch (Exception e) {
            Log.error("Failed to retrieve invoices with customer name: " + customerName, e);
            return null;
        }
    }

}

