/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import static com.LoginModel.emailLogin;
import static com.SearchController.selectedDate;
import static com.SearchController.selectedHostEmail;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author Joana Cabral
 */
public class MyEventsController implements Initializable {

    Connection connection;
    private final List<MyEventsDB> listGuest = new LinkedList();

    //Connecting with our database:
    public MyEventsController() {
        connection = SQLiteConnection.Connector();
        if (connection == null) {

            System.out.println("connection not successful");
            System.exit(1);
        }
    }

    //final ObservableList myEvents = FXCollections.observableArrayList();
    @FXML
    private AnchorPane anchorPaneMyEvents;

    @FXML
    private Button btnMyEvents;

    @FXML
    private Button btnProfile;

    @FXML
    private Button btnLogOut;

    @FXML
    private Button btnHome;

    @FXML
    private Button btnCreateEvent;

    @FXML
    private Button btnSearchEvent;

    @FXML
    private TableView tViewMyEvents;

    @FXML
    private TableColumn colName;

    @FXML
    private TableColumn colCity;

    @FXML
    private TableColumn colDate;

    @FXML
    private TableColumn colHostName;

    @FXML
    private TableColumn colMoreAction;

    @FXML
    public static Label lblErrorMessage;

    @FXML
    private TableView tViewMyEventsGuest;

    @FXML
    private TableColumn colGName;

    @FXML
    private TableColumn colGCity;

    @FXML
    private TableColumn colGDate;

    @FXML
    private TableColumn colGHostName;

    @FXML
    private TableColumn colGMoreAction;

    @FXML
    private void btnMyEventsAction(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/com/MyEvents.fxml"));
        anchorPaneMyEvents.getChildren().setAll(pane);
    }

    @FXML
    private void btnProfileAction(ActionEvent event) throws IOException, SQLException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/com/Profile.fxml"));
        anchorPaneMyEvents.getChildren().setAll(pane);
    }

    @FXML
    private void btnHomeAction(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/com/Main.fxml"));
        anchorPaneMyEvents.getChildren().setAll(pane);
    }

    @FXML
    private void btnCreateEventAction(ActionEvent event) throws IOException, SQLException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/com/CreateEvent.fxml"));
        anchorPaneMyEvents.getChildren().setAll(pane);
    }

    @FXML
    private void btnSearchEventAction(ActionEvent event) throws IOException, SQLException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/com/Search.fxml"));
        anchorPaneMyEvents.getChildren().setAll(pane);
    }

    @FXML
    private void btnLogOutAction(ActionEvent event) throws IOException {
        ((Node) (event.getSource())).getScene().getWindow().hide();
        Parent parent = FXMLLoader.load(getClass().getResource("/com/Login.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.show();
    }

    //Gets the list of events where emailLogin = HostEmail in the Events database
    public List<MyEventsDB> getMyEventsHost() {
        List listHost = new LinkedList();
        Statement st;
        ResultSet rs;
        try {
            st = connection.createStatement();
            String recordQuery = ("Select * from Events WHERE HostEmail = '" + emailLogin + "'");
            rs = st.executeQuery(recordQuery);
            while (rs.next()) {
                String hostEmail = rs.getString("HostEmail");
                String name = rs.getString("Name");
                String city = rs.getString("City");
                String date = rs.getString("Date");
                String hostName = rs.getString("HostName");
                listHost.add(new MyEventsDB(hostEmail, name, city, date, hostName));
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(MyEventsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listHost;
    }

    //returns list; Gets the HostEmail and Date from Guests database, and runs  
    //getMyEventsGuestEvent() for every entry found
    public List<MyEventsDB> getMyEventsGuest() {

        Statement st;
        ResultSet results;
        System.out.println("Establishing connection");
        try {
            st = connection.createStatement();
            String queryGuest = ("Select HostEmail, Date FROM Guests WHERE GuestEmail = '" + emailLogin + "'");
            System.out.println(queryGuest);
            results = st.executeQuery(queryGuest);
            while (results.next()) {
                String host = results.getString("HostEmail");
                String dateEvent = results.getString("Date");
                getMyEventsGuestEvent(host, dateEvent);
            }
            results.close();
        } catch (SQLException ex) {
            Logger.getLogger(MyEventsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listGuest;
    }

    //Gets the list of events where host = HostEmail and dateEvent = Date the Events database
    private void getMyEventsGuestEvent(String host, String dateEvent) throws SQLException {

        String queryEvent = ("SELECT * FROM Events WHERE HostEmail = '"
                + host + "' AND Date = '" + dateEvent + "'");

        System.out.println(queryEvent);

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(queryEvent);
            String hostEmail = rs.getString("HostEmail");
            String name = rs.getString("Name");
            String city = rs.getString("City");
            String date = rs.getString("Date");
            String hostName = rs.getString("HostName");
            System.out.println(hostEmail + ", " + name + ", " + city + ", "
                    + date + ", " + hostName + " added.");
            listGuest.add(new MyEventsDB(hostEmail, name, city, date, hostName));
            rs.close();
        } catch (Exception e) {
        }

    }

    //Method to change pane to ShowEvent
    private void mouseClickPaneChange() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/com/ShowEvent.fxml"));
        anchorPaneMyEvents.getChildren().setAll(pane);
    }

    private void populateTViewHost() {
        colName.setCellValueFactory(new PropertyValueFactory<MyEventsDB, String>("title"));
        colCity.setCellValueFactory(new PropertyValueFactory<MyEventsDB, String>("city"));
        colDate.setCellValueFactory(new PropertyValueFactory<MyEventsDB, String>("date"));

        tViewMyEvents.getItems().setAll(getMyEventsHost());

        //Double-click to select an event
        tViewMyEvents.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    //Gets the selected row number
                    int selectedIndex = tViewMyEvents.getSelectionModel().getSelectedIndex();
                    //Gets object builded in the class MyEventsDB -> Aamir explain
                    MyEventsDB selectedEvent = (MyEventsDB) tViewMyEvents.getItems().get(selectedIndex);
                    //Save HostEmail and Date of event to variables, so we can use them to get the whole event
                    selectedHostEmail = selectedEvent.getHostEmail();
                    selectedDate = selectedEvent.getDate();

                    //Opening ShowEvent AnchorPane on eventclick
                    try {
                        mouseClickPaneChange();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    private void populateTViewGuest() {
        colGName.setCellValueFactory(new PropertyValueFactory<MyEventsDB, String>("title"));
        colGCity.setCellValueFactory(new PropertyValueFactory<MyEventsDB, String>("city"));
        colGDate.setCellValueFactory(new PropertyValueFactory<MyEventsDB, String>("date"));
        colGHostName.setCellValueFactory(new PropertyValueFactory<MyEventsDB, String>("hostName"));

        tViewMyEventsGuest.getItems().setAll(getMyEventsGuest());

        //Double-click to select an event
        tViewMyEventsGuest.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    //Gets the selected row number
                    int selectedIndex = tViewMyEventsGuest.getSelectionModel().getSelectedIndex();
                    //Gets object builded in the class MyEventsDB -> Aamir explain
                    MyEventsDB selectedEvent = (MyEventsDB) tViewMyEventsGuest.getItems().get(selectedIndex);
                    //Save HostEmail and Date of event to variables, so we can use them to get the whole event
                    selectedHostEmail = selectedEvent.getHostEmail();
                    selectedDate = selectedEvent.getDate();

                    //Opening ShowEvent AnchorPane on eventclick
                    try {
                        mouseClickPaneChange();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        populateTViewHost();
        populateTViewGuest();
    }

}
