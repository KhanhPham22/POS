package service;

import java.util.Date;
import java.util.List;

import model.Invoice;

public interface InvoiceService {

    boolean createInvoice(Invoice invoice);

    boolean updateInvoice(Invoice invoice);

    boolean deleteInvoiceById(long invoiceId);

    boolean deleteInvoice(Invoice invoice);

    Invoice getInvoiceById(long invoiceId);

    List<Invoice> getAllInvoices();

    List<Invoice> getInvoicesByPaymentMethod(String paymentMethod);

    List<Invoice> getInvoicesByStatus(String status);

    List<Invoice> getInvoicesByDate(Date date);
    
    List<Invoice> getInvoicesByEmployeeName(String employeeName);

    List<Invoice> getInvoicesByCustomerName(String customerName);
}

