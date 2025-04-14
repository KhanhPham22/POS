package model;

import java.util.List;

public class Category extends BaseEntity {

    private String name;
    private String description;
    private List<Product> products;

    // Default Constructor
    public Category() {
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
        this.products = products;
    }
}