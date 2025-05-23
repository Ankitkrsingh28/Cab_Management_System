
package cab.booking.management.system;




import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class MainMenuFrame extends JFrame {

    private JButton registerButton;
    private JButton loginButton;
    private JLabel titleLabel;

    public MainMenuFrame() {
        setTitle("Cab Booking System - Main Menu");
        setSize(500, 300);
        setLocationRelativeTo(null); // Center on screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Main panel with gradient background
        JPanel mainPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                Color color1 = new Color(52, 143, 80);
                Color color2 = new Color(86, 180, 211);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Title label
        titleLabel = new JLabel("Welcome to Cab Booking System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 15, 15));
        buttonPanel.setOpaque(false);

        registerButton = createStyledButton("Register");
        loginButton = createStyledButton("Login");

        buttonPanel.add(registerButton);
        buttonPanel.add(loginButton);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // Button actions
        registerButton.addActionListener(e -> {
        

            new RegisterFrame().setVisible(true);
            dispose();
        });

        loginButton.addActionListener(e -> {
            
             new LoginFrame().setVisible(true);
            dispose();
        });

        setContentPane(mainPanel);
        setVisible(true);
    }

    // Create styled button with hover effect
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(34, 119, 173));
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(25, 90, 140));
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(34, 119, 173));
            }
        });

        return button;
    }

    // Main method
    public static void main(String[] args) {
        try {
            // Use system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new MainMenuFrame());
    }
}
