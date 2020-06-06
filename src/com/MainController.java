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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author christianrittermadsen
 */
public class MainController implements Initializable {

    @FXML
    private AnchorPane anchorPaneMain;

    @FXML
    private Button btnMyEvents;

    @FXML
    private Button btnProfile;

    @FXML
    private Button btnLogOut;

    @FXML
    private TextField txtSearchEvent;
    
    @FXML
    private Button btnSearchEvent;
    
    @FXML
    private Label lblOr;

    @FXML
    private Button btnCreateEvent;

    @FXML
    private TextField txtSearchDate;

    @FXML
    private Button btnHome;
    
    
    //Button funtions:
    
    @FXML
    private void btnMyEventsAction(ActionEvent event) throws IOException {
    AnchorPane pane = FXMLLoader.load(getClass().getResource("/com/MyEvents.fxml"));
        anchorPaneMain.getChildren().setAll(pane);
    }

    @FXML
    private void btnProfileAction(ActionEvent event) throws IOException, SQLException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/com/Profile.fxml"));
        anchorPaneMain.getChildren().setAll(pane);
    }

    @FXML
    private void btnHomeAction(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/com/Main.fxml"));
        anchorPaneMain.getChildren().setAll(pane);
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
         SearchController search = new SearchController();
        search.textSearch = txtSearchEvent.getText();
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/com/Search.fxml"));
        anchorPaneMain.getChildren().setAll(pane);
    }

    @FXML
    private void btnCreateEventAction(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/com/CreateEvent.fxml"));
        anchorPaneMain.getChildren().setAll(pane);
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
