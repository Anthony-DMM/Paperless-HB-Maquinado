/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Interfaces.MOG;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
        
        // Sentencia de consulta en ERP
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
            if (ordenManufactura.contains("BHL")) {
                numeroParte = no.replace("-TB", "");
                numeroParte = numeroParte.trim();
                datosMOG.setNo_parte(numeroParte);
                
            } else {
                if (ordenManufactura.contains("BCO")) {
                    numeroParte = no.replace("-TCO", "");
                    numeroParte = numeroParte.trim();
                    datosMOG.setNo_parte(numeroParte);
                } else {
                    if (ordenManufactura.contains("BCH")) {
                        numeroParte = no.replace("-TCH", "");
                        numeroParte = numeroParte.trim();
                        datosMOG.setNo_parte(numeroParte);
                    } else {
                        if (ordenManufactura.contains("BGR")) {
                            numeroParte = no.replace("-TGR", "");
                            numeroParte = numeroParte.trim();
                            datosMOG.setNo_parte(numeroParte);
                        } else {
                            if (ordenManufactura.contains("PCK")) {
                                numeroParte = no.replace("-MX", "");
                                numeroParte = numeroParte.trim();
                                datosMOG.setNo_parte(numeroParte);
                            } else {
                                if (ordenManufactura.contains("ASL")) {
                                    numeroParte = no.replace("-MX", "");
                                    numeroParte = numeroParte.trim();
                                    datosMOG.setNo_parte(numeroParte);
                                }
                            }
                        }
                    }
                }
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
    
    
    /*public String validarLinea(String linea_produccion, String proceso){
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
            Logger.getLogger(Captura_Orden_Manufactura_Model.class.getName()).log(Level.SEVERE, null, ex);
        }
        return supervisorAsignado;
    }*/
}