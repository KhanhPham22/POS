package model;

import java.util.Date;

public class Feedback extends BaseEntity { 

    private Long feedbackId;

    private Customer customer;

    private String type;

    private String content;

    private Integer rating;

    private Store store; // Added store field

    public Feedback() {}

    public Feedback(Long feedbackId, Customer customer, String type, String content, Integer rating, Store store) {
        this.feedbackId = feedbackId;
        this.customer = customer;
        this.type = type;
        this.content = content;
        this.rating = rating;
        this.store = store; // Initializing store field
    }

    // Getters and setters
    public Long getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(Long feedbackId) {
        this.feedbackId = feedbackId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }
}
