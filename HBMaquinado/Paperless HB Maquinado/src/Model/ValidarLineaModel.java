/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Entities.LineaProduccion;
import Utils.MostrarMensaje;
import View.ValidarLineaView;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class ValidarLineaModel {
    
    private final DBConexion conexion;

    public ValidarLineaModel() {
        this.conexion = new DBConexion();
    }

    public LineaProduccion validarLinea(String lineaProduccion, String procesoEsperado) {
        Connection con = null;
        CallableStatement cst = null;

        try {
            con = conexion.conexionMySQL();
            cst = con.prepareCall("{call Supervisorname(?,?,?,?)}");
            cst.setString(1, lineaProduccion);
            cst.setString(2, procesoEsperado);
            cst.registerOutParameter(3, java.sql.Types.VARCHAR);
            cst.registerOutParameter(4, java.sql.Types.VARCHAR);
            cst.executeQuery();

            String supervisorAsignado = cst.getString(3);
            String procesoMaquina = cst.getString(4);

            if (procesoMaquina == null) {
                return null;
            }

            return crearLineaProduccion(lineaProduccion, supervisorAsignado, procesoMaquina);

        } catch (SQLException ex) {
            manejarExcepcion(ex);
            return null;
        } finally {
            cerrarRecursos(con, cst);
        }
    }
    
    private LineaProduccion crearLineaProduccion(String lineaProduccion, String supervisorAsignado, String procesoMaquina) {
        LineaProduccion linea = LineaProduccion.getInstance();
        linea.setLinea(lineaProduccion);
        linea.setSupervisor(supervisorAsignado);
        linea.setProceso(procesoMaquina);
        return linea;
    }
    
    private void manejarExcepcion(SQLException ex) {
        Logger.getLogger(ValidarLineaModel.class.getName()).log(Level.SEVERE, "Error al validar la línea de producción", ex);
        MostrarMensaje.mostrarError("No se encontró ningún supervisor asignado.");
    }
    
    private void cerrarRecursos(Connection con, CallableStatement cst) {
        if (cst != null) {
            try {
                cst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ValidarLineaModel.class.getName()).log(Level.SEVERE, "Error al cerrar CallableStatement", ex);
            }
        }
        if (con != null) {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ValidarLineaModel.class.getName()).log(Level.SEVERE, "Error al cerrar Connection", ex);
            }
        }
    }
}