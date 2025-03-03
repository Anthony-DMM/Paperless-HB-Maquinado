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

    public RegistroDASModel(DBConexion conexion) {
        this.conexion = conexion;
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
                datosDAS.setSoporteRapido(soporteEncontrado);
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
                datosDAS.setInspector(inspectorEncontrado);
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
                datosDAS.setEmpleado(empleadoEncontrado);
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

        String fecha = fechaHora.fechaActual("yyyy-MM-dd");
        Date fechaUtil = fechaHora.stringToDate(fecha, "yyyy-MM-dd");
        java.sql.Date fechaF = new java.sql.Date(fechaUtil.getTime());
        String hora = fechaHora.horaActual();

        // Obtener la lista de piezas procesadas
        List<HoraxHora> piezas = obtenerPiezasProcesadasHora();

        // Obtener el último acumulado registrado
        int ultimoAcumulado = 0;
        if (!piezas.isEmpty()) {
            HoraxHora ultimaPieza = piezas.get(piezas.size() - 1);
            ultimoAcumulado = ultimaPieza.getAcumulado();
        }

        // Validar que el acumulado actual sea igual o mayor que el último acumulado
        if (acumulado < ultimoAcumulado) {
            throw new IllegalArgumentException("El acumulado actual no puede ser menor que el último acumulado registrado.");
        }

        // Calcular las piezas procesadas
        int piezasProcesadas = acumulado - ultimoAcumulado;

        try (Connection con = conexion.conexionMySQL();
             CallableStatement cst = con.prepareCall("{call registro_x_hora_maq(?,?,?,?,?,?,?,?,?)}")) {

            cst.setString(1, datosMOG.getMog());
            cst.setString(2, datosMOG.getOrden_manufactura());
            cst.setString(3, numero_empleado);
            cst.setString(4, hora);
            cst.setInt(5, piezasProcesadas); // Usar las piezas procesadas calculadas
            cst.setInt(6, acumulado); // Acumulado actual
            cst.setString(7, calidad);
            cst.setDate(8, fechaF);
            cst.setString(9, lineaProduccion.getLinea());

            cst.executeQuery();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al registrar la producción por hora", ex);
            throw ex;
        }
    }
    
    public List<HoraxHora> obtenerPiezasProcesadasHora() throws SQLException {
        List<HoraxHora> piezas = new ArrayList<>();

        try (Connection con = conexion.conexionMySQL();
             CallableStatement cst = con.prepareCall("{call obtener_piezas_x_hora_maq(?)}")) {

            RBP datosRBP = RBP.getInstance();
            cst.setInt(1, datosRBP.getId());
            
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
}