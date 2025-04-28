package ui;

import javax.swing.*;

import model.Item;
import model.Supplier;
import service.ItemService;
import service.ItemServiceImpl;
import service.SupplierService;
import service.SupplierServiceImpl;

import java.awt.*;

public class ItemPanel extends JDialog {
    private JTextField txtId,txtName, txtType, txtUnit, txtDescription, txtQuantity;
    private JButton btnSave, btnCancel;
    private Item item;
    private Supplier supplier;
    private ItemService itemService;
    private SupplierPanel supplierPanel; // Thêm tham chiếu đến SupplierPanel

    public ItemPanel(JFrame parent, Supplier supplier, ItemService itemService, SupplierPanel supplierPanel) {
        super(parent, "Thêm Item Mới", true);
        this.supplier = supplier;
        this.itemService = itemService;
        this.supplierPanel = supplierPanel; // Lưu reference
        this.item = new Item();
        initUI();
        setLocationRelativeTo(parent);
        setSize(400, 350);
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Form Panel với tất cả các trường
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));

        txtId = new JTextField();
        txtName = new JTextField();
        txtType = new JTextField();
        txtUnit = new JTextField();
        txtDescription = new JTextField();
        txtQuantity = new JTextField();

        formPanel.add(new JLabel("Mã sản phẩm"));
        formPanel.add(txtId);
        formPanel.add(new JLabel("Tên sản phẩm:"));
        formPanel.add(txtName);
        formPanel.add(new JLabel("Loại:"));
        formPanel.add(txtType);
        formPanel.add(new JLabel("Đơn vị:"));
        formPanel.add(txtUnit);
        formPanel.add(new JLabel("Mô tả:"));
        formPanel.add(txtDescription);
        formPanel.add(new JLabel("Số lượng:"));
        formPanel.add(txtQuantity);

        mainPanel.add(formPanel);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnCancel = new JButton("Hủy");
        btnCancel.setBackground(new Color(231, 76, 60));
        btnCancel.addActionListener(e -> {
            dispose();
            supplierPanel.setVisible(true); // Hiển thị lại SupplierPanel
        });

        btnSave = new JButton("Lưu");
        btnSave.setBackground(new Color(46, 204, 113));
        btnSave.addActionListener(e -> saveItem());

        buttonPanel.add(btnCancel);
        buttonPanel.add(btnSave);
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void saveItem() {
        try {
            int quantity = Integer.parseInt(txtQuantity.getText().trim());
            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Số lượng phải lớn hơn 0!");
                return;
            }

            if (item.getId() == 0) {
                // Nếu item mới (id = 0) → Parse ID
                long id = Long.parseLong(txtId.getText().trim());
                if (id <= 0) {
                    JOptionPane.showMessageDialog(this, "ID phải là số nguyên dương!");
                    return;
                }
                item.setId(id);
            }

            item.setName(txtName.getText().trim());
            item.setType(txtType.getText().trim());
            item.setUnit(txtUnit.getText().trim());
            item.setDescription(txtDescription.getText().trim());
            item.setQuantity(quantity);
            item.setSupplier(supplier);

            boolean success;
            if (itemService.getItem(item.getId()) != null) {
                success = itemService.updateItem(item); // Nếu có ID rồi thì cập nhật
            } else {
                success = itemService.createItem(item); // Không có thì tạo mới
            }

            if (success) {
                JOptionPane.showMessageDialog(this, "Lưu item thành công!");
                supplierPanel.refreshItems();
                dispose();
                supplierPanel.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi khi lưu item!");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số hợp lệ!");
        }
    }

    public void setItem(Item currentItem) {
        if (currentItem != null) {
            this.item = currentItem;
            txtId.setText(String.valueOf(item.getId()));
            txtName.setText(item.getName());
            txtType.setText(item.getType());
            txtUnit.setText(item.getUnit());
            txtDescription.setText(item.getDescription());
            txtQuantity.setText(String.valueOf(item.getQuantity()));
            txtId.setEditable(false); // ID không cho sửa
        }
}
    
}
