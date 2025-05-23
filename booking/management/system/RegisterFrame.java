
package cab.booking.management.system;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
//import com.cabbooking.db.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class RegisterFrame extends JFrame {

    private JTextField nameField;
    private JTextField phoneField;
    private JPasswordField passwordField;
    private JButton registerButton;
    private JButton backButton;

    public RegisterFrame() {
        setTitle("User Registration");
        setSize(500, 350);
        setLocationRelativeTo(null); // Center
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(255, 153, 102),
                        0, getHeight(), new Color(255, 94, 98));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel titleLabel = new JLabel("User Registration", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setOpaque(false);

        formPanel.add(createLabel("Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(createLabel("Phone:"));
        phoneField = new JTextField();
        formPanel.add(phoneField);

        formPanel.add(createLabel("Password:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);

        registerButton = createStyledButton("Register");
        backButton = createStyledButton("Back");

        formPanel.add(registerButton);
        formPanel.add(backButton);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Button actions
       registerButton.addActionListener(e -> {
    String name = nameField.getText().trim();
    String phone = phoneField.getText().trim();
    String password = new String(passwordField.getPassword()).trim();

    // Validate input fields
    if (name.isEmpty() || phone.isEmpty() || password.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Database connection and registration logic
    try (Connection conn = DatabaseConnection.getConnection()) {
        if (conn == null) {
            JOptionPane.showMessageDialog(this, "Database connection failed.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // SQL query to insert the user
        String sql = "INSERT INTO users (name, phone, password) VALUES (?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, name);
        stmt.setString(2, phone);
        stmt.setString(3, password);

        int rowsInserted = stmt.executeUpdate();
        if (rowsInserted > 0) {
            JOptionPane.showMessageDialog(this, "Registration successful!");
            new MainMenuFrame(); // Open the main menu
            dispose(); // Close the registration frame
        } else {
            JOptionPane.showMessageDialog(this, "Failed to register. Try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }

    } catch (SQLException ex) {
        // Handle SQL exceptions, e.g., duplicate phone number
        if (ex.getMessage().contains("Duplicate entry")) {
            JOptionPane.showMessageDialog(this, "Phone number already registered.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
});


        backButton.addActionListener(e -> {
            new MainMenuFrame(); // Back to main menu
            dispose();
        });

        setContentPane(mainPanel);
        setVisible(true);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setForeground(Color.WHITE);
        return label;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(204, 51, 51));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(153, 0, 0));
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(204, 51, 51));
            }
        });
        return button;
    }
        // ... existing methods like createStyledButton(), etc.

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new RegisterFrame());
    }
}



