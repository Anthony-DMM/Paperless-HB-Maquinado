/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Interfaces.DAS;
import Interfaces.HoraxHora;
import Interfaces.LineaProduccion;
import Interfaces.MOG;
import Interfaces.RBP;
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
    private DBConexion conexion;
    private LocalDate fechaF;
    private LocalTime hora;

    public RegistroDASModel() {
        inicializarConexion();
        inicializarFechaHora();
    }

    private void inicializarConexion() {
        this.conexion = new DBConexion();
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

    public boolean validarSoporteRapido(String codigoSoporteRapido) throws SQLException {
        return validarEntidad(codigoSoporteRapido, "traerKeeper", "No se encontró ningún soporte rápido asignado", DAS.getInstance()::setNombreSoporteRapido);
    }

    public boolean validarInspector(String codigoInspector) throws SQLException {
        return validarEntidad(codigoInspector, "traerInspector", "No se encontró ningún inspector asignado", DAS.getInstance()::setNombreInspector);
    }

    public boolean validarOperador(String numeroEmpleado) throws SQLException {
        return validarEntidad(numeroEmpleado, "traerOperador", "No se encontró ningún empleado", DAS.getInstance()::setNombreEmpleado);
    }

    private boolean validarEntidad(String codigo, String storedProcedure, String mensajeError, java.util.function.Consumer<String> setter) throws SQLException {
        int proceso = 1;
        try (Connection con = conexion.conexionMySQL(); CallableStatement cst = con.prepareCall("{call " + storedProcedure + "(?,?,?,?)}")) {

            cst.setString(1, codigo);
            cst.registerOutParameter(2, java.sql.Types.INTEGER);
            cst.setInt(3, proceso);
            cst.registerOutParameter(4, java.sql.Types.VARCHAR);
            cst.executeQuery();

            int valor = cst.getInt(2);
            String entidadEncontrada = cst.getString(4);

            if (valor == 0) {
                JOptionPane.showMessageDialog(null, mensajeError);
                return false;
            } else {
                setter.accept(entidadEncontrada);
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

    public void registrarDAS(String codigoSoporte, String codigoInspector, String codigoEmpleado) throws SQLException {
        int idDAS = 0;
        try (Connection con = conexion.conexionMySQL(); CallableStatement cst = con.prepareCall("{call llenarDas(?,?,?,?,?,?,?)}")) {

            LineaProduccion lineaProduccion = LineaProduccion.getInstance();

            cst.registerOutParameter(1, java.sql.Types.INTEGER);
            cst.setString(2, lineaProduccion.getLinea());
            cst.setString(3, codigoEmpleado);
            cst.setString(4, codigoSoporte);
            cst.setString(5, codigoInspector);
            cst.setDate(6, java.sql.Date.valueOf(fechaF));
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
        try (Connection con = conexion.conexionMySQL(); CallableStatement cst = con.prepareCall("{call buscar_das(?,?)}")) {

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
