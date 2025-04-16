/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Entities.Operador;
import Entities.DAS;
import Entities.DetalleParo;
import Interfaces.LineaProduccion;
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
public class ParoProcesoModel {

    private final DBConexion conexion;
    private static final Logger LOGGER = Logger.getLogger(ManufacturaModel.class.getName());
    FechaHora fechaHora = new FechaHora();
    String fecha;
    Date fechaUtil;
    java.sql.Date fechaF;
    int proceso = 1;
    DAS datosDAS = DAS.getInstance();

    public ParoProcesoModel() {
        this.conexion = new DBConexion();
        fechaHora = new FechaHora();
        try {
            fecha = fechaHora.fechaActual("yyyy-MM-dd");
            fechaUtil = fechaHora.stringToDate(fecha, "yyyy-MM-dd");
            fechaF = new java.sql.Date(fechaUtil.getTime());
        } catch (SQLException ex) {
            Logger.getLogger(ParoProcesoModel.class.getName()).log(Level.SEVERE, "Error al inicializar la conexión o al obtener la fecha", ex);
        } catch (ParseException ex) {
            Logger.getLogger(ParoProcesoModel.class.getName()).log(Level.SEVERE, "Error al parsear la fecha", ex);
        }
    }
    
    public boolean obtenerCategoriasParoProceso() throws SQLException {
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

    public boolean obtenerAndonPorProceso() throws SQLException {
        try (Connection con = conexion.conexionMySQL(); CallableStatement cst = con.prepareCall("{call obtenerAndonPorProceso(?)}")) {

            cst.setInt(1, proceso);
            ResultSet resultSet = cst.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                MostrarMensaje.mostrarError("No se pudieron obtener los andones de paro en proceso");
                return false;
            } else {
                List<ParoProceso> listaAndones = new ArrayList<>();
                while (resultSet.next()) {
                    ParoProceso paroProceso = ParoProceso.builder()
                            .id_andon(resultSet.getInt("id_andon"))
                            .descripcion_andon(resultSet.getString("descripcion"))
                            .build();
                    listaAndones.add(paroProceso);
                }

                ParoProceso paroProceso = ParoProceso.getInstance();
                paroProceso.setListaAndones(listaAndones);
                return true;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al obtener los andones de paro en proceso", ex);
            throw ex;
        }
    }
    
    public boolean obtenerNivelMaquinado() throws SQLException {
        try (Connection con = conexion.conexionMySQL(); CallableStatement cst = con.prepareCall("{call obtenerNivelMaquinado()}")) {
            ResultSet resultSet = cst.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                MostrarMensaje.mostrarError("No se pudieron obtener los niveles de paro en proceso");
                return false;
            } else {
                List<ParoProceso> listaNiveles = new ArrayList<>();
                while (resultSet.next()) {
                    ParoProceso paroProceso = ParoProceso.builder()
                            .id_nivel(resultSet.getInt("id_nivel"))
                            .nivel(resultSet.getInt("nivel"))
                            .build();
                    listaNiveles.add(paroProceso);
                }

                ParoProceso paroProceso = ParoProceso.getInstance();
                paroProceso.setListaNiveles(listaNiveles);
                return true;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al obtener los niveles de paro en proceso", ex);
            throw ex;
        }
    }
    
    public boolean obtenerCausasPorCategoriaParoProceso(String categoria) throws SQLException {
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

    public void registrarParoProceso(int idCausa, int duracion, String detalle, String horaInicio, String horaFin, Integer idAndon, Integer idNivel) throws SQLException {
        try (Connection con = conexion.conexionMySQL();
             CallableStatement cst = con.prepareCall("{call InsertarRegistroCausasParo(?,?,?,?,?,?,?,?,?,?,?,?)}")) {

            DAS datosDAS = DAS.getInstance();
            Operador datosOperador = Operador.getInstance();
            LineaProduccion datosLineaProduccion = LineaProduccion.getInstance();
            MOG datosMOG = MOG.getInstance();

            cst.setString(1, datosOperador.getCódigo());
            cst.setInt(2, idCausa);
            cst.setInt(3, duracion);
            cst.setString(4, detalle);
            cst.setString(5, horaInicio);
            cst.setDate(6, fechaF);
            cst.setString(7, datosLineaProduccion.getLinea());
            cst.setInt(8, datosDAS.getIdDAS());
            cst.setString(9, datosMOG.getMog());
            cst.setString(10, horaFin);
            cst.setObject(11, idAndon, java.sql.Types.INTEGER);
            cst.setObject(12, idNivel, java.sql.Types.INTEGER);

            cst.executeQuery();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Ocurrió un error al registrar el paro de línea", ex);
            throw ex;
        }
    }
    
    public List<DetalleParo> obtenerHistorialParos() throws SQLException {
        List<DetalleParo> historialParos = new ArrayList<>();

        try (Connection con = conexion.conexionMySQL(); CallableStatement cst = con.prepareCall("{call obtenerParosMaquinado(?)}")) {
            cst.setInt(1, datosDAS.getIdDAS());

            try (ResultSet r = cst.executeQuery()) {
                while (r.next()) {
                    DetalleParo paro = DetalleParo.builder()
                        .descripcion(r.getString("descripcion"))
                        .hora_inicio(r.getString("hora_inicio"))
                        .hora_fin(r.getString("hora_fin"))
                        .tiempo(r.getInt("tiempo"))
                        .andon(r.getString("andon"))
                        .escalacion(r.getString("escalacion"))
                        .detalle(r.getString("detalle"))
                        .build();
                historialParos.add(paro);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al obtener la producción por hora", ex);
            throw ex;
        }

        return historialParos;
    }
}
