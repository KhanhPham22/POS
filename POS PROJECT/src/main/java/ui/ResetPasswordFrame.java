package ui;

import javax.swing.*;
import java.awt.*;

public class ResetPasswordFrame extends JFrame {

    public ResetPasswordFrame() {
        setTitle("Reset Password - Coffee LCK");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        add(contentPanel);

        // ===== Tiêu đề =====
        GridBagConstraints gbcTitle = new GridBagConstraints();
        gbcTitle.insets = new Insets(12, 20, 12, 20);
        gbcTitle.gridx = 0;
        gbcTitle.gridy = 0;
        gbcTitle.gridwidth = 2;
        gbcTitle.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Reset Your Password");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(titleLabel, gbcTitle);

        // ===== Email Label =====
        GridBagConstraints gbcEmailLabel = new GridBagConstraints();
        gbcEmailLabel.insets = new Insets(10, 20, 10, 10);
        gbcEmailLabel.gridx = 0;
        gbcEmailLabel.gridy = 1;
        gbcEmailLabel.anchor = GridBagConstraints.EAST;

        JLabel emailLabel = new JLabel("Email:");
        contentPanel.add(emailLabel, gbcEmailLabel);

        // ===== Email Field =====
        GridBagConstraints gbcEmailField = new GridBagConstraints();
        gbcEmailField.insets = new Insets(10, 10, 10, 20);
        gbcEmailField.gridx = 1;
        gbcEmailField.gridy = 1;
        gbcEmailField.fill = GridBagConstraints.HORIZONTAL;

        JTextField emailField = new JTextField(20);
        contentPanel.add(emailField, gbcEmailField);

        // ===== New Password Label =====
        GridBagConstraints gbcNewPassLabel = new GridBagConstraints();
        gbcNewPassLabel.insets = new Insets(10, 20, 10, 10);
        gbcNewPassLabel.gridx = 0;
        gbcNewPassLabel.gridy = 2;
        gbcNewPassLabel.anchor = GridBagConstraints.EAST;

        JLabel newPasswordLabel = new JLabel("New Password:");
        contentPanel.add(newPasswordLabel, gbcNewPassLabel);

        // ===== New Password Field =====
        GridBagConstraints gbcNewPassField = new GridBagConstraints();
        gbcNewPassField.insets = new Insets(10, 10, 10, 20);
        gbcNewPassField.gridx = 1;
        gbcNewPassField.gridy = 2;
        gbcNewPassField.fill = GridBagConstraints.HORIZONTAL;

        JPasswordField newPasswordField = new JPasswordField(20);
        contentPanel.add(newPasswordField, gbcNewPassField);

        // ===== Confirm Password Label =====
        GridBagConstraints gbcConfirmLabel = new GridBagConstraints();
        gbcConfirmLabel.insets = new Insets(10, 20, 10, 10);
        gbcConfirmLabel.gridx = 0;
        gbcConfirmLabel.gridy = 3;
        gbcConfirmLabel.anchor = GridBagConstraints.EAST;

        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        contentPanel.add(confirmPasswordLabel, gbcConfirmLabel);

        // ===== Confirm Password Field =====
        GridBagConstraints gbcConfirmField = new GridBagConstraints();
        gbcConfirmField.insets = new Insets(10, 10, 10, 20);
        gbcConfirmField.gridx = 1;
        gbcConfirmField.gridy = 3;
        gbcConfirmField.fill = GridBagConstraints.HORIZONTAL;

        JPasswordField confirmPasswordField = new JPasswordField(20);
        contentPanel.add(confirmPasswordField, gbcConfirmField);

        // ===== Reset Button =====
        GridBagConstraints gbcButton = new GridBagConstraints();
        gbcButton.insets = new Insets(20, 20, 20, 20);
        gbcButton.gridx = 0;
        gbcButton.gridy = 4;
        gbcButton.gridwidth = 2;
        gbcButton.anchor = GridBagConstraints.CENTER;

        JButton resetButton = new JButton("Reset Password");
        resetButton.setBackground(new Color(30, 144, 255));
        resetButton.setForeground(Color.WHITE);
        resetButton.setFont(new Font("Arial", Font.BOLD, 16));
        resetButton.setPreferredSize(new Dimension(200, 40));
        contentPanel.add(resetButton, gbcButton);

        // ===== Event Reset =====
        resetButton.addActionListener(e -> {
            String email = emailField.getText().trim();
            String newPassword = new String(newPasswordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            if (email.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.");
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match.");
                return;
            }

            // TODO: Gọi service reset mật khẩu ở đây
            JOptionPane.showMessageDialog(this, "Password reset successfully!");
            dispose();
        });
    }
}



