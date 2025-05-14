package ui;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import model.Employee;
import model.Owner;
import model.Person;
import service.PersonService;

public class ProfilePanel extends JPanel {

    private final PersonService personService;
    private Person currentPerson;
    private JLabel avatarLabel;
    private JButton uploadButton;
    private JButton removeButton;
    private final int AVATAR_SIZE = 150;

    // Labels for displaying personal information
    private JLabel fullNameLabel;
    private JLabel usernameLabel;
    private JLabel emailLabel;
    private JLabel genderLabel;
    private JLabel statusLabel;
    private JLabel typeLabel;
    private JLabel dobLabel;
    private JLabel phoneLabel;
    private JLabel addressLabel;
    private JLabel descriptionLabel;

    public ProfilePanel(PersonService personService) {
        this.personService = personService;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(245, 245, 245)); // Light gray background

        // Main content panel with card effect
        JPanel contentPanel = new JPanel(new BorderLayout(20, 20));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        contentPanel.setOpaque(true);
        contentPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Avatar display panel
        JPanel avatarPanel = new JPanel();
        avatarPanel.setBackground(Color.WHITE);
        avatarPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        avatarLabel = new JLabel();
        avatarLabel.setPreferredSize(new Dimension(AVATAR_SIZE, AVATAR_SIZE));
        avatarLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150), 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        avatarLabel.setHorizontalAlignment(SwingConstants.CENTER);
        avatarLabel.setVerticalAlignment(SwingConstants.CENTER);
        // Make avatar circular
        avatarLabel.setOpaque(true);
        avatarLabel.setBackground(Color.WHITE);
        avatarLabel.setBorder(new CircleBorder());

        avatarPanel.add(avatarLabel);
        contentPanel.add(avatarPanel, BorderLayout.WEST);

        // Information display panel
        JPanel infoPanel = new JPanel(new GridLayout(10, 2, 15, 10));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 100)),
            "Personal Information",
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
            javax.swing.border.TitledBorder.DEFAULT_POSITION,
            new Font("Segoe UI", Font.BOLD, 16)
        ));

        // Initialize information labels with alternating row colors
        infoPanel.add(createLabel("Full Name:", true));
        fullNameLabel = createLabel("", false);
        infoPanel.add(fullNameLabel);

        infoPanel.add(createLabel("Username:", true));
        usernameLabel = createLabel("", false);
        infoPanel.add(usernameLabel);

        infoPanel.add(createLabel("Email:", true));
        emailLabel = createLabel("", false);
        infoPanel.add(emailLabel);

        infoPanel.add(createLabel("Gender:", true));
        genderLabel = createLabel("", false);
        infoPanel.add(genderLabel);

        infoPanel.add(createLabel("Date of Birth:", true));
        dobLabel = createLabel("", false);
        infoPanel.add(dobLabel);

        infoPanel.add(createLabel("Phone:", true));
        phoneLabel = createLabel("", false);
        infoPanel.add(phoneLabel);

        infoPanel.add(createLabel("Address:", true));
        addressLabel = createLabel("", false);
        infoPanel.add(addressLabel);

        infoPanel.add(createLabel("Status:", true));
        statusLabel = createLabel("", false);
        infoPanel.add(statusLabel);

        infoPanel.add(createLabel("Type:", true));
        typeLabel = createLabel("", false);
        infoPanel.add(typeLabel);

        infoPanel.add(createLabel("Description:", true));
        descriptionLabel = createLabel("", false);
        infoPanel.add(descriptionLabel);

        contentPanel.add(infoPanel, BorderLayout.CENTER);
        add(contentPanel, BorderLayout.CENTER);

        // Button panel for avatar actions
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(new Color(245, 245, 245));

        uploadButton = new JButton("Upload Avatar");
        styleButton(uploadButton, new Color(66, 165, 245)); // Blue
        uploadButton.addActionListener(new UploadActionListener());

        removeButton = new JButton("Remove Avatar");
        styleButton(removeButton, new Color(239, 83, 80)); // Red
        removeButton.addActionListener(e -> removeAvatar());

        buttonPanel.add(uploadButton);
        buttonPanel.add(removeButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
    }

    private JLabel createLabel(String text, boolean isTitle) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", isTitle ? Font.BOLD : Font.PLAIN, 14));
        label.setBackground(isTitle ? new Color(230, 230, 230) : Color.WHITE);
        label.setOpaque(true);
        label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return label;
    }

    public void setPerson(Person person) {
        this.currentPerson = person;
        updateAvatarDisplay();
        updateInfoDisplay();
    }

    private void updateAvatarDisplay() {
        if (currentPerson == null) {
            avatarLabel.setIcon(null);
            avatarLabel.setText("No Person Selected");
            removeButton.setEnabled(false);
            return;
        }

        String avatarPath = null;
        if (currentPerson instanceof Employee) {
            avatarPath = ((Employee) currentPerson).getAvatarPath();
        } else if (currentPerson instanceof Owner) {
            avatarPath = ((Owner) currentPerson).getAvatarPath();
        }

        if (avatarPath != null && !avatarPath.isEmpty()) {
            try {
                File imageFile = new File(avatarPath);
                if (imageFile.exists()) {
                    BufferedImage img = ImageIO.read(imageFile);
                    Image scaledImg = img.getScaledInstance(AVATAR_SIZE, AVATAR_SIZE, Image.SCALE_SMOOTH);
                    avatarLabel.setIcon(new ImageIcon(scaledImg));
                    avatarLabel.setText("");
                    removeButton.setEnabled(true);
                    return;
                }
            } catch (Exception e) {
                showError("Error loading avatar: " + e.getMessage());
            }
        }

        avatarLabel.setIcon(null);
        avatarLabel.setText("No Avatar");
        removeButton.setEnabled(false);
    }

    private void updateInfoDisplay() {
        if (currentPerson == null) {
            fullNameLabel.setText("N/A");
            usernameLabel.setText("N/A");
            emailLabel.setText("N/A");
            genderLabel.setText("N/A");
            dobLabel.setText("N/A");
            phoneLabel.setText("N/A");
            addressLabel.setText("N/A");
            statusLabel.setText("N/A");
            typeLabel.setText("N/A");
            descriptionLabel.setText("N/A");
            return;
        }

        // Construct full name
        StringBuilder fullName = new StringBuilder();
        if (currentPerson.getPersonFirstName() != null) {
            fullName.append(currentPerson.getPersonFirstName()).append(" ");
        }
        if (currentPerson.getPersonMiddleName() != null) {
            fullName.append(currentPerson.getPersonMiddleName()).append(" ");
        }
        if (currentPerson.getPersonLastName() != null) {
            fullName.append(currentPerson.getPersonLastName());
        }
        fullNameLabel.setText(fullName.toString().trim().isEmpty() ? "N/A" : fullName.toString().trim());

        // Set username based on Employee or Owner
        String username = "N/A";
        if (currentPerson instanceof Employee employee) {
            username = employee.getLoginUsername() != null ? employee.getLoginUsername() : "N/A";
        } else if (currentPerson instanceof Owner owner) {
            username = owner.getLoginUsername() != null ? owner.getLoginUsername() : "N/A";
        }
        usernameLabel.setText(username);

        // Set other fields with null checks
        emailLabel.setText(currentPerson.getEmail() != null ? currentPerson.getEmail() : "N/A");
        genderLabel.setText(currentPerson.getPersonGender() != null ? currentPerson.getPersonGender() : "N/A");
        dobLabel.setText(currentPerson.getDateOfBirth() != null ? currentPerson.getDateOfBirth() : "N/A");
        phoneLabel.setText(currentPerson.getPhone() != null ? currentPerson.getPhone() : "N/A");

        // Construct address
        StringBuilder address = new StringBuilder();
        if (currentPerson.getAddress() != null) {
            address.append(currentPerson.getAddress());
        }
        if (currentPerson.getCity() != null) {
            address.append(", ").append(currentPerson.getCity());
        }
        if (currentPerson.getState() != null) {
            address.append(", ").append(currentPerson.getState());
        }
        if (currentPerson.getCountry() != null) {
            address.append(", ").append(currentPerson.getCountry());
        }
        addressLabel.setText(address.toString().startsWith(", ") ? address.substring(2) :
                            address.toString().isEmpty() ? "N/A" : address.toString());

        statusLabel.setText(currentPerson.isEnabledFlag() ? "Active" : "Inactive");

        // Set type and description
        if (currentPerson instanceof Employee employee) {
            typeLabel.setText(employee.getEmployeeType() != null ? employee.getEmployeeType() : "Employee");
            descriptionLabel.setText(employee.getDescription() != null ? employee.getDescription() : "N/A");
        } else if (currentPerson instanceof Owner owner) {
            typeLabel.setText("Owner");
            descriptionLabel.setText(owner.getDescription() != null ? owner.getDescription() : "N/A");
        } else {
            typeLabel.setText("N/A");
            descriptionLabel.setText("N/A");
        }
    }

    private class UploadActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentPerson == null) {
                showError("Please select a person first");
                return;
            }

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Image files", "jpg", "jpeg", "png", "gif"));
            int result = fileChooser.showOpenDialog(ProfilePanel.this);

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    // Validate image
                    BufferedImage img = ImageIO.read(selectedFile);
                    if (img == null) {
                        showError("Invalid image file");
                        return;
                    }

                    // Set avatar path
                    String avatarPath = selectedFile.getAbsolutePath();
                    if (currentPerson instanceof Employee employee) {
                        employee.setAvatarPath(avatarPath);
                        personService.updateEmployee(employee);
                    } else if (currentPerson instanceof Owner owner) {
                        owner.setAvatarPath(avatarPath);
                        personService.updateOwner(owner);
                    }

                    updateAvatarDisplay();
                } catch (Exception ex) {
                    showError("Error uploading avatar: " + ex.getMessage());
                }
            }
        }
    }

    private void removeAvatar() {
        if (currentPerson == null) {
            return;
        }

        try {
            if (currentPerson instanceof Employee employee) {
                employee.setAvatarPath(null);
                personService.updateEmployee(employee);
            } else if (currentPerson instanceof Owner owner) {
                owner.setAvatarPath(null);
                personService.updateOwner(owner);
            }
            updateAvatarDisplay();
        } catch (Exception ex) {
            showError("Error removing avatar: " + ex.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Custom border for circular avatar
    private static class CircleBorder implements javax.swing.border.Border {
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(150, 150, 150));
            g2.drawOval(x, y, width - 1, height - 1);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(2, 2, 2, 2);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }
    }
}