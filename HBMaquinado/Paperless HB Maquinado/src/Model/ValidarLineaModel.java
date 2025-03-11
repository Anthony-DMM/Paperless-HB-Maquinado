/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Interfaces.LineaProduccion;
import Utils.MostrarMensaje;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class ValidarLineaModel {

    private static final Logger LOGGER = Logger.getLogger(ValidarLineaModel.class.getName());
    private final DBConexion conexion;

    public ValidarLineaModel() {
        this.conexion = new DBConexion();
    }

    public LineaProduccion validarLinea(String lineaProduccion, String procesoEsperado) {
        try (Connection con = conexion.conexionMySQL();
             CallableStatement cst = con.prepareCall("{call Supervisorname(?,?,?,?)}")) {

            cst.setString(1, lineaProduccion);
            cst.setString(2, procesoEsperado);
            cst.registerOutParameter(3, java.sql.Types.VARCHAR);
            cst.registerOutParameter(4, java.sql.Types.VARCHAR);
            cst.executeQuery();

            return procesarResultado(lineaProduccion, cst);

        } catch (SQLException ex) {
            manejarExcepcion(ex);
            return null;
        }
    }

    private LineaProduccion procesarResultado(String lineaProduccion, CallableStatement cst) throws SQLException {
        String supervisorAsignado = cst.getString(3);
        String procesoMaquina = cst.getString(4);

        return (procesoMaquina != null) ? crearLineaProduccion(lineaProduccion, supervisorAsignado, procesoMaquina) : null;
    }

    private LineaProduccion crearLineaProduccion(String lineaProduccion, String supervisor, String proceso) {
        LineaProduccion linea = LineaProduccion.getInstance();
        linea.setLinea(lineaProduccion);
        linea.setSupervisor(supervisor);
        linea.setProceso(proceso);
        return linea;
    }

    private void manejarExcepcion(SQLException ex) {
        LOGGER.log(Level.SEVERE, "Error al validar la línea de producción", ex);
        MostrarMensaje.mostrarError("Ocurrió un error al validar la linea de producción, comunicate con el área de IT y comparte este código de error: " + ex);
    }
}