/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Entities.DAS;
import Entities.PiezasProducidas;
import Entities.RBP;
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
    private final RBP datosRBP = RBP.getInstance();
    private final PiezasProducidas datosPiezasProducidas = PiezasProducidas.getInstance();
    private static final Logger LOGGER = Logger.getLogger(ManufacturaModel.class.getName());

    public PreviaDASModel() {
        this.conexion = new DBConexion();
    }
    
    public List<Object[]> obtenerRegistroProduccion() throws SQLException {
        List<Object[]> registroProduccion = new ArrayList<>();

        try (Connection con = conexion.conexionMySQL();
             CallableStatement cst = con.prepareCall("{call obtener_Registro_Produccion_Maquinado(?,?)}")) {
            cst.setInt(1, datosDAS.getIdDAS());
            cst.setInt(2, datosPiezasProducidas.getColumnaTurno());

            try (ResultSet r = cst.executeQuery()) {
                while (r.next()) {
                    Object[] rowData = {
                            r.getString("mog"),
                            r.getString("modelo"),
                            r.getString("std"),
                            r.getString("lote"),
                            r.getString("inicio_tp"),
                            r.getString("fin_tp"),
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
             CallableStatement cst = con.prepareCall("{call obtener_Registro_Produccion_Maquinado(?,?)}")) {
            cst.setInt(1, datosDAS.getIdDAS());
            cst.setInt(2, datosPiezasProducidas.getColumnaTurno());

            try (ResultSet r = cst.executeQuery()) {
                while (r.next()) {
                    Object[] rowData = {
                            r.getString("inicio_tp"),
                            r.getString("fin_tp"),
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
             CallableStatement cst = con.prepareCall("{call obtener_Registro_Produccion_Maquinado(?,?)}")) {
            cst.setInt(1, datosDAS.getIdDAS());
            cst.setInt(2, datosPiezasProducidas.getColumnaTurno());

            try (ResultSet r = cst.executeQuery()) {
                while (r.next()) {
                    Object[] rowData = {
                            r.getString("cantidad_piezas_procesadas"),
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
