package service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.Month;
import java.time.Year;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Payroll;
import dao.PayrollDao;
import service.PayrollServiceImpl;

public class PayrollServiceImplTest {

	private PayrollDao mockPayrollDao;
	private PayrollServiceImpl payrollService;

	@BeforeEach
	public void setUp() {
		mockPayrollDao = mock(PayrollDao.class);
		payrollService = new PayrollServiceImpl(mockPayrollDao);
	}

	@Test
	public void testCreatePayroll() throws Exception {
		Payroll payroll = new Payroll();
		when(mockPayrollDao.create(payroll)).thenReturn(true);

		boolean result = payrollService.createPayroll(payroll);

		assertTrue(result);
		verify(mockPayrollDao).create(payroll);
	}
	
	@Test
    public void testUpdatePayroll() throws Exception {
        Payroll payroll = new Payroll();
        when(mockPayrollDao.update(payroll)).thenReturn(true);

        boolean result = payrollService.updatePayroll(payroll);

        assertTrue(result);
        verify(mockPayrollDao).update(payroll);
    }

    @Test
    public void testDeletePayrollById() throws Exception {
        long payrollId = 1L;
        when(mockPayrollDao.deleteById(payrollId)).thenReturn(true);

        boolean result = payrollService.deletePayrollById(payrollId);

        assertTrue(result);
        verify(mockPayrollDao).deleteById(payrollId);
    }
    
    @Test
    public void testDeletePayroll() throws Exception {
        Payroll payroll = new Payroll();
        when(mockPayrollDao.delete(payroll)).thenReturn(true);

        boolean result = payrollService.deletePayroll(payroll);

        assertTrue(result);
        verify(mockPayrollDao).delete(payroll);
    }

    @Test
    public void testGetPayrollById() throws Exception {
        long id = 1L;
        Payroll payroll = new Payroll();
        when(mockPayrollDao.findById(id)).thenReturn(payroll);

        Payroll result = payrollService.getPayrollById(id);

        assertNotNull(result);
        assertEquals(payroll, result);
        verify(mockPayrollDao).findById(id);
    }


    @Test
    public void testGetPayrollsByMonthAndYear() throws Exception {
        Month month = Month.JANUARY;
        Year year = Year.of(2025);
        List<Payroll> payrolls = Arrays.asList(new Payroll(), new Payroll());
        when(mockPayrollDao.findByMonthAndYear(month, year)).thenReturn(payrolls);

        List<Payroll> result = payrollService.getPayrollsByMonthAndYear(month, year);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(mockPayrollDao).findByMonthAndYear(month, year);
    }

    // --- Exception tests ---

    @Test
    public void testCreatePayrollException() throws Exception {
        Payroll payroll = new Payroll();
        when(mockPayrollDao.create(payroll)).thenThrow(new RuntimeException("DB error"));

        boolean result = payrollService.createPayroll(payroll);

        assertFalse(result);
        verify(mockPayrollDao).create(payroll);
    }

    @Test
    public void testUpdatePayrollException() throws Exception {
        Payroll payroll = new Payroll();
        when(mockPayrollDao.update(payroll)).thenThrow(new RuntimeException("DB error"));

        boolean result = payrollService.updatePayroll(payroll);

        assertFalse(result);
        verify(mockPayrollDao).update(payroll);
    }

    @Test
    public void testDeletePayrollByIdException() throws Exception {
        long id = 1L;
        when(mockPayrollDao.deleteById(id)).thenThrow(new RuntimeException("DB error"));

        boolean result = payrollService.deletePayrollById(id);

        assertFalse(result);
        verify(mockPayrollDao).deleteById(id);
    }

    @Test
    public void testDeletePayrollException() throws Exception {
        Payroll payroll = new Payroll();
        when(mockPayrollDao.delete(payroll)).thenThrow(new RuntimeException("DB error"));

        boolean result = payrollService.deletePayroll(payroll);

        assertFalse(result);
        verify(mockPayrollDao).delete(payroll);
    }

    @Test
    public void testGetPayrollByIdException() throws Exception {
        long id = 1L;
        when(mockPayrollDao.findById(id)).thenThrow(new RuntimeException("DB error"));

        Payroll result = payrollService.getPayrollById(id);

        assertNull(result);
        verify(mockPayrollDao).findById(id);
    }



    @Test
    public void testGetPayrollsByMonthAndYearException() throws Exception {
        Month month = Month.FEBRUARY;
        Year year = Year.of(2025);
        when(mockPayrollDao.findByMonthAndYear(month, year)).thenThrow(new RuntimeException("DB error"));

        List<Payroll> result = payrollService.getPayrollsByMonthAndYear(month, year);

        assertNull(result);
        verify(mockPayrollDao).findByMonthAndYear(month, year);
    }
}
