package cab.booking.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class PaymentFrame extends JFrame {

    private int fareAmount;
    private int bookingId;

    public PaymentFrame(int fareAmount, int bookingId) {
        this.fareAmount = fareAmount;
        this.bookingId = bookingId;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Payment");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel amountLabel = new JLabel("Amount to Pay: ₹" + fareAmount, SwingConstants.CENTER);
        amountLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        panel.add(amountLabel);

        JComboBox<String> paymentOptions = new JComboBox<>(new String[]{
                "Paytm", "PhonePe", "Google Pay", "Credit Card", "Debit Card", "Net Banking"
        });
        panel.add(paymentOptions);

        JButton payButton = new JButton("Pay Now");
        JButton cancelButton = new JButton("Cancel");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(payButton);
        buttonPanel.add(cancelButton);
        panel.add(buttonPanel);

        // Pay Now Button Logic
        payButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedMethod = (String) paymentOptions.getSelectedItem();

                try (Connection conn = DatabaseConnection.getConnection()) {
                    String sql = "INSERT INTO payments (booking_id, payment_method, amount) VALUES (?, ?, ?)";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, bookingId);
                    stmt.setString(2, selectedMethod);
                    stmt.setInt(3, fareAmount);
                    stmt.executeUpdate();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(PaymentFrame.this,
                            "Error saving payment to database.", "Database Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                JOptionPane.showMessageDialog(PaymentFrame.this,
                        "Payment of ₹" + fareAmount + " via " + selectedMethod + " was successful!",
                        "Payment Successful", JOptionPane.INFORMATION_MESSAGE);

                new MainMenuFrame(); // Return to main menu
                dispose();
            }
        });

        // Cancel Button Logic
        cancelButton.addActionListener(e -> {
            new BookingFrame(); // Return to booking screen
            dispose();
        });

        setContentPane(panel);
        setVisible(true);
    }

    // Optional: main method for testing independently
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PaymentFrame(999, 1)); // dummy fare and bookingId
    }
}
