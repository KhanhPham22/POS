package model;

import java.util.UUID;


public class Employee extends Person {

    private String employeeNumber;
    private String employeeType;
    private String description;
    private String loginUsername;
    private String loginPassword;

    public Employee() {
    }

    // Thay thế @PrePersist bằng phương thức gọi thủ công trong Service/DAO
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
}

