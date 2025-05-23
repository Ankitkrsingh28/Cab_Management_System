
package cab.booking.management.system;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;


public class LoginFrame extends JFrame {

    private JTextField phoneField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton backButton;

    public LoginFrame() {
        setTitle("User Login");
        setSize(500, 300);
        setLocationRelativeTo(null); // Center screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(72, 219, 251),
                        0, getHeight(), new Color(0, 210, 211));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel titleLabel = new JLabel("Login", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setOpaque(false);

        formPanel.add(createLabel("Phone:"));
        phoneField = new JTextField();
        formPanel.add(phoneField);

        formPanel.add(createLabel("Password:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);

        loginButton = createStyledButton("Login");
        backButton = createStyledButton("Back");

        formPanel.add(loginButton);
        formPanel.add(backButton);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Actions
        loginButton.addActionListener(e -> {
    String phone = phoneField.getText().trim();
    String password = new String(passwordField.getPassword()).trim();

    // Validate input fields
    if (phone.isEmpty() || password.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please enter both phone number and password.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Database connection and login validation
    try (Connection conn = DatabaseConnection.getConnection()) {
        if (conn == null) {
            JOptionPane.showMessageDialog(this, "Database connection failed.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // SQL query to check if the user exists with the given phone and password
        String sql = "SELECT * FROM users WHERE phone = ? AND password = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, phone);
        stmt.setString(2, password);

        // Execute query
        ResultSet rs = stmt.executeQuery();

        // Check if a matching record is found
        if (rs.next()) {
            // Login successful, user found
            JOptionPane.showMessageDialog(this, "Login successful!");
            new BookingFrame(); // Open the main menu
            dispose(); // Close the login frame
        } else {
            // Invalid login credentials
            JOptionPane.showMessageDialog(this, "Invalid phone number or password.", "Error", JOptionPane.ERROR_MESSAGE);
        }

    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "An error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
});


        backButton.addActionListener(e -> {
            new MainMenuFrame();
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
        button.setBackground(new Color(0, 153, 153));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(0, 102, 102));
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(0, 153, 153));
            }
        });
        return button;
    }

    // For testing directly
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new LoginFrame());
    }
}

