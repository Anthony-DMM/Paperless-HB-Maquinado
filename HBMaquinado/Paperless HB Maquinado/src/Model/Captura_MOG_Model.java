/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class Captura_MOG_Model {
    
    private DBConexion conexion;

    public Captura_MOG_Model(DBConexion conexion) {
        this.conexion = conexion;
    }
    
    public String validarLinea(String linea_produccion, String proceso){
        String supervisorAsignado = null;
        Connection con;
        con=conexion.conexionMySQL();
        try {
            CallableStatement cst = con.prepareCall("{call Supervisorname(?,?,?)}");
            cst.setString(1, linea_produccion);
            cst.setString(2, proceso);
            cst.registerOutParameter(3, java.sql.Types.VARCHAR);
            cst.executeQuery();
            supervisorAsignado=cst.getString(3);
            con.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "No se encontró ningún supervisor asignado");
            Logger.getLogger(Captura_MOG_Model.class.getName()).log(Level.SEVERE, null, ex);
        }
        return supervisorAsignado;
    }
}