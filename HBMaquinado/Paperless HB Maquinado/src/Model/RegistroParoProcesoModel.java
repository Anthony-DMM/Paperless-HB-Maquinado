/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Entities.DAS;
import Entities.LineaProduccion;
import Entities.MOG;
import Entities.ParoProceso;
import Utils.FechaHora;
import Utils.MostrarMensaje;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class RegistroParoProcesoModel {

    private final DBConexion conexion;
    private static final Logger LOGGER = Logger.getLogger(ManufacturaModel.class.getName());
    FechaHora fechaHora = new FechaHora();
    String fecha;
    Date fechaUtil;
    java.sql.Date fechaF;

    public RegistroParoProcesoModel() throws SQLException, ParseException {
        conexion = new DBConexion();

        fecha = fechaHora.fechaActual("yyyy-MM-dd");
        fechaUtil = fechaHora.stringToDate(fecha, "yyyy-MM-dd");
        fechaF = new java.sql.Date(fechaUtil.getTime());
    }

    public boolean obtenerCategoriasParoProceso() throws SQLException {
        int proceso = 1;
        try (Connection con = conexion.conexionMySQL(); CallableStatement cst = con.prepareCall("{call obtenerCategoriasParoProceso(?)}")) {

            cst.setInt(1, proceso);
            ResultSet resultSet = cst.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                MostrarMensaje.mostrarError("No se pudieron obtener las categorías de paro en proceso");
                return false;
            } else {
                List<ParoProceso> listaParos = new ArrayList<>();
                while (resultSet.next()) {
                    ParoProceso paroProceso = ParoProceso.builder()
                            .categoria(resultSet.getString("categoria"))
                            .build();
                    listaParos.add(paroProceso);
                }

                ParoProceso paroProceso = ParoProceso.getInstance();
                paroProceso.setListaCategorias(listaParos);
                return true;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al obtener las categorías de paros en proceso", ex);
            throw ex;
        }
    }

    public boolean obtenerCausasPorCategoriaParoProceso(String categoria) throws SQLException {
        int proceso = 1;
        try (Connection con = conexion.conexionMySQL(); CallableStatement cst = con.prepareCall("{call obtenerCausasPorCategoriaParoProceso(?,?)}")) {

            cst.setInt(1, proceso);
            cst.setString(2, categoria);
            ResultSet resultSet = cst.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                return false;
            } else {
                List<ParoProceso> listaParos = new ArrayList<>();
                while (resultSet.next()) {
                    ParoProceso paroProceso = ParoProceso.builder()
                            .idcausas_paro(resultSet.getInt("idcausas_paro"))
                            .descripcion(resultSet.getString("descripcion"))
                            .build();
                    listaParos.add(paroProceso);
                }

                ParoProceso paroProceso = ParoProceso.getInstance();
                paroProceso.setListaCausas(listaParos);
                return true;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al obtener las causas de paros en proceso", ex);
            throw ex;
        }
    }

    public void registrarParoProceso(int idCausa, int duracion, String detalle, String horaInicio, String horaFin) throws SQLException {
        try (Connection con = conexion.conexionMySQL(); CallableStatement cst = con.prepareCall("{call InsertarRegistroCausasParo(?,?,?,?,?,?,?,?,?,?)}")) {
            DAS datosDAS = DAS.getInstance();
            LineaProduccion datosLineaProduccion = LineaProduccion.getInstance();
            MOG datosMOG = MOG.getInstance();

            cst.setString(1, datosDAS.getCodigoEmpleado());
            cst.setInt(2, idCausa);
            cst.setInt(3, duracion);
            cst.setString(4, detalle);
            cst.setString(5, horaInicio);
            cst.setDate(6, fechaF);
            cst.setString(7, datosLineaProduccion.getLinea());
            cst.setInt(8, datosDAS.getIdDAS());
            cst.setString(9, datosMOG.getMog());
            cst.setString(10, horaFin);

            cst.executeQuery();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Ocurrió un error al registrar el paro ", ex);
            throw ex;
        }
    }
}
