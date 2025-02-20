/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Interfaces.MOG;
import View.Cambio_MOG;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class Captura_MOG_Model {
    
    public Cambio_MOG cambio_MOG;
    public MOG mog;
    private DBConexion conexion;

    public Captura_MOG_Model(DBConexion conexion) {
        this.conexion = conexion;
        this.mog = mog;
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
        
        // Inicializar mog ANTES del bucle while
        this.mog = MOG.builder()
                .build();

        while (res.next()) {
            // Usar setters directamente en el objeto mog
            this.mog.setMog(res.getString(2));
            this.mog.setModelo(res.getString(3));
            this.mog.setOrden_manufactura(res.getString(4));
            this.mog.setCantidad_planeada(res.getInt(5));
            this.mog.setNo_dibujo(res.getString(6));
            this.mog.setPeso(res.getDouble(7));
            this.mog.setSequ(res.getInt(8));
            this.mog.setStd(res.getString(9));
            this.mog.setTm(res.getString(10));
        }

        // Verificar si se encontraron resultados DESPUÉS del bucle while
        if (this.mog.getMog() != null) { // O cualquier otro campo obligatorio de MOG
            // Se encontraron resultados, mog está listo para usarse
        } else {
            // No se encontraron resultados
            System.out.println("No se encontraron resultados para la orden: " + ordenManufactura);
            this.mog = MOG.builder()
                .build();
            throw new SQLException("No se encontraron datos para la orden: " + ordenManufactura);
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
            Logger.getLogger(Captura_MOG_Model.class.getName()).log(Level.SEVERE, null, ex);
        }
        return supervisorAsignado;
    }*/
}