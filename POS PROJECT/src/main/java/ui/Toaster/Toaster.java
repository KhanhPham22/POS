package ui.Toaster;

import javax.swing.*;
import java.awt.*;

public class Toaster {

    private final JComponent componentToToastOn;

    public Toaster(JComponent componentToToastOn) {
        this.componentToToastOn = componentToToastOn;
    }

    public void success(String message) {
        showToast(message, new Color(76, 175, 80)); // Green
    }

    public void error(String message) {
        showToast(message, new Color(244, 67, 54)); // Red
    }

    private void showToast(String message, Color backgroundColor) {
        JLabel toastLabel = new JLabel(message);
        toastLabel.setOpaque(true);
        toastLabel.setBackground(backgroundColor);
        toastLabel.setForeground(Color.WHITE);
        toastLabel.setHorizontalAlignment(SwingConstants.CENTER);
        toastLabel.setFont(new Font("Arial", Font.BOLD, 14));
        toastLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        toastLabel.setSize(toastLabel.getPreferredSize());

        JLayeredPane layeredPane = componentToToastOn.getRootPane().getLayeredPane();
        Dimension paneSize = componentToToastOn.getSize();

        int x = (paneSize.width - toastLabel.getWidth()) / 2;
        int y = 30; // top padding

        toastLabel.setBounds(x, y, toastLabel.getWidth(), toastLabel.getHeight());
        layeredPane.add(toastLabel, JLayeredPane.POPUP_LAYER);

        Timer timer = new Timer(2500, e -> layeredPane.remove(toastLabel));
        timer.setRepeats(false);
        timer.start();

        layeredPane.repaint();
    }
}
