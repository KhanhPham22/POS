package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame {

    private JFrame frame;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    LoginFrame window = new LoginFrame();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public LoginFrame() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Coffee LCK");
        frame.setBounds(100, 100, 900, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setLayout(new BorderLayout());

        // **PANEL ĐĂNG NHẬP (WEST)**
        JPanel loginPanel = new JPanel();
        loginPanel.setPreferredSize(new Dimension(350, 600));
        loginPanel.setLayout(new GridBagLayout());
        frame.getContentPane().add(loginPanel, BorderLayout.WEST);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // **Tiêu đề "Login"**
        JLabel lblLogin = new JLabel("Coffee LCK");
        lblLogin.setFont(new Font("Arial", Font.BOLD, 24));
        lblLogin.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        loginPanel.add(lblLogin, gbc);
        gbc = new GridBagConstraints();
        
        // **Ô nhập Email với Icon**
        JPanel emailPanel = new JPanel(new BorderLayout(5, 0));
        emailPanel.setBorder(BorderFactory.createTitledBorder("Email Address"));

        
        ImageIcon emailIcon = new ImageIcon("C:\\TTTN\\POS PROJECT\\img\\login.png"); // Thay bằng đường dẫn icon của bạn
        Image emailImg = emailIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        JLabel emailIconLabel = new JLabel(new ImageIcon(emailImg));
        emailPanel.add(emailIconLabel, BorderLayout.WEST);

        JTextField emailField = new JTextField(20);
        emailField.setFont(new Font("Arial", Font.PLAIN, 16)); // Tăng kích thước font
        emailField.setPreferredSize(new Dimension(200, 40)); // Tăng chiều cao
        emailPanel.add(emailField, BorderLayout.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        loginPanel.add(emailPanel, gbc);
        gbc = new GridBagConstraints();
        
        // **Ô nhập Password với Icon**
        JPanel passwordPanel = new JPanel(new BorderLayout(5, 0));
        passwordPanel.setBorder(BorderFactory.createTitledBorder("Password"));

        // Tải icon password
        ImageIcon passwordIcon = new ImageIcon("C:\\TTTN\\POS PROJECT\\img\\password.png"); 
        Image passwordImg = passwordIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        JLabel passwordIconLabel = new JLabel(new ImageIcon(passwordImg));
        passwordPanel.add(passwordIconLabel, BorderLayout.WEST);

        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16)); // Tăng kích thước font
        passwordField.setPreferredSize(new Dimension(200, 40)); // Tăng chiều cao
        passwordPanel.add(passwordField, BorderLayout.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        loginPanel.add(passwordPanel, gbc);
        gbc = new GridBagConstraints();
        
        // **Checkbox "Keep me logged in"**
        JCheckBox keepMeLoggedIn = new JCheckBox("Keep me logged in");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        loginPanel.add(keepMeLoggedIn, gbc);
        gbc = new GridBagConstraints();
        
        // **Nút "Log in"**
        JButton loginButton = new JButton("Log in");
        loginButton.setBackground(new Color(30, 144, 255));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setFont(new Font("Arial", Font.BOLD, 18)); 
        loginButton.setPreferredSize(new Dimension(150, 50));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        loginPanel.add(loginButton, gbc);
        gbc = new GridBagConstraints();
        
        // **PANEL HÌNH ẢNH (CENTER)**
        JPanel imagePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                // Tạo gradient từ màu #E6F0FA (trên) đến #B3D4FF (dưới)
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(230, 240, 250), // #E6F0FA
                    0, getHeight(), new Color(179, 212, 255) // #B3D4FF
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        frame.getContentPane().add(imagePanel, BorderLayout.CENTER);

        // **Tải ảnh & resize**
        String imagePath = "C:\\TTTN\\POS PROJECT\\img\\lck.png"; // Đường dẫn ảnh
        ImageIcon icon = new ImageIcon(imagePath);
        Image img = icon.getImage();
        Image newImg = img.getScaledInstance(400, 300, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(newImg);

        // **Hiển thị ảnh**
        JLabel imageLabel = new JLabel(resizedIcon);
        imagePanel.add(imageLabel);
    }
}
