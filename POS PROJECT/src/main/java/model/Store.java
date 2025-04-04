package model;

public class Store extends BaseEntity {

    private long storeId;
    private static Store instance;
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

    // Default constructor
    public Store() {}

    // Constructor with all fields
    public Store(long storeId, String name, String shortName, String description, String city, String state,
                 String zip, String address, String phone, String email, String website, String fax) {
        this.storeId = storeId;
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
    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

    public static Store getInstance() {
        if (instance == null) {
            instance = new Store();
        }
        return instance;
    }

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
}
