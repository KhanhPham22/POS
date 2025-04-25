package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Supplier extends BaseEntity {

    private String name;
    private String contactName;
    private String phone;
    private String email;
    private String address;
    private String taxCode;
    private Set<Warehouse> warehouseImports = new HashSet<>();
    private Set<Item> items = new HashSet<>();
    
    // Default Constructor
    public Supplier() {}

    // Constructor with all fields
    public Supplier(long id, String name, String contactName, String phone, String email, 
                    String address, String taxCode, Set<Warehouse> warehouseImports,Set<Item> items) {
        setId(id); // Use BaseEntity's id
        this.name = name;
        this.contactName = contactName;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.taxCode = taxCode;
        this.warehouseImports = warehouseImports != null ? warehouseImports : new HashSet<>();
        this.items = items != null ? items : new HashSet<>();
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

    public Set<Warehouse> getWarehouseImports() {
        return warehouseImports;
    }

    public void setWarehouseImports(Set<Warehouse> warehouseImports) {
        this.warehouseImports = warehouseImports;
    }
    
    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }
}