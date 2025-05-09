package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Category extends BaseEntity {

    private String name;
    private String description;
    private Set<Product> products = new HashSet<>(); // Changed to Set and initialized with HashSet

    // Default Constructor
    public Category() {}

    // Constructor with all fields
    public Category(long id, String name, String description, Set<Product> products) {
        setId(id); // Use BaseEntity's id
        this.name = name;
        this.description = description;
        this.products = products != null ? products : new HashSet<>();
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

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products != null ? products : new HashSet<>();
    }
}