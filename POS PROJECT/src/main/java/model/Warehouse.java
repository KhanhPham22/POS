package model;

public class Warehouse extends BaseEntity {

    private Store store;
    private String shortName;
    private String name;
    private String description;
    private String city;
    private String state;
    private String zip;
    private String address;
    private int enabledFlag;
    private Supplier supplier;
    private Item item;
    
    // Default Constructor
    public Warehouse() {}

    // Constructor with all fields
    public Warehouse(long id, Store store, String shortName, String name, String description, 
                     String city, String state, String zip, String address, int enabledFlag) {
        setId(id); // Use BaseEntity's id
        this.store = store;
        this.shortName = shortName;
        this.name = name;
        this.description = description;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.address = address;
        this.enabledFlag = enabledFlag;
    }

    // Getter and Setter methods
    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getEnabledFlag() {
        return enabledFlag;
    }

    public void setEnabledFlag(int enabledFlag) {
        this.enabledFlag = enabledFlag;
    }
    
    public Supplier getSupplier() {
        return supplier;
    }
    
    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}