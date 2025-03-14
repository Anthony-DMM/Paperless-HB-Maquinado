/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Entities.Operador;
import Entities.RBP;
import Entities.DAS;
import Interfaces.LineaProduccion;
import Utils.FechaHora;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class DASModel {

    private final DBConexion conexion;
    private static final Logger LOGGER = Logger.getLogger(RegistroRBPModel.class.getName());
    private final FechaHora fechaHora = new FechaHora();
    private LocalDate fechaF = LocalDate.now();
    private final RBP datosRBP = RBP.getInstance();
    private final DAS datosDAS = DAS.getInstance();
    private final Operador datosOperador = Operador.getInstance();
    int idDAS = 0;

    public DASModel() {
        this.conexion = new DBConexion();
        try {
            String fechaStr = fechaHora.fechaActual("yyyy-MM-dd");
            if (fechaStr != null) {
                fechaF = LocalDate.parse(fechaStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al obtener la fecha actual", ex);
        }
    }

    public boolean buscarDASExistente(int turno) throws SQLException {
        try (Connection con = conexion.conexionMySQL(); CallableStatement cst = con.prepareCall("{call buscar_das(?,?,?)}")) {

            LineaProduccion lineaProduccion = LineaProduccion.getInstance();

            cst.setString(1, lineaProduccion.getLinea());
            cst.registerOutParameter(2, java.sql.Types.INTEGER);
            cst.setInt(3, turno);

            cst.executeQuery();
            idDAS = cst.getInt(2);

            if (idDAS != 0) {
                datosDAS.setIdDAS(idDAS);
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al buscar DAS existente", ex);
            throw ex;
        }
    }
    
    public void registrarDAS(String codigoSoporte, String codigoInspector, String codigoEmpleado, int turno) throws SQLException {
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
                datosDAS.setIdDAS(idDAS);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al registrar la producción por hora", ex);
            throw ex;
        }
    }
    
    public void actualizarDASPadre(String codigoInspector, String codigoSoporte) throws SQLException {
        
        try (Connection con = conexion.conexionMySQL(); CallableStatement cst = con.prepareCall("{call actualizarDASPadre(?,?,?)}")) {
            cst.setInt(1, datosDAS.getIdDAS());
            cst.setString(2, codigoInspector);
            cst.setString(3, codigoSoporte);

            cst.executeQuery();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al actualizar el DAS principal", ex);
            throw ex;
        }
    }
}
