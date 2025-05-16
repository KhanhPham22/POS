package ui.Elements;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class SearchBar extends JPanel {
    private JTextField searchField;
    private SearchListener searchListener;
    private String placeholder;

    // Interface for listening to search events
    public interface SearchListener {
        void onSearch(String query);
    }

    // Constructor accepting a search listener
    public SearchBar(SearchListener listener) {
        this.searchListener = listener;
        initUI(); // Initialize the user interface
    }

    // Initialize the search bar UI
    private void initUI() {
        setLayout(new BorderLayout());

        // Create a text field for input
        searchField = new JTextField("");
        searchField.setPreferredSize(new Dimension(0, 40)); // Set height of the text field
        searchField.setBorder(BorderFactory.createLineBorder(Color.GRAY)); // Add border

        // Handle placeholder text when focus is gained or lost
        searchField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                // Clear the placeholder when the field is focused
                if (searchField.getText().equals(placeholder)) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                // Show the placeholder if the field is empty when focus is lost
                if (searchField.getText().isEmpty()) {
                    searchField.setText(placeholder);
                    searchField.setForeground(Color.GRAY);
                }
            }
        });

        // Trigger search when Enter is pressed
        searchField.addActionListener(e -> fireSearchEvent());

        // Trigger search event when the text changes (for real-time updates if needed)
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { fireSearchEvent(); }
            public void removeUpdate(DocumentEvent e) { fireSearchEvent(); }
            public void changedUpdate(DocumentEvent e) {} // Not used for plain text components
        });

        // Add the text field to the panel
        add(searchField, BorderLayout.CENTER);
    }

    // Method to notify listener when a search event occurs
    private void fireSearchEvent() {
        String query = searchField.getText().trim();
        // Only notify if listener is set, input is not empty, and not equal to the placeholder
        if (searchListener != null && !query.isEmpty() && !query.equals(placeholder)) {
            searchListener.onSearch(query);
        }
    }

    // Set the placeholder text
    public void setPlaceholder(String text) {
        this.placeholder = text;
        searchField.setText(text);
        searchField.setForeground(Color.GRAY); // Set placeholder color
    }

    // Get the placeholder text
    public String getPlaceholder() {
        return placeholder;
    }

    // Get the current text in the search field
    public String getSearchText() {
        return searchField.getText().trim();
    }
}
