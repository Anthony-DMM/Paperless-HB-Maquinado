/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Interfaces.LineaProduccion;
import Interfaces.MOG;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class Captura_Orden_Manufactura_Model {
    
    private DBConexion conexion;

    public Captura_Orden_Manufactura_Model(DBConexion conexion) {
        this.conexion = conexion;
    }
    
    public void obtenerDatosOrden(String ordenManufactura) throws SQLException {
        String art = "HB";
        Statement sen;
        Connection con, cone;
        ResultSet res;
        con = conexion.conexionMySQL();
        cone = conexion.oracle();
        sen = cone.createStatement();
        
        // Consulta en ERP
        sen.executeUpdate("ALTER SESSION SET CURRENT_SCHEMA = BAANLN");
        res = sen.executeQuery("SELECT ttcibd001500.T$ITEM, ttidms602500.T$RPNO,ttcibd001500.T$SEAK,\n"
                + "ttidms602500.T$PDNO,ttidms602500.T$OQTY,ttcibd001500.T$SEAB, ttcibd001500.T$WGHT,"
                + "ttidms602500.T$SEQN, ttcibd001500.t$dscc, ttidms602500.T$CLOT\n"
                + "FROM ttcibd001500 INNER JOIN ttidms602500 ON ttidms602500.T$ITEM=ttcibd001500.T$ITEM \n"
                + "WHERE ttidms602500.T$PDNO='" + ordenManufactura + "'");
        
        while (res.next()) {
            
            MOG datosMOG = MOG.getInstance();
            String no = res.getString(1);
            String numeroParte = null;
            if (ordenManufactura.contains("HBL")) {
                numeroParte = no.replace("-TH", "");
                numeroParte = numeroParte.trim();
                datosMOG.setNo_parte(numeroParte);
                
            } else {
                datosMOG.setNo_parte(numeroParte);
            }
            
            datosMOG.setMog(res.getString(2));
            datosMOG.setModelo(res.getString(3));
            datosMOG.setOrden_manufactura(res.getString(4));
            datosMOG.setCantidad_planeada(res.getInt(5));
            datosMOG.setNo_dibujo(res.getString(6));
            datosMOG.setPeso(res.getDouble(7));
            datosMOG.setSequ(res.getInt(8));
            datosMOG.setStd(res.getString(9));
            datosMOG.setTm(res.getString(10));
        }
        
        con.close();
        cone.close();
    }
    
    public boolean validarSupervisor(String codigo_supervisor) throws SQLException {
        String linea_produccion = LineaProduccion.getInstance().getLinea();
        String linea_obtenida = null, supervisor_obtenido = null;

        try (Connection con = conexion.conexionMySQL();
             CallableStatement cst = con.prepareCall("{call ObtenerSupervisorAsignado(?,?,?,?,?)}")) {

            // Establecer parámetros de entrada
            cst.setString(1, codigo_supervisor);
            cst.setString(2, linea_produccion);

            // Registrar parámetros de salida
            cst.registerOutParameter(3, java.sql.Types.VARCHAR); // nombre_completo
            cst.registerOutParameter(4, java.sql.Types.VARCHAR); // codigo_maquina
            cst.registerOutParameter(5, java.sql.Types.VARCHAR); // nombre_work_center

            // Ejecutar el procedimiento almacenado
            cst.execute();

            // Obtener los valores de los parámetros de salida
            supervisor_obtenido = cst.getString(3);
            linea_obtenida = cst.getString(4);

            // Verificar si los valores son nulos
            if (supervisor_obtenido == null || linea_obtenida == null) {
                JOptionPane.showMessageDialog(null, "No se encontró ningún supervisor asignado");
                return false; // La consulta no es válida
            }

            // Comparar los valores obtenidos
            if (!linea_obtenida.equals(linea_produccion)) {
                JOptionPane.showMessageDialog(null, "El supervisor pertenece a otra línea de producción");
                return false; // La consulta no es válida
            } else if (!supervisor_obtenido.equals(LineaProduccion.getInstance().getSupervisor())) {
                JOptionPane.showMessageDialog(null, "El supervisor no coincide con el esperado");
                return false; // La consulta no es válida
            } else {
                // La consulta es válida
                JOptionPane.showMessageDialog(null, "Supervisor válido");
                // Actualizar la instancia de LineaProduccion con el supervisor obtenido
                LineaProduccion.getInstance().setSupervisor(supervisor_obtenido);
                return true; // La consulta es válida
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al validar el supervisor: " + ex.getMessage());
            Logger.getLogger(Captura_Orden_Manufactura_Model.class.getName()).log(Level.SEVERE, null, ex);
            throw ex; // Relanzar la excepción para manejarla en el controlador
        }
    }
}