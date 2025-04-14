package model;

public class Item extends BaseEntity {

    private String name;
    private String type;
    private String unit;
    private String description;

    // Default Constructor
    public Item() {}

    // Constructor with all fields
    public Item(Long id, String name, String type, String unit, String description) {
        setId(id); // Use BaseEntity's id
        this.name = name;
        this.type = type;
        this.unit = unit;
        this.description = description;
    }

    // Getter and Setter Methods
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}