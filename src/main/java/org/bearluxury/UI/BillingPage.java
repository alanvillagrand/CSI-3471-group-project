package org.bearluxury.UI;

import org.bearluxury.Billing.SaleJDBCDAO;
import org.bearluxury.account.*;
import org.bearluxury.controllers.ClerkAccountController;
import org.bearluxury.controllers.GuestAccountController;
import org.bearluxury.shop.CreditCardPayment;
import org.bearluxury.shop.Sale;
import org.bearluxury.controllers.SaleController;
import org.bearluxury.state.SessionManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
/**
 * Represents a billing page GUI for displaying sales information and allowing bill payment.
 * @author Will Clore
 * @author Harrsion Hassler
 * @author Derek Martinez
 * @author Nicholas Nolen
 * @author Joseph Zuniga
 * @author Alan Vilagrand
 *
 */
public class BillingPage extends JFrame {
    private final Color backgroundColor = new Color(232, 223, 185);
    private final Font headerFont = new Font("Arial", Font.BOLD, 18);
    private final Font tableFont = new Font("Arial", Font.PLAIN, 14);

    private JTable saleTable;

    /**
     * Constructs a new BillingPage.
     *
     * @param rowVal The row value.
     */
    public BillingPage(int rowVal) {
        setTitle("Billing Page");
        setSize(1000, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(backgroundColor);

        createComponents(rowVal);

        setVisible(true);
    }

    /**
     * Creates components for the billing page GUI.
     *
     * @param rowVal The row value.
     */
    private void createComponents(int rowVal) {
        SaleController controller = new SaleController(new SaleJDBCDAO());
        Set<Sale> sales = new HashSet<>();
        if(SessionManager.getInstance().getCurrentAccount() != null) {
            if (SessionManager.getInstance().getCurrentAccount().getRole().equals(Role.GUEST)) {
                sales = controller.listSale(SessionManager.getInstance().getCurrentAccount().getId());

            }
            else {
                sales = controller.listSale(rowVal);
            }
        }
        createSaleTable(sales);
        add(createButtonsPanel(), BorderLayout.SOUTH);
    }

    /**
     * Creates the sales table based on the provided set of sales.
     *
     * @param sales The set of sales to display in the table.
     */
    private void createSaleTable(Set<Sale> sales) {
        String[] columnNames = {"Sale ID", "Date", "Name", "Price", "Quantity"};

        // Create a DefaultTableModel with column names
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");

        // Populate the table model with sales data
        for (Sale sale : sales) {
            Object[] rowData = {sale.getSaleId(),
                    formatter.format(sale.getSaleDate()),
                    sale.getProductName(),
                    sale.getPrice(),
                    sale.getQuantity()};
            model.addRow(rowData);
        }

        // Create JTable with the model
        saleTable = new JTable(model);

        // Set table font and background color
        saleTable.setFont(tableFont);
        saleTable.setBackground(Color.WHITE);
        saleTable.setRowHeight(30); // Increase row height

        // Set table header font and background color
        JTableHeader header = saleTable.getTableHeader();
        header.setFont(headerFont);
        header.setBackground(backgroundColor);
        header.setForeground(Color.BLACK);

        saleTable.setRowSelectionAllowed(false);
        saleTable.setColumnSelectionAllowed(false);

        // Add the table to a JScrollPane
        JScrollPane scrollPane = new JScrollPane(saleTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding
        getContentPane().add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Creates the back button.
     *
     * @return The created back button.
     */
    private JButton createBackButton() {
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                try {
                    switch (SessionManager.getInstance().getCurrentAccount().getRole()) {
                        case GUEST:
                            HotelManagementSystem.openGuestHomePage();
                            break;
                        case CLERK:
                            break;
                        case ADMIN:
                            HotelManagementSystem.openAdminHomePage();
                            break;
                        default:
                            throw new RuntimeException();
                    }
                } catch (RuntimeException exc) {
                    JOptionPane.showMessageDialog(null, "Invalid user info! Please contact admin.");
                }
            }
        });


        // Set button font, size, and background color
        backButton.setFont(headerFont);
        backButton.setForeground(Color.BLACK);

        return backButton;
    }
    /**
     * Creates the panel containing the back button and pay bill button.
     *
     * @return The created panel.
     */
    private JPanel createButtonsPanel() {
        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2, 10, 0)); // 1 row, 2 columns, horizontal gap 10, vertical gap 0
        buttonsPanel.setBackground(backgroundColor);

        JButton backButton = createBackButton();
        JButton payBillButton = createPayBillButton();

        buttonsPanel.add(backButton);
        buttonsPanel.add(payBillButton);

        return buttonsPanel;
    }

    /**
     * Creates the pay bill button.
     *
     * @return The created pay bill button.
     */
    private JButton createPayBillButton() {
        JButton payBillButton = new JButton("Pay Bill");
        payBillButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (saleTable.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(null, "There are no items in the bill to pay.");
                    return; // Exit method
                }
                // Add payment logic here
                if(SessionManager.getInstance().getCurrentAccount().getRole() == Role.GUEST){
                    GuestAccountController controller= new GuestAccountController(new GuestAccountJDBCDAO());
                    SaleController saleController = new SaleController(new SaleJDBCDAO());
                    Guest account = (Guest)SessionManager.getInstance().getCurrentAccount();
                    CreditCard card = account.getCreditCard();
                    double billPrice = 0;
                    for(int i = 0; i < saleTable.getRowCount(); i++) {
                        double price = Double.parseDouble(saleTable.getValueAt(i, 3).toString());
                        double quantity = Double.parseDouble(saleTable.getValueAt(i, 4).toString());

                        billPrice += price * quantity;
                    }
                    CreditCardPayment payment = new CreditCardPayment(billPrice, account.getCreditCard());
                    if(payment.processPayment()){
                        account.setCreditCard(payment.getCard());
                        controller.updateAccounts(account,account.getEmail());
                        DefaultTableModel model = (DefaultTableModel) saleTable.getModel();
                        model.setRowCount(0);
                        saleController.deleteSaleByAcctId(SessionManager.getInstance().getCurrentAccount().getId());
                        JOptionPane.showMessageDialog(null,"Your bill has been paid!");
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "You cannot afford this bill...uh oh!");
                    }

                }
                else{
                    JOptionPane.showMessageDialog(null, "Only a guest can pay their bill!");
                }
            }
        });

        // Set button font, size, and background color
        payBillButton.setFont(headerFont);
        payBillButton.setForeground(Color.BLACK);


        return payBillButton;
    }
    /**
     * Updates the billing page with new sales information.
     *
     * @param sales The updated set of sales.
     */
    public void updatePage(Set<Sale> sales){
        createSaleTable(sales);
    }

}
