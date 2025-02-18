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
    
    private DBConexion conexion;

    public Autenticacion_Model(DBConexion conexion) {
        this.conexion = conexion;
    }
    
    public String validar_supervisor(String codigoSupervisor){
        String areaSupervisor = null;
        Connection con;
        con=conexion.conexionMySQL();
        try {
            CallableStatement cst = con.prepareCall("{call login(?,?,?)}");
            cst.setString(1, codigoSupervisor);
            cst.registerOutParameter(2, java.sql.Types.INTEGER);
            cst.registerOutParameter(3, java.sql.Types.VARCHAR);
            cst.executeQuery();
            areaSupervisor=cst.getString(3);
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Autenticacion_Model.class.getName()).log(Level.SEVERE, null, ex);
        }
        return areaSupervisor;
    }
}