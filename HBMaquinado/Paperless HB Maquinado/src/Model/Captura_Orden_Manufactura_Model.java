/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Interfaces.LineaProduccion;
import Interfaces.MOG;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
    
    public String validarSupervisor(String codigo_supervisor){
        String linea_produccion = LineaProduccion.getInstance().getLinea();
        Connection con;
        con=conexion.conexionMySQL();
        try {
            PreparedStatement pst = con.prepareStatement("SELECT nombre_empleado, apellido, codigo_maquina, nombre_work_center\n" +
                                                    "FROM empleado\n" +
                                                    "INNER JOIN empleado_supervisor ON empleado_supervisor.empleado_id_empleado = empleado.id_empleado\n" +
                                                    "INNER JOIN work_center_maquina ON work_center_maquina.empleado_supervisor_id_empleado_supervisor = empleado_supervisor.id_empleado_supervisor\n" +
                                                    "WHERE empleado_supervisor.empleado_id_empleado = " + codigo_supervisor + " AND work_center_maquina.codigo_maquina = '" + linea_produccion + "';");
            ResultSet rs = pst.executeQuery();
            /*cst.setString(1, linea_produccion);
            cst.setString(2, proceso);
            cst.registerOutParameter(3, java.sql.Types.VARCHAR);
            cst.executeQuery();*/
            while (rs.next()) {
                String nombreEmpleado = rs.getString("nombre_empleado");
                String apellido = rs.getString("apellido");
                String codigoMaquina = rs.getString("codigo_maquina");
                String nombreWorkCenter = rs.getString("nombre_work_center");

                // Imprimir los resultados
                System.out.println("Nombre: " + nombreEmpleado + " " + apellido);
                System.out.println("Código Máquina: " + codigoMaquina);
                System.out.println("Work Center: " + nombreWorkCenter);
                System.out.println("-----------------------------");
            }
            con.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "No se encontró ningún supervisor asignado");
            Logger.getLogger(Captura_Orden_Manufactura_Model.class.getName()).log(Level.SEVERE, null, ex);
        }
        return linea_produccion;
    }
}