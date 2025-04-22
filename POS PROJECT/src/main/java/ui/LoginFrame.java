package ui;

import javax.swing.*;

import dao.EmployeeDao;
import dao.OwnerDao;
import dao.UserSessionDao;
import model.UserSession;
import service.AuthenticationService;
import service.HashService;

import java.awt.*;
import java.awt.event.*;
import ui.Utils.*;
import ui.Toaster.Toaster;

public class LoginFrame {
	
	private final AuthenticationService authService;
    JFrame frame;

    public LoginFrame() {
        
        this.authService = new AuthenticationService(
            new EmployeeDao(), 
            new OwnerDao(),   
            new UserSessionDao(), 
            new HashService() 
        );
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Coffee LCK");
        frame.setBounds(100, 100, 900, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setLayout(new BorderLayout());

        Toaster toaster = new Toaster(frame.getRootPane());

        // === LOGIN PANEL (WEST) ===
        JPanel loginPanel = new JPanel();
        loginPanel.setPreferredSize(new Dimension(400, 600));
        loginPanel.setLayout(new GridBagLayout());
        loginPanel.setBackground(Color.WHITE); // trắng cho hiện đại
        frame.getContentPane().add(loginPanel, BorderLayout.WEST);

        Font fontLabel = new Font("Arial", Font.BOLD, 24);
        Font fontField = new Font("Arial", Font.PLAIN, 16);

        GridBagConstraints gbc;

        // **Tiêu đề**
        JLabel lblLogin = new JLabel("Coffee LCK");
        lblLogin.setFont(fontLabel);
        lblLogin.setHorizontalAlignment(SwingConstants.CENTER);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 30, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        loginPanel.add(lblLogin, gbc);

        // **Email**
        JPanel emailPanel = new JPanel(new BorderLayout(10, 0));
        emailPanel.setBorder(BorderFactory.createTitledBorder("Username"));
        emailPanel.setBackground(Color.WHITE);

        ImageIcon emailIcon = new ImageIcon("C:\\TTTN\\POS PROJECT\\img\\login.png");
        Image emailImg = emailIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        JLabel emailIconLabel = new JLabel(new ImageIcon(emailImg));
        emailPanel.add(emailIconLabel, BorderLayout.WEST);

        JTextField emailField = new JTextField(20);
        emailField.setFont(fontField);
        emailField.setBorder(null);
        emailPanel.add(emailField, BorderLayout.CENTER);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        loginPanel.add(emailPanel, gbc);

        // **Password**
        JPanel passwordPanel = new JPanel(new BorderLayout(10, 0));
        passwordPanel.setBorder(BorderFactory.createTitledBorder("Password"));
        passwordPanel.setBackground(Color.WHITE);

        ImageIcon passIcon = new ImageIcon("C:\\TTTN\\POS PROJECT\\img\\password.png");
        Image passImg = passIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        JLabel passIconLabel = new JLabel(new ImageIcon(passImg));
        passwordPanel.add(passIconLabel, BorderLayout.WEST);

        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setFont(fontField);
        passwordField.setBorder(null);
        passwordPanel.add(passwordField, BorderLayout.CENTER);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        loginPanel.add(passwordPanel, gbc);

        // **Checkbox "Keep me logged in"**
        JCheckBox keepMeLoggedIn = new JCheckBox("Keep me logged in");
        keepMeLoggedIn.setBackground(Color.WHITE);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 20, 5, 20);
        gbc.anchor = GridBagConstraints.WEST;
        loginPanel.add(keepMeLoggedIn, gbc);

        // **Link Forgot Password**
        HyperlinkText forgotPasswordLink = new HyperlinkText("Forgot password?", 100, 0, () -> {
            ResetPasswordFrame resetFrame = new ResetPasswordFrame(authService);
            resetFrame.setVisible(true);
        });
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 20, 20, 20);
        gbc.anchor = GridBagConstraints.CENTER;
        loginPanel.add(forgotPasswordLink, gbc);

        // **Nút Login**
        JButton loginButton = new JButton("Log in");
        loginButton.setBackground(new Color(30, 144, 255));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 18));
        loginButton.setPreferredSize(new Dimension(160, 45));
        loginButton.setFocusPainted(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 20, 10, 20);
        gbc.anchor = GridBagConstraints.CENTER;
        loginPanel.add(loginButton, gbc);

        // === IMAGE PANEL (CENTER) ===
        JPanel imagePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(230, 240, 250),
                    0, getHeight(), new Color(179, 212, 255)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        imagePanel.setLayout(new GridBagLayout());
        frame.getContentPane().add(imagePanel, BorderLayout.CENTER);

        ImageIcon icon = new ImageIcon("C:\\TTTN\\POS PROJECT\\img\\lck.png");
        Image img = icon.getImage().getScaledInstance(400, 300, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(img));
        imagePanel.add(imageLabel);

        // Login logic
        loginButton.addActionListener(e -> handleLogin(emailField, passwordField, toaster));

        frame.setVisible(true);
    }

        
    private void handleLogin(JTextField emailField, JPasswordField passwordField, Toaster toaster) {
        String username = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            toaster.error("Username and password cannot be empty!");
            return;
        }

        try {
            UserSession session = authService.login(username, password);
            toaster.success("Login successful!");

            if (session.getEmployee() != null) {
                SwingUtilities.invokeLater(() -> {
                    PosUI posUI = new PosUI();
                    posUI.setVisible(true);
                });
            } else if (session.getOwner() != null) {
                SwingUtilities.invokeLater(() -> {
                    ManagerFrame managerFrame = new ManagerFrame();
                    managerFrame.setVisible(true);
                });
            } else {
                toaster.error("Unknown role!");
                return;
            }

            frame.dispose(); // đóng login sau khi thành công

        } catch (Exception ex) {
            toaster.error("Login failed: " + ex.getMessage());
        }
        }
    
}
