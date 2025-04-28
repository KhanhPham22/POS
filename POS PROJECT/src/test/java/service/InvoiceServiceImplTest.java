package service;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import model.Invoice;
import dao.InvoiceDao;
import service.InvoiceServiceImpl;

public class InvoiceServiceImplTest {
	private InvoiceDao mockInvoiceDao;
	private InvoiceServiceImpl invoiceService;
	
	@BeforeEach
	public void setUp() {
    	mockInvoiceDao = mock(InvoiceDao.class);
        invoiceService = new InvoiceServiceImpl(mockInvoiceDao);
    }
	
	  @Test
	    public void testCreateInvoice() throws Exception {
	        Invoice invoice = new Invoice();
	        when(mockInvoiceDao.create(invoice)).thenReturn(true);

	        boolean result = invoiceService.createInvoice(invoice);

	        assertTrue(result);
	        verify(mockInvoiceDao).create(invoice);
	    }

	    @Test
	    public void testUpdateInvoice() throws Exception {
	        Invoice invoice = new Invoice();
	        when(mockInvoiceDao.update(invoice)).thenReturn(true);

	        boolean result = invoiceService.updateInvoice(invoice);

	        assertTrue(result);
	        verify(mockInvoiceDao).update(invoice);
	    }
	    
	    @Test
	    public void testDeleteInvoiceById() throws Exception {
	        long invoiceId = 1L;
	        when(mockInvoiceDao.deleteById(invoiceId)).thenReturn(true);

	        boolean result = invoiceService.deleteInvoiceById(invoiceId);

	        assertTrue(result);
	        verify(mockInvoiceDao).deleteById(invoiceId);
	    }

	    @Test
	    public void testDeleteInvoice() throws Exception {
	        Invoice invoice = new Invoice();
	        when(mockInvoiceDao.delete(invoice)).thenReturn(true);

	        boolean result = invoiceService.deleteInvoice(invoice);

	        assertTrue(result);
	        verify(mockInvoiceDao).delete(invoice);
	    }
	    
	    @Test
	    public void testGetInvoiceById() throws Exception {
	        long invoiceId = 1L;
	        Invoice invoice = new Invoice();
	        when(mockInvoiceDao.findById(invoiceId)).thenReturn(invoice);

	        Invoice result = invoiceService.getInvoiceById(invoiceId);

	        assertNotNull(result);
	        assertEquals(invoice, result);
	        verify(mockInvoiceDao).findById(invoiceId);
	    }

//	    @Test
//	    public void testGetAllInvoices() throws Exception {
//	        List<Invoice> invoices = Arrays.asList(new Invoice(), new Invoice());
//	        when(mockInvoiceDao.findAll()).thenReturn(invoices);
//
//	        List<Invoice> result = invoiceService.getAllInvoices();
//
//	        assertNotNull(result);
//	        assertEquals(2, result.size());
//	        verify(mockInvoiceDao).findAll();
//	    }

	    @Test
	    public void testGetInvoicesByPaymentMethod() throws Exception {
	        String method = "Credit Card";
	        List<Invoice> invoices = Arrays.asList(new Invoice());
	        when(mockInvoiceDao.findByPaymentMethod(method)).thenReturn(invoices);

	        List<Invoice> result = invoiceService.getInvoicesByPaymentMethod(method);

	        assertNotNull(result);
	        assertEquals(1, result.size());
	        verify(mockInvoiceDao).findByPaymentMethod(method);
	    }

	    @Test
	    public void testGetInvoicesByStatus() throws Exception {
	        String status = "Paid";
	        List<Invoice> invoices = Arrays.asList(new Invoice());
	        when(mockInvoiceDao.findByStatus(status)).thenReturn(invoices);

	        List<Invoice> result = invoiceService.getInvoicesByStatus(status);

	        assertNotNull(result);
	        assertEquals(1, result.size());
	        verify(mockInvoiceDao).findByStatus(status);
	    }

	    @Test
	    public void testGetInvoicesByDate() throws Exception {
	        Date date = new Date();
	        List<Invoice> invoices = Arrays.asList(new Invoice());
	        when(mockInvoiceDao.findByInvoiceDate(date)).thenReturn(invoices);

	        List<Invoice> result = invoiceService.getInvoicesByDate(date);

	        assertNotNull(result);
	        assertEquals(1, result.size());
	        verify(mockInvoiceDao).findByInvoiceDate(date);
	    }

	    @Test
	    public void testGetInvoicesByEmployeeName() throws Exception {
	        String name = "John Doe";
	        List<Invoice> invoices = Arrays.asList(new Invoice());
	        when(mockInvoiceDao.findByEmployeeName(name)).thenReturn(invoices);

	        List<Invoice> result = invoiceService.getInvoicesByEmployeeName(name);

	        assertNotNull(result);
	        assertEquals(1, result.size());
	        verify(mockInvoiceDao).findByEmployeeName(name);
	    }

	    @Test
	    public void testGetInvoicesByCustomerName() throws Exception {
	        String name = "Jane Smith";
	        List<Invoice> invoices = Arrays.asList(new Invoice());
	        when(mockInvoiceDao.findByCustomerName(name)).thenReturn(invoices);

	        List<Invoice> result = invoiceService.getInvoicesByCustomerName(name);

	        assertNotNull(result);
	        assertEquals(1, result.size());
	        verify(mockInvoiceDao).findByCustomerName(name);
	    }

	    // --- Exception test cases ---

	    @Test
	    public void testCreateInvoiceException() throws Exception {
	        Invoice invoice = new Invoice();
	        when(mockInvoiceDao.create(invoice)).thenThrow(new RuntimeException("DB error"));

	        boolean result = invoiceService.createInvoice(invoice);

	        assertFalse(result);
	        verify(mockInvoiceDao).create(invoice);
	    }

	    @Test
	    public void testUpdateInvoiceException() throws Exception {
	        Invoice invoice = new Invoice();
	        when(mockInvoiceDao.update(invoice)).thenThrow(new RuntimeException("DB error"));

	        boolean result = invoiceService.updateInvoice(invoice);

	        assertFalse(result);
	        verify(mockInvoiceDao).update(invoice);
	    }

	    @Test
	    public void testDeleteInvoiceByIdException() throws Exception {
	        long id = 1L;
	        when(mockInvoiceDao.deleteById(id)).thenThrow(new RuntimeException("DB error"));

	        boolean result = invoiceService.deleteInvoiceById(id);

	        assertFalse(result);
	        verify(mockInvoiceDao).deleteById(id);
	    }

	    @Test
	    public void testDeleteInvoiceException() throws Exception {
	        Invoice invoice = new Invoice();
	        when(mockInvoiceDao.delete(invoice)).thenThrow(new RuntimeException("DB error"));

	        boolean result = invoiceService.deleteInvoice(invoice);

	        assertFalse(result);
	        verify(mockInvoiceDao).delete(invoice);
	    }
}
