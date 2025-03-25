/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Entities.DAS;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class PreviaDASModel {
    private final DBConexion conexion;
    private final DAS datosDAS = DAS.getInstance();
    private static final Logger LOGGER = Logger.getLogger(ManufacturaModel.class.getName());

    public PreviaDASModel() {
        this.conexion = new DBConexion();
    }
    
    public List<Object[]> obtenerRegistroProduccion() throws SQLException {
        List<Object[]> registroProduccion = new ArrayList<>();

        try (Connection con = conexion.conexionMySQL();
             CallableStatement cst = con.prepareCall("{call obtener_Registro_Produccion_Maquinado(?)}")) {
            cst.setInt(1, datosDAS.getIdDAS());

            try (ResultSet r = cst.executeQuery()) {
                while (r.next()) {
                    Object[] rowData = {
                            r.getString("mog"),
                            r.getString("modelo"),
                            r.getString("std"),
                            r.getString("lote"),
                    };
                    registroProduccion.add(rowData);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al obtener los registros de producción", ex);
            throw ex;
        }

        return registroProduccion;
    }
    
    public List<Object[]> obtenerRegistroProduccion2() throws SQLException {
        List<Object[]> registroProduccion = new ArrayList<>();

        try (Connection con = conexion.conexionMySQL();
             CallableStatement cst = con.prepareCall("{call obtener_Registro_Produccion_Maquinado(?)}")) {
            cst.setInt(1, datosDAS.getIdDAS());

            try (ResultSet r = cst.executeQuery()) {
                while (r.next()) {
                    Object[] rowData = {
                            r.getString("hri_cambio_modelo"),
                            r.getString("hrf_cambio_modelo"),
                            r.getString("hri_proceso"),
                            r.getString("hrf_proceso"),
                    };
                    registroProduccion.add(rowData);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al obtener los registros de producción", ex);
            throw ex;
        }

        return registroProduccion;
    }
    
    public List<Object[]> obtenerRegistroProduccion3() throws SQLException {
        List<Object[]> registroProduccion = new ArrayList<>();

        try (Connection con = conexion.conexionMySQL();
             CallableStatement cst = con.prepareCall("{call obtener_Registro_Produccion_Maquinado(?)}")) {
            cst.setInt(1, datosDAS.getIdDAS());

            try (ResultSet r = cst.executeQuery()) {
                while (r.next()) {
                    Object[] rowData = {
                            r.getString("piezas_procesadas"),
                            r.getString("piezas_buenas"),
                            r.getString("piezas_rechazadas"),
                    };
                    registroProduccion.add(rowData);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al obtener los registros de producción", ex);
            throw ex;
        }

        return registroProduccion;
    }
    
}
