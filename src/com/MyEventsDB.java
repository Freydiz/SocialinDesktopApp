/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;

/**
 *
 * @author Joana Cabral
 */
public class MyEventsDB {

    private final SimpleStringProperty hostEmailCol;
    private final SimpleStringProperty titleCol;
    private final SimpleStringProperty cityCol;
    private final SimpleStringProperty dateCol;
    private final SimpleStringProperty hostNameCol;
    private Button btnMoreCol;

    public MyEventsDB(String hostEmail, String title, String city, String date, String hostName) {
        this.hostEmailCol = new SimpleStringProperty(hostEmail);
        this.titleCol = new SimpleStringProperty(title);
        this.cityCol = new SimpleStringProperty(city);
        this.dateCol = new SimpleStringProperty(date);
        this.hostNameCol = new SimpleStringProperty(hostName);
        this.btnMoreCol = new Button ("Show more");
    }

    public String getHostEmail() {
        return hostEmailCol.get();
    }
    
    public void setHostEmail (String hostEmail) {
        hostEmailCol.set(hostEmail);
    }

    public String getTitle() {
        return titleCol.get();
    }
    
    public void setTitle (String title) {
        titleCol.set(title);
    }

    public String getCity() {
        return cityCol.get();
    }
    
    public void setCity (String city) {
        cityCol.set(city);
    }

    public String getDate() {
        return dateCol.get();
    }
    
    public void setDate (String date) {
        dateCol.set(date);
    }

    public String getHostName() {
        return hostNameCol.get();
    }
    
    public void setHostName (String hostName) {
        hostNameCol.set(hostName);
    }
    

 public void setMoreButton (Button btnMore) {
     this.btnMoreCol = btnMore;
 }
 
 public Button getMoreButton () {
        return null;
   //     return btnMore;
   // }
}}
