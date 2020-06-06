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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author christianrittermadsen
 */
public class CreateUserController implements Initializable {

    Connection connection;

//Connecting with our database:
    public CreateUserController() {
        connection = SQLiteConnection.Connector();
        if (connection == null) {

            System.out.println("connection not successful");
            System.exit(1);
        }
    }

    @FXML
    private AnchorPane anchorPaneProfile;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtSurname;

    @FXML
    private TextField txtBirthDay;

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
    private PasswordField pswfPassword;

    @FXML
    private PasswordField pswfRepeatPassword;

    @FXML
    private Label lblErrorMessage;

//Button for going back to login screen:
    @FXML
    private void btnBackAction(ActionEvent event) throws IOException {
        ((Node) (event.getSource())).getScene().getWindow().hide();
        Parent parent = FXMLLoader.load(getClass().getResource("/com/Login.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.show();
    }

//If new user's exists in database - returns true. If not - returns false
    private boolean emailExists(String Email) throws IOException {
        ResultSet results;
        try {
            String query2 = "SELECT Email FROM UserInformation WHERE Email = \"" + Email + "\"";
            Statement preparedStatement;
            preparedStatement = connection.createStatement();
            results = preparedStatement.executeQuery(query2);

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
//Button for finishing creating a new user:

    @FXML
    private void btnCreateUserAction(ActionEvent event) throws IOException {

        //Getting text from TextFields:
        String Name = txtName.getText();
        String Surname = txtSurname.getText();
        String BirthDate = txtBirthDay.getText();
        String PhoneNumber = txtPhoneNumber.getText();
        String Email = txtEmail.getText();
        String Address = txtAddress.getText();
        String ZipCode = txtZipCode.getText();
        String City = txtCity.getText();
        String Password = String.valueOf(pswfPassword.getText());
        String RepeatPassword = String.valueOf(pswfRepeatPassword.getText());

        //Creating Connection and PreparedStatement objects:
        Connection conn = null;
        PreparedStatement preparedStatement = null;

        //If statement for matching Password and ´Repeat´password field, and already existing email:
        if (Objects.equals(Password, RepeatPassword) && !emailExists(Email)) {

            //Inserting input from TextFields to database table:
            String query = "insert into UserInformation values(?,?,?,?,?,?,?,?,?)";
            try {
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, Name);
                preparedStatement.setString(2, Surname);
                preparedStatement.setString(3, BirthDate);
                preparedStatement.setString(4, PhoneNumber);
                preparedStatement.setString(5, Email);
                preparedStatement.setString(6, Address);
                preparedStatement.setString(7, ZipCode);
                preparedStatement.setString(8, City);
                preparedStatement.setString(9, Password);

                //Setting system to tell you if data is inserted - and go back to Login screen:
                int i = preparedStatement.executeUpdate();

                if (i > 0) {
                    System.out.println("Data is saved");
                    ((Node) (event.getSource())).getScene().getWindow().hide();
                    Parent parent = FXMLLoader.load(getClass().getResource("/com/Login.fxml"));
                    Stage stage = new Stage();
                    Scene scene = new Scene(parent);

                    stage.setScene(scene);
                    stage.setTitle("Login");
                    stage.show();
                    

                } else {
                    lblErrorMessage.setText("Data is not saved");
                }
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } else if (emailExists(Email)) {
            lblErrorMessage.setText("Email already exists in system");
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
        // TODO
    }
}
