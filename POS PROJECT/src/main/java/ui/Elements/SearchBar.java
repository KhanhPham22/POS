package ui.Elements;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import service.SupplierService;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.List;

public class SearchBar extends JPanel {
	private SupplierService supplierService;
	private JTextField searchField;
    private SearchListener searchListener;
    private String placeholder;
    
    public interface SearchListener {
        void onSearch(String query);
    }

    public SearchBar(SearchListener listener) {
        this.searchListener = listener;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        searchField = new JTextField("");
        searchField.setPreferredSize(new Dimension(0, 40));
        searchField.setBorder(BorderFactory.createLineBorder(Color.GRAY));

     // Placeholder handling
        searchField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals(placeholder)) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText(placeholder);
                    searchField.setForeground(Color.GRAY);
                }
            }
        });
        // Khi nhấn Enter
        searchField.addActionListener(e -> fireSearchEvent());

        // Khi thay đổi text (realtime nếu cần)
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { fireSearchEvent(); }
            public void removeUpdate(DocumentEvent e) { fireSearchEvent(); }
            public void changedUpdate(DocumentEvent e) {}
        });

        add(searchField, BorderLayout.CENTER);
    }

    private void fireSearchEvent() {
        String query = searchField.getText().trim();
        if (searchListener != null && !query.isEmpty() && !query.equals(placeholder)) {
            searchListener.onSearch(query);
        }
    }

    public void setPlaceholder(String text) {
        this.placeholder = text;
        searchField.setText(text);
        searchField.setForeground(Color.GRAY);
    }
}
