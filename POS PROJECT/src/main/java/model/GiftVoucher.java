package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GiftVoucher extends BaseEntity {

    private Customer customer;
    private String voucherName;
    private Boolean discountStatus = false;
    private Date startDate;
    private Date endDate;
    private String discountType;
    private Set<Product> products = new HashSet<>();
    
    // Default constructor (Hibernate needs it)
    public GiftVoucher() {}

    // Constructor with parameters
    public GiftVoucher(long id, Customer customer, String voucherName, Boolean discountStatus, Date startDate, Date endDate, String discountType) {
        setId(id); // Use BaseEntity's id
        this.customer = customer;
        this.voucherName = voucherName;
        this.discountStatus = discountStatus;
        this.startDate = startDate;
        this.endDate = endDate;
        this.discountType = discountType;
    }
	
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getVoucherName() {
        return voucherName;
    }

    public void setVoucherName(String voucherName) {
        this.voucherName = voucherName;
    }

    public Boolean getDiscountStatus() {
        return discountStatus;
    }

    public void setDiscountStatus(Boolean discountStatus) {
        this.discountStatus = discountStatus;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }
    
    public Set<Product> getProducts() {
		return products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products != null ? products : new HashSet<>();
	}
}