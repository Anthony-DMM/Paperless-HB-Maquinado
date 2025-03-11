/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Interfaces.DAS;
import Interfaces.LineaProduccion;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class RegistroRBPModel {

    private final DBConexion conexion;
    private static final Logger LOGGER = Logger.getLogger(RegistroRBPModel.class.getName());
    private final LocalDate fechaF;

    public RegistroRBPModel() {
        this.conexion = new DBConexion();
        fechaF = LocalDate.now();
    }
    
    public void registrarDAS(String codigoSoporte, String codigoInspector, String codigoEmpleado) throws SQLException {
        int idDAS = 0;
        int turno = 0;
        try (Connection con = conexion.conexionMySQL(); CallableStatement cst = con.prepareCall("{call llenarDas(?,?,?,?,?,?,?,?)}")) {

            LineaProduccion lineaProduccion = LineaProduccion.getInstance();

            cst.registerOutParameter(1, java.sql.Types.INTEGER);
            cst.setString(2, lineaProduccion.getLinea());
            cst.setString(3, codigoEmpleado);
            cst.setString(4, codigoSoporte);
            cst.setString(5, codigoInspector);
            cst.setDate(6, java.sql.Date.valueOf(fechaF));
            cst.setString(7, lineaProduccion.getProceso());
            cst.setInt(8, turno);

            cst.executeQuery();
            idDAS = cst.getInt(1);
            if (idDAS != 0) {
                DAS datosDAS = DAS.getInstance();
                datosDAS.setIdDAS(idDAS);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al registrar la producción por hora", ex);
            throw ex;
        }
    }

    public int obtenerDASExistente() throws SQLException {
        int idDAS = 0;
        int turno = 0;
        try (Connection con = conexion.conexionMySQL(); CallableStatement cst = con.prepareCall("{call buscar_das(?,?,?)}")) {

            LineaProduccion lineaProduccion = LineaProduccion.getInstance();

            cst.setString(1, lineaProduccion.getLinea());
            cst.registerOutParameter(2, java.sql.Types.INTEGER);
            cst.setInt(3, turno);

            cst.executeQuery();
            idDAS = cst.getInt(2);

            if (idDAS != 0) {
                DAS datosDAS = DAS.getInstance();
                datosDAS.setIdDAS(idDAS);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al buscar DAS existente", ex);
            throw ex;
        }
        return idDAS;
    }
}
