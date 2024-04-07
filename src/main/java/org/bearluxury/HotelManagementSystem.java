package org.bearluxury;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

//Window
public class HotelManagementSystem  {

    public static void openRoomCatalogPane(int beds, LocalDate checkIn, LocalDate checkOut){
        RoomCatalog rooms = new RoomCatalog();
        rooms.setRooms(new RoomBuilder("src/main/resources/RoomList.csv").getRoomList());
        AvaliableRoomsGUI catalogPane = new AvaliableRoomsGUI(rooms,beds, checkIn, checkOut);
        catalogPane.setVisible(true);
    }

    public static void openHomePage() {
        HotelHomePage hotelHomePage = new HotelHomePage();
        hotelHomePage.setVisible(true);
    }

    public static void openLoginPage() {
        LoginPage loginPage = new LoginPage();
        loginPage.setVisible(true);
    }

    public static void openRegisterPage() {
        RegisterPage registerPage = new RegisterPage();
        registerPage.setVisible(true);
    }

    public static void openHotelManagmentSystem(){
        InfoFilterPane window = new InfoFilterPane();
        window.setVisible(true);
    }

    public static void openRegisterPane() {
        RegisterPane register = new RegisterPane();
        register.setVisible(true);
    }

    public static void main(String[] args) {
        FlatRobotoFont.install();
        UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
        FlatLightLaf.setup();
        ReservationJDBCDAO data = new ReservationJDBCDAO();
        List<Reservation> reservations = new ArrayList<>();
        ReservationBuilder builder = new ReservationBuilder("src/main/resources/ReservationList.csv");
        List<Reservation> res = builder.getReservationList();
        RoomJDBCDAO dao = new RoomJDBCDAO();
        RoomBuilder builder1 = new RoomBuilder("src/main/resources/RoomList.csv");
        Set<Room> res1 = builder1.getRoomList();
        for(Room res4: res1){
            dao.insert(res4);
        }
        Set<Room> res2 = dao.list();
        for(Room res4: res2){
            System.out.println(res4.getRoomNumber());
        }
        openLoginPage();
    }
}
