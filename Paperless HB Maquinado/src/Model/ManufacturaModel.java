/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Interfaces.LineaProduccion;
import Entities.MOG;
import Entities.RBP;
import Utils.FechaHora;
import Utils.MostrarMensaje;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class ManufacturaModel {

    private final DBConexion conexion;
    private static final Logger LOGGER = Logger.getLogger(ManufacturaModel.class.getName());
    private static final String ART = "HB";
    private static final String PROCESO_VALIDO = "HBL";
    FechaHora fechaHora = new FechaHora();
    MOG datosMOG = MOG.getInstance();
    RBP datosRBP = RBP.getInstance();
    LineaProduccion datosLineaProduccion = LineaProduccion.getInstance();

    public ManufacturaModel() {
        this.conexion = new DBConexion();
    }

    public boolean obtenerDatosOrden(String ordenManufactura) {
        try (Connection cone = conexion.oracle(); Statement sen = cone.createStatement()) {

            // Configurar esquema y ejecutar consulta
            sen.executeUpdate("ALTER SESSION SET CURRENT_SCHEMA = BAANLN");
            String query = buildQuery(ordenManufactura);
            ResultSet res = sen.executeQuery(query);

            return procesarResultadoConsulta(res, datosMOG, ordenManufactura);

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al obtener datos de la orden", ex);
        }
        return false;
    }

    private String buildQuery(String ordenManufactura) {
        return "SELECT ttcibd001500.T$ITEM, ttidms602500.T$RPNO, ttcibd001500.T$SEAK, "
                + "ttidms602500.T$PDNO, ttidms602500.T$OQTY, ttcibd001500.T$SEAB, ttcibd001500.T$WGHT, "
                + "ttidms602500.T$SEQN, ttcibd001500.t$dscc, ttidms602500.T$CLOT "
                + "FROM ttcibd001500 INNER JOIN ttidms602500 ON ttidms602500.T$ITEM=ttcibd001500.T$ITEM "
                + "WHERE ttidms602500.T$PDNO='" + ordenManufactura + "'";
    }

    private boolean procesarResultadoConsulta(ResultSet res, MOG datosMOG, String ordenManufactura) throws SQLException {
        boolean ordenEncontrada = false;
        String numeroParte = null;

        while (res.next()) {
            ordenEncontrada = true;
            numeroParte = procesarNumeroParte(res.getString(1), ordenManufactura);
            datosMOG.setNo_parte(numeroParte);
            datosMOG.setMog(res.getString(2));
            datosMOG.setModelo(res.getString(3));
            datosMOG.setOrden_manufactura(res.getString(4));
            datosMOG.setCantidad_planeada(res.getInt(5));
            datosMOG.setNo_dibujo(res.getString(6));
            datosMOG.setPeso(res.getDouble(7));
            datosMOG.setSequ(res.getInt(8));
            datosMOG.setTm(res.getString(10));
            datosMOG.setStd(limpiarEstandar(res.getString(9)));
        }

        if (!ordenEncontrada) {
            JOptionPane.showMessageDialog(null, "No se encontró la orden de manufactura");
            return false;
        } else if (!ordenManufactura.contains(PROCESO_VALIDO)) {
            JOptionPane.showMessageDialog(null, "La orden de manufactura no pertenece al proceso de MAQUINADO");
            return false;
        }

        return true;
    }

    private String procesarNumeroParte(String numeroParte, String ordenManufactura) {
        if (ordenManufactura.contains(PROCESO_VALIDO)) {
            return numeroParte.replace("-TH", "").trim();
        }
        return numeroParte;
    }

    private String limpiarEstandar(String estandar) {
        String[] colores = {"Café", "CAFE", "Rojo", "Azul", "Verde", "Rosa", "Amarillo", "Negro", "Cafe", "CAFÉ",
            "ROJO", "AZUL", "VERDE", "ROSA", "AMARILLO", "NEGRO", "YELLOW", "BROWN", "GREEN",
            "MORADO", "BLUE", "BLANCO", "PURPLE", "Purple", "Blue", "PINK", "Pink", "White",
            "WHITE", "RED", "BLACK", "Black", "Blanca", "BLANCA", "Morado", "-"};
        for (String color : colores) {
            estandar = estandar.replace(color, "");
        }
        return estandar.trim();
    }

    public void ejecutarTransacciones() {
        try (Connection con = conexion.conexionMySQL()) {
            int id = insertarDatosMog(con, datosMOG);
            insertarDatosRbp(con, datosMOG, datosLineaProduccion, id);
            actualizarTm(con, datosMOG);
            insertarCorriendo(con, datosMOG, datosLineaProduccion);
        } catch (SQLException ex) {
            Logger.getLogger(ManufacturaModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private int insertarDatosMog(Connection con, MOG datosMOG) throws SQLException {
        try (CallableStatement cst = con.prepareCall("{call llenarMog(?,?,?,?,?,?,?,?,?,?)}")) {
            cst.setString(1, datosMOG.getMog());
            cst.setString(2, ART + " " + datosMOG.getModelo());
            cst.setString(3, datosMOG.getNo_dibujo());
            cst.setString(4, datosMOG.getNo_parte());
            cst.setString(5, datosMOG.getModelo());
            cst.setString(6, datosMOG.getStd());
            cst.setInt(7, datosMOG.getCantidad_planeada());
            cst.registerOutParameter(8, java.sql.Types.INTEGER);
            cst.setDouble(9, datosMOG.getPeso());
            cst.setDouble(10, 1.1);
            cst.execute();
            return cst.getInt(8);
        }
    }

    private void insertarDatosRbp(Connection con, MOG datosMOG, LineaProduccion lineaProduccion, int id) throws SQLException {
        try (CallableStatement cs = con.prepareCall("{call llenarRBP(?,?,?,?,?)}")) {
            cs.setString(1, datosMOG.getOrden_manufactura());
            cs.setString(2, lineaProduccion.getProceso());
            cs.setInt(3, id);
            cs.setInt(4, datosMOG.getSequ());
            cs.registerOutParameter(5, java.sql.Types.INTEGER);
            cs.execute();

            int id_rbp = cs.getInt(5);
            datosRBP.setId(id_rbp);
        }
    }

    private void actualizarTm(Connection con, MOG datosMOG) throws SQLException {
        try (CallableStatement cs1 = con.prepareCall("{call actualizarTM(?,?)}")) {
            cs1.setString(1, datosMOG.getOrden_manufactura());
            cs1.setString(2, datosMOG.getTm());
            cs1.execute();
        }
    }

    private void insertarCorriendo(Connection con, MOG datosMOG, LineaProduccion lineaProduccion) throws SQLException {
        try (CallableStatement cst2 = con.prepareCall("{call insertarCorriendo(?,?,?,?,?)}")) {
            String hora = fechaHora.horaActual();
            String fecha = fechaHora.fechaActual("yyyy-MM-dd");

            cst2.setString(1, datosMOG.getOrden_manufactura());
            cst2.setString(2, datosMOG.getMog());
            cst2.setString(3, hora);
            cst2.setString(4, fecha);
            cst2.setString(5, lineaProduccion.getLinea());
            cst2.execute();
            
            datosRBP.setHora(hora);
            datosRBP.setFecha(fecha);
        }
    }

    public boolean validarSupervisor(String codigoSupervisor) {
        try (Connection con = conexion.conexionMySQL(); CallableStatement cst = con.prepareCall("{call login(?,?,?,?)}")) {

            cst.setString(1, codigoSupervisor);
            cst.registerOutParameter(2, java.sql.Types.INTEGER);
            cst.registerOutParameter(3, java.sql.Types.VARCHAR);
            cst.registerOutParameter(4, java.sql.Types.VARCHAR);
            cst.execute();

            int valor = cst.getInt(2);
            String procesoObtenido = cst.getString(3);
            String supervisorObtenido = cst.getString(4);

            if (valor == 0) {
                MostrarMensaje.mostrarError("No se encontró ningún supervisor asignado");
                return false;
            } else if (!procesoObtenido.equals(datosLineaProduccion.getProceso())) {
                MostrarMensaje.mostrarError("El supervisor no pertenece al área de MAQUINADO");
                return false;
            } else {
                datosLineaProduccion.setSupervisor(supervisorObtenido);
                return true;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al validar el supervisor", ex);
            MostrarMensaje.mostrarError("Ocurrió un error al validar la linea de producción, comunicate con el área de IT y comparte este código de error: " + ex);
        }
        return false;
    }
}
