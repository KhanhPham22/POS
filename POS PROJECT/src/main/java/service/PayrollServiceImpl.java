package service;

import java.time.Month;
import java.time.Year;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.PayrollDao;
import model.Payroll;

public class PayrollServiceImpl implements PayrollService {

    private static final Logger Log = LogManager.getLogger(PayrollServiceImpl.class);
    private final PayrollDao payrollDao;

    public PayrollServiceImpl(PayrollDao payrollDao) {
        this.payrollDao = new PayrollDao();
        this.payrollDao.setClass(Payroll.class);
    }

    @Override
    public boolean createPayroll(Payroll payroll) {
        try {
            return payrollDao.create(payroll);
        } catch (Exception e) {
            Log.error("Failed to create payroll", e);
            return false;
        }
    }

    @Override
    public boolean updatePayroll(Payroll payroll) {
        try {
            return payrollDao.update(payroll);
        } catch (Exception e) {
            Log.error("Failed to update payroll", e);
            return false;
        }
    }

    @Override
    public boolean deletePayrollById(long payrollId) {
        try {
            return payrollDao.deleteById(payrollId);
        } catch (Exception e) {
            Log.error("Failed to delete payroll with ID: " + payrollId, e);
            return false;
        }
    }

    @Override
    public boolean deletePayroll(Payroll payroll) {
        try {
            return payrollDao.delete(payroll);
        } catch (Exception e) {
            Log.error("Failed to delete payroll", e);
            return false;
        }
    }

    @Override
    public Payroll getPayrollById(long payrollId) {
        try {
            return payrollDao.findById(payrollId);
        } catch (Exception e) {
            Log.error("Failed to retrieve payroll with ID: " + payrollId, e);
            return null;
        }
    }

    @Override
    public List<Payroll> getAllPayrolls() {
        try {
            return payrollDao.findAll();
        } catch (Exception e) {
            Log.error("Failed to retrieve all payrolls", e);
            return null;
        }
    }
    
    @Override
    public List<Payroll> getPayrollsByMonthAndYear(Month month, Year year) {
        try {
            return payrollDao.findByMonthAndYear(month, year);
        } catch (Exception e) {
            Log.error("Failed to retrieve payrolls by month and year: " + month + ", " + year, e);
            return null;
        }
    }
}

