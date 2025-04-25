package ui.Elements;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import service.SupplierService;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class SearchBar extends JPanel {
	private SupplierService supplierService;
	private JTextField searchField;
    private SearchListener searchListener;

    public interface SearchListener {
        void onSearch(String query);
    }

    public SearchBar(SearchListener listener) {
        this.searchListener = listener;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        searchField = new JTextField("Search ...");
        searchField.setPreferredSize(new Dimension(0, 40));
        searchField.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // Khi nhấn Enter
        searchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireSearchEvent();
            }
        });

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
        if (searchListener != null && !query.isEmpty()) {
            searchListener.onSearch(query);
        }
    }

    public void setPlaceholder(String text) {
        searchField.setText(text);
    }
}
