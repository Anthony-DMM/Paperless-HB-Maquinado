/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Entities.DAS;
import Entities.LineaProduccion;
import Entities.MOG;
import Entities.RBP;
import Utils.FechaHora;
import Utils.MostrarMensaje;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
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
    FechaHora fechaHora = new FechaHora();

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
    
    public void registrarPiezasPorHora(String numero_empleado, int acumulado, String calidad) throws SQLException, ParseException {
        MOG datosMOG = MOG.getInstance();
        LineaProduccion lineaProduccion = LineaProduccion.getInstance();
        
        String fecha = fechaHora.fechaActual("yyyy-MM-dd");
        Date fechaUtil = fechaHora.stringToDate(fecha, "yyyy-MM-dd");
        java.sql.Date fechaF = new java.sql.Date(fechaUtil.getTime());
        String hora = fechaHora.horaActual();

        try (Connection con = conexion.conexionMySQL();
             CallableStatement cst = con.prepareCall("{call registro_x_hora_maq(?,?,?,?,?,?,?,?,?)}")) {

            cst.setString(1, datosMOG.getMog());
            cst.setString(2, datosMOG.getOrden_manufactura());
            cst.setString(3, numero_empleado);
            cst.setString(4, hora);
            cst.setInt(5, acumulado);
            cst.setInt(6, acumulado);
            cst.setString(7, calidad);
            cst.setDate(8, fechaF);
            cst.setString(9, lineaProduccion.getLinea());

            cst.executeQuery();
            obtener_piezas_procesadas_hora();
            JOptionPane.showMessageDialog(null, "LAS PIEZAS PROCESADAS EN LA HORA: " + hora + " FUERON REGISTRADAS CON ÉXITO");
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al registrar la producción por hora", ex);
            throw ex;
        }
    }
    
    public void obtener_piezas_procesadas_hora() throws SQLException {
        try (Connection con = conexion.conexionMySQL();
             CallableStatement cst = con.prepareCall("{call obtener_piezas_x_hora_maq(?)}")) {

            RBP datosRBP = RBP.getInstance();
            cst.setInt(1, datosRBP.getId()); // Establecer el parámetro de entrada

            // Ejecutar la consulta y obtener el ResultSet
            try (ResultSet r = cst.executeQuery()) {
                // Obtener metadatos del ResultSet
                ResultSetMetaData metaData = r.getMetaData();
                int columnCount = metaData.getColumnCount(); // Número de columnas

                // Recorrer el ResultSet e imprimir los resultados
                while (r.next()) {
                    // Iterar sobre cada columna
                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = metaData.getColumnName(i); // Nombre de la columna
                        Object columnValue = r.getObject(i); // Valor de la columna

                        // Imprimir el nombre y el valor de la columna
                        System.out.println(columnName + ": " + columnValue);
                    }
                    System.out.println("-----------------------------"); // Separador entre filas
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al obtener la producción por hora", ex);
            throw ex;
        }
    }
}