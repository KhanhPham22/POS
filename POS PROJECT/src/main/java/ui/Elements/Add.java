package ui.Elements;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class Add {

	Font font = new Font("Tahoma", Font.PLAIN, 30);
	
	GridBagConstraints c = new GridBagConstraints();

	public void AddLabel(String text, int x, int y, GridBagConstraints c, JPanel contentPanel) {
	    JLabel label = new JLabel(text);
	    label.setFont(font);
	    label.setMaximumSize(new Dimension(250,75));
	    c.gridx = x;
	    c.gridy = y;
	    contentPanel.add(label, c);
	}

	public JTextField AddTextField(int x, int y, GridBagConstraints c, JPanel contentPanel) {
	    JTextField textField = new RoundedTextField();
	    textField.setColumns(20);
	    textField.setFont(font);
	    textField.setMaximumSize(new Dimension(500, 75));
	    c.gridx = x;
	    c.gridy = y;
	    contentPanel.add(textField, c);
	    return textField;
	}
	
	public void Infor_AddLabel(String text, int x, int y, GridBagConstraints c, JPanel contentPanel) {
		
	    JLabel label = new JLabel(text);
	    label.setFont(font);
	    label.setBackground(new Color(204, 220, 255));
	    label.setOpaque(true);
	    c.gridx = x;
	    c.gridy = y;
	    contentPanel.add(label, c);
		
	}
}
