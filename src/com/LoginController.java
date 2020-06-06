/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author christianrittermadsen
 */
public class LoginController implements Initializable {

    LoginModel loginModel = new LoginModel();

    @FXML
    private Hyperlink hplSignUp;

    @FXML
    private Label lblSocialin;

    @FXML
    private Label lblIsConnected;

    @FXML
    public Label lblErrorMessage;

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtPassword;

    public void setLabelText() {
        lblErrorMessage.setText("User created successfully!");
        //hplSignUp.setOnAction(null);
    }

    public void btnLoginAction(ActionEvent event) throws SQLException, IOException {

        if (loginModel.isLogin(txtEmail.getText(), txtPassword.getText())) {

//isConnected.setText("Username and password is correct");) {
            ((Node) (event.getSource())).getScene().getWindow().hide();
            Parent parent = FXMLLoader.load(getClass().getResource("/com/Main.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.setTitle("Socialin'");
            stage.show();

        } else {
            lblErrorMessage.setText("Invalid Email or Password");

        }

    }

    public void hplSignUpAction(ActionEvent event) throws IOException {
        ((Node) (event.getSource())).getScene().getWindow().hide();
        Parent parent = FXMLLoader.load(getClass().getResource("/com/CreateUser.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.setTitle("Create User");
        stage.show();

    }

    /**
     * Initializes the controller class.
     *
     */
    //@Override
    //public void initialize(URL url, ResourceBundle rb) {
    // TODO
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        if (loginModel.isDbConnected()) {
            lblIsConnected.setText("Login");
        } else {

            lblIsConnected.setText("Not Connected");
        }
    }
}
