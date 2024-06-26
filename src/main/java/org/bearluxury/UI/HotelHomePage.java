package org.bearluxury.UI;

import org.bearluxury.account.Role;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Represents the abstract class for the hotel's home page.
 * Extends JFrame.
 *
 * @author Will Clore
 * @author Harrsion Hassler
 * @author Derek Martinez
 * @author Nicholas Nolen
 * @author Joseph Zuniga
 * @author Alan Vilagrand
 *
 */
public abstract class HotelHomePage extends JFrame {
    Font font = new Font("Goudy Old Style", Font.PLAIN, 30);
    JPanel reservePanel = new JPanel();

    /**
     * Constructs a HotelHomePage object.
     */
    public HotelHomePage() {
        setTitle("Baylor Bear Luxury");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 720);
        setLocationRelativeTo(null); // Center the frame on the screen

        Color backgroundColor = new Color(232, 223, 185, 255);
        getContentPane().setBackground(backgroundColor);

        JPanel logoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    BufferedImage hotelImage = ImageIO.read(new File("src/main/resources/hotelStockImage.jpg"));
                    BufferedImage logoImage = ImageIO.read(new File("src/main/resources/logo.png"));


                    int hotelX = (getWidth() - hotelImage.getWidth()) / 2;
                    int hotelY = (getHeight() - hotelImage.getHeight()) / 2;
                    g.drawImage(hotelImage, hotelX, hotelY, this);


                    int logoX = (getWidth() - logoImage.getWidth()) / 2;
                    int logoY = (getHeight() - logoImage.getHeight()) / 6;
                    g.drawImage(logoImage, logoX, logoY, this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        logoPanel.setBackground(backgroundColor);

        JPanel welcomePanel = new JPanel();
        welcomePanel.setBackground(backgroundColor);
        JLabel welcomeLabel = new JLabel("Welcome to Baylor Bear Luxury!");
        welcomeLabel.setFont(font);
        welcomePanel.add(welcomeLabel);

        reservePanel.setBackground(backgroundColor);

        setLayout(new BorderLayout());
        add(logoPanel, BorderLayout.CENTER);
        add(welcomePanel, BorderLayout.NORTH);
        add(reservePanel, BorderLayout.SOUTH);

    }
}
