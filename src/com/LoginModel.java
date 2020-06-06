/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

import java.sql.*;

/**
 *
 * @author christianrittermadsen
 */
public class LoginModel {

    Connection connection;

    public LoginModel() {
        connection = SQLiteConnection.Connector();
        if (connection == null) {

            System.out.println("connection not successful");
            System.exit(1);
        }
    }

    public boolean isDbConnected() {
        try {
            return !connection.isClosed();
        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }
    }
//Creating variables to remeber user
    public static String emailLogin;
    public static String nameLogin;
    public static String surnameLogin;

    public boolean isLogin(String email, String pass) throws SQLException {

        String query = "select Name, Surname from UserInformation where "
                + "Email = ? and Password = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, pass);

            //Saves the name and surname of logged in variables: nameLogin and surnameLogin
            nameLogin = preparedStatement.executeQuery().getString("Name");
            surnameLogin = preparedStatement.executeQuery().getString("Surname");

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {

                //Save the logged in user's email into variable
                emailLogin = email;
                System.out.println(nameLogin);
                resultSet.close();
                return true;
                
            } else {
                return false;
            }
            
        } catch (Exception e) {
            return false;

        }
    }
}
