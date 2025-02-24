/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Interfaces.LineaProduccion;
import Interfaces.MOG;
import Utils.FechaHora;
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
    
    public boolean obtenerDatosOrden(String ordenManufactura) throws SQLException {
        String art = "HB";
        String no = null;
        String numeroParte = null;
        int id;
        String procesoValido = "HBL";
        boolean ordenEncontrada = false;
        MOG datosMOG = MOG.getInstance();
        LineaProduccion lineaProduccion = LineaProduccion.getInstance();
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
            ordenEncontrada = true;
            no = res.getString(1);
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
            datosMOG.setTm(res.getString(10));
            
            String estandar = res.getString(9);
            String estandarSinColor = estandar.replace("Café", "").replace("CAFE", "").replace("Rojo", "").replace("Azul", "")
                    .replace("Verde", "").replace("Rosa", "").replace("Amarillo", "").replace("Negro", "")
                    .replace("Cafe", "").replace("CAFÉ", "").replace("ROJO", "").replace("AZUL", "").replace("VERDE", "")
                    .replace("ROSA", "").replace("AMARILLO", "").replace("NEGRO", "").replace("YELLOW", "")
                    .replace("BROWN", "").replace("GREEN", "").replace("MORADO", "").replace("BLUE", "")
                    .replace("BLANCO", "").replace("PURPLE", "").replace("Purple", "").replace("Blue", "")
                    .replace("PINK", "").replace("Pink", "").replace("White", "").replace("WHITE", "").replace("RED", "")
                    .replace("BLACK", "").replace("Black", "").replace("Blanca", "").replace("BLANCA", "")
                    .replace("Morado", "").replace("-", "");
            
            datosMOG.setStd(estandarSinColor.trim());
        }
        
        if (!ordenEncontrada) {
            JOptionPane.showMessageDialog(null, "No se encontró la orden de manufactura");
        } else if (numeroParte == null) {
            JOptionPane.showMessageDialog(null, "La orden de manufactura no pertenece al proceso de MAQUINADO");
            ordenEncontrada = false;
        } else {
            CallableStatement cst = con.prepareCall("{call llenarMog(?,?,?,?,?,?,?,?,?,?)}");
            cst.setString(1, datosMOG.getMog());
            cst.setString(2, art + " " + datosMOG.getModelo());
            cst.setString(3, datosMOG.getNo_dibujo());
            cst.setString(4, numeroParte);
            cst.setString(5, datosMOG.getModelo());
            cst.setString(6, datosMOG.getStd());
            cst.setInt(7, datosMOG.getCantidad_planeada());
            cst.registerOutParameter(8, java.sql.Types.INTEGER);
            cst.setDouble(9, datosMOG.getPeso());
            cst.setDouble(10, 1.1);
            cst.execute();
            id = cst.getInt(8);

            CallableStatement cs = con.prepareCall("{call llenarRBP(?,?,?,?)}");
            cs.setString(1, datosMOG.getOrden_manufactura());
            cs.setString(2, lineaProduccion.getProceso());
            cs.setInt(3, id);
            cs.setInt(4, datosMOG.getSequ());
            cs.execute();

            CallableStatement cs1 = con.prepareCall("{call actualizarTM(?,?)}");
            cs1.setString(1, datosMOG.getOrden_manufactura());
            cs1.setString(2, datosMOG.getTm());
            cs1.execute();
            
            CallableStatement cst2 = con.prepareCall("call insertarCorriendo(?,?,?,?,?)");
            cst2.setString(1, datosMOG.getOrden_manufactura());
            cst2.setString(2, datosMOG.getMog());
            cst2.setString(3, FechaHora.horaActual());
            cst2.setString(4, FechaHora.fechaActual());
            cst2.setString(5, lineaProduccion.getLinea());
            cst2.execute();
        }
        
        con.close();
        cone.close();
        
        return ordenEncontrada;
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