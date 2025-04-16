/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

import Model.DBConexion;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class FechaHora {

    DBConexion conexion = new DBConexion();
    Connection con = null;
    private static final Logger LOGGER = Logger.getLogger(FechaHora.class.getName());

    public Timestamp obtenerTimestampActual() throws SQLException {
        Timestamp timestamp = null;
        Statement sen = null;
        ResultSet res = null;

        try {
            con = conexion.conexionMySQL();
            sen = con.createStatement();
            res = sen.executeQuery("SELECT NOW()");

            if (res.next()) {
                timestamp = res.getTimestamp(1);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al obtener el timestamp actual", ex);
            throw ex;
        } finally {
            if (res != null) {
                res.close();
            }
            if (sen != null) {
                sen.close();
            }
            if (con != null) {
                con.close();
            }
        }

        return timestamp;
    }

    public String horaActual() throws SQLException {
        Timestamp timestamp = obtenerTimestampActual();
        if (timestamp != null) {
            Date date = new Date(timestamp.getTime());
            DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
            return hourFormat.format(date);
        }
        return null;
    }

    public String fechaActual(String formato) throws SQLException {
        Timestamp timestamp = obtenerTimestampActual();
        if (timestamp != null) {
            Date date = new Date(timestamp.getTime());
            DateFormat dateFormat = new SimpleDateFormat(formato);
            return dateFormat.format(date);
        }
        return null;
    }

    public Date stringToDate(String fechaString, String formato) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(formato);
        return dateFormat.parse(fechaString);
    }

    public LocalTime stringHoraToLocalTime(String horaString, String formato) throws DateTimeParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formato);
        return LocalTime.parse(horaString, formatter);
    }

    public String timestampToString(Timestamp timestamp, String formato) {
        if (timestamp != null) {
            Date date = new Date(timestamp.getTime());
            DateFormat dateFormat = new SimpleDateFormat(formato);
            return dateFormat.format(date);
        }
        return null;
    }
}
