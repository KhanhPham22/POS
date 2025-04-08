package ui.Elements;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import ui.ManagerFrame;

public class SuccessButton extends JButton {

    Border rounded = new LineBorder(new Color(212, 207, 195), 5, true);
    Border empty = new EmptyBorder(0, 0, 0, 0);

    public SuccessButton(JDialog dialog, JPanel panel) {
        panel.removeAll();
        panel.setLayout(null);

        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);
        panel.setSize(400, 300);

        panel.setBackground(Color.white);

        JLabel success_Text = new JLabel("Success");
        success_Text.setHorizontalAlignment(SwingConstants.CENTER);
        success_Text.setFont(new Font("Tahoma", Font.PLAIN, 30));
        success_Text.setBounds(0, 0, 400, 100);
        panel.add(success_Text);

        JButton closeButton = new RoundedButton("<html>Close<br><font size='-1'>[ESC]</font></html>");
        closeButton.setForeground(Color.RED);
        closeButton.setFont(new Font("Tahoma", Font.PLAIN, 35));
        closeButton.setBounds(100, 150, 200, 75);
        closeButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ManagerFrame.framesetEnable();
                dialog.dispose();
            }
        });

        // Add key binding for the Escape key
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "exit");
        panel.getActionMap().put("exit", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ManagerFrame.framesetEnable();
                dialog.dispose();
            }
        });

        panel.add(closeButton);
    }
}
