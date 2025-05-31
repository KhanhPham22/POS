package service;

import model.GiftVoucher;
import model.Customer;
import dao.GiftVoucherDao;
import java.util.Date;
import java.util.Calendar;

public class InsertGiftVoucher {
    public static void main(String[] args) {
        try {
            GiftVoucherDao giftVoucherDao = new GiftVoucherDao();
            GiftVoucher voucher = new GiftVoucher();
            voucher.setVoucherName("SUMMER_SALE_2025");
            voucher.setDiscountStatus(true);
            voucher.setDiscountType("PERCENTAGE_5");
            
            // Set start date to now (May 31, 2025)
            Date startDate = new Date();
            voucher.setStartDate(startDate);
            
            // Set end date to August 31, 2025
            Calendar calendar = Calendar.getInstance();
            calendar.set(2025, Calendar.AUGUST, 31);
            voucher.setEndDate(calendar.getTime());
            
            // No specific customer (applies to all)
            voucher.setCustomer(null);
            
            giftVoucherDao.create(voucher);
            System.out.println("Gift Voucher created successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}