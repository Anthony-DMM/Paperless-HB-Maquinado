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
        int valor;
        String proceso_obtenido = null;
        String supervisor_obtenido = null;

        try (Connection con = conexion.conexionMySQL();
            CallableStatement cst = con.prepareCall("{call login(?,?,?,?)}")) {
            cst.setString(1, codigo_supervisor);
            cst.registerOutParameter(2, java.sql.Types.INTEGER);
            cst.registerOutParameter(3, java.sql.Types.VARCHAR);
            cst.registerOutParameter(4, java.sql.Types.VARCHAR);
            cst.execute();
            valor = cst.getInt(2);
            proceso_obtenido = cst.getString(3);
            supervisor_obtenido = cst.getString(4);
            
            if (valor==0) {
                JOptionPane.showMessageDialog(null, "No se encontró ningún supervisor asignado");
                return false;
            } else if (!proceso_obtenido.equals(LineaProduccion.getInstance().getProceso())){ 
                JOptionPane.showMessageDialog(null, "El supervisor no pertenece al área de MAQUINADO");
            } else {
                LineaProduccion lineaProduccion = LineaProduccion.getInstance();
                lineaProduccion.setSupervisor(supervisor_obtenido);
                return true;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al validar el supervisor: " + ex.getMessage());
            Logger.getLogger(Captura_Orden_Manufactura_Model.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
        return false;
    }
}