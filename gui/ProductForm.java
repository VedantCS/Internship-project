package gui;

import dao.InventoryDAO;
import model.Product;
import javax.swing.*;
import java.awt.*;

public class ProductForm extends JDialog {
    private JTextField nameField, qtyField, priceField;
    private InventoryDAO dao = new InventoryDAO();
    private boolean isEditMode = false;
    private Product editingProduct;

    public ProductForm(JFrame parent, Product product) {
        super(parent, product == null ? "Add Product" : "Edit Product", true);
        editingProduct = product;
        isEditMode = product != null;
        initComponents();
        if (isEditMode) {
            loadProductData();
        }
    }

    private void initComponents() {
        setLayout(new GridLayout(5, 2, 10, 10));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JLabel nameLbl = new JLabel("Product Name:");
        nameField = new JTextField(20);
        JLabel qtyLbl = new JLabel("Quantity:");
        qtyField = new JTextField(10);
        JLabel priceLbl = new JLabel("Price:");
        priceField = new JTextField(10);

        JButton saveBtn = new JButton(isEditMode ? "Update" : "Add");
        JButton cancelBtn = new JButton("Cancel");

        saveBtn.addActionListener(e -> saveProduct());
        cancelBtn.addActionListener(e -> dispose());

        add(nameLbl); add(nameField);
        add(qtyLbl); add(qtyField);
        add(priceLbl); add(priceField);
        add(saveBtn); add(cancelBtn);

        pack();
        setLocationRelativeTo(getParent());
    }

    private void loadProductData() {
        if (editingProduct != null) {
            nameField.setText(editingProduct.getName());
            qtyField.setText(String.valueOf(editingProduct.getQuantity()));
            priceField.setText(String.valueOf(editingProduct.getPrice()));
        }
    }

    private void saveProduct() {
        try {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name is required!");
                return;
            }
            int qty = Integer.parseInt(qtyField.getText().trim());
            double price = Double.parseDouble(priceField.getText().trim());

            if (isEditMode) {
                editingProduct.setName(name);
                editingProduct.setQuantity(qty);
                editingProduct.setPrice(price);
                dao.updateProduct(editingProduct);
                JOptionPane.showMessageDialog(this, "Product updated!");
            } else {
                Product newProduct = new Product(0, name, qty, price);
                dao.addProduct(newProduct);
                JOptionPane.showMessageDialog(this, "Product added!");
            }
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number format!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }
}
