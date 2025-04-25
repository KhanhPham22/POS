package model;

import java.util.ArrayList;
import java.util.List;

public class Category extends BaseEntity {

    private String name;
    private String description;
    private List<Product> products = new ArrayList<>(); // Initialize to avoid null

    // Default Constructor
    public Category() {}

    // Constructor with all fields
    public Category(long id, String name, String description, List<Product> products) {
        setId(id); // Use BaseEntity's id
        this.name = name;
        this.description = description;
        this.products = products != null ? products : new ArrayList<>();
    }

    // Getter and Setter Methods
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

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products != null ? products : new ArrayList<>();
    }
}