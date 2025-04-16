/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Entities.DAS;
import Entities.MOG;
import Entities.PiezasProducidas;
import Entities.RBP;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class PreviaRBPModel {
    private final DBConexion conexion;
    private final DAS datosDAS = DAS.getInstance();
    private final RBP datosRBP = RBP.getInstance();
    private final MOG datosMOG = MOG.getInstance();
    private final PiezasProducidas datosPiezasProducidas = PiezasProducidas.getInstance();
    private static final Logger LOGGER = Logger.getLogger(ManufacturaModel.class.getName());
    
    public PreviaRBPModel() {
        this.conexion = new DBConexion();
    }
    
    public List<Object[]> obtenerRegistroProduccion() throws SQLException {
        List<Object[]> registroPiezas = new ArrayList<>();
        int indice = 1;
        
        try (Connection con = conexion.conexionMySQL();
             CallableStatement cst = con.prepareCall("{call obtenerPreviaRBP(?)}")) {
            cst.setInt(1, datosRBP.getId());

            try (ResultSet r = cst.executeQuery()) {
                while (r.next()) {
                    Object[] rowData = {
                            indice,
                            r.getString("linea"),
                            r.getString("fecha"),
                            r.getString("horas_trabajadas"),
                            r.getString("nombre_empleado"),
                            r.getString("rango_canastas"),
                            r.getString("cantidad_piezas_procesadas"),
                    };
                    registroPiezas.add(rowData);
                    indice++;
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al obtener los registros de producción", ex);
            throw ex;
        }

        return registroPiezas;
    }
    
    public List<Map<String, Object>> obtenerTarjetasProcesadas() throws SQLException {
        List<Map<String, Object>> resultadosDinamicos = new ArrayList<>();

        try (Connection con = conexion.conexionMySQL();
             CallableStatement cst = con.prepareCall("{call previaTarjetasRBP(?)}")) {
            cst.setInt(1, datosRBP.getId());

            try (ResultSet r = cst.executeQuery()) {
                while (r.next()) {
                    int totalTarjetas = r.getInt("tarjetas_procesadas");
                    resultadosDinamicos.addAll(distribuirNumeros(totalTarjetas));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al obtener los registros de tarjetas procesadas", ex);
            throw ex;
        }

        return resultadosDinamicos;
    }

    private List<Map<String, Object>> distribuirNumeros(int totalNumeros) {
        List<Map<String, Object>> filasTransformadas = new ArrayList<>();
        int columnaActual = 1;
        int secuenciaActual = 1;
        Map<String, Object> filaActual = new HashMap<>();

        while (secuenciaActual <= totalNumeros) {
            if (columnaActual > 30) {
                filasTransformadas.add(new HashMap<>(filaActual)); // Crear una nueva instancia para evitar modificaciones
                filaActual.clear();
                columnaActual = 1;
            }
            filaActual.put("tarjeta_" + columnaActual, secuenciaActual);
            columnaActual++;
            secuenciaActual++;
        }

        if (!filaActual.isEmpty()) {
            filasTransformadas.add(filaActual);
        }

        return filasTransformadas;
    }
    
    public void obtenerTotalesRBP() {
        try (Connection con = conexion.conexionMySQL(); CallableStatement cst = con.prepareCall("{call obtenerTotalesRBP(?,?)}")) {

            cst.setInt(1, datosRBP.getId());
            cst.setString(2, datosMOG.getMog());
            ResultSet r = cst.executeQuery();
            
            while(r.next()){
                datosRBP.setPiezasProcesadas(r.getInt("piezas_procesadas"));
                datosRBP.setPiezasRecibidas(r.getInt("piezas_recibidas"));
                datosRBP.setPiezasWcCompletos(r.getInt("piezas_wc_completos"));
                datosRBP.setWcCompletos(r.getInt("wc_completos"));
                datosRBP.setPiezasWcIncompletos(r.getInt("piezas_wc_incompletos"));
                datosRBP.setPiezasCambioMOG(r.getInt("piezas_cambio_mog"));
                datosRBP.setScrap(r.getInt("total_scrap"));
            }
            
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al obtener los datos totales de producción", ex);
        }
    }
}
