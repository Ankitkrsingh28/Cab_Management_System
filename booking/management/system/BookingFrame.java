
package cab.booking.management.system;



import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import java.sql.*;

public class BookingFrame extends JFrame {

    private JComboBox<String> sourceBox;
    private JComboBox<String> destinationBox;
    private JLabel distanceLabel, timeLabel, fareLabel;
    private JButton calculateButton, backButton;

    // Store distances in km between cities
    private Map<String, Map<String, Integer>> distanceMap;

    public BookingFrame() {
        setTitle("Book a Cab");
        setSize(550, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initializeDistances();

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10)) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(255, 175, 189),
                        0, getHeight(), new Color(255, 195, 160));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel title = new JLabel("Cab Booking", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.DARK_GRAY);
        mainPanel.add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setOpaque(false);

        String[] cities = {"Delhi", "Mumbai", "Bangalore", "Chennai", "Kolkata", "Hyderabad"};
        sourceBox = new JComboBox<>(cities);
        destinationBox = new JComboBox<>(cities);

        formPanel.add(createLabel("From:"));
        formPanel.add(sourceBox);
        formPanel.add(createLabel("To:"));
        formPanel.add(destinationBox);

        formPanel.add(createLabel("Distance:"));
        distanceLabel = createInfoLabel();
        formPanel.add(distanceLabel);

        formPanel.add(createLabel("Estimated Time:"));
        timeLabel = createInfoLabel();
        formPanel.add(timeLabel);

        formPanel.add(createLabel("Fare:"));
        fareLabel = createInfoLabel();
        formPanel.add(fareLabel);

        calculateButton = createStyledButton("Book Now");
        backButton = createStyledButton("Back");

        formPanel.add(calculateButton);
        formPanel.add(backButton);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Action: Book Now
        // Action: Book Now
calculateButton.addActionListener(e -> {
    String from = (String) sourceBox.getSelectedItem();
    String to = (String) destinationBox.getSelectedItem();

    if (from.equals(to)) {
        JOptionPane.showMessageDialog(this, "Source and destination cannot be the same.");
        return;
    }

    Integer distance = distanceMap.getOrDefault(from, new HashMap<>()).get(to);
    if (distance == null) {
        JOptionPane.showMessageDialog(this, "Distance data not available for selected cities.");
        return;
    }

    double speed = 40.0; // Average cab speed in km/h
    double time = distance / speed;

    int minFare = distance * 12;
    int maxFare = distance * 18;
    int fare = (int) (Math.random() * (maxFare - minFare + 1)) + minFare;

    distanceLabel.setText(distance + " km");
    timeLabel.setText(String.format("%.1f minutes", time * 60));
    fareLabel.setText("₹" + fare);

    int confirm = JOptionPane.showConfirmDialog(this,
            "Proceed to pay ₹" + fare + " for your ride?", "Confirm Booking", JOptionPane.YES_NO_OPTION);

    if (confirm == JOptionPane.YES_OPTION) {
        int bookingId = -1;

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO bookings (source, destination, distance, time_minutes, fare) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, from);
            stmt.setString(2, to);
            stmt.setInt(3, distance);
            stmt.setDouble(4, time * 60);
            stmt.setInt(5, fare);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                bookingId = rs.getInt(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving booking.");
            return;
        }

        // Proceed to PaymentFrame
        new PaymentFrame(fare, bookingId);
        dispose();
    }
});


        // Back to main menu
        backButton.addActionListener(e -> {
            new MainMenuFrame();
            dispose();
        });

        setContentPane(mainPanel);
        setVisible(true);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 15));
        label.setForeground(Color.DARK_GRAY);
        return label;
    }

    private JLabel createInfoLabel() {
        JLabel label = new JLabel("-");
        label.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        label.setForeground(Color.BLACK);
        return label;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(102, 51, 153));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(75, 0, 130));
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(102, 51, 153));
            }
        });

        return button;
    }

    private void initializeDistances() {
        distanceMap = new HashMap<>();

        distanceMap.put("Delhi", Map.of(
                "Mumbai", 1400,
                "Bangalore", 2150,
                "Chennai", 2200,
                "Kolkata", 1500,
                "Hyderabad", 1550
        ));

        distanceMap.put("Mumbai", Map.of(
                "Delhi", 1400,
                "Bangalore", 980,
                "Chennai", 1330,
                "Kolkata", 2050,
                "Hyderabad", 710
        ));

        distanceMap.put("Bangalore", Map.of(
                "Delhi", 2150,
                "Mumbai", 980,
                "Chennai", 350,
                "Kolkata", 1850,
                "Hyderabad", 570
        ));

        distanceMap.put("Chennai", Map.of(
                "Delhi", 2200,
                "Mumbai", 1330,
                "Bangalore", 350,
                "Kolkata", 1670,
                "Hyderabad", 630
        ));

        distanceMap.put("Kolkata", Map.of(
                "Delhi", 1500,
                "Mumbai", 2050,
                "Bangalore", 1850,
                "Chennai", 1670,
                "Hyderabad", 1500
        ));

        distanceMap.put("Hyderabad", Map.of(
                "Delhi", 1550,
                "Mumbai", 710,
                "Bangalore", 570,
                "Chennai", 630,
                "Kolkata", 1500
        ));
    }

    // Optional: main method to test directly
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new BookingFrame());
    }
}

