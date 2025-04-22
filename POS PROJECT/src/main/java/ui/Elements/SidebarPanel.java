package ui.Elements;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SidebarPanel extends JPanel {
    private final String[] iconFiles;
    private final String[] pageNames;
    private final String iconPath;
    private final SidebarListener listener;

    public interface SidebarListener {
        void onSidebarItemClick(String pageName);
    }

    public SidebarPanel(String[] iconFiles, String[] pageNames, String iconPath, SidebarListener listener) {
        this.iconFiles = iconFiles;
        this.pageNames = pageNames;
        this.iconPath = iconPath;
        this.listener = listener;

        initUI();
    }

    private void initUI() {
        setLayout(new GridLayout(iconFiles.length, 1));
        setBackground(Color.DARK_GRAY);

        for (int i = 0; i < iconFiles.length; i++) {
            JLabel label = createSidebarItem(iconFiles[i], pageNames[i]);
            add(label);
        }
    }

    private JLabel createSidebarItem(String iconFile, String pageName) {
        ImageIcon icon = new ImageIcon(iconPath + iconFile);
        JLabel label = new JLabel(pageName, icon, JLabel.LEFT);
        label.setForeground(Color.WHITE);
        label.setIconTextGap(10);
        label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                listener.onSidebarItemClick(pageName);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                label.setForeground(Color.CYAN);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                label.setForeground(Color.WHITE);
            }
        });

        return label;
    }
}
