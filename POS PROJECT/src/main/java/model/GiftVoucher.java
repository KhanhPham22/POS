package model;

import java.util.Date;

public class GiftVoucher extends BaseEntity { 

    private Long giftVoucherId;
    private Customer customer;
    private String voucherName;
    private Boolean discountStatus = false;
    private Date startDate;
    private Date endDate;
    private String discountType;

    // Default constructor (Hibernate needs it)
    public GiftVoucher() {}

    // Constructor with parameters
    public GiftVoucher(Long giftVoucherId, Customer customer, String voucherName, Boolean discountStatus, Date startDate, Date endDate, String discountType) {
        this.giftVoucherId = giftVoucherId;
        this.customer = customer;
        this.voucherName = voucherName;
        this.discountStatus = discountStatus;
        this.startDate = startDate;
        this.endDate = endDate;
        this.discountType = discountType;
    }

    // Getter and Setter methods
    public Long getGiftVoucherId() {
        return giftVoucherId;
    }

    public void setGiftVoucherId(Long giftVoucherId) {
        this.giftVoucherId = giftVoucherId;
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
}
