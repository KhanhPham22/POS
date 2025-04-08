package ui.Elements;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class Button extends JButton {
	    private Timer flashTimer;

	    public Button(JButton buttonName, String text, BufferedImage img) {
	    	
	        setText(text);
	        setIcon(new ImageIcon(img));
	        setForeground(Color.BLACK);
	        setFont(new Font("Tahoma", Font.BOLD, 20));
	        setBackground(Color.LIGHT_GRAY);

	        setVerticalTextPosition(SwingConstants.BOTTOM);
	        setHorizontalTextPosition(SwingConstants.CENTER);

	        setBorder(null);
	        setBorderPainted(false);
	        setContentAreaFilled(false);
	        setFocusPainted(false);

	        addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                flashButton();
	            }
	        });

	        // Create a timer for the flash effect
	        flashTimer = new Timer(100, new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                setContentAreaFilled(false);
	                repaint();
	            }
	        });
	        flashTimer.setRepeats(false);
	    }

	    private void flashButton() {
	        setContentAreaFilled(true);
	        repaint();

	        // Start the timer to revert the button's appearance
	        flashTimer.restart();
	    }
	}	
