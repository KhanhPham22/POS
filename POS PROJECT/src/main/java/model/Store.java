package model;

import java.util.HashSet;
import java.util.Set;

public class Store extends BaseEntity {

    private String name;
    private String shortName;
    private String description;
    private String city;
    private String state;
    private String zip;
    private String address;
    private String phone;
    private String email;
    private String website;
    private String fax;
    
    private Set<Dashboard> dashboards = new HashSet<>();
    private Set<Warehouse> warehouseImports = new HashSet<>();
    // Default constructor
    public Store() {}

    // Constructor with all fields
    public Store(long id, String name, String shortName, String description, String city, String state,
                 String zip, String address, String phone, String email, String website, String fax) {
    	setId(id); // Use BaseEntity's id
        this.name = name;
        this.shortName = shortName;
        this.description = description;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.website = website;
        this.fax = fax;
    }

    // Getter and Setter methods
   
 

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }
    
    public Set<Dashboard> getDashboards() {
        return dashboards;
    }
    
    public void setDashboards(Set<Dashboard> dashboards) {
        this.dashboards = dashboards;
    }
    
    public Set<Warehouse> getWarehouseImports() {
        return warehouseImports;
    }
    
    public void setWarehouseImports(Set<Warehouse> warehouseImports) {
        this.warehouseImports = warehouseImports;
    }
}
