/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Entities.MOGHija;
import Entities.RBP;
import Utils.MostrarMensaje;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class CambioMOGModel {
    private final DBConexion conexion;
    private static final Logger LOGGER = Logger.getLogger(ManufacturaModel.class.getName());
    MOGHija datosMOGHija = MOGHija.getInstance();
    RBP datosRBP = RBP.getInstance();
    private static final String ART = "HB";
    private static final String PROCESO_VALIDO = "HBL";

    public CambioMOGModel() {
        this.conexion = new DBConexion();
    }
    
    public boolean buscarMOGExistente(String mog) throws SQLException {
        try (Connection con = conexion.conexionMySQL();
             CallableStatement cst = con.prepareCall("{call buscarMOG(?)}")) {
            cst.setString(1, mog);
            cst.execute();
            ResultSet rs = cst.getResultSet();
            if (rs.next()) {
                MostrarMensaje.mostrarError("Esta MOG ya ha sido registrada, no se puede volver a capturar");
                return false;
            } else {
                obtenerDatosOrden(mog);
                return true;
            }
        }
    }
    
    public boolean obtenerDatosOrden(String mog) throws SQLException {
        try (Connection cone = conexion.oracle(); Statement sen = cone.createStatement()) {

            // Configurar esquema y ejecutar consulta
            sen.executeUpdate("ALTER SESSION SET CURRENT_SCHEMA = BAANLN");
            String query = buildQuery(mog);
            ResultSet res = sen.executeQuery(query);

            if (!procesarResultadoConsulta(res)) {
                return false;
            }

            return true;

        } catch (SQLException ex) {
            MostrarMensaje.mostrarError("Error al obtener datos de la orden" + ex);
            throw ex;
        }
    }

    private String buildQuery(String mog) {
        return "SELECT ttcibd001500.T$ITEM, ttidms602500.T$RPNO, ttcibd001500.T$SEAK, "
                + "ttidms602500.T$PDNO, ttidms602500.T$OQTY, ttcibd001500.T$SEAB, ttcibd001500.T$WGHT, "
                + "ttidms602500.T$SEQN, ttcibd001500.t$dscc, ttidms602500.T$CLOT "
                + "FROM ttcibd001500 INNER JOIN ttidms602500 ON ttidms602500.T$ITEM=ttcibd001500.T$ITEM "
                + "WHERE ttidms602500.T$RPNO='" + mog + "' AND ttidms602500.T$PDNO LIKE '%"+ PROCESO_VALIDO +"%'";
    }

    private boolean procesarResultadoConsulta(ResultSet res) throws SQLException {
        boolean ordenEncontrada = false;
        String numeroParte = null;

        while (res.next()) {
            ordenEncontrada = true;
            datosMOGHija.setMog(res.getString(2));
            datosMOGHija.setModelo(res.getString(3));
            datosMOGHija.setOrden_manufactura(res.getString(4));
            datosMOGHija.setCantidad_planeada(res.getInt(5));
            datosMOGHija.setNo_dibujo(res.getString(6));
            datosMOGHija.setPeso(res.getDouble(7));
            datosMOGHija.setSequ(res.getInt(8));
            datosMOGHija.setTm(res.getString(10));
            datosMOGHija.setStd(limpiarEstandar(res.getString(9)));
            numeroParte = procesarNumeroParte(res.getString(1));
            datosMOGHija.setNo_parte(numeroParte);
            
            System.out.println(datosMOGHija.getOrden_manufactura());
            System.out.println(numeroParte);
        }

        if (!ordenEncontrada) {
            MostrarMensaje.mostrarError("No se encontró la orden de manufactura de la MOG");
            return false;
        } else if (!datosMOGHija.getOrden_manufactura().contains(PROCESO_VALIDO)) {
            MostrarMensaje.mostrarError("La orden de manufactura de la MOG no pertenece al proceso de MAQUINADO");
            return false;
        }

        return true;
    }

    private String procesarNumeroParte(String numeroParte) {
        if (datosMOGHija.getOrden_manufactura().contains(PROCESO_VALIDO)) {
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
    
    public int insertarDatosMog() throws SQLException {
        try (Connection con = conexion.conexionMySQL();
             CallableStatement cst = con.prepareCall("{call llenarMog(?,?,?,?,?,?,?,?,?,?)}")) {
            cst.setString(1, datosMOGHija.getMog());
            cst.setString(2, ART + " " + datosMOGHija.getModelo());
            cst.setString(3, datosMOGHija.getNo_dibujo());
            cst.setString(4, datosMOGHija.getNo_parte());
            cst.setString(5, datosMOGHija.getModelo());
            cst.setString(6, datosMOGHija.getStd());
            cst.setInt(7, datosMOGHija.getCantidad_planeada());
            cst.registerOutParameter(8, java.sql.Types.INTEGER);
            cst.setDouble(9, datosMOGHija.getPeso());
            cst.setDouble(10, 1.1);
            cst.execute();

            int idMOGObtenido = cst.getInt(8);
            if (idMOGObtenido != 0) {
                datosMOGHija.setIdMogHija(idMOGObtenido);
                insertarCambioMOG();
            }
            return idMOGObtenido;
        }
    }

    public void insertarCambioMOG() throws SQLException { // Cambiado a void, ya que no devuelve un booleano, y relanza la excepción.
        try (Connection con = conexion.conexionMySQL();
             CallableStatement cst = con.prepareCall("{call insertar_cambio_mog(?,?)}")) {
            cst.setInt(1, datosRBP.getId());
            cst.setInt(2, datosMOGHija.getIdMogHija());
            cst.execute();
        }
    }
    
    public List<Object[]> obtenerHistorialCambios() throws SQLException {
        List<Object[]> historialCambios = new ArrayList<>();

        try (Connection con = conexion.conexionMySQL();
             CallableStatement cst = con.prepareCall("{call obtenerCambiosMOG(?)}")) {
            cst.setInt(1, datosRBP.getId());

            try (ResultSet r = cst.executeQuery()) {
                while (r.next()) {
                    Object[] rowData = {
                            r.getString("mog_hija"),
                            r.getString("cantidad_planeada"),
                    };
                    historialCambios.add(rowData);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al obtener la producción por hora", ex);
            throw ex;
        }

        return historialCambios;
    }
}
