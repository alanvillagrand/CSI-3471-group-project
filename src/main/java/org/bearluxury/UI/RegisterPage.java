package org.bearluxury.UI;

import org.bearluxury.Email.EmailSender;
import org.bearluxury.UI.shopUI.CreditCardFrame;
import org.bearluxury.account.*;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import org.bearluxury.controllers.ClerkAccountController;
import org.bearluxury.controllers.GuestAccountController;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

public class RegisterPage extends JFrame implements ActionListener {

    Color backgroundColor = new Color(232,223,185,255);

    AccountBuilder accountBuilder;

    ImageIcon logo;

    private JPanel registerPanel;

    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField emailTextField;
    private JFormattedTextField phoneTextField;
    private JPasswordField passwordTextField;
    private JPasswordField confirmPasswordField;
    private JButton registerButton;
    private JButton cmdRegister;

    private JLabel emptyFirstNameLabel;
    private JLabel emptyLastNameLabel;
    private JLabel emptyEmailLabel;
    private JLabel badEmailLabel;
    private JLabel emptyPhoneLabel;
    private JLabel emptyPasswordLabel;
    private JLabel badPasswordLabel;
    private JLabel emptyConfirmPasswordLabel;

    private JLabel emailInUseLabel;
    private JLabel phoneInUseLabel;
    private JLabel passwordNotMatchLabel;

    private PasswordSpecifier passwordSpecifier = new PasswordSpecifier();
    private JTextField cardNumberField;
    private JTextField expDateField;
    private JTextField cvvField;
    private JTextField cardHolderNameField;

    public RegisterPage() {
        setTitle("Register");
        setSize(1280, 920);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new MigLayout("fill,insets 20", "[center]", "[center]"));
        getContentPane().setBackground(backgroundColor);

        //accountBuilder = new AccountBuilder("src/main/resources/AccountList.csv");

        logo = new ImageIcon("src/main/resources/bbl-logo-transparent.png");
        JLabel logoLabel = new JLabel(logo);

        firstNameField = new JTextField();
        lastNameField = new JTextField();
        emailTextField = new JTextField();
        try {
            MaskFormatter maskFormatter = new MaskFormatter("###-###-####");
            phoneTextField = new JFormattedTextField(maskFormatter);
        }catch(ParseException ignored){
        }
        passwordTextField = new JPasswordField();
        confirmPasswordField = new JPasswordField();

        registerButton = new JButton("Register");
        registerButton.putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]background:darken(@background,10%);" +
                "[dark]background:lighten(@background,10%);" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "innerFocusWidth:0");
        registerButton.addActionListener(this);//

        registerPanel = new JPanel(new MigLayout("wrap,fillx,insets 0 45 30 45", "fill,250:280"));
        registerPanel.setBackground(backgroundColor);
        registerPanel.putClientProperty(FlatClientProperties.STYLE, "" +
                "arc:20;" +
                "background:darken(@background,3%);");

        passwordTextField.putClientProperty(FlatClientProperties.STYLE, "" +
                "showRevealButton:true");
        confirmPasswordField.putClientProperty(FlatClientProperties.STYLE, "" +
                "showRevealButton:true");

        JLabel header = new JLabel("Welcome to Baylor Bear Luxury");
        header.putClientProperty(FlatClientProperties.STYLE, "" + "font:bold +7");

        JLabel description = new JLabel("Please fill in the information below to get started");
        description.putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]foreground:lighten(@foreground,30%);" +
                "[dark]foreground:darken(@foreground,30%)");

        emptyFirstNameLabel = new JLabel("First name is required");
        emptyFirstNameLabel.setForeground(Color.red);
        emptyLastNameLabel = new JLabel("Last name is required");
        emptyLastNameLabel.setForeground(Color.red);
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

        emailInUseLabel = new JLabel("This email is already in use");
        emailInUseLabel.setForeground(Color.red);
        phoneInUseLabel = new JLabel("This phone number is already in use");
        phoneInUseLabel.setForeground(Color.red);
        passwordNotMatchLabel = new JLabel("Passwords do not match");
        passwordNotMatchLabel.setForeground(Color.red);

        cardNumberField = new JTextField();
        expDateField = new JTextField();
        cvvField = new JTextField();
        cardHolderNameField = new JTextField();

        // Add labels and fields for card information



        registerPanel.add(logoLabel);
        registerPanel.add(header, "gapy 10");
        registerPanel.add(description);
        registerPanel.add(new JLabel("First name"), "gapy 6");
        registerPanel.add(firstNameField);
        registerPanel.add(new JLabel("Last name"), "gapy 6");
        registerPanel.add(lastNameField);
        registerPanel.add(new JLabel("Email"), "gapy 6");
        registerPanel.add(emailTextField);
        registerPanel.add(new JLabel("Phone"), "gapy 6");
        registerPanel.add(phoneTextField);
        registerPanel.add(new JLabel("Password"), "gapy 6");
        registerPanel.add(passwordTextField);
        registerPanel.add(new JLabel("Confirm password"), "gapy 6");
        registerPanel.add(confirmPasswordField);

        registerPanel.add(registerButton, "gapy 10");
        registerPanel.add(createRegisterLabel(), "gapy 10");

        add(registerPanel);
    }

    private Component createRegisterLabel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panel.putClientProperty(FlatClientProperties.STYLE, "" +
                "background:null");
        cmdRegister = new JButton("<html><a href=\"#\">Log in now</a></html>");
        cmdRegister.putClientProperty(FlatClientProperties.STYLE, "" +
                "border:3,3,3,3");
        cmdRegister.setContentAreaFilled(false);
        cmdRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cmdRegister.addActionListener(this);
        JLabel label = new JLabel("Already have an account?");
        label.putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]foreground:lighten(@foreground,30%);");
        panel.add(label);
        panel.add(cmdRegister);
        return panel;
    }
    //
    private Boolean checkCredentials() {
        Boolean validCredentials = true;
        GuestAccountController controller = new GuestAccountController(new GuestAccountJDBCDAO());

        // Check if fields are empty
        int addedComponentCount = 0;
        if (firstNameField.getText().isEmpty()) {
            registerPanel.add(emptyFirstNameLabel, 5 + addedComponentCount);
            addedComponentCount++;
            validCredentials = false;
        } else { registerPanel.remove(emptyFirstNameLabel); }
        if (lastNameField.getText().isEmpty()) {
            registerPanel.add(emptyLastNameLabel, 7 + addedComponentCount);
            addedComponentCount++;
            validCredentials = false;
        } else { registerPanel.remove(emptyLastNameLabel); }
        if (emailTextField.getText().isEmpty()) {
            registerPanel.add(emptyEmailLabel, 9 + addedComponentCount);
            addedComponentCount++;
            validCredentials = false;
        } else {
            registerPanel.remove(emptyEmailLabel);
            registerPanel.remove(emailInUseLabel);
            registerPanel.remove(badEmailLabel);

            // Check if email is in use
            for (Account account : controller.listAccounts()) {
                if (account.getEmail().equalsIgnoreCase(emailTextField.getText())) {
                    registerPanel.add(emailInUseLabel, 9 + addedComponentCount);
                    validCredentials = false;
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
            registerPanel.remove(emptyPhoneLabel);
            registerPanel.remove(phoneInUseLabel);

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
            registerPanel.remove(emptyPasswordLabel);
        }
        // password does not meet specification, show error
        if(!passwordSpecifier.checkPassword(passwordTextField.getText())){
            // if there is a problem with the password, it's not empty
            registerPanel.remove(emptyPasswordLabel);

            badPasswordLabel.setText(passwordSpecifier.getPasswordProblem());
            registerPanel.add(badPasswordLabel, 13 + addedComponentCount);
            addedComponentCount++;
            validCredentials = false;
        }else{
            registerPanel.remove(badPasswordLabel);
        }

        if (confirmPasswordField.getText().isEmpty()) {
            registerPanel.add(emptyConfirmPasswordLabel, 15 + addedComponentCount);
            validCredentials = false;
        } else {
            registerPanel.remove(emptyConfirmPasswordLabel);
            registerPanel.remove(passwordNotMatchLabel);

            // Check if passwords match
            if (!passwordTextField.getText().isEmpty()) {
                if (!passwordTextField.getText().equals(confirmPasswordField.getText())) {
                    registerPanel.add(passwordNotMatchLabel, 15 + addedComponentCount);
                    validCredentials = false;
                }
            }
        }

        setVisible(true);

        return validCredentials;
    }

    /////TEMP FIX: MADE ACCOUNT BUILDER ROLE GUEST
    private void registerAccount(String cardNumber, String cardHolderName, String expDate, String cvv) {
        GuestAccountController controller = new GuestAccountController(new GuestAccountJDBCDAO());
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailTextField.getText();
        // username is not needed. Using email for now
        String userName = emailTextField.getText();
        // remove unwanted "-" character from phone number
        long phoneNumber = Long.parseLong(phoneTextField.getText().replaceAll("-",""));
        String password = passwordTextField.getText();
        //FIXME
        Role role = Role.GUEST;
        controller.insertAccount(new Guest(firstName,lastName, userName, email,phoneNumber,password, role,new CreditCard(cardNumber,cardHolderName, expDate,cvv)));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cmdRegister) {
            dispose();
            HotelManagementSystem.openLoginPage();
        } else if (e.getSource() == registerButton) {
            if (checkCredentials()) {
                JOptionPane.showMessageDialog(this, "Account successfully registered.");
                EmailSender.sendConfirmationEmail(firstNameField.getText()+", " + lastNameField.getText(), emailTextField.getText());
                dispose();
                HotelManagementSystem.openLoginPage();
                CreditCardFrame cardFrame = new CreditCardFrame();
                cardFrame.setVisible(true);
                registerAccount(cardFrame.getCardNumberField(),
                        cardFrame.getCardHolderNameField(),
                        cardFrame.getExpDateField(),
                        cardFrame.getCvvField());
                this.dispose();

            }
        }
    }
}