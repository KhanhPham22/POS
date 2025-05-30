package ui;

import javax.swing.*;
import java.awt.*;
import java.io.File;

import model.Product;
import service.ProductService;

public class PromotionPanel extends JPanel {
    private static final String IMG_DIR = "C:\\TTTN\\POS PROJECT\\img\\Product\\";
    private static final String MATCHA_LATTE_IMG_PATH = IMG_DIR + "MatchaLatte_L.jpg";
    private static final String TIRAMISU_IMG_PATH = IMG_DIR + "Tiramisu Cake_Default.jpg";
    private static final String MATCHA_LATTE_NAME = "Matcha Latte";
    private static final String TIRAMISU_NAME = "Tiramisu";

    private final ProductService productService;

    public PromotionPanel(ProductService productService, MenuePanel menuePanel) {
        this.productService = productService;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Reduced padding
        initializeUI();
    }

    private void initializeUI() {
        // Top panel for promotion label
        JPanel topPanel = new JPanel(new GridLayout(3, 1));
        topPanel.setBackground(Color.WHITE);

        JLabel promoLabel = new JLabel("KHUYẾN MÃI Tháng 6", SwingConstants.CENTER);
        promoLabel.setForeground(new Color(70, 130, 180));
        promoLabel.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Slightly smaller font

        JLabel titleLabel = new JLabel("MUA 1 TẶNG 1", SwingConstants.CENTER);
        titleLabel.setForeground(new Color(0, 128, 0));
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18)); // Smaller font

        JLabel subtitleLabel = new JLabel("Cho khách hàng mới", SwingConstants.CENTER);
        subtitleLabel.setForeground(Color.BLACK);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12)); // Smaller font

        topPanel.add(promoLabel);
        topPanel.add(titleLabel);
        topPanel.add(subtitleLabel);
        add(topPanel, BorderLayout.NORTH);

        // Center panel for smaller images
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5)); // Reduced spacing
        centerPanel.setBackground(Color.WHITE);

        // Matcha Latte image
        JLabel matchaImageLabel = new JLabel();
        loadImage(MATCHA_LATTE_IMG_PATH, matchaImageLabel, 500, 500); // Smaller image
        centerPanel.add(matchaImageLabel);

        JLabel plusLabel = new JLabel("+", SwingConstants.CENTER);
        plusLabel.setFont(new Font("Segoe UI", Font.BOLD, 18)); // Smaller font
        centerPanel.add(plusLabel);

        // Tiramisu image
        JLabel tiramisuImageLabel = new JLabel();
        loadImage(TIRAMISU_IMG_PATH, tiramisuImageLabel, 500, 500); // Smaller image
        centerPanel.add(tiramisuImageLabel);

        add(centerPanel, BorderLayout.CENTER);
    }

    private void loadImage(String path, JLabel label, int width, int height) {
        ImageIcon icon = null;
        try {
            File imageFile = new File(path);
            if (imageFile.exists()) {
                icon = new ImageIcon(path);
            } else {
                // Fallback to a default image if the specified image doesn't exist
                File defaultFile = new File(IMG_DIR + "default.png");
                String defaultPath = defaultFile.exists() ? IMG_DIR + "default.png" : "C:\\TTTN\\POS PROJECT\\img\\default.png";
                icon = new ImageIcon(defaultPath);
            }
            Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            label.setIcon(new ImageIcon());
            System.err.println("Failed to load image: " + e.getMessage());
        }
    }
}