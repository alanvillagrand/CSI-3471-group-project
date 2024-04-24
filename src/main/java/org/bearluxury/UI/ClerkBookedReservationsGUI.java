package org.bearluxury.UI;

import org.bearluxury.account.AccountJDBCDAO;
import org.bearluxury.controllers.AccountController;
import org.bearluxury.controllers.ReservationController;
import org.bearluxury.reservation.Reservation;
import org.bearluxury.reservation.ReservationCatalog;
import org.bearluxury.reservation.ReservationJDBCDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;

public class ClerkBookedReservationsGUI extends BookedReservationsGUI {


    public ClerkBookedReservationsGUI(ReservationCatalog reservationCatalog) {
        super(reservationCatalog);

        JButton editButton = new JButton("Edit Reservation");
        JButton deleteButton = new JButton("Delete Reservation");
        JButton checkInButton = new JButton("Check In");


        editButton.setFont(Style.defaultFont);
        deleteButton.setFont(Style.defaultFont);
        checkInButton.setFont(Style.defaultFont);


        deleteButton.addActionListener(new DeleteReservationAction(table, model));
        editButton.addActionListener(new EditReservationAction(table));
        checkInButton.addActionListener(new CheckInAction(table, model));


        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 0)); // 1 row, 3 columns, with 10px horizontal gap
        buttonPanel.setBackground(Style.backgroundColor);
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));


        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(checkInButton);

        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
}

class EditReservationAction implements ActionListener {
    private JTable table;

    public EditReservationAction(JTable table) {
        this.table = table;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            ReservationController controller = new ReservationController(new ReservationJDBCDAO());
            Optional<Reservation> opReservation = controller.getReservationByReservationId(Integer.parseInt(table.getValueAt(selectedRow, 1).toString()));
            Reservation reservation = opReservation.orElseThrow(() -> new NoSuchElementException("Reservation not found"));
            EditReservationPane pane = new EditReservationPane(reservation, (DefaultTableModel) table.getModel(), table);
            pane.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Please select a row first.");
        }
    }
}

class DeleteReservationAction implements ActionListener {
    private JTable table;
    private DefaultTableModel model;

    public DeleteReservationAction(JTable table, DefaultTableModel model) {
        this.table = table;
        this.model = model;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            ReservationController controller = new ReservationController(new ReservationJDBCDAO());
            if (controller.deleteReservationByReservationId(Integer.parseInt(table.getValueAt(selectedRow, 1).toString()))) {
                model.removeRow(selectedRow);
            } else {
                JOptionPane.showMessageDialog(null, "Reservation not found");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select a row first.");
        }
    }
}
class CheckInAction implements ActionListener{

    private JTable table;
    private DefaultTableModel model;

    public CheckInAction(JTable table, DefaultTableModel model) {
        this.table = table;
        this.model = model;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        int selectedRow = table.getSelectedRow();
        ReservationController controller = new ReservationController(new ReservationJDBCDAO());
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");


        if(selectedRow != -1) {
            Reservation res = controller.
                    getReservationByReservationId(Integer.parseInt(table.getValueAt(selectedRow, 1).toString())).
                    orElseThrow(() -> new NoSuchElementException("Reservation Doesn't exist"));
            LocalDate currentDate = LocalDate.now();
            LocalDate reservationStartDate = Instant.ofEpochMilli(res.getStartDate().getTime())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            if(reservationStartDate.equals(currentDate)) {
                if(res.isCheckedIn()){
                    JOptionPane.showMessageDialog(null, "You are already checked in!");
                }
                else{
                    res.setCheckedIn(true);
                    controller.updateReservationByReservationId(res, res.getReservationID());
                    model.removeRow(selectedRow);
                    model.addRow(new Object[]{
                            new AccountController(new AccountJDBCDAO()).getAccount(res.getEmail()).
                                    orElseThrow(()-> new NoSuchElementException("No active accounts with reservations")).
                                    getId(),
                            res.getReservationID(),
                            res.getRoomNumber(),
                            res.getFirstName(),
                            res.getLastName(),
                            res.getEmail(),
                            res.getNumberOfGuests(),
                            formatter.format(res.getStartDate()),
                            formatter.format(res.getEndDate()),
                            res.isCheckedIn()
                    });
                    JOptionPane.showMessageDialog(null, "Reservation updated");
                }

            }
            else if(reservationStartDate.isAfter(currentDate)){
                JOptionPane.showMessageDialog(null, "Check-in date is " + formatter.format(res.getStartDate())+ ". Please check in on that date!");
            }
            else{
                JOptionPane.showMessageDialog(null, "Your check-in window has passed");
            }

        }


    }
}