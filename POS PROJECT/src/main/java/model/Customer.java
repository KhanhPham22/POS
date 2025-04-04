package model;

import java.util.UUID;

public class Customer extends Person {

    private String customerNumber;
    private String description;
    private double points;

    public Customer() {
    }

    public Customer(String firstName, String phone) {
        this.setPersonFirstName(firstName); // dùng method từ Person
        this.setPhone(phone);               // dùng method từ Person
        this.points = 0.0;
    }

    // Tạo số customer tự động
    public void generateCustomerNumber() {
        this.customerNumber = "CUS-" + UUID.randomUUID().toString();
    }

    // Getter và Setter
    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Getter/Setter cho tên và sđt kế thừa từ Person
    public String getCustomerFirstName() {
        return getPersonFirstName(); // dùng getter của Person
    }

    public void setCustomerFirstName(String firstName) {
        setPersonFirstName(firstName); // dùng setter của Person
    }

    public String getCustomerPhone() {
        return getPhone(); // dùng getter của Person
    }

    public void setCustomerPhone(String phone) {
        setPhone(phone); // dùng setter của Person
    }
}
