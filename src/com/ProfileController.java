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
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import static com.LoginModel.emailLogin;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

/**
 * FXML Controller class
 *
 * @author christianrittermadsen
 */
public class ProfileController implements Initializable {

    Connection connection;

//Connecting with our database:
    public ProfileController() {
        connection = SQLiteConnection.Connector();
        if (connection == null) {

            System.out.println("connection not successful");
            System.exit(1);
        }
    }

    @FXML
    private AnchorPane anchorPaneProfile;

    @FXML
    private Button btnMyEvents;

    @FXML
    private Button btnProfile;

    @FXML
    private Button btnLogOut;

    @FXML
    private Button btnHome;

    @FXML
    private Button btnSaveChanges;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtSurname;

    @FXML
    private TextField txtBirthDate;

    @FXML
    private TextField txtPhoneNumber;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtZipCode;

    @FXML
    private TextField txtCity;

    @FXML
    private PasswordField pswfChangePassword;

    @FXML
    private PasswordField pswfRepeatPassword;

    @FXML
    private Label lblErrorMessage;

    

    @FXML
    private void btnMyEventsAction(ActionEvent event) throws IOException {
    AnchorPane pane = FXMLLoader.load(getClass().getResource("/com/MyEvents.fxml"));
        anchorPaneProfile.getChildren().setAll(pane);
    }

    @FXML
    private void btnProfileAction(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/com/Profile.fxml"));
        anchorPaneProfile.getChildren().setAll(pane);
    }

    @FXML
    private void btnHomeAction(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/com/Main.fxml"));
        anchorPaneProfile.getChildren().setAll(pane);
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

    @FXML
    private void btnSearchEventAction(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/com/Search.fxml"));
        anchorPaneProfile.getChildren().setAll(pane);
    }

//Creating Connection and PreparedStatement objects:
    Connection conn = null;
    PreparedStatement preparedStatement = null;

//pick which user and which parameters should be selected:
    public void getActiveUser() throws SQLException {

//Make PreparedStatement and ResultSet variables, and select logged in user's data
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = "SELECT * FROM UserInformation where email='" + emailLogin + "'";
        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            //Assign database values to variables 
            String firstName = resultSet.getString(1);
            String surname = resultSet.getString(2);
            String birthDate = resultSet.getString(3);
            String phoneNumber = resultSet.getString(4);
            String email = resultSet.getString(5);
            String address = resultSet.getString(6);
            String zipCode = resultSet.getString(7);
            String city = resultSet.getString(8);
            String password = resultSet.getString(9);

            //put database values into TextFields
            txtName.setText(firstName);
            txtSurname.setText(surname);
            txtBirthDate.setText(birthDate);
            txtPhoneNumber.setText(phoneNumber);
            txtEmail.setText(email);
            txtAddress.setText(address);
            txtZipCode.setText(zipCode);
            txtCity.setText(city);
            pswfChangePassword.setText(password);
            pswfRepeatPassword.setText(password);
            resultSet.close();
        } catch (Exception e) {
        }

    }
    
    @FXML
    private void btnSaveAction(ActionEvent event) throws SQLException, IOException {

        //Getting text from TextFields:
        String Name = txtName.getText();
        String Surname = txtSurname.getText();
        String BirthDate = txtBirthDate.getText();
        String PhoneNumber = txtPhoneNumber.getText();
        String Email = txtEmail.getText();
        String Address = txtAddress.getText();
        String ZipCode = txtZipCode.getText();
        String City = txtCity.getText();
        String ChangePassword = String.valueOf(pswfChangePassword.getText());
        String RepeatPassword = String.valueOf(pswfRepeatPassword.getText());

        System.out.println(txtName.getText());
        Connection conn = null;
        PreparedStatement preparedStatement = null;

        if (Objects.equals(ChangePassword, RepeatPassword)) {

            String query = "UPDATE UserInformation SET Name = '" + txtName.getText() + 
                    "', Surname = '" + txtSurname.getText() + "', BirthDate = '" + txtBirthDate.getText() + 
                    "', PhoneNumber = '" + txtPhoneNumber.getText() + "', Email = '" + txtEmail.getText() + 
                    "', Address = '" + txtAddress.getText() + "', ZipCode = '" + txtZipCode.getText() + 
                    "', City = '" + txtCity.getText() + "', Password = '" + pswfChangePassword.getText() + 
                    "' WHERE Email = '" + emailLogin + "'";
            
//"UPDATE * UserInformation where email='" + emailLogin + "'";
            try {
                preparedStatement = connection.prepareStatement(query);

                //Setting system to tell you if data is inserted - and go back to Login screen:
                int i = preparedStatement.executeUpdate();

                if (i > 0) {
                    lblErrorMessage.setText("Data is saved");

                } else {
                    lblErrorMessage.setText("Data is not saved");
                }
                preparedStatement.close();
            } catch (SQLException e) {
                lblErrorMessage.setText("Something went wrong. Try again");
            }
        } else {
            lblErrorMessage.setText("Passwords not matching");
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
            getActiveUser();
        } catch (SQLException ex) {
            Logger.getLogger(ProfileController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
