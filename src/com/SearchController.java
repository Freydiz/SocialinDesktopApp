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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author christianrittermadsen
 */
public class SearchController implements Initializable {
// creating variables to store HostEmail and date of event from clicked events

    public static String selectedHostEmail;
    public static String selectedDate;
    //Making Connection object
    Connection connection;

    //Connecting with our database:
    public SearchController() {
        connection = SQLiteConnection.Connector();
        if (connection == null) {

            System.out.println("connection not successful");
            System.exit(1);
        }
    }

    @FXML
    private AnchorPane anchorPaneSearch;

    @FXML
    private Button btnMyEvents;

    @FXML
    private Button btnHome;

    @FXML
    private Button btnProfile;

    @FXML
    private Button btnLogOut;

    @FXML
    private Button btnSearchEvent;

    @FXML
    private TextField txtSearchEvent;

    @FXML
    private TextField txtSearchDate;

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
    private void btnMyEventsAction(ActionEvent event) throws IOException {
AnchorPane pane = FXMLLoader.load(getClass().getResource("/com/MyEvents.fxml"));
        anchorPaneSearch.getChildren().setAll(pane);
    }

    @FXML
    private void btnProfileAction(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/com/Profile.fxml"));
        anchorPaneSearch.getChildren().setAll(pane);
    }

    @FXML
    private void btnHomeAction(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/com/Main.fxml"));
        anchorPaneSearch.getChildren().setAll(pane);
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

    //Variable that saves the search text input to be used in getEventSearch()
    public static String textSearch;

    @FXML
    private void btnSearchEventAction(ActionEvent event) throws IOException {
        //Getting text from Search field
        textSearch = txtSearchEvent.getText();
        //Runs initialize() again -> new search:
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/com/Search.fxml"));
        anchorPaneSearch.getChildren().setAll(pane);
    }

    //Method to change pane to ShowEvent
    private void mouseClickPaneChange() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/com/ShowEvent.fxml"));
        anchorPaneSearch.getChildren().setAll(pane);
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Connecting TableColumns to values in MyEventsDB class
        colName.setCellValueFactory(new PropertyValueFactory<MyEventsDB, String>("title"));
        colCity.setCellValueFactory(new PropertyValueFactory<MyEventsDB, String>("city"));
        colDate.setCellValueFactory(new PropertyValueFactory<MyEventsDB, String>("date"));
        colHostName.setCellValueFactory(new PropertyValueFactory<MyEventsDB, String>("hostName"));

        //Populating the TableView with the search results
        tViewMyEvents.getItems().setAll(getEventsSearch());

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

    //Function that gets String value from txtSearchEvent and retrieves the values from the Events table in the database
    public List<MyEventsDB> getEventsSearch() {
        List ll = new LinkedList();
        Statement st;
        ResultSet rs;
        String recordQuery;
        try {
            st = connection.createStatement();
            if (textSearch != "") {
                recordQuery = "Select * from Events where City LIKE '%" + textSearch + "%' OR HostName LIKE '%" + textSearch + "%' OR Name LIKE '%" + textSearch + "%' OR Date LIKE '%" + textSearch + "%'";
            } else {
                recordQuery = "Select * from Events";
            }
            rs = st.executeQuery(recordQuery);
            while (rs.next()) {
                String hostEmail = rs.getString("HostEmail");
                String name = rs.getString("Name");
                String city = rs.getString("City");
                String date = rs.getString("Date");
                String hostName = rs.getString("HostName");
                ll.add(new MyEventsDB(hostEmail, name, city, date, hostName));
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(MyEventsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ll;
    }

}
