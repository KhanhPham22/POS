package ui;

import javax.swing.*;
import java.awt.*;

public class StoreFrame extends JFrame {
    public StoreFrame() {
        // Thiết lập JFrame chính
        setTitle("Thông tin cửa hàng");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel chính chứa toàn bộ nội dung, đặt tên là storePanel
        JPanel storePanel = new JPanel();
        storePanel.setLayout(new BorderLayout(10, 10));
        storePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        storePanel.setBackground(Color.WHITE);

        // Panel chứa tiêu đề
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel titleLabel = new JLabel("Thông tin cửa hàng");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titlePanel.add(titleLabel);
        titlePanel.setBackground(Color.WHITE);

        // Panel chứa logo và thông tin chi tiết
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BorderLayout(10, 10));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        // Logo
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(Color.WHITE);
        JLabel logoLabel = new JLabel("LOGO SHOP", SwingConstants.CENTER);
        logoLabel.setPreferredSize(new Dimension(100, 100));
        logoLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        logoPanel.add(logoLabel);

        // Thông tin chi tiết
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new GridBagLayout());
        detailsPanel.setBackground(Color.WHITE);

        // Tiêu đề thông tin chi tiết
        JLabel infoTitleLabel = new JLabel("Thông tin chi tiết");
        infoTitleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        GridBagConstraints gbcInfoTitle = new GridBagConstraints();
        gbcInfoTitle.insets = new Insets(5, 5, 5, 5);
        gbcInfoTitle.anchor = GridBagConstraints.WEST;
        gbcInfoTitle.gridx = 0;
        gbcInfoTitle.gridy = 0;
        gbcInfoTitle.gridwidth = 2;
        detailsPanel.add(infoTitleLabel, gbcInfoTitle);

        // Thông tin cửa hàng - Tên cửa hàng
        GridBagConstraints gbcStoreNameLabel = new GridBagConstraints();
        gbcStoreNameLabel.insets = new Insets(5, 5, 5, 5);
        gbcStoreNameLabel.anchor = GridBagConstraints.WEST;
        gbcStoreNameLabel.gridx = 0;
        gbcStoreNameLabel.gridy = 1;
        detailsPanel.add(new JLabel("Tên cửa hàng:"), gbcStoreNameLabel);

        GridBagConstraints gbcStoreNameValue = new GridBagConstraints();
        gbcStoreNameValue.insets = new Insets(5, 5, 5, 5);
        gbcStoreNameValue.anchor = GridBagConstraints.WEST;
        gbcStoreNameValue.gridx = 1;
        gbcStoreNameValue.gridy = 1;
        detailsPanel.add(new JLabel("Coffee House"), gbcStoreNameValue);

        // Địa chỉ
        GridBagConstraints gbcAddressLabel = new GridBagConstraints();
        gbcAddressLabel.insets = new Insets(5, 5, 5, 5);
        gbcAddressLabel.anchor = GridBagConstraints.WEST;
        gbcAddressLabel.gridx = 0;
        gbcAddressLabel.gridy = 2;
        detailsPanel.add(new JLabel("Địa chỉ:"), gbcAddressLabel);

        GridBagConstraints gbcAddressValue = new GridBagConstraints();
        gbcAddressValue.insets = new Insets(5, 5, 5, 5);
        gbcAddressValue.anchor = GridBagConstraints.WEST;
        gbcAddressValue.gridx = 1;
        gbcAddressValue.gridy = 2;
        detailsPanel.add(new JLabel("123 Đường Lê Lợi, Quận 1"), gbcAddressValue);

        // Số điện thoại
        GridBagConstraints gbcPhoneLabel = new GridBagConstraints();
        gbcPhoneLabel.insets = new Insets(5, 5, 5, 5);
        gbcPhoneLabel.anchor = GridBagConstraints.WEST;
        gbcPhoneLabel.gridx = 0;
        gbcPhoneLabel.gridy = 3;
        detailsPanel.add(new JLabel("Số điện thoại:"), gbcPhoneLabel);

        GridBagConstraints gbcPhoneValue = new GridBagConstraints();
        gbcPhoneValue.insets = new Insets(5, 5, 5, 5);
        gbcPhoneValue.anchor = GridBagConstraints.WEST;
        gbcPhoneValue.gridx = 1;
        gbcPhoneValue.gridy = 3;
        detailsPanel.add(new JLabel("028 1234 5678"), gbcPhoneValue);

        // Email
        GridBagConstraints gbcEmailLabel = new GridBagConstraints();
        gbcEmailLabel.insets = new Insets(5, 5, 5, 5);
        gbcEmailLabel.anchor = GridBagConstraints.WEST;
        gbcEmailLabel.gridx = 0;
        gbcEmailLabel.gridy = 4;
        detailsPanel.add(new JLabel("Email:"), gbcEmailLabel);

        GridBagConstraints gbcEmailValue = new GridBagConstraints();
        gbcEmailValue.insets = new Insets(5, 5, 5, 5);
        gbcEmailValue.anchor = GridBagConstraints.WEST;
        gbcEmailValue.gridx = 1;
        gbcEmailValue.gridy = 4;
        detailsPanel.add(new JLabel("info@coffeehouse.com"), gbcEmailValue);

        // Mã số thuế
        GridBagConstraints gbcTaxLabel = new GridBagConstraints();
        gbcTaxLabel.insets = new Insets(5, 5, 5, 5);
        gbcTaxLabel.anchor = GridBagConstraints.WEST;
        gbcTaxLabel.gridx = 0;
        gbcTaxLabel.gridy = 5;
        detailsPanel.add(new JLabel("Mã số thuế:"), gbcTaxLabel);

        GridBagConstraints gbcTaxValue = new GridBagConstraints();
        gbcTaxValue.insets = new Insets(5, 5, 5, 5);
        gbcTaxValue.anchor = GridBagConstraints.WEST;
        gbcTaxValue.gridx = 1;
        gbcTaxValue.gridy = 5;
        detailsPanel.add(new JLabel("0123456789"), gbcTaxValue);

        // Giờ mở cửa - Tiêu đề
        JLabel openHoursTitleLabel = new JLabel("Giờ mở cửa");
        openHoursTitleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        GridBagConstraints gbcOpenHoursTitle = new GridBagConstraints();
        gbcOpenHoursTitle.insets = new Insets(5, 5, 5, 5);
        gbcOpenHoursTitle.anchor = GridBagConstraints.WEST;
        gbcOpenHoursTitle.gridx = 0;
        gbcOpenHoursTitle.gridy = 6;
        gbcOpenHoursTitle.gridwidth = 2;
        detailsPanel.add(openHoursTitleLabel, gbcOpenHoursTitle);

        // Giờ mở cửa - Thứ Hai đến Thứ Sáu
        GridBagConstraints gbcMonFriLabel = new GridBagConstraints();
        gbcMonFriLabel.insets = new Insets(5, 5, 5, 5);
        gbcMonFriLabel.anchor = GridBagConstraints.WEST;
        gbcMonFriLabel.gridx = 0;
        gbcMonFriLabel.gridy = 7;
        detailsPanel.add(new JLabel("Thứ Hai - Thứ Sáu:"), gbcMonFriLabel);

        GridBagConstraints gbcMonFriValue = new GridBagConstraints();
        gbcMonFriValue.insets = new Insets(5, 5, 5, 5);
        gbcMonFriValue.anchor = GridBagConstraints.WEST;
        gbcMonFriValue.gridx = 1;
        gbcMonFriValue.gridy = 7;
        detailsPanel.add(new JLabel("7:00 - 22:00"), gbcMonFriValue);

        // Giờ mở cửa - Thứ Bảy đến Chủ Nhật
        GridBagConstraints gbcSatSunLabel = new GridBagConstraints();
        gbcSatSunLabel.insets = new Insets(5, 5, 5, 5);
        gbcSatSunLabel.anchor = GridBagConstraints.WEST;
        gbcSatSunLabel.gridx = 0;
        gbcSatSunLabel.gridy = 8;
        detailsPanel.add(new JLabel("Thứ Bảy - Chủ Nhật:"), gbcSatSunLabel);

        GridBagConstraints gbcSatSunValue = new GridBagConstraints();
        gbcSatSunValue.insets = new Insets(5, 5, 5, 5);
        gbcSatSunValue.anchor = GridBagConstraints.WEST;
        gbcSatSunValue.gridx = 1;
        gbcSatSunValue.gridy = 8;
        detailsPanel.add(new JLabel("8:00 - 23:00"), gbcSatSunValue);

        // Thêm logo và thông tin vào infoPanel
        infoPanel.add(logoPanel, BorderLayout.WEST);
        infoPanel.add(detailsPanel, BorderLayout.CENTER);

        // Panel chứa các nút
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);

        JButton editButton = new JButton("Chỉnh sửa");
        editButton.setBackground(new Color(0, 120, 215)); // Màu xanh
        editButton.setForeground(Color.WHITE);
        JButton addInfoButton = new JButton("Thêm thông tin");
        addInfoButton.setBackground(Color.LIGHT_GRAY);

        buttonPanel.add(editButton);
        buttonPanel.add(addInfoButton);

        // Thêm các thành phần vào storePanel
        storePanel.add(titlePanel, BorderLayout.NORTH);
        storePanel.add(infoPanel, BorderLayout.CENTER);
        storePanel.add(buttonPanel, BorderLayout.SOUTH);

        // Thêm storePanel vào JFrame
        add(storePanel);
    }
}
