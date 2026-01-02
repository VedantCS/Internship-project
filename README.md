#                                                                                                   Internship-project

# Title: Inventory Analyzer
**Objective:To apply all the concepts I've learned during the internship.**

The Inventory Analyzer is a desktop application for managing product inventory, **enabling CRUD operations** on products, stock tracking, searches, and low-stock alerts. It uses **Core Java** for logic, Java **Swing** for GUI, and **JDBC with SQLite or MySQL** for persistent data storage.

**Technology Stack:**

Core Java SE handles business logic and OOP principles like encapsulation in model classes. Java Swing builds intuitive desktop UIs with components like JTable for inventory views and JDialogs for forms, ideal for cross-platform desktop apps without web dependencies. JDBC connects to relational databases like SQLite (file-based, no server setup) or MySQL (scalable), using prepared statements to prevent SQL injection and ensure data integrity—perfect for beginners as it teaches real-world persistence without complexity.
​

**Purpose and Scope:**

The app tracks products with details like ID, name, category, quantity, price, and timestamps. Core features include adding/updating/deleting products, searching/filtering by name/category/stock, real-time stock updates, and alerts for low stock (e.g., quantity < 10). Insights show total value, low-stock lists, and basic reports via tables or dialogs, scoped for small businesses without advanced analytics to keep it beginner-friendly.
​

**Database Design:**

Design a schema with tables: products (id PK, name, quantity, price, category_id FK, created_at, updated_at), categories (id PK, name). Add transactions (id PK, product_id FK, type 'in/out', quantity, date) for audit trails. Use INTEGER PRIMARY KEY AUTOINCREMENT for IDs, TEXT for names, INTEGER for quantity, REAL for price—this normalizes data, avoids redundancy, and supports queries like low-stock alerts (SELECT * FROM products WHERE quantity < 10).

## Implementation Steps

**Planning and Setup**

Create a new Java project in Eclipse/IntelliJ/NetBeans: File > New > Java Project, name "InventoryAnalyzer". Add JDBC driver (sqlite-jdbc or mysql-connector via libraries). Structure packages: model (Product.java), dao (InventoryDAO.java), util (DatabaseUtil.java), gui (MainFrame.java, ProductForm.java). This separation follows MVC: models for data, DAOs for persistence, GUI for views—improving maintainability as UI changes don't affect database logic.
​

**Database and JDBC Setup**

In DatabaseUtil.java, load driver (Class.forName("org.sqlite.JDBC")), connect via DriverManager.getConnection("jdbc:sqlite:inventory.db"), and create schema with Statement.execute() for tables. Implement CRUD in InventoryDAO: use PreparedStatement for safe inserts/updates (prevents injection unlike Statement), e.g., "INSERT INTO products (name,quantity,price) VALUES (?,?,?)". Test connection/CRUD standalone with main() method printing results—this isolates DB issues early.
​

**Swing UI Design**

Build MainFrame extending JFrame with JTabbedPane for tabs: "Products" (JTable with DefaultTableModel, search JTextField+JButton), "Alerts" (low-stock JTable), "Add/Edit" (form with JTextFields, JComboBox for categories). Use JScrollPane for tables, GridBagLayout for forms. ActionListeners on buttons call DAO methods and refresh tables (model.setRowCount(0); dao.getAllProducts(model)). Separating UI events from DAO calls promotes loose coupling—easier to swap databases later.
​

**Integration and Features**

Wire actions: "Add" button validates inputs (e.g., !name.isEmpty(), quantity > 0 via try-catch NumberFormatException), calls dao.insertProduct(), refreshes table, shows JOptionPane success/error. For low-stock: JButton runs query, populates table if matches found. Handle errors with try-catch SQLException, user dialogs. Real-time: after sales/purchases, update quantity via dao.updateStock().
​

Testing and Validation
Test incrementally: unit test DAO with JUnit (addProduct, getLowStock), UI manually (add product, verify table/DB sync). Edge cases: empty fields (show dialog), invalid numbers, delete non-existent (no-op with message). Run app, add 10 products, simulate low stock, confirm alerts—this ensures robustness
​
