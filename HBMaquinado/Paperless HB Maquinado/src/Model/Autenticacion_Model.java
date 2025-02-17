/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import View.Autenticacion;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class Autenticacion_Model {
    public String validar_supervisor(String user, Autenticacion autenticacion, DBConexion conexion){
        Connection con;
        con=conexion.conexionMySQL();
        try {
            PreparedStatement pst = con.prepareStatement("SELECT * FROM empleado WHERE codigo = ? AND tipo_usuario = ?");
            pst.setString(1, user);
            pst.setString(2, "Supervisor");  
            pst.executeQuery();
            System.out.println(pst);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                System.out.println("Acceso concedido");
            } else {
                System.out.println("Acceso denegado");
            }
            autenticacion.txt_codigo_supervisor.setText(null);
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Autenticacion_Model.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
