package gui;

import dao.InventoryDAO;
import model.Product;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class MainFrame extends JFrame {
    private JTable productTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private InventoryDAO dao = new InventoryDAO();

    public MainFrame() {
        setTitle("Inventory Analyzer");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        initComponents();
        loadAllProducts();
        
    }
    private void loadLowStock(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        try {
            List<Product> lowStock = dao.getLowStockProducts();
            model.setRowCount(0);
            for (Product p : lowStock) {
                model.addRow(new Object[]{p.getId(), p.getName(), p.getQuantity(), p.getPrice()});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading low stock: " + ex.getMessage());
        }
    }

    private void initComponents() {
        // Toolbar
        JToolBar toolbar = new JToolBar();
        JButton addBtn = new JButton("Add Product");
        JButton editBtn = new JButton("Edit Selected");
        JButton deleteBtn = new JButton("Delete Selected");
        JButton refreshBtn = new JButton("Refresh");
        searchField = new JTextField(15);
        JButton searchBtn = new JButton("Search");

        addBtn.addActionListener(this::addProduct);
        editBtn.addActionListener(this::editProduct);
        deleteBtn.addActionListener(this::deleteProduct);
        refreshBtn.addActionListener(e -> loadAllProducts());
        searchBtn.addActionListener(this::searchProducts);

        toolbar.add(addBtn);
        toolbar.add(editBtn);
        toolbar.add(deleteBtn);
        toolbar.add(refreshBtn);
        toolbar.add(new JLabel("Search:"));
        toolbar.add(searchField);
        toolbar.add(searchBtn);

        // Table
        String[] columns = {"ID", "Name", "Quantity", "Price"};
        tableModel = new DefaultTableModel(columns, 0);
        productTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(productTable);

        // Tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("All Products", scrollPane);

     // FIXED Low Stock Tab
        String[] columns2 = {"ID", "Name", "Quantity", "Price"};
        DefaultTableModel lowStockModel = new DefaultTableModel(columns2, 0);
        JTable lowStockTable = new JTable(lowStockModel);
        JScrollPane lowStockPane = new JScrollPane(lowStockTable);

        JPanel lowStockPanel = new JPanel(new BorderLayout());
        lowStockPanel.add(lowStockPane, BorderLayout.CENTER);
        JButton refreshLowBtn = new JButton("🔄 Refresh");
        refreshLowBtn.addActionListener(e -> loadLowStock(lowStockTable));
        lowStockPanel.add(refreshLowBtn, BorderLayout.SOUTH);

        tabbedPane.addTab("Low Stock (<10)", lowStockPanel);
        loadLowStock(lowStockTable);  // Initial load


        add(toolbar, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }

    private void loadAllProducts() {
        try {
            List<Product> products = dao.getAllProducts();
            tableModel.setRowCount(0);
            for (Product p : products) {
                tableModel.addRow(new Object[]{p.getId(), p.getName(), p.getQuantity(), p.getPrice()});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading products: " + ex.getMessage());
        }
    }

    private void addProduct(ActionEvent e) {
        new ProductForm(this, null).setVisible(true);
        loadAllProducts();
    }

    private void editProduct(ActionEvent e) {
        int row = productTable.getSelectedRow();
        if (row >= 0) {
            int id = (int) tableModel.getValueAt(row, 0);
            try {
                Product product = dao.getProductById(id);
                new ProductForm(this, product).setVisible(true);
                loadAllProducts();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select a product to edit!");
        }
    }

    private void deleteProduct(ActionEvent e) {
        int row = productTable.getSelectedRow();
        if (row >= 0) {
            int id = (int) tableModel.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Delete this product?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    dao.deleteProduct(id);
                    loadAllProducts();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Delete failed: " + ex.getMessage());
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select a product to delete!");
        }
    }

    private void searchProducts(ActionEvent e) {
        String searchTerm = searchField.getText().trim();
        try {
            List<Product> products = dao.searchProducts(searchTerm);
            tableModel.setRowCount(0);
            for (Product p : products) {
                tableModel.addRow(new Object[]{p.getId(), p.getName(), p.getQuantity(), p.getPrice()});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Search error: " + ex.getMessage());
        }
    }

  

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                util.DatabaseUtil.createSchema();  // Creates DB/table if missing
                new MainFrame().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Failed to connect to MySQL: " + e.getMessage() + 
                    "\n1. Start MySQL server\n2. Check credentials in DatabaseUtil.java\n3. Add mysql-connector-j-9.4.0.jar to classpath");
            }
        });
    }

}
