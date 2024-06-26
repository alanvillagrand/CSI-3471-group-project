package org.bearluxury.UI;

import org.bearluxury.account.ClerkAccountDAO;
import org.bearluxury.account.GuestAccountJDBCDAO;
import org.bearluxury.account.Role;
import org.bearluxury.controllers.ClerkAccountController;
import org.bearluxury.controllers.GuestAccountController;
import org.bearluxury.reservation.Reservation;
import org.bearluxury.reservation.ReservationCatalog;
import org.bearluxury.state.SessionManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents a GUI for displaying booked reservations.
 *
 * @author Will Clore
 * @author Harrsion Hassler
 * @author Derek Martinez
 * @author Nicholas Nolen
 * @author Joseph Zuniga
 * @author Alan Vilagrand
 *
 */
public class BookedReservationsGUI extends JFrame {
    private final Color backgroundColor = new Color(232, 223, 185);
    private final Color tableHeaderColor = new Color(184, 134, 11);
    private final Font tableHeaderFont = new Font("Arial", Font.BOLD, 18);
    private final Font tableFont = new Font("Arial", Font.BOLD, 14);
    protected JTable table;
    protected DefaultTableModel model;

    /**
     * Constructs a new BookedReservationsGUI.
     *
     * @param reservationCatalog The reservation catalog.
     */
    public BookedReservationsGUI(ReservationCatalog reservationCatalog) {
        setTitle("Booked Reservations");
        setSize(1280, 720);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        model = createTableModel();

        table = createTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel panel = createPanel(scrollPane);

        getContentPane().setBackground(backgroundColor);

        fillTableRows(reservationCatalog.getReservations(), model);

        JButton backButton = createBackButton();


        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Style.backgroundColor);
        topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        topPanel.add(backButton, BorderLayout.WEST);




        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(topPanel, BorderLayout.NORTH);
        getContentPane().add(panel, BorderLayout.CENTER);
    }

    /**
     * Creates the table model for the GUI.
     *
     * @return The table model.
     */
    private DefaultTableModel createTableModel() {
        String[] columnNames = {"Account ID", "Reservation ID","Room ID", "First Name", "Last Name", "Email", "# Of Guests", "Start Date", "End Date" ,"Checked In"};
        return new DefaultTableModel(columnNames, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }
    /**
     * Creates the back button for the GUI.
     *
     * @return The back button.
     */
    private JButton createBackButton() {
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
           //FIXME: NEED TO FIX ACTION
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                try {
                    System.out.println("This is my role: " + SessionManager.getInstance().getCurrentAccount().getRole());
                    if (SessionManager.getInstance().getCurrentAccount().getRole() == Role.GUEST) {
                        HotelManagementSystem.openGuestHomePage();
                    }
                    else if (SessionManager.getInstance().getCurrentAccount().getRole() == Role.CLERK) {
                        HotelManagementSystem.openClerkHomePage();
                    }
                    else if (SessionManager.getInstance().getCurrentAccount().getRole() == Role.ADMIN) {
                        HotelManagementSystem.openAdminHomePage();
                    }
                    else{
                        throw new RuntimeException();
                    }
                }catch(RuntimeException exc){
                    JOptionPane.showMessageDialog(null,"Invalid user info! please contact admin.");
                }
            }
        });
        return backButton;
    }


    /**
     * Creates the table for the GUI.
     *
     * @param model The table model.
     * @return The created table.
     */
    private JTable createTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setBackground(Color.WHITE);
        table.getTableHeader().setBackground(tableHeaderColor);
        table.getTableHeader().setForeground(Color.BLACK);
        table.getTableHeader().setFont(tableHeaderFont);
        table.setGridColor(Color.BLACK);
        table.setFillsViewportHeight(true);
        table.setFont(tableFont);
        table.setRowHeight(30);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        return table;
    }


    /**
     * Creates the panel to hold the table.
     *
     * @param scrollPane The scroll pane containing the table.
     * @return The panel containing the table.
     */
    private JPanel createPanel(JScrollPane scrollPane) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(backgroundColor);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Fills the table rows with booked reservations.
     *
     * @param unsortedReservations The unsorted set of reservations.
     * @param model                The table model.
     */
    public void fillTableRows(Set<Reservation> unsortedReservations, DefaultTableModel model) {
        List<Reservation> reservations = unsortedReservations.stream().
                sorted(Comparator.comparing(Reservation::getFirstName).
                        thenComparing(Reservation::getRoomNumber)).
                collect(Collectors.toList());

        // format output dates
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
        GuestAccountController controller = new GuestAccountController(new GuestAccountJDBCDAO());

        // room number,first name,last name,email,number of guests,start date,end date
        reservations
                .forEach(reservation -> model.addRow(new Object[]{
                        controller.getAccount(reservation.getEmail()).
                                orElseThrow(()-> new NoSuchElementException("Account not found")).
                                getId(),
                        reservation.getReservationID(),
                        reservation.getRoomNumber(),
                        reservation.getFirstName(),
                        reservation.getLastName(),
                        reservation.getEmail(),
                        reservation.getNumberOfGuests(),
                        formatter.format(reservation.getStartDate()),
                        formatter.format(reservation.getEndDate()),
                        reservation.isCheckedIn()
                }));
    }

}
