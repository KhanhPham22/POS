package service;

import java.util.List;

import model.Payroll;

public interface PayrollService {
    boolean createPayroll(Payroll payroll);

    boolean updatePayroll(Payroll payroll);

    boolean deletePayrollById(long payrollId);

    boolean deletePayroll(Payroll payroll);

    Payroll getPayrollById(long payrollId);

    List<Payroll> getAllPayrolls();
}

