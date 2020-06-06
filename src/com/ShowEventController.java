/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

import static com.LoginModel.emailLogin;
import static com.LoginModel.nameLogin;
import static com.LoginModel.surnameLogin;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import static com.SearchController.selectedHostEmail;
import static com.SearchController.selectedDate;
import java.sql.Statement;

/**
 * FXML Controller class
 *
 * @author Joana Cabral
 */
public class ShowEventController implements Initializable {

    Connection connection;
    private static int guests;

    //Connecting with our database:
    public ShowEventController() {
        connection = SQLiteConnection.Connector();
        if (connection == null) {

            System.out.println("connection not successful");
            System.exit(1);
        }
    }

    @FXML
    private AnchorPane anchorPaneShowEvent;

    @FXML
    private Button btnMyEvents;

    @FXML
    private Button btnProfile;

    @FXML
    private Button btnLogOut;

    @FXML
    private Button btnHome;

    @FXML
    private Button btnDeleteEvent;

    @FXML
    private Button btnBookEvent;

    @FXML
    private Button btnCancelBookEvent;

    @FXML
    private Label lblEventName;

    @FXML
    private Label lblHost;

    @FXML
    private Label lblPlace;

    @FXML
    private Label lblDateAndTime;

    @FXML
    private Label lblSeatsLeft;

    @FXML
    private Label lblDescription;

    @FXML
    private Label lblErrorMessage;

    @FXML
    private void btnMyEventsAction(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/com/MyEvents.fxml"));
        anchorPaneShowEvent.getChildren().setAll(pane);
    }

    @FXML
    private void btnProfileAction(ActionEvent event) throws IOException, SQLException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/com/Profile.fxml"));
        anchorPaneShowEvent.getChildren().setAll(pane);
    }

    @FXML
    private void btnHomeAction(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/com/Main.fxml"));
        anchorPaneShowEvent.getChildren().setAll(pane);
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

    public void getSelectedEvent() throws SQLException {

        //Make PreparedStatement and ResultSet variables, and select logged in user's data
        // takes the data from clicked event in SearchEvent and populates to ShowEvent
        SearchController search = new SearchController();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = "Select * from Events where HostEmail='" + selectedHostEmail + "' and Date ='" + selectedDate + "'";
        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            //Assign database values to variables 
            String name = resultSet.getString(1);
            String hostEmail = resultSet.getString(2);
            String address = resultSet.getString(3);
            String zipCode = resultSet.getString(4);
            String city = resultSet.getString(5);
            String date = resultSet.getString(6);
            String time = resultSet.getString(7);
            String nGuests = resultSet.getString(8);
            String hostName = resultSet.getString(9);
            String description = resultSet.getString(10);
            guests = getInt(nGuests);

            //put database values into TextFields
            lblHost.setText(hostName);
            lblEventName.setText(name);
            lblPlace.setText(address + ", " + zipCode + ", " + city);
            lblDateAndTime.setText(date + " at " + time);
            lblSeatsLeft.setText(guests - guestsAttending() + " out of " + guests);
            lblDescription.setText(description);
            resultSet.close();

        } catch (Exception e) {
        }

    }

    //method to convert string to integer
    public int getInt(String test) {
        try {
            return Integer.parseInt(test.trim());
        } catch (Exception e) {
            return 0;
        }
    }

    //gets the amount of guests attending one event
    private int guestsAttending() {
        int guests = 0;
        try {
            //checks the Events database and counts the amount of entries related to the selected event
            String queryCount = "select count() from Guests WHERE HostEmail = '"
                    + selectedHostEmail + "' AND Date = '" + selectedDate + "'";
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(queryCount);
            while (results.next()) {
                guests = results.getInt(1);
                results.close();
            }
        } catch (SQLException e) {
            return guests;
        }
        return guests;
    }

    private void showDeleteButton() throws IOException, SQLException {
        if (selectedHostEmail.equals(emailLogin)) {
            btnDeleteEvent.setVisible(true);
            btnCancelBookEvent.setVisible(false);
            btnBookEvent.setVisible(false);
        } else {
            btnDeleteEvent.setVisible(false);

            if (!bookingExists() && !oneEventPerDay() && !eventBooked()) {
                //Logged in user is NOT attending the event NOR has an event
                //the same day NOR the event is fully booked
                btnBookEvent.setVisible(true);
                btnCancelBookEvent.setVisible(false);

            } else if (bookingExists()) {
                //Logged in user is already attending the event
                btnBookEvent.setVisible(false);
                btnCancelBookEvent.setVisible(true);

            } else if (eventBooked()) {
                // Hides the buttons if fully booked
                btnBookEvent.setVisible(false);
                btnCancelBookEvent.setVisible(false);
                lblErrorMessage.setText("Event fully booked");
            } else if (oneEventPerDay()) {
                // Hides the buttons if fully booked
                btnBookEvent.setVisible(false);
                btnCancelBookEvent.setVisible(false);
                lblErrorMessage.setText("You already have an event this day");
            } 
        }
    }

    private boolean eventBooked() {
        System.out.println(guestsAttending() + " " + guests);
        if (guestsAttending() >= guests) {
            return true;
        } else {
            return false;
        }
    }

    //checks if the user already has an event for that date, hosted or attending 
    private boolean oneEventPerDay() {
        try {
            //checks events attending as guest -> Guests database
            String queryCond = "SELECT * FROM Guests WHERE  Date = '"
                    + selectedDate + "' AND GuestEmail = '" + emailLogin + "'";
            Statement preparedStatement;
            preparedStatement = connection.createStatement();
            ResultSet results = preparedStatement.executeQuery(queryCond);

            if (results.isAfterLast()) {
                try {
                    //checks events hosted -> Events database
                    String queryCond2 = "SELECT * FROM Events WHERE Date = '"
                            + selectedDate + "' AND HostEmail = '" + emailLogin + "'";
                    results = preparedStatement.executeQuery(queryCond2);

                    if (results.isAfterLast()) {
                        results.close();
                        return false;

                    } else {
                        results.close();
                        return true;
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                    results.close();
                    return false;
                }
            } else {
                return true;
            }
        } catch (SQLException e) {
            return false;
        }
    }

    // Checks if the Logged in user is already registered in the event
    private boolean bookingExists() throws IOException {
        try {
            String queryCond = "SELECT * FROM Guests WHERE HostEmail = '"
                    + selectedHostEmail + "' AND Date = '" + selectedDate
                    + "' AND GuestEmail = '" + emailLogin + "'";
            Statement preparedStatement;
            preparedStatement = connection.createStatement();
            ResultSet results = preparedStatement.executeQuery(queryCond);

            if (results.isAfterLast()) {
                results.close();
                return false;
            } else {
                results.close();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @FXML
    private void btnDeleteEventAction(ActionEvent event) throws Exception {
        //Establish connection
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        //Delete event which has HostEmail and SelectedDate
        String queryDelete = "DELETE from Events where HostEmail='" + selectedHostEmail + "' and Date ='" + selectedDate + "'";
        System.out.println(queryDelete);
        try {
            preparedStatement = connection.prepareStatement(queryDelete);
            preparedStatement.execute();

            AnchorPane pane = FXMLLoader.load(getClass().getResource("/com/MyEvents.fxml"));
            anchorPaneShowEvent.getChildren().setAll(pane);
            connection.close();
        } catch (Exception e) {
            lblErrorMessage.setText("Could not delete the event");
        }
    }

    @FXML
    private void btnBookEventAction(ActionEvent event) throws Exception {

        String guestName = nameLogin + " " + surnameLogin;

        //Establish connection
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        //Delete event which has HostEmail and SelectedDate
        String queryBook = "INSERT into Guests values(?,?,?,?)";
        System.out.println(queryBook);
        try {
            preparedStatement = connection.prepareStatement(queryBook);
            preparedStatement.setString(1, selectedHostEmail);
            preparedStatement.setString(2, selectedDate);
            preparedStatement.setString(3, emailLogin);
            preparedStatement.setString(4, guestName);

            //Setting system to tell you if data is inserted - and go back to Login screen:
            int i = preparedStatement.executeUpdate();

            if (i > 0) {
                AnchorPane pane = FXMLLoader.load(getClass().getResource("/com/MyEvents.fxml"));
                anchorPaneShowEvent.getChildren().setAll(pane);
                preparedStatement.close();
            } else {
                lblErrorMessage.setText("Something went wrong. Try again");
            }

        } catch (Exception e) {
        }
    }

    @FXML
    private void btnCancelBookAction(ActionEvent event) throws Exception {
        //Establish connection
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        //Delete event which has HostEmail and SelectedDate
        String queryDelete = "DELETE from Guests where GuestEmail ='" + emailLogin + "' AND HostEmail='" + selectedHostEmail + "' and Date ='" + selectedDate + "'";
        System.out.println(queryDelete);
        try {
            preparedStatement = connection.prepareStatement(queryDelete);
            preparedStatement.execute();
            AnchorPane pane = FXMLLoader.load(getClass().getResource("/com/MyEvents.fxml"));
            anchorPaneShowEvent.getChildren().setAll(pane);
            preparedStatement.close();
        } catch (Exception e) {
            System.out.println("Could not delete");
        }
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            getSelectedEvent();
            showDeleteButton();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(ShowEventController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
