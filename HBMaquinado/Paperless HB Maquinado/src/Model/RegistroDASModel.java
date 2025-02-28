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
        try (Connection con = conexion.conexionMySQL();
            CallableStatement cst = con.prepareCall("{call traerKeeper(?,?,?,?)}")){
            
            cst.setString(1, codigoSoporteRapido);
            cst.registerOutParameter(2, java.sql.Types.INTEGER);
            cst.setInt(3, proceso);
            cst.registerOutParameter(4, java.sql.Types.VARCHAR);
            cst.executeQuery();
            
            int valor = cst.getInt(2);
            String soporteEncontrado=cst.getString(4);
            
            if (valor == 0) {
                JOptionPane.showMessageDialog(null, "No se encontró ningún soporte rápido asignado");
                return false;
            } else {
                DAS datosDAS = DAS.getInstance();
                datosDAS.setSoporteRapido(soporteEncontrado);
                return true;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al obtener datos del soporte rápido", ex);
            throw ex;
        }
    }
    
    public boolean validarInspector(String codigoInspector) throws SQLException{
        int proceso = 1;
        try (Connection con = conexion.conexionMySQL();
            CallableStatement cst = con.prepareCall("{call traerInspector(?,?,?,?)}")){
            
            cst.setString(1, codigoInspector);
            cst.registerOutParameter(2, java.sql.Types.INTEGER);
            cst.setInt(3, proceso);
            cst.registerOutParameter(4, java.sql.Types.VARCHAR);
            cst.executeQuery();
            
            int valor = cst.getInt(2);
            String inspectorEncontrado=cst.getString(4);
            
            if (valor == 0) {
                JOptionPane.showMessageDialog(null, "No se encontró ningún inspector asignado");
                return false;
            } else {
                DAS datosDAS = DAS.getInstance();
                datosDAS.setInspector(inspectorEncontrado);
                return true;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al obtener datos del inspector", ex);
            throw ex;
        }
    }
    
    public boolean validarOperador(String numeroEmpleado) throws SQLException{
        int proceso = 1;
        try (Connection con = conexion.conexionMySQL();
            CallableStatement cst = con.prepareCall("{call traerOperador(?,?,?,?)}")){
            
            cst.setString(1, numeroEmpleado);
            cst.registerOutParameter(2, java.sql.Types.INTEGER);
            cst.setInt(3, proceso);
            cst.registerOutParameter(4, java.sql.Types.VARCHAR);
            cst.executeQuery();
            
            int valor = cst.getInt(2);
            String empleadoEncontrado=cst.getString(4);
            
            if (valor == 0) {
                JOptionPane.showMessageDialog(null, "No se encontró ningún empleado");
                return false;
            } else {
                DAS datosDAS = DAS.getInstance();
                datosDAS.setEmpleado(empleadoEncontrado);
                return true;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al obtener datos del empleado", ex);
            throw ex;
        }
    }
}