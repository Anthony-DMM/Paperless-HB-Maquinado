/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author ALEJANDRO LARES
 */
public class Conection {
    
    public java.sql.Connection conexionMySQL() {
        java.sql.Connection connectionDB = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connectionDB = DriverManager.getConnection("jdbc:mysql://192.168.155.16:3306" 
                    + "/rbppaperlessbush?user=adminpaperless&" + "password=paperless2018");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error cerrando " + e.getMessage());
        }
        return connectionDB;
    }
    
    public java.sql.Connection conexionMySQLPrueba() {
        java.sql.Connection connectionDB = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connectionDB = DriverManager.getConnection("jdbc:mysql://192.168.155.16:3306" 
                    + "/rbppaperlessbush?user=adminpaperless&" + "password=paperless2018");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error cerrando " + e.getMessage());
        }
        return connectionDB;
    }
    
    public java.sql.Connection conexionMySQL2() {
        java.sql.Connection connectionDB = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connectionDB = DriverManager.getConnection("jdbc:mysql://192.168.155.16:3306" 
                    + "/rbppaperlesshalfp?user=adminpaperless&" + "password=paperless2018");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error cerrando " + e.getMessage());
        }
        return connectionDB;
    }
    
    /*
    public java.sql.Connection conexionMySQL() {
        java.sql.Connection connectionDB = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connectionDB = DriverManager.getConnection("jdbc:mysql://localhost:3306" 
                    + "/rbppaperlessbush?user=root&" + "password=");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error cerrando " + e.getMessage());
        }
        return connectionDB;
    }
    */
}
