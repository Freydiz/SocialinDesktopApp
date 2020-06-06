/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import static com.LoginModel.emailLogin;
import javafx.scene.control.TextArea;
import static com.LoginModel.nameLogin;
import static com.LoginModel.surnameLogin;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;

/**
 * FXML Controller class
 *
 * @author christianrittermadsen
 */
public class CreateEventController implements Initializable {

    Connection connection;

    //Connecting with our database:
    public CreateEventController() {
        connection = SQLiteConnection.Connector();
        if (connection == null) {

            System.out.println("connection not successful");
            System.exit(1);
        }
    }

    @FXML
    private AnchorPane anchorPaneCreateEvent;

    @FXML
    private Button btnHome;

    @FXML
    private Button btnMyEvents;

    @FXML
    private Button btnProfile;

    @FXML
    private Button btnLogOut;

    @FXML
    private Button btnFinishEvent;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtGuests;

    @FXML
    private TextField txtDate;

    @FXML
    private TextField txtTime;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtZipCode;

    @FXML
    private TextField txtCity;

    @FXML
    private TextArea txtDescription;

    @FXML
    private Label lblErrorMessage;

    //checks if the user already has an event for that date, hosted or attending 
    private boolean oneEventPerDay(String Date) throws IOException, SQLException {
        try {
            //checks events hosted -> Events database
            String queryCond = "SELECT * FROM Events WHERE Date = \"" + Date
                    + "\" AND HostEmail = \"" + emailLogin + "\"";
            Statement preparedStatement;
            preparedStatement = connection.createStatement();
            ResultSet results = preparedStatement.executeQuery(queryCond);

            if (results.isAfterLast()) {
                try {
                    //checks events attending as guest -> Guests database
                    String queryCond2 = "SELECT * FROM Guests WHERE Date = '"
                            + Date + "' AND GuestEmail = '" + emailLogin + "'";
                    results = preparedStatement.executeQuery(queryCond2);

                    if (results.isAfterLast()) {
                        return false;
                    } else {
                        return true;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    return false;
                }
            } else {
                return true;
            }

        } catch (SQLException e) {
            return false;
        }
    }

    @FXML
    private void btnMyEventsAction(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/com/MyEvents.fxml"));
        anchorPaneCreateEvent.getChildren().setAll(pane);
    }

    @FXML
    private void btnProfileAction(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/com/Profile.fxml"));
        anchorPaneCreateEvent.getChildren().setAll(pane);
    }

    @FXML
    private void btnHomeAction(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/com/Main.fxml"));
        anchorPaneCreateEvent.getChildren().setAll(pane);
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

    private int getInt() {
        try {
            return Integer.parseInt(txtGuests.getText().trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @FXML
    private void btnFinishEventAction(ActionEvent event) throws IOException, SQLException {
        //Getting text from TextFields:
        String Name = txtName.getText();
        String HostEmail = emailLogin;
        String Address = txtAddress.getText();
        String ZipCode = txtZipCode.getText();
        String City = txtCity.getText();
        String Date = txtDate.getText();
        String Time = txtTime.getText();
        String NGuests = txtGuests.getText();
        String HostName = nameLogin + " " + surnameLogin;
        String Description = txtDescription.getText();

        //Creating Connection and PreparedStatement objects:
        Connection conn = null;
        PreparedStatement preparedStatement = null;

        //If statement for matching Password and ´Repeat´password field, and already existing email:
        if (!oneEventPerDay(Date)) {

            //Inserting input from TextFields to database table:
            String query = "insert into Events values(?,?,?,?,?,?,?,?,?,?)";
            try {
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, Name);
                preparedStatement.setString(2, HostEmail);
                preparedStatement.setString(3, Address);
                preparedStatement.setString(4, ZipCode);
                preparedStatement.setString(5, City);
                preparedStatement.setString(6, Date);
                preparedStatement.setString(7, Time);
                preparedStatement.setString(8, NGuests);
                preparedStatement.setString(9, HostName);
                preparedStatement.setString(10, Description);

                //Setting system to tell you if data is inserted - and go back to Login screen:
                int i = preparedStatement.executeUpdate();

                if (i > 0) {
                    lblErrorMessage.setText("Data is saved");
                    AnchorPane pane = FXMLLoader.load(getClass().getResource("/com/MyEvents.fxml"));
                    anchorPaneCreateEvent.getChildren().setAll(pane);
                } else {
                    lblErrorMessage.setText("Data is not saved");
                }
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            lblErrorMessage.setText("Already attending an event on this date");
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

    }

}
