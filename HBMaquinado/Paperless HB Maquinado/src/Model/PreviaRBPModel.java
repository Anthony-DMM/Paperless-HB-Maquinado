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
public class PreviaRBPModel {
    private final DBConexion conexion;
    private final DAS datosDAS = DAS.getInstance();
    private final RBP datosRBP = RBP.getInstance();
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
}
