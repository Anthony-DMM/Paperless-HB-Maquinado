/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Entities.DAS;
import Entities.HoraxHora;
import Entities.LineaProduccion;
import Entities.MOG;
import Entities.RBP;
import Utils.FechaHora;
import Utils.MostrarMensaje;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class RegistroDASModel {
    private static final Logger LOGGER = Logger.getLogger(CapturaOrdenManufacturaModel.class.getName());
    private DBConexion conexion;
    FechaHora fechaHora = new FechaHora();
    String fecha, hora;
    Date fechaUtil;
    java.sql.Date fechaF;

    public RegistroDASModel(DBConexion conexion) throws SQLException, ParseException {
        this.conexion = conexion;
        
        fecha = fechaHora.fechaActual("yyyy-MM-dd");
        fechaUtil = fechaHora.stringToDate(fecha, "yyyy-MM-dd");
        fechaF = new java.sql.Date(fechaUtil.getTime());
        hora = fechaHora.horaActual();
    }
    
    public boolean validarSoporteRapido(String codigoSoporteRapido) throws SQLException{
        int proceso = 1;
        try (Connection con = conexion.conexionMySQL();
            CallableStatement cst = con.prepareCall("{call traerKeeper(?,?,?,?)}")){
            
            cst.setString(1, codigoSoporteRapido);
            cst.registerOutParameter(2, java.sql.Types.INTEGER);
            cst.setInt(3, proceso);
            cst.registerOutParameter(4, java.sql.Types.VARCHAR);
            cst.executeQuery();
            
            int valor = cst.getInt(2);
            String soporteEncontrado=cst.getString(4);
            
            if (valor == 0) {
                JOptionPane.showMessageDialog(null, "No se encontró ningún soporte rápido asignado");
                return false;
            } else {
                DAS datosDAS = DAS.getInstance();
                datosDAS.setNombreSoporteRapido(soporteEncontrado);
                return true;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al obtener datos del soporte rápido", ex);
            throw ex;
        }
    }
    
    public boolean validarInspector(String codigoInspector) throws SQLException{
        int proceso = 1;
        try (Connection con = conexion.conexionMySQL();
            CallableStatement cst = con.prepareCall("{call traerInspector(?,?,?,?)}")){
            
            cst.setString(1, codigoInspector);
            cst.registerOutParameter(2, java.sql.Types.INTEGER);
            cst.setInt(3, proceso);
            cst.registerOutParameter(4, java.sql.Types.VARCHAR);
            cst.executeQuery();
            
            int valor = cst.getInt(2);
            String inspectorEncontrado=cst.getString(4);
            
            if (valor == 0) {
                JOptionPane.showMessageDialog(null, "No se encontró ningún inspector asignado");
                return false;
            } else {
                DAS datosDAS = DAS.getInstance();
                datosDAS.setNombreInspector(inspectorEncontrado);
                return true;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al obtener datos del inspector", ex);
            throw ex;
        }
    }
    
    public boolean validarOperador(String numeroEmpleado) throws SQLException{
        int proceso = 1;
        try (Connection con = conexion.conexionMySQL();
            CallableStatement cst = con.prepareCall("{call traerOperador(?,?,?,?)}")){
            
            cst.setString(1, numeroEmpleado);
            cst.registerOutParameter(2, java.sql.Types.INTEGER);
            cst.setInt(3, proceso);
            cst.registerOutParameter(4, java.sql.Types.VARCHAR);
            cst.executeQuery();
            
            int valor = cst.getInt(2);
            String empleadoEncontrado=cst.getString(4);
            
            if (valor == 0) {
                JOptionPane.showMessageDialog(null, "No se encontró ningún empleado");
                return false;
            } else {
                DAS datosDAS = DAS.getInstance();
                datosDAS.setNombreEmpleado(empleadoEncontrado);
                return true;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al obtener datos del empleado", ex);
            throw ex;
        }
    }
    
    public void registrarPiezasPorHora(String numero_empleado, int acumulado, String calidad) throws SQLException, ParseException {
        MOG datosMOG = MOG.getInstance();
        LineaProduccion lineaProduccion = LineaProduccion.getInstance();

        List<HoraxHora> piezas = obtenerPiezasProcesadasHora();

        int ultimoAcumulado = 0;
        if (!piezas.isEmpty()) {
            HoraxHora ultimaPieza = piezas.get(piezas.size() - 1);
            ultimoAcumulado = ultimaPieza.getAcumulado();
        }

        if (acumulado < ultimoAcumulado) {
            throw new IllegalArgumentException("El acumulado actual no puede ser menor que el último acumulado registrado.");
        }

        int piezasProcesadas = acumulado - ultimoAcumulado;

        validarIntervaloHora(piezas, hora);

        try (Connection con = conexion.conexionMySQL();
             CallableStatement cst = con.prepareCall("{call registro_x_hora_maq(?,?,?,?,?,?,?,?,?)}")) {

            cst.setString(1, datosMOG.getMog());
            cst.setString(2, datosMOG.getOrden_manufactura());
            cst.setString(3, numero_empleado);
            cst.setString(4, hora);
            cst.setInt(5, piezasProcesadas);
            cst.setInt(6, acumulado);
            cst.setString(7, calidad);
            cst.setDate(8, fechaF);
            cst.setString(9, lineaProduccion.getLinea());

            cst.executeQuery();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al registrar la producción por hora", ex);
            throw ex;
        }
    }
    
    private void validarIntervaloHora(List<HoraxHora> piezas, String horaActual) {
        if (!piezas.isEmpty()) {
            HoraxHora ultimaHora = piezas.get(piezas.size() - 1);
            String ultimaHoraRegistrada = ultimaHora.getHora();

            int horaRegistrada = Integer.parseInt(ultimaHoraRegistrada.split(":")[0]);
            int horaActualInt = Integer.parseInt(horaActual.split(":")[0]);

            if (horaActualInt == horaRegistrada) {
                throw new IllegalStateException("No se puede registrar en el mismo intervalo de hora.");
            }
        }
    }
    
    public List<HoraxHora> obtenerPiezasProcesadasHora() throws SQLException, ParseException {
        List<HoraxHora> piezas = new ArrayList<>();

        try (Connection con = conexion.conexionMySQL();
             CallableStatement cst = con.prepareCall("{call obtener_piezas_x_hora_maq(?,?)}")) {

            RBP datosRBP = RBP.getInstance();
            cst.setInt(1, datosRBP.getId());
            
            String fecha = fechaHora.fechaActual("yyyy-MM-dd");
            Date fechaUtil = fechaHora.stringToDate(fecha, "yyyy-MM-dd");
            java.sql.Date fechaF = new java.sql.Date(fechaUtil.getTime());
            cst.setDate(2, fechaF);
            
            try (ResultSet r = cst.executeQuery()) {
                while (r.next()) {
                    String hora = r.getString("hora");
                    int piezasXHora = r.getInt("cantidadxhora");
                    int acumulado = r.getInt("acumulado");
                    String okNg = r.getString("ok_ng");
                    String nombre = r.getString("nombre_empleado");

                    HoraxHora pieza = HoraxHora.builder()
                        .hora(hora)
                        .piezasXHora(piezasXHora)
                        .acumulado(acumulado)
                        .okNg(okNg)
                        .nombre(nombre)
                        .build();
                    
                    piezas.add(pieza);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al obtener la producción por hora", ex);
            throw ex;
        }

        return piezas;
    }
    
    public void registrarDAS(String codigoSoporte, String codigoInspector, String codigoEmpleado) throws SQLException{
        int idDAS = 0;
        try (Connection con = conexion.conexionMySQL();
             CallableStatement cst = con.prepareCall("{call llenarDas(?,?,?,?,?,?,?)}")) {

            LineaProduccion lineaProduccion = LineaProduccion.getInstance();
            
            cst.registerOutParameter(1, java.sql.Types.INTEGER);
            cst.setString(2, lineaProduccion.getLinea());
            cst.setString(3, codigoEmpleado);
            cst.setString(4, codigoSoporte);
            cst.setString(5, codigoInspector);
            cst.setDate(6, fechaF);
            cst.setString(7, lineaProduccion.getProceso());

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
        try (Connection con = conexion.conexionMySQL();
             CallableStatement cst = con.prepareCall("{call buscar_das(?,?)}")) {

            LineaProduccion lineaProduccion = LineaProduccion.getInstance();

            cst.setString(1, lineaProduccion.getLinea());
            cst.registerOutParameter(2, java.sql.Types.INTEGER);

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