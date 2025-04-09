package service;

import java.util.List;
import java.time.Month;
import java.time.Year;
import model.Payroll;

public interface PayrollService {
    boolean createPayroll(Payroll payroll);

    boolean updatePayroll(Payroll payroll);

    boolean deletePayrollById(long payrollId);

    boolean deletePayroll(Payroll payroll);

    Payroll getPayrollById(long payrollId);

    List<Payroll> getAllPayrolls();
    
 // Lấy theo cả tháng và năm
    List<Payroll> getPayrollsByMonthAndYear(Month month, Year year);
}

