package model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


public class Employee extends Person {

    private String employeeNumber;
    private String employeeType;
    private String description;
    private String loginUsername;
    private String loginPassword;
    private String avatarPath; // New field for avatar path
    private Set<Invoice> invoices = new HashSet<>();
    private Set<Payroll> payrolls = new HashSet<>();
    
    public Employee() {
    	super();
    }

    //random UID employee
    public void generateEmployeeNumber() {
        this.employeeNumber = "EMP-" + UUID.randomUUID().toString();
    }

    // Getter và Setter cho các thuộc tính của Employee
    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(String employeeType) {
        this.employeeType = employeeType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLoginUsername() {
        return loginUsername;
    }

    public void setLoginUsername(String loginUsername) {
        this.loginUsername = loginUsername;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }
    
    public Set<Invoice> getInvoices() {
        return invoices;
    }
    
    public void setInvoices(Set<Invoice> invoices) {
        this.invoices = invoices;
    }
    
    public Set<Payroll> getPayrolls() {
    	return payrolls;
    }
    
    public void setPayrolls(Set<Payroll>payrolls) {
    	this.payrolls = payrolls;
    }
    
    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }
}

