/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author christianrittermadsen
 */
public class SQLiteConnection {
    public static Connection Connector() {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:Socialin Database.sqlite");
            return conn;
        } catch (Exception e) {
            System.out.println(e);
            return null;

        }
    }
}
