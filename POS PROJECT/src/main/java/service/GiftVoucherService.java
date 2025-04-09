package service;

import java.util.List;

import model.GiftVoucher;

public interface GiftVoucherService {
    boolean createGiftVoucher(GiftVoucher giftVoucher);

    boolean updateGiftVoucher(GiftVoucher giftVoucher);

    boolean deleteGiftVoucherById(long giftVoucherId);

    boolean deleteGiftVoucher(GiftVoucher giftVoucher);

    GiftVoucher getGiftVoucherById(long giftVoucherId);

    GiftVoucher getGiftVoucherByName(String voucherName);

    
    List<GiftVoucher> getAllGiftVouchers();
}

