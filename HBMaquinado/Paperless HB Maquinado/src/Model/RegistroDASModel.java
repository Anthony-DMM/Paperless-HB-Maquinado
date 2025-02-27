/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Entities.DAS;
import Utils.MostrarMensaje;
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
public class RegistroDASModel {
    private static final Logger LOGGER = Logger.getLogger(CapturaOrdenManufacturaModel.class.getName());
    private DBConexion conexion;

    public RegistroDASModel(DBConexion conexion) {
        this.conexion = conexion;
    }
    
    public boolean validarSoporteRapido(String codigoSoporteRapido) throws SQLException{
        int proceso = 1;
        Connection con;
        con=conexion.conexionMySQL();
        try {
            CallableStatement cst = con.prepareCall("{call traerKeeper(?,?,?,?)}");
            cst.setString(1, codigoSoporteRapido);
            cst.registerOutParameter(2, java.sql.Types.INTEGER);
            cst.setInt(3, proceso);
            cst.registerOutParameter(4, java.sql.Types.VARCHAR);
            cst.executeQuery();
            
            String soporteEncontrado=cst.getString(4);
            if(soporteEncontrado==null){
                return false;
            }
            
            DAS datosDAS = DAS.getInstance();
            datosDAS.setSoporteRapido(soporteEncontrado);
            
            con.close();
            return true;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al obtener datos de la orden", ex);
            throw ex;
        }
    }
}
