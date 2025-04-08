package service;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.GiftVoucherDao;
import model.GiftVoucher;

public class GiftVoucherServiceImpl implements GiftVoucherService {

    private static final Logger Log = LogManager.getLogger(GiftVoucherServiceImpl.class);
    private final GiftVoucherDao giftVoucherDao;

    public GiftVoucherServiceImpl() {
        this.giftVoucherDao = new GiftVoucherDao();
        this.giftVoucherDao.setClass(GiftVoucher.class);
    }

    @Override
    public boolean createGiftVoucher(GiftVoucher giftVoucher) {
        try {
            return giftVoucherDao.create(giftVoucher);
        } catch (Exception e) {
            Log.error("Failed to create gift voucher", e);
            return false;
        }
    }

    @Override
    public boolean updateGiftVoucher(GiftVoucher giftVoucher) {
        try {
            return giftVoucherDao.update(giftVoucher);
        } catch (Exception e) {
            Log.error("Failed to update gift voucher", e);
            return false;
        }
    }

    @Override
    public boolean deleteGiftVoucherById(long giftVoucherId) {
        try {
            return giftVoucherDao.deleteById(giftVoucherId);
        } catch (Exception e) {
            Log.error("Failed to delete gift voucher with ID: " + giftVoucherId, e);
            return false;
        }
    }

    @Override
    public boolean deleteGiftVoucher(GiftVoucher giftVoucher) {
        try {
            return giftVoucherDao.delete(giftVoucher);
        } catch (Exception e) {
            Log.error("Failed to delete gift voucher", e);
            return false;
        }
    }

    @Override
    public GiftVoucher getGiftVoucherById(long giftVoucherId) {
        try {
            return giftVoucherDao.findById(giftVoucherId);
        } catch (Exception e) {
            Log.error("Failed to retrieve gift voucher with ID: " + giftVoucherId, e);
            return null;
        }
    }

    @Override
    public List<GiftVoucher> getAllGiftVouchers() {
        try {
            return giftVoucherDao.findAll();
        } catch (Exception e) {
            Log.error("Failed to retrieve all gift vouchers", e);
            return null;
        }
    }
}

