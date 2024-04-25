package org.bearluxury.UI.shopUI;

import com.formdev.flatlaf.FlatClientProperties;
import org.bearluxury.account.Account;
import org.bearluxury.account.CreditCard;
import org.bearluxury.account.Guest;
import org.bearluxury.product.Product;
import org.bearluxury.state.SessionManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CheckoutDialog extends JDialog implements ActionListener {
    private final double SALES_TAX = 0.0625;

    Map<Product, Integer> cart;

    JPanel cartPanel;
    JPanel centerPanel;
    JPanel summaryPanel;
    JPanel leftPanel;
    JPanel rightPanel;
    JPanel totalPanel;
    JPanel purchasePanel;

    JLabel cartLabel;
    DefaultListModel<String> listModel;
    JList<String> cartList;
    JScrollPane cartScrollPane;

    JLabel summaryLabel;
    JLabel totalProductsLabel;
    JLabel totalProductsAmountLabel;
    JLabel taxLabel;
    JLabel taxAmountLabel;

    JLabel overallTotalLabel;
    JLabel overallTotalAmountLabel;
    double overallTotalCost;

    private JButton purchaseButton;
    private JButton putOnTabButton;

    private CreditCard card;

    public CheckoutDialog(JFrame parent, Map<Product, Integer> cart, double totalPrice) {
        super(parent, "Checkout", true);
        //setLayout(new FlowLayout(FlowLayout.LEADING, 10, 10));
        setLocationRelativeTo(parent);
        setSize(400, 600);

        this.cart = cart;

        // Cart panel
        cartPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 10, 10));
        cartPanel.setPreferredSize(new Dimension(this.getWidth(), 300));

        cartLabel = new JLabel("Your Cart");
        cartLabel.putClientProperty(FlatClientProperties.STYLE, "font:bold +10");

        listModel = new DefaultListModel<>();
        cartList = new JList<>(listModel);
        cartList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        cartList.setFocusable(false);
        this.cart.forEach((product, quantity) -> listModel.addElement(quantity + "x " + product.getName() + " - $" + (product.getPrice() * quantity)));

        cartScrollPane = new JScrollPane(cartList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        cartScrollPane.setPreferredSize(new Dimension(300, 200));

        summaryLabel = new JLabel("Purchase Summary");
        summaryLabel.putClientProperty(FlatClientProperties.STYLE, "font:bold +10");

        cartPanel.add(cartLabel);
        cartPanel.add(cartScrollPane);
        cartPanel.add(summaryLabel);

        centerPanel = new JPanel(new BorderLayout());

        // Summary panel
        summaryPanel = new JPanel(new BorderLayout());
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        leftPanel = new JPanel(new GridLayout(2, 1));
        rightPanel = new JPanel(new GridLayout(2, 1));

        totalProductsLabel = new JLabel(this.cart.size() + " products");
        taxLabel = new JLabel("Sales tax");
        leftPanel.add(totalProductsLabel);
        leftPanel.add(taxLabel);

        totalProductsAmountLabel = new JLabel("$" + String.format("%.2f", totalPrice));
        totalProductsAmountLabel.putClientProperty(FlatClientProperties.STYLE, "font:bold");
        taxAmountLabel = new JLabel("$" + String.format("%.2f", totalPrice * SALES_TAX));
        taxAmountLabel.putClientProperty(FlatClientProperties.STYLE, "font:bold");
        rightPanel.add(totalProductsAmountLabel);
        rightPanel.add(taxAmountLabel);

        summaryPanel.add(leftPanel, BorderLayout.WEST);
        summaryPanel.add(rightPanel, BorderLayout.EAST);

        totalPanel = new JPanel(new BorderLayout());
        overallTotalLabel = new JLabel("Total:");
        overallTotalLabel.putClientProperty(FlatClientProperties.STYLE, "font:bold +8");
        overallTotalCost = totalPrice + (totalPrice * SALES_TAX);
        overallTotalAmountLabel = new JLabel("$" + String.format("%.2f", overallTotalCost));
        overallTotalAmountLabel.putClientProperty(FlatClientProperties.STYLE, "font:bold +8");
        totalPanel.add(overallTotalLabel, BorderLayout.WEST);
        totalPanel.add(overallTotalAmountLabel, BorderLayout.EAST);

        centerPanel.add(summaryPanel, BorderLayout.NORTH);
        centerPanel.add(totalPanel, BorderLayout.CENTER);


        // Checkout panel
        purchasePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        purchaseButton = new JButton("Confirm purchase");
        purchaseButton.addActionListener(this);
        purchasePanel.setPreferredSize(new Dimension(400, 50));
        purchasePanel.add(purchaseButton);

        putOnTabButton = new JButton("Put on Tab");
        putOnTabButton.addActionListener(this);
        purchasePanel.add(putOnTabButton); // Add the button to the purchase pan

        add(cartPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(purchasePanel, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == purchaseButton) {
            ShopHomePage.openCreditCardEntryScreen(cart, overallTotalCost);
        } else if (e.getSource() == putOnTabButton) {
            // Handle putting the purchase on the tab
            putOnTab();
        }
    }

    private void putOnTab() {
        System.out.println("put on tab");
    }


    public CreditCard getCard(CreditCardEntryScreen creditCardEntryScreen) {
        this.card = creditCardEntryScreen.getCard();
        return this.card;
    }
}
