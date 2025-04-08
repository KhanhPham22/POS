package ui.Elements;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.text.*;

public class NumberFilter extends JTextField {
	
    private int arcWidth = 20;
    private int arcHeight = 20;
    
    public NumberFilter() {
        ((PlainDocument) this.getDocument()).setDocumentFilter(new DecimalFilter());
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setOpaque(false);
        setColumns(20);
    }

    @Override
    protected void paintComponent(Graphics g) {
    	Graphics2D g2d = (Graphics2D) g.create();
    	g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    	
    	// Create a rounded rectangle shape
    	RoundRectangle2D.Double rect = new RoundRectangle2D.Double(1, 1, getWidth() - 2, getHeight() - 2, arcWidth - 2, arcHeight - 2);
    	
    	// Fill the rounded rectangle with the text field's background color
    	g2d.setColor(getBackground());
    	g2d.fill(rect);
    	
    	// Draw the text field's border
    	g2d.setColor(Color.BLACK);
    	g2d.draw(rect);
    	
    	// Draw the text field's text
    	super.paintComponent(g2d);
    	
    	g2d.dispose();
    }
    
    private class DecimalFilter extends DocumentFilter {
    	 private static final Pattern INTEGER_PATTERN = Pattern.compile("^\\d*$");
    	    private static final Pattern DECIMAL_PATTERN = Pattern.compile("^\\d*\\.?\\d*$");
    	    private static final Pattern ZIP_PATTERN = Pattern.compile("^\\d{0,5}(-\\d{0,4})?$");

    	    @Override
    	    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
    	        String newText = fb.getDocument().getText(0, fb.getDocument().getLength()) + string;
    	        if (isValidInput(newText)) {
    	            super.insertString(fb, offset, string, attr);
    	        }
    	    }

    	    @Override
    	    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
    	        String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
    	        String newText = currentText.substring(0, offset) + text + currentText.substring(offset + length);
    	        if (isValidInput(newText)) {
    	            super.replace(fb, offset, length, text, attrs);
    	        }
    	    }

    	    private boolean isValidInput(String text) {
    	        return INTEGER_PATTERN.matcher(text).matches() ||
    	               DECIMAL_PATTERN.matcher(text).matches() ||
    	               ZIP_PATTERN.matcher(text).matches();
    	    }
    	}
}
