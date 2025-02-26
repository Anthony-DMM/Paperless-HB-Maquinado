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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    
    public String horaActual() throws SQLException {
        String hora = null;
        Statement sen;
        con = conexion.conexionMySQL();
        ResultSet res;
        sen = con.createStatement();
        try{
        res = sen.executeQuery("SELECT NOW()");
        if (res.next()) {
            Timestamp timestamp = res.getTimestamp(1);
            Date date = new Date(timestamp.getTime());

            // Obtener la hora con formato:
            DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
            hora = hourFormat.format(date);
        }
        res.close();
        sen.close();
        con.close();
        }catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al obtener la hora actual", ex);
        }
        return hora;
    }
    
    public String fechaActual() throws SQLException {
        String fecha = null;
        Statement sen;
        con = conexion.conexionMySQL();
        ResultSet res;
        sen = con.createStatement();
        try{
        res = sen.executeQuery("SELECT NOW()");
        if (res.next()) {
            Timestamp timestamp = res.getTimestamp(1);
            Date date = new Date(timestamp.getTime());

            // Obtener la hora con formato:
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            fecha = dateFormat.format(date);
        }
        res.close();
        sen.close();
        con.close();
        }catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al obtener la hora actual", ex);
        }
        return fecha;
    }
}
