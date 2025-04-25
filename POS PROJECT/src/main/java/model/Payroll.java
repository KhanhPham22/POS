package model;

import java.time.Month;
import java.time.Year;

public class Payroll extends BaseEntity {

    private Employee employee;
    private Double baseSalary;
    private Double bonus = 0.0;
    private Double commission = 0.0;
    private Double totalSalary;
    private Month month;
    private Year year;

    // Default Constructor
    public Payroll() {}

    // Constructor with all fields
    public Payroll(long id, Employee employee, Double baseSalary, Double bonus, 
                   Double commission, Double totalSalary, Month month, Year year) {
        setId(id); // Use BaseEntity's id
        this.employee = employee;
        this.baseSalary = baseSalary;
        this.bonus = bonus;
        this.commission = commission;
        this.totalSalary = totalSalary;
        this.month = month;
        this.year = year;
    }

    // Getter and Setter Methods
    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Double getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(Double baseSalary) {
        this.baseSalary = baseSalary;
    }

    public Double getBonus() {
        return bonus;
    }

    public void setBonus(Double bonus) {
        this.bonus = bonus;
    }

    public Double getCommission() {
        return commission;
    }

    public void setCommission(Double commission) {
        this.commission = commission;
    }

    public Double getTotalSalary() {
        return totalSalary;
    }

    public void setTotalSalary(Double totalSalary) {
        this.totalSalary = totalSalary;
    }

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public Year getYear() {
        return year;
    }

    public void setYear(Year year) {
        this.year = year;
    }
}