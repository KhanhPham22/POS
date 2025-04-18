package service;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;

import model.GiftVoucher;
import dao.GiftVoucherDao;
import service.GiftVoucherServiceImpl;

public class GiftVoucherServiceImplTest {

	private GiftVoucherDao mockGiftVoucherDao;

    private GiftVoucherServiceImpl giftVoucherService;
    
    @BeforeEach
    public void setUp() {
    	mockGiftVoucherDao = mock(GiftVoucherDao.class);
        giftVoucherService = new GiftVoucherServiceImpl(mockGiftVoucherDao);
    }
    
    @Test
    public void testCreateGiftVoucher() throws Exception {
        GiftVoucher giftVoucher = new GiftVoucher();
        when(mockGiftVoucherDao.create(giftVoucher)).thenReturn(true);

        boolean result = giftVoucherService.createGiftVoucher(giftVoucher);

        assertTrue(result);
        verify(mockGiftVoucherDao).create(giftVoucher);
    }
    
    @Test
    public void testUpdateGiftVoucher() throws Exception {
        GiftVoucher giftVoucher = new GiftVoucher();
        when(mockGiftVoucherDao.update(giftVoucher)).thenReturn(true);

        boolean result = giftVoucherService.updateGiftVoucher(giftVoucher);

        assertTrue(result);
        verify(mockGiftVoucherDao).update(giftVoucher);
    }

    @Test
    public void testDeleteGiftVoucherById() throws Exception {
        long giftVoucherId = 1L;
        when(mockGiftVoucherDao.deleteById(giftVoucherId)).thenReturn(true);

        boolean result = giftVoucherService.deleteGiftVoucherById(giftVoucherId);

        assertTrue(result);
        verify(mockGiftVoucherDao).deleteById(giftVoucherId);
    }

    @Test
    public void testDeleteGiftVoucher() throws Exception {
        GiftVoucher giftVoucher = new GiftVoucher();
        when(mockGiftVoucherDao.delete(giftVoucher)).thenReturn(true);

        boolean result = giftVoucherService.deleteGiftVoucher(giftVoucher);

        assertTrue(result);
        verify(mockGiftVoucherDao).delete(giftVoucher);
    }
    @Test
    public void testGetGiftVoucherById() throws Exception {
        long giftVoucherId = 1L;
        GiftVoucher giftVoucher = new GiftVoucher();
        when(mockGiftVoucherDao.findById(giftVoucherId)).thenReturn(giftVoucher);

        GiftVoucher result = giftVoucherService.getGiftVoucherById(giftVoucherId);

        assertNotNull(result);
        assertEquals(giftVoucher, result);
        verify(mockGiftVoucherDao).findById(giftVoucherId);
    }

    @Test
    public void testGetAllGiftVouchers() throws Exception {
        List<GiftVoucher> giftVouchers = Arrays.asList(new GiftVoucher(), new GiftVoucher());
        when(mockGiftVoucherDao.findAll()).thenReturn(giftVouchers);

        List<GiftVoucher> result = giftVoucherService.getAllGiftVouchers();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(mockGiftVoucherDao).findAll();
    }

    @Test
    public void testGetGiftVoucherByName() throws Exception {
        String voucherName = "HolidayVoucher";
        GiftVoucher giftVoucher = new GiftVoucher();
        when(mockGiftVoucherDao.findByVoucherName(voucherName)).thenReturn(giftVoucher);

        GiftVoucher result = giftVoucherService.getGiftVoucherByName(voucherName);

        assertNotNull(result);
        assertEquals(giftVoucher, result);
        verify(mockGiftVoucherDao).findByVoucherName(voucherName);
    }

   

    @Test
    public void testCreateGiftVoucherException() throws Exception {
        GiftVoucher giftVoucher = new GiftVoucher();
        when(mockGiftVoucherDao.create(giftVoucher)).thenThrow(new RuntimeException("Database error"));

        boolean result = giftVoucherService.createGiftVoucher(giftVoucher);

        assertFalse(result);
        verify(mockGiftVoucherDao).create(giftVoucher);
    }

    @Test
    public void testUpdateGiftVoucherException() throws Exception {
        GiftVoucher giftVoucher = new GiftVoucher();
        when(mockGiftVoucherDao.update(giftVoucher)).thenThrow(new RuntimeException("Database error"));

        boolean result = giftVoucherService.updateGiftVoucher(giftVoucher);

        assertFalse(result);
        verify(mockGiftVoucherDao).update(giftVoucher);
    }

    @Test
    public void testDeleteGiftVoucherByIdException() throws Exception {
        long giftVoucherId = 1L;
        when(mockGiftVoucherDao.deleteById(giftVoucherId)).thenThrow(new RuntimeException("Database error"));

        boolean result = giftVoucherService.deleteGiftVoucherById(giftVoucherId);

        assertFalse(result);
        verify(mockGiftVoucherDao).deleteById(giftVoucherId);
    }

    @Test
    public void testDeleteGiftVoucherException() throws Exception {
        GiftVoucher giftVoucher = new GiftVoucher();
        when(mockGiftVoucherDao.delete(giftVoucher)).thenThrow(new RuntimeException("Database error"));

        boolean result = giftVoucherService.deleteGiftVoucher(giftVoucher);

        assertFalse(result);
        verify(mockGiftVoucherDao).delete(giftVoucher);
    }

    @Test
    public void testGetGiftVoucherByIdException() throws Exception {
        long giftVoucherId = 1L;
        when(mockGiftVoucherDao.findById(giftVoucherId)).thenThrow(new RuntimeException("Database error"));

        GiftVoucher result = giftVoucherService.getGiftVoucherById(giftVoucherId);

        assertNull(result);
        verify(mockGiftVoucherDao).findById(giftVoucherId);
    }

    @Test
    public void testGetAllGiftVouchersException() throws Exception {
        when(mockGiftVoucherDao.findAll()).thenThrow(new RuntimeException("Database error"));

        List<GiftVoucher> result = giftVoucherService.getAllGiftVouchers();

        assertNull(result);
        verify(mockGiftVoucherDao).findAll();
    }

    @Test
    public void testGetGiftVoucherByNameException() throws Exception {
        String voucherName = "HolidayVoucher";
        when(mockGiftVoucherDao.findByVoucherName(voucherName)).thenThrow(new RuntimeException("Database error"));

        GiftVoucher result = giftVoucherService.getGiftVoucherByName(voucherName);

        assertNull(result);
        verify(mockGiftVoucherDao).findByVoucherName(voucherName);
    }

    
}
