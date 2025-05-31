package ui;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import model.GiftVoucher;
import service.GiftVoucherService;
import java.util.List;

//Import necessary Java Swing and utility classes
public class GiftVoucherPanel extends JPanel {

 // Reference to the service used to fetch gift voucher data
 private final GiftVoucherService giftVoucherService;

 // Label to display information about the gift voucher
 private JLabel voucherInfoLabel;

 // Date format used for displaying voucher validity dates
 private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

 // Constructor initializing the panel with the provided gift voucher service
 public GiftVoucherPanel(GiftVoucherService giftVoucherService) {
     this.giftVoucherService = giftVoucherService;

     // Set the layout and styling of the panel
     setLayout(new BorderLayout(10, 10));
     setBackground(Color.WHITE);
     setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

     // Initialize and set up the UI components
     initializeUI();
 }

 // Method to initialize UI elements
 private void initializeUI() {
     // Create and customize the title label
     JLabel titleLabel = new JLabel("Active Gift Voucher", SwingConstants.CENTER);
     titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
     titleLabel.setForeground(new Color(50, 50, 50));
     add(titleLabel, BorderLayout.NORTH);  // Add title label to the top of the panel

     // Create and customize the label that displays voucher info
     voucherInfoLabel = new JLabel("", SwingConstants.CENTER);
     voucherInfoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
     voucherInfoLabel.setForeground(new Color(50, 50, 50));
     add(voucherInfoLabel, BorderLayout.CENTER);  // Add it to the center of the panel

     // Load the voucher information from the service
     loadVoucherInfo();
 }

 // Method to load voucher data and update the label accordingly
 private void loadVoucherInfo() {
     try {
         // Retrieve one gift voucher (assuming the latest or most relevant)
         List<GiftVoucher> vouchers = giftVoucherService.getAllGiftVouchers(1, 1);

         // Check if there is at least one voucher
         if (!vouchers.isEmpty()) {
             GiftVoucher voucher = vouchers.get(0);

             // Check if the voucher is currently valid
             if (isValidVoucher(voucher)) {
                 // Format and display the voucher details
                 voucherInfoLabel.setText(
                     "<html><center>" +
                     "Voucher: " + voucher.getVoucherName() + "<br>" +
                     "Discount: 5% on all products<br>" +
                     "Valid from: " + DATE_FORMAT.format(voucher.getStartDate()) + "<br>" +
                     "Valid until: " + DATE_FORMAT.format(voucher.getEndDate()) +
                     "</center></html>"
                 );
             } else {
                 // Voucher exists but is not valid
                 voucherInfoLabel.setText("No valid voucher available");
             }
         } else {
             // No voucher found
             voucherInfoLabel.setText("No voucher available");
         }
     } catch (Exception e) {
         // Handle any error in loading voucher data
         voucherInfoLabel.setText("Error loading voucher: " + e.getMessage());
     }
 }

 // Method to check whether a voucher is valid (active and within date range)
 public boolean isValidVoucher(GiftVoucher voucher) {
     if (voucher == null || !voucher.getDiscountStatus()) {
         return false;  // Invalid if null or not active
     }

     java.util.Date currentDate = new java.util.Date();
     // Check if current date is within the voucher's valid date range
     return voucher.getStartDate().before(currentDate) && voucher.getEndDate().after(currentDate);
 }

 // Method to apply the discount if a valid voucher is found
 public double applyDiscount(double totalAmount) {
     try {
         // Retrieve the most recent or relevant voucher
         List<GiftVoucher> vouchers = giftVoucherService.getAllGiftVouchers(1, 1);
         if (!vouchers.isEmpty()) {
             GiftVoucher voucher = vouchers.get(0);

             // Apply a 5% discount if the voucher is valid and matches the expected discount type
             if (isValidVoucher(voucher) && "PERCENTAGE_5".equals(voucher.getDiscountType())) {
                 return totalAmount * 0.95;
             }
         }
     } catch (Exception e) {
         // Print error if any issue occurs while applying discount
         System.err.println("Error applying discount: " + e.getMessage());
     }

     // Return the original amount if no valid discount was applied
     return totalAmount;
 }
}
