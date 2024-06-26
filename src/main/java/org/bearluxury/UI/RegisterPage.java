package org.bearluxury.UI;

import org.bearluxury.Email.EmailSender;
import org.bearluxury.account.*;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import org.bearluxury.controllers.GuestAccountController;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

/**
 * The RegisterPage class UI is for registering a new guest account.
 *
 * @author Will Clore
 * @author Harrsion Hassler
 * @author Derek Martinez
 * @author Nicholas Nolen
 * @author Joseph Zuniga
 * @author Alan Vilagrand
 */
public class RegisterPage extends JFrame implements ActionListener {

    ImageIcon logo;

    private JPanel registerPanel;
    private JPanel cardPanel;
    private JScrollPane scrollPane;

    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField emailTextField;
    private JFormattedTextField phoneTextField;
    private JPasswordField passwordTextField;
    private JPasswordField confirmPasswordField;
    private JFormattedTextField cardNumberField;
    private JFormattedTextField dateField;
    private JFormattedTextField cvvField;
    private JButton registerButton;
    private JButton cmdLogin;

    private JLabel emptyFirstNameLabel;
    private JLabel badFirstNameLabel;
    private JLabel emptyLastNameLabel;
    private JLabel badLastNameLabel;
    private JLabel emptyEmailLabel;
    private JLabel badEmailLabel;
    private JLabel emptyPhoneLabel;
    private JLabel emptyPasswordLabel;
    private JLabel badPasswordLabel;
    private JLabel emptyConfirmPasswordLabel;
    private JLabel emptyCardInfoLabel;

    private JLabel emailInUseLabel;
    private JLabel phoneInUseLabel;
    private JLabel passwordNotMatchLabel;

    private PasswordSpecifier passwordSpecifier = new PasswordSpecifier();

    /**
     * Constructs a new RegisterPage object.
     */
    public RegisterPage() {
        // Set window preferences
        setTitle("Register");
        setSize(1280, 920);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new MigLayout("fill,insets 20", "[center]", "[center]"));
        getContentPane().setBackground(Style.backgroundColor);

        logo = new ImageIcon("src/main/resources/bbl-logo-transparent.png");
        JLabel logoLabel = new JLabel(logo);

        firstNameField = new JTextField();
        firstNameField.addActionListener(this);
        lastNameField = new JTextField();
        lastNameField.addActionListener(this);
        emailTextField = new JTextField();
        emailTextField.addActionListener(this);
        // Create formatted fields
        try {
            MaskFormatter phoneFormatter = new MaskFormatter("###-###-####");
            phoneTextField = new JFormattedTextField(phoneFormatter);
            phoneTextField.addActionListener(this);
            MaskFormatter dateFormatter = new MaskFormatter("##/##");
            dateField = new JFormattedTextField(dateFormatter);
            dateField.addActionListener(this);
            MaskFormatter cardFormatter = new MaskFormatter("####-####-####-####");
            cardNumberField = new JFormattedTextField(cardFormatter);
            cardNumberField.addActionListener(this);
            MaskFormatter cvvFormatter = new MaskFormatter("###");
            cvvField = new JFormattedTextField(cvvFormatter);
            cvvField.addActionListener(this);
        }catch(ParseException ignored){
        }
        passwordTextField = new JPasswordField();
        passwordTextField.addActionListener(this);
        confirmPasswordField = new JPasswordField();
        confirmPasswordField.addActionListener(this);

        registerButton = new JButton("Register");
        registerButton.putClientProperty(FlatClientProperties.STYLE,
                "[light]background:darken(@background,10%);" +
                        "[dark]background:lighten(@background,10%);" +
                        "borderWidth:0;" +
                        "focusWidth:0;" +
                        "innerFocusWidth:0");
        registerButton.addActionListener(this);

        registerPanel = new JPanel(new MigLayout("wrap,fillx,insets 0 45 30 45", "fill,250:280"));
        registerPanel.putClientProperty(FlatClientProperties.STYLE,
                "arc:20;" +
                        "background:darken(@background,3%);");

        passwordTextField.putClientProperty(FlatClientProperties.STYLE,
                "showRevealButton:true");
        confirmPasswordField.putClientProperty(FlatClientProperties.STYLE,
                "showRevealButton:true");

        JLabel header = new JLabel("Welcome to Baylor Bear Luxury");
        header.putClientProperty(FlatClientProperties.STYLE, "font:bold +7");

        JLabel description = new JLabel("Please fill in the information below to get started");
        description.putClientProperty(FlatClientProperties.STYLE,
                "[light]foreground:lighten(@foreground,30%);" +
                        "[dark]foreground:darken(@foreground,30%)");

        emptyFirstNameLabel = new JLabel("First name is required");
        badFirstNameLabel = new JLabel("First name must be alphabetical");
        badFirstNameLabel.setForeground(Color.red);
        emptyFirstNameLabel.setForeground(Color.red);
        emptyLastNameLabel = new JLabel("Last name is required");
        emptyLastNameLabel.setForeground(Color.red);
        badLastNameLabel = new JLabel("Last name must be alphabetical");
        badLastNameLabel.setForeground(Color.red);
        emptyEmailLabel = new JLabel("Email address is required");
        emptyEmailLabel.setForeground(Color.red);
        badEmailLabel = new JLabel("Email address not valid.");
        badEmailLabel.setForeground(Color.red);
        emptyPhoneLabel = new JLabel("Phone number is required");
        emptyPhoneLabel.setForeground(Color.red);
        emptyPasswordLabel = new JLabel("Field cannot be empty");
        emptyPasswordLabel.setForeground(Color.red);
        badPasswordLabel = new JLabel();
        badPasswordLabel.setForeground(Color.red);
        emptyConfirmPasswordLabel = new JLabel("Confirm password is required");
        emptyConfirmPasswordLabel.setForeground(Color.red);
        emptyCardInfoLabel = new JLabel("Card information is required");
        emptyCardInfoLabel.setForeground(Color.red);

        emailInUseLabel = new JLabel("This email is already in use");
        emailInUseLabel.setForeground(Color.red);
        phoneInUseLabel = new JLabel("This phone number is already in use");
        phoneInUseLabel.setForeground(Color.red);
        passwordNotMatchLabel = new JLabel("Passwords do not match");
        passwordNotMatchLabel.setForeground(Color.red);

        cardPanel = new JPanel(new MigLayout("fillx,insets 0"));
        cardPanel.putClientProperty(FlatClientProperties.STYLE, "background:darken(@background,3%);");
        cardPanel.setPreferredSize(new Dimension(0, 50));
        cardPanel.add(new JLabel("Date"));
        cardPanel.add(new JLabel("CVV"), "wrap");
        cardPanel.add(dateField);
        cardPanel.add(cvvField);

        registerPanel.add(logoLabel);
        registerPanel.add(header, "gapy 0");
        registerPanel.add(description);
        registerPanel.add(new JLabel("First name"), "gapy 0");
        registerPanel.add(firstNameField);
        registerPanel.add(new JLabel("Last name"), "gapy 0");
        registerPanel.add(lastNameField);
        registerPanel.add(new JLabel("Email"), "gapy 0");
        registerPanel.add(emailTextField);
        registerPanel.add(new JLabel("Phone"), "gapy 0");
        registerPanel.add(phoneTextField);
        registerPanel.add(new JLabel("Password"), "gapy 0");
        registerPanel.add(passwordTextField);
        registerPanel.add(new JLabel("Confirm password"), "gapy 0");
        registerPanel.add(confirmPasswordField, "gapy 0");
        registerPanel.add(new JLabel("Card number"));
        registerPanel.add(cardNumberField);
        registerPanel.add(cardPanel);
        registerPanel.add(registerButton, "gapy 0");
        registerPanel.add(createLoginLabel(), "gapy 0");

        scrollPane = new JScrollPane(registerPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.putClientProperty(FlatClientProperties.STYLE,
                "arc:20;" +
                        "background:darken(@background,3%);");

        add(scrollPane);
    }

    /**
     * Creates a login label to allow the user to login if wanted
     * @return a JLabel with the message
     */
    private Component createLoginLabel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panel.putClientProperty(FlatClientProperties.STYLE,
                "background:null");
        cmdLogin = new JButton("<html><a href=\"#\">Log in now</a></html>");
        cmdLogin.putClientProperty(FlatClientProperties.STYLE,
                "border:3,3,3,3");
        cmdLogin.setContentAreaFilled(false);
        cmdLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cmdLogin.addActionListener(this);
        JLabel label = new JLabel("Already have an account?");
        label.putClientProperty(FlatClientProperties.STYLE,
                "[light]foreground:lighten(@foreground,30%);");
        panel.add(label);
        panel.add(cmdLogin);
        return panel;
    }

    /**
     * Removes all error labels
     */
    private void removeErrorLabels() {
        registerPanel.remove(emptyFirstNameLabel);
        registerPanel.remove(badFirstNameLabel);
        registerPanel.remove(emptyLastNameLabel);
        registerPanel.remove(badLastNameLabel);
        registerPanel.remove(emptyEmailLabel);
        registerPanel.remove(badEmailLabel);
        registerPanel.remove(emailInUseLabel);
        registerPanel.remove(emptyPhoneLabel);
        registerPanel.remove(phoneInUseLabel);
        registerPanel.remove(emptyPasswordLabel);
        registerPanel.remove(badPasswordLabel);
        registerPanel.remove(emptyConfirmPasswordLabel);
        registerPanel.remove(passwordNotMatchLabel);
        registerPanel.remove(emptyCardInfoLabel);
    }

    /**
     * Checks to see if every credential is filled and in correct format.
     * Also checks to see if existing information such as email and phone already exist
     *
     * @return true if all credentials are valid, false otherwise
     */
    private boolean checkCredentials() {
        boolean validCredentials = true;
        GuestAccountController controller = new GuestAccountController(new GuestAccountJDBCDAO());
        removeErrorLabels();

        // Check if fields are empty
        int addedComponentCount = 0;
        if (firstNameField.getText().isEmpty()) {
            registerPanel.add(emptyFirstNameLabel, 5 + addedComponentCount);
            addedComponentCount++;
            validCredentials = false;
        } else {

            // Check if alphabetical
            if (!firstNameField.getText().matches("[a-zA-Z]*$")) {
                registerPanel.add(badFirstNameLabel, 5 + addedComponentCount);
                addedComponentCount++;
                validCredentials = false;
            }
        }
        if (lastNameField.getText().isEmpty()) {
            registerPanel.add(emptyLastNameLabel, 7 + addedComponentCount);
            addedComponentCount++;
            validCredentials = false;
        } else {

            // Check if alphabetical
            if (!lastNameField.getText().matches("[a-zA-Z]*$")) {
                registerPanel.add(badLastNameLabel, 7 + addedComponentCount);
                addedComponentCount++;
                validCredentials = false;
            }
        }
        if (emailTextField.getText().isEmpty()) {
            registerPanel.add(emptyEmailLabel, 9 + addedComponentCount);
            addedComponentCount++;
            validCredentials = false;
        } else {

            // Check if email is in use
            for (Account account : controller.listAccounts()) {
                if (account.getEmail().equalsIgnoreCase(emailTextField.getText())) {
                    registerPanel.add(emailInUseLabel, 9 + addedComponentCount);
                    validCredentials = false;
                    addedComponentCount++;

                }
            }
            if(!EmailSpecifier.isValidEmail(emailTextField.getText())){
                registerPanel.add(badEmailLabel, 9 + addedComponentCount);
                validCredentials = false;
                addedComponentCount++;
            }
        }
        if (phoneTextField.getValue() == null) {
            registerPanel.add(emptyPhoneLabel, 11 + addedComponentCount);
            addedComponentCount++;
            validCredentials = false;
        } else {

            // Check if phone is in use
            for (Account account : controller.listAccounts()) {
                if (account.getPhoneNumber() == Long
                        .parseLong(String.valueOf(phoneTextField.getValue())
                                .replaceAll("-",""))) {
                    registerPanel.add(phoneInUseLabel, 11 + addedComponentCount);
                    addedComponentCount++;
                    validCredentials = false;
                }
            }
        } // password is empty, show error
        if (passwordTextField.getText().isEmpty()) {
            registerPanel.add(emptyPasswordLabel, 13 + addedComponentCount);
            addedComponentCount++;
            validCredentials = false;
        }else{

            // password does not meet specification, show error
            if(!passwordSpecifier.checkPassword(passwordTextField.getText())){
                // if there is a problem with the password, it's not empty
                registerPanel.remove(emptyPasswordLabel);

                badPasswordLabel.setText(passwordSpecifier.getPasswordProblem());
                registerPanel.add(badPasswordLabel, 13 + addedComponentCount);
                addedComponentCount++;
                validCredentials = false;
            }
        }
        if (confirmPasswordField.getText().isEmpty()) {
            registerPanel.add(emptyConfirmPasswordLabel, 15 + addedComponentCount);
            addedComponentCount++;
            validCredentials = false;
        } else {

            // Check if passwords match
            if (!passwordTextField.getText().isEmpty()) {
                if (!passwordTextField.getText().equals(confirmPasswordField.getText())) {
                    registerPanel.add(passwordNotMatchLabel, 15 + addedComponentCount);
                    validCredentials = false;
                }
            }
        }
        // Check if card info is filled
        if (cardNumberField.getValue() == null || dateField.getValue() == null || cvvField.getValue() == null) {
            registerPanel.add(emptyCardInfoLabel, 18 + addedComponentCount);
            validCredentials = false;
        }

        setVisible(true);

        return validCredentials;
    }

    /**
     * Registers a new account to the database
     */
    private void registerAccount() {
        GuestAccountController controller = new GuestAccountController(new GuestAccountJDBCDAO());
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailTextField.getText();
        long phoneNumber = Long.parseLong(phoneTextField.getText().replaceAll("-",""));
        String password = passwordTextField.getText();

        // Billing info
        String cardNumber = cardNumberField.getText().replaceAll("-", "");
        String cardHolderName = firstName + " " + lastName;
        String expDate = dateField.getText();
        String cvv = cvvField.getText();

        Role role = Role.GUEST;
        controller.insertAccount(new Guest(firstName,lastName, email,phoneNumber,password, role,new CreditCard(cardNumber,cardHolderName, expDate,cvv)));
    }

    /**
     * Handles action listeners.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cmdLogin) {
            dispose();
            HotelManagementSystem.openLoginPage();
        } else if (e.getSource() == registerButton || e.getSource() == cvvField) {
            if (checkCredentials()) {
                registerAccount();
                JOptionPane.showMessageDialog(this, "Account successfully registered.");
                try {
                    EmailSender.sendAccountCreationEmail(firstNameField.getText() + ", " + lastNameField.getText(), emailTextField.getText());
                }catch(Exception e1){
                    EmailSender.sendAccountCreationEmail(firstNameField.getText() + ", " + lastNameField.getText(), emailTextField.getText());

                }
                dispose();
                HotelManagementSystem.openLoginPage();
            }
        }
        // Move to next field when enter is pressed
        if (e.getSource() == firstNameField) {
            lastNameField.requestFocusInWindow();
        } else if (e.getSource() == lastNameField) {
            emailTextField.requestFocusInWindow();
        } else if (e.getSource() == emailTextField) {
            phoneTextField.requestFocusInWindow();
        } else if (e.getSource() == phoneTextField) {
            passwordTextField.requestFocusInWindow();
        } else if (e.getSource() == passwordTextField) {
            confirmPasswordField.requestFocusInWindow();
        } else if (e.getSource() == confirmPasswordField) {
            cardNumberField.requestFocusInWindow();
        } else if (e.getSource() == cardNumberField) {
            dateField.requestFocusInWindow();
        } else if (e.getSource() == dateField) {
            cvvField.requestFocusInWindow();
        }
    }
}