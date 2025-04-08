package ui.Elements;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;


public class RoundedButton extends JButton {
    private int arcWidth = 20;
    private int arcHeight = 20;

    public RoundedButton(String text) {
    	
        super(text);
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setContentAreaFilled(false);
        setBackground(new Color(204, 220, 255));
        setFocusPainted(false);
        setOpaque(false);
        
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Create a rounded rectangle shape
        RoundRectangle2D.Double rect = new RoundRectangle2D.Double(1, 1, getWidth() - 2, getHeight() - 2, arcWidth - 2, arcHeight - 2);

        // Fill the rounded rectangle with the button's background color
        g2d.setColor(getBackground());
        g2d.fill(rect);

        // Draw the button's border
        g2d.setColor(Color.BLACK);
        g2d.draw(rect);

        // Draw the button's text
        super.paintComponent(g2d);

        g2d.dispose();
    }
}
