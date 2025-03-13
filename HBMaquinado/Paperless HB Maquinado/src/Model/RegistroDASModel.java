/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Interfaces.DAS;
import Interfaces.HoraxHora;
import Interfaces.LineaProduccion;
import Interfaces.MOG;
import Entities.RBP;
import Utils.MostrarMensaje;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class RegistroDASModel {

    private static final Logger LOGGER = Logger.getLogger(RegistroDASModel.class.getName());
    private final DBConexion conexion;
    private LocalDate fechaF;
    private LocalTime hora;
    DAS datosDAS = DAS.getInstance();

    public RegistroDASModel() {
        this.conexion = new DBConexion();
        inicializarFechaHora();
    }

    private void inicializarFechaHora() {
        try {
            fechaF = LocalDate.now();
            hora = LocalTime.now();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al inicializar la fecha y hora", e);
            JOptionPane.showMessageDialog(null, "Error al obtener la fecha y hora.");
        }
    }
    
    public boolean validarInspector(String codigoInspector) throws SQLException {
        int proceso = 1;
        try (Connection con = conexion.conexionMySQL(); CallableStatement cst = con.prepareCall("{call traerInspector(?,?,?,?)}")) {

            cst.setString(1, codigoInspector);
            cst.registerOutParameter(2, java.sql.Types.INTEGER);
            cst.setInt(3, proceso);
            cst.registerOutParameter(4, java.sql.Types.VARCHAR);
            cst.executeQuery();

            int valor = cst.getInt(2);
            String inspectorEncontrado = cst.getString(4);

            if (valor == 0) {
                MostrarMensaje.mostrarError("No se encontró ningún inspector asignado");
                return false;
            } else {
                datosDAS.setNombreInspector(inspectorEncontrado);
                return true;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al obtener datos de la entidad", ex);
            throw ex;
        }
    }
    
    public boolean validarSoporteRapido(String codigoSoporteRapido) throws SQLException {
        int proceso = 1;
        try (Connection con = conexion.conexionMySQL(); CallableStatement cst = con.prepareCall("{call traerKeeper(?,?,?,?)}")) {

            cst.setString(1, codigoSoporteRapido);
            cst.registerOutParameter(2, java.sql.Types.INTEGER);
            cst.setInt(3, proceso);
            cst.registerOutParameter(4, java.sql.Types.VARCHAR);
            cst.executeQuery();

            int valor = cst.getInt(2);
            String soporteEncontrado = cst.getString(4);

            if (valor == 0) {
                MostrarMensaje.mostrarError("No se encontró ningún soporte rápido asignado");
                return false;
            } else {
                datosDAS.setNombreSoporteRapido(soporteEncontrado);
                return true;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al obtener datos de la entidad", ex);
            throw ex;
        }
    }

    public void registrarPiezasPorHora(String numero_empleado, int acumulado, String calidad) throws SQLException {
        MOG datosMOG = MOG.getInstance();
        LineaProduccion lineaProduccion = LineaProduccion.getInstance();

        List<HoraxHora> piezas = obtenerPiezasProcesadasHora();

        int ultimoAcumulado = piezas.isEmpty() ? 0 : piezas.get(piezas.size() - 1).getAcumulado();

        if (acumulado < ultimoAcumulado) {
            throw new IllegalArgumentException("El acumulado actual no puede ser menor que el último acumulado registrado.");
        }

        int piezasProcesadas = acumulado - ultimoAcumulado;

        validarIntervaloHora(piezas, hora.format(DateTimeFormatter.ofPattern("HH:mm:ss")));

        try (Connection con = conexion.conexionMySQL(); CallableStatement cst = con.prepareCall("{call registro_x_hora_maq(?,?,?,?,?,?,?,?,?)}")) {

            cst.setString(1, datosMOG.getMog());
            cst.setString(2, datosMOG.getOrden_manufactura());
            cst.setString(3, numero_empleado);
            cst.setString(4, hora.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            cst.setInt(5, piezasProcesadas);
            cst.setInt(6, acumulado);
            cst.setString(7, calidad);
            cst.setDate(8, java.sql.Date.valueOf(fechaF));
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

    public List<HoraxHora> obtenerPiezasProcesadasHora() throws SQLException {
        List<HoraxHora> piezas = new ArrayList<>();

        try (Connection con = conexion.conexionMySQL(); CallableStatement cst = con.prepareCall("{call obtener_piezas_x_hora_maq(?,?)}")) {

            RBP datosRBP = RBP.getInstance();
            cst.setInt(1, datosRBP.getId());
            cst.setDate(2, java.sql.Date.valueOf(fechaF));

            try (ResultSet r = cst.executeQuery()) {
                while (r.next()) {
                    HoraxHora pieza = HoraxHora.builder()
                            .hora(r.getString("hora"))
                            .piezasXHora(r.getInt("cantidadxhora"))
                            .acumulado(r.getInt("acumulado"))
                            .okNg(r.getString("ok_ng"))
                            .nombre(r.getString("nombre_empleado"))
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
