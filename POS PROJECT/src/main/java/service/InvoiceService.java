package service;

import java.util.Date;
import java.util.List;

import model.Invoice;

public interface InvoiceService {

    boolean createInvoice(Invoice invoice);

    boolean updateInvoice(Invoice invoice);

    boolean deleteInvoiceById(Long invoiceId);

    boolean deleteInvoice(Invoice invoice);

    Invoice getInvoiceById(Long invoiceId);

    List<Invoice> getAllInvoices();

    List<Invoice> getInvoicesByPaymentMethod(String paymentMethod);

    List<Invoice> getInvoicesByStatus(String status);

    List<Invoice> getInvoicesByDate(Date date);
}

