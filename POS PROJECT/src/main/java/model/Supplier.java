package model;

import java.util.ArrayList;
import java.util.List;

public class Supplier extends BaseEntity {

    private String name;
    private String contactName;
    private String phone;
    private String email;
    private String address;
    private String taxCode;
    private List<Warehouse> warehouseImports = new ArrayList<>();

    // Default Constructor
    public Supplier() {}

    // Constructor with all fields
    public Supplier(Long id, String name, String contactName, String phone, String email, 
                    String address, String taxCode, List<Warehouse> warehouseImports) {
        setId(id); // Use BaseEntity's id
        this.name = name;
        this.contactName = contactName;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.taxCode = taxCode;
        this.warehouseImports = warehouseImports != null ? warehouseImports : new ArrayList<>();
    }

    // Getter and Setter methods
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public List<Warehouse> getWarehouseImports() {
        return warehouseImports;
    }

    public void setWarehouseImports(List<Warehouse> warehouseImports) {
        this.warehouseImports = warehouseImports;
    }
}