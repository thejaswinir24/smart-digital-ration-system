package advration;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.*;
import java.util.*;

public class RationGUI extends JFrame {

    // === ALL REQUIRED ASSIGNMENT COLLECTIONS ===
    private static ArrayList<Customer> customerList = new ArrayList<>();
    private static LinkedList<Customer> queue = new LinkedList<>();
    private static HashMap<String, RationItem> itemMap = new HashMap<>();
    private static TreeMap<Integer, Token> tokenMap = new TreeMap<>();
    private static int tokenCounter = 1;

    // UI Components
    private JTextArea outputArea;

    public RationGUI() {
        // Apply Modern System Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Failed to set modern Look & Feel.");
        }

        // Frame Setup
        setTitle("Smart Digital Ration System");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        setLayout(new BorderLayout(10, 10)); 
        getContentPane().setBackground(new Color(240, 245, 250)); 

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 102, 204));
        headerPanel.setBorder(new EmptyBorder(15, 0, 15, 0));
        
        JLabel titleLabel = new JLabel("SMART DIGITAL RATION SYSTEM");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Main Content Panel
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        mainPanel.setOpaque(false);

        // Buttons Panel (Grid Layout)
        JPanel buttonPanel = new JPanel(new GridLayout(3, 4, 15, 15));
        buttonPanel.setOpaque(false);

        JButton btnAddCustomer = createStyledButton("Add Customer");
        JButton btnDisplayCustomers = createStyledButton("Display Customers");
        JButton btnAddItem = createStyledButton("Add Item");
        JButton btnDisplayItems = createStyledButton("Display Items");
        
        JButton btnJoinQueue = createStyledButton("Join Queue");
        JButton btnServeCustomer = createStyledButton("Serve Customer");
        JButton btnDisplayQueue = createStyledButton("Display Queue");
        JButton btnSortCustomers = createStyledButton("Sort Customers");
        
        JButton btnTokenReport = createStyledButton("Token Report");
        JButton btnClear = createStyledButton("Clear Console");
        JButton btnExit = createStyledButton("Exit System");
        btnExit.setBackground(new Color(220, 53, 69)); 
        btnExit.setForeground(Color.WHITE);

        // Add buttons
        buttonPanel.add(btnAddCustomer);
        buttonPanel.add(btnDisplayCustomers);
        buttonPanel.add(btnAddItem);
        buttonPanel.add(btnDisplayItems);
        buttonPanel.add(btnJoinQueue);
        buttonPanel.add(btnServeCustomer);
        buttonPanel.add(btnDisplayQueue);
        buttonPanel.add(btnSortCustomers);
        buttonPanel.add(btnTokenReport);
        buttonPanel.add(btnClear);
        buttonPanel.add(new JLabel()); 
        buttonPanel.add(btnExit);

        mainPanel.add(buttonPanel, BorderLayout.NORTH);

        // Output Area
        outputArea = new JTextArea();
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        outputArea.setEditable(false);
        outputArea.setBackground(new Color(30, 30, 30));
        outputArea.setForeground(new Color(0, 255, 0)); 
        outputArea.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("System Console"));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);

        // ================= EVENT LISTENERS =================
        btnAddCustomer.addActionListener(e -> handleAddCustomer());
        btnDisplayCustomers.addActionListener(e -> handleDisplayCustomers());
        btnAddItem.addActionListener(e -> handleAddItem());
        btnDisplayItems.addActionListener(e -> handleDisplayItems());
        
        btnJoinQueue.addActionListener(e -> handleJoinQueue());
        btnServeCustomer.addActionListener(e -> handleServeCustomer());
        btnDisplayQueue.addActionListener(e -> handleDisplayQueue());
        btnSortCustomers.addActionListener(e -> handleSortCustomers());
        
        btnTokenReport.addActionListener(e -> handleTokenReport());
        btnClear.addActionListener(e -> outputArea.setText(""));
        btnExit.addActionListener(e -> System.exit(0));
        
        // Load initial data from DB on startup
        loadDataFromDatabase();
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBackground(Color.WHITE);
        return btn;
    }

    private void printLine(String message) {
        outputArea.append(message + "\n");
        outputArea.setCaretPosition(outputArea.getDocument().getLength()); 
    }
    
    // Loads initial data into collections
    private void loadDataFromDatabase() {
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) return;
            
            // Load Customers
            try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM customer")) {
                while (rs.next()) {
                    customerList.add(new Customer(rs.getInt(1), rs.getString(2), rs.getString(3)));
                }
            }
            
            // Load Items
            try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM item")) {
                while (rs.next()) {
                    itemMap.put(rs.getString(1), new RationItem(rs.getString(1), rs.getString(2), rs.getInt(3)));
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading DB: " + e.getMessage());
        }
    }

    // ================= ACTIONS =================

    private void handleAddCustomer() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        JTextField txtId = new JTextField();
        JTextField txtName = new JTextField();
        JTextField txtCard = new JTextField();

        panel.add(new JLabel("Customer ID:"));
        panel.add(txtId);
        panel.add(new JLabel("Full Name:"));
        panel.add(txtName);
        panel.add(new JLabel("Ration Card No:"));
        panel.add(txtCard);

        if (JOptionPane.showConfirmDialog(this, panel, "Add New Customer", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try {
                int cid = Integer.parseInt(txtId.getText());
                String name = txtName.getText();
                String card = txtCard.getText();
                
                // Add to Collection (ArrayList)
                customerList.add(new Customer(cid, name, card));

                // Add to Database
                try (Connection con = DBConnection.getConnection();
                     PreparedStatement ps = con.prepareStatement("INSERT INTO customer VALUES (?, ?, ?)")) {
                    
                    if(con != null) {
                        ps.setInt(1, cid);
                        ps.setString(2, name);
                        ps.setString(3, card);
                        ps.executeUpdate();
                    }
                    printLine("✔ Success: Customer Added & Stored in DB!");

                } catch (SQLException ex) {
                    printLine("❌ Database Error: " + ex.getMessage());
                }

            } catch (Exception ex) {
                printLine("❌ Error: Valid ID required!");
            }
        }
    }

    private void handleDisplayCustomers() {
        printLine("\n--- CUSTOMER DIRECTORY (Using Iterator) ---");
        if(customerList.isEmpty()) {
            printLine("No customers found.");
            return;
        }
        
        // Using standard Iterator as per your code
        Iterator<Customer> it = customerList.iterator();
        while(it.hasNext()) {
            printLine(it.next().toString());
        }
    }

    private void handleAddItem() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        JTextField txtId = new JTextField();
        JTextField txtName = new JTextField();
        JTextField txtQty = new JTextField();

        panel.add(new JLabel("Item ID:"));
        panel.add(txtId);
        panel.add(new JLabel("Item Name:"));
        panel.add(txtName);
        panel.add(new JLabel("Quantity:"));
        panel.add(txtQty);

        if (JOptionPane.showConfirmDialog(this, panel, "Add New Item", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try {
                String id = txtId.getText();
                String name = txtName.getText();
                int qty = Integer.parseInt(txtQty.getText());
                
                // Add to Collection (HashMap)
                itemMap.put(id, new RationItem(id, name, qty));

                // Add to Database
                try (Connection con = DBConnection.getConnection();
                     PreparedStatement ps = con.prepareStatement("INSERT INTO item VALUES (?, ?, ?)")) {
                    
                    if(con != null) {
                        ps.setString(1, id);
                        ps.setString(2, name);
                        ps.setInt(3, qty);
                        ps.executeUpdate();
                    }
                    printLine("✔ Success: Item Added & Stored in DB!");

                } catch (SQLException ex) {
                    printLine("❌ Database Error: " + ex.getMessage());
                }

            } catch (Exception ex) {
                printLine("❌ Error: Valid quantity required!");
            }
        }
    }

    private void handleDisplayItems() {
        printLine("\n--- AVAILABLE RATION ITEMS (Using HashMap) ---");
        if(itemMap.isEmpty()) {
            printLine("No items found.");
            return;
        }
        
        // Iterating Map as per your code
        for(Map.Entry<String, RationItem> entry : itemMap.entrySet()) {
            printLine(entry.getValue().toString());
        }
    }

    private void handleJoinQueue() {
        String input = JOptionPane.showInputDialog(this, "Enter Customer ID to join queue:");
        if (input == null || input.trim().isEmpty()) return;

        try {
            int id = Integer.parseInt(input.trim());
            
            // Search in ArrayList as per your code
            for(Customer c : customerList) {
                if(c.getId() == id) {
                    queue.add(c);

                    Token token = new Token(tokenCounter++, id);
                    tokenMap.put(token.getTokenNo(), token);

                    printLine("✔ Joined Queue. Customer added to queue with Token " + token.getTokenNo());
                    return;
                }
            }
            printLine("❌ Customer not found in records!");

        } catch (Exception ex) {
            printLine("❌ Error: Invalid format.");
        }
    }

    private void handleServeCustomer() {
        if (queue.isEmpty()) {
            printLine("⚠ Queue is empty!");
            return;
        }

        Customer served = queue.removeFirst();
        printLine("🛎 Serving Customer: " + served);
    }

    private void handleDisplayQueue() {
        printLine("\n--- CURRENT LIVE QUEUE ---");
        if(queue.isEmpty()) {
            printLine("Queue is empty!");
            return;
        }

        for(Customer c : queue) {
            printLine(c.toString());
        }
    }

    private void handleSortCustomers() {
        // Sorting Collection using custom Comparator as per your code
        Collections.sort(customerList, new Comparator<Customer>() {
            public int compare(Customer c1, Customer c2) {
                return c1.getName().compareTo(c2.getName());
            }
        });

        printLine("\n--- CUSTOMERS SORTED BY NAME ---");
        handleDisplayCustomers(); // Reuse iterator display
    }

    private void handleTokenReport() {
        printLine("\n--- DAILY TOKEN REPORT (Using TreeMap) ---");
        if (tokenMap.isEmpty()) {
            printLine("No tokens yet!");
            return;
        }

        // Iterating TreeMap as per your code
        for (Map.Entry<Integer, Token> entry : tokenMap.entrySet()) {
            printLine(entry.getValue().toString());
        }
    }

    // ================= MAIN RUNNER =================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RationGUI app = new RationGUI();
            app.setVisible(true);
        });
    }
}

