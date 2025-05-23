package service;

import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.InvoiceDao;
import model.Invoice;

/**
 * Implementation of the InvoiceService interface.
 * Handles business logic related to invoice management by interacting with InvoiceDao.
 */
public class InvoiceServiceImpl implements InvoiceService {

    // Logger for logging errors and information
    private static final Logger Log = LogManager.getLogger(InvoiceServiceImpl.class);
    
    // Data Access Object for Invoice
    private final InvoiceDao invoiceDao;

    // Constructor that injects the InvoiceDao dependency
    public InvoiceServiceImpl(InvoiceDao invoiceDao) {
        this.invoiceDao = invoiceDao;
    }

    /**
     * Creates a new invoice.
     * @param invoice The invoice object to be created
     * @return true if creation is successful, false otherwise
     */
    @Override
    public boolean createInvoice(Invoice invoice) {
        try {
            return invoiceDao.create(invoice);
        } catch (Exception e) {
            Log.error("Failed to create invoice", e);
            return false;
        }
    }

    /**
     * Updates an existing invoice.
     * @param invoice The invoice object to be updated
     * @return true if update is successful, false otherwise
     */
    @Override
    public boolean updateInvoice(Invoice invoice) {
        try {
            return invoiceDao.update(invoice);
        } catch (Exception e) {
            Log.error("Failed to update invoice", e);
            return false;
        }
    }

    /**
     * Deletes an invoice by its ID.
     * @param invoiceId The ID of the invoice to be deleted
     * @return true if deletion is successful, false otherwise
     */
    @Override
    public boolean deleteInvoiceById(long invoiceId) {
        try {
            return invoiceDao.deleteById(invoiceId);
        } catch (Exception e) {
            Log.error("Failed to delete invoice with ID: " + invoiceId, e);
            return false;
        }
    }

    /**
     * Deletes an invoice using the invoice object.
     * @param invoice The invoice to be deleted
     * @return true if deletion is successful, false otherwise
     */
    @Override
    public boolean deleteInvoice(Invoice invoice) {
        try {
            return invoiceDao.delete(invoice);
        } catch (Exception e) {
            Log.error("Failed to delete invoice", e);
            return false;
        }
    }

    /**
     * Retrieves an invoice by its ID.
     * @param invoiceId The ID of the invoice to retrieve
     * @return The invoice object if found, otherwise null
     */
    @Override
    public Invoice getInvoiceById(long invoiceId) {
        try {
            return invoiceDao.findById(invoiceId);
        } catch (Exception e) {
            Log.error("Failed to retrieve invoice with ID: " + invoiceId, e);
            return null;
        }
    }

    /**
     * Retrieves all invoices with pagination.
     * @param pageNumber The page number
     * @param pageSize The number of records per page
     * @return A list of invoices or null if retrieval fails
     */
    @Override
    public List<Invoice> getAllInvoices(int pageNumber, int pageSize) {
        try {
            return invoiceDao.findAll(pageNumber, pageSize);
        } catch (Exception e) {
            Log.error("Failed to retrieve all invoices", e);
            return null;
        }
    }

    /**
     * Retrieves invoices by payment method.
     * @param paymentMethod The payment method to filter by
     * @return A list of invoices or null if retrieval fails
     */
    @Override
    public List<Invoice> getInvoicesByPaymentMethod(String paymentMethod) {
        try {
            return invoiceDao.findByPaymentMethod(paymentMethod);
        } catch (Exception e) {
            Log.error("Failed to retrieve invoices with payment method: " + paymentMethod, e);
            return null;
        }
    }

    /**
     * Retrieves invoices by status.
     * @param status The invoice status to filter by
     * @return A list of invoices or null if retrieval fails
     */
    @Override
    public List<Invoice> getInvoicesByStatus(String status) {
        try {
            return invoiceDao.findByStatus(status);
        } catch (Exception e) {
            Log.error("Failed to retrieve invoices with status: " + status, e);
            return null;
        }
    }

    /**
     * Retrieves invoices by invoice date.
     * @param date The date to filter by
     * @return A list of invoices or null if retrieval fails
     */
    @Override
    public List<Invoice> getInvoicesByDate(Date date) {
        try {
            return invoiceDao.findByInvoiceDate(date);
        } catch (Exception e) {
            Log.error("Failed to retrieve invoices by date: " + date, e);
            return null;
        }
    }

    /**
     * Retrieves invoices by employee name.
     * @param employeeName The employee name to filter by
     * @return A list of invoices or null if retrieval fails
     */
    @Override
    public List<Invoice> getInvoicesByEmployeeName(String employeeName) {
        try {
            return invoiceDao.findByEmployeeName(employeeName);
        } catch (Exception e) {
            Log.error("Failed to retrieve invoices with employee name: " + employeeName, e);
            return null;
        }
    }

    /**
     * Retrieves invoices by customer name.
     * @param customerName The customer name to filter by
     * @return A list of invoices or null if retrieval fails
     */
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

