/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class DBConexion {

    Connection con = null, cone = null;

    public Connection conexionMySQL() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://192.168.155.16:3306"
                    + "/rbppaperlesshalfpr?user=adminpaperless"
                    + "&password=paperless2018");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error de conexión");
            JOptionPane.showMessageDialog(null, "Error de conexion " + e);
        }
        return con;
    }

    public Connection oracle() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            cone = DriverManager.getConnection("jdbc:oracle:thin:@192.168.155.11:1521:ERPLNFP7", "DMMROU", "DMM");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error de conexion");
            JOptionPane.showMessageDialog(null, "Error de conexion " + e);
        }
        return cone;
    }
}
