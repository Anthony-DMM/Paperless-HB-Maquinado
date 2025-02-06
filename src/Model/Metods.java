/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import View.First_windowRBP;
import View.Second_windowRBP;
import View.StopView;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author BRYAN-LOPEZ
 */
public class Metods extends DefaultTableModel {
    
    String ln;
    Connection con = null, cone = null;
    
    public void setLn(String ln) {
        this.ln = ln;
    }
    
     public Connection conexionMySQL() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://192.168.155.16:3306"
                    + "/rbppaperlesshbPrueba?user=adminpaperless"
                    + "&password=paperless2018");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("error de cone+xion");
            JOptionPane.showMessageDialog(null, "error de conexion " + e);
        }
        return con;
    }
    
    public Connection oracle() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            cone = DriverManager.getConnection("jdbc:oracle:thin:@192.168.155.11:1521:ERPLNFP7", "DMMROU", "DMM");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("error de conexion");
            JOptionPane.showMessageDialog(null, "error de conexion " + e);
        }
        return cone;
    }
    
    public String PermitSoloNum(String texto) {
        String expresion = "\\d";
        Pattern pat = Pattern.compile(expresion);
        Matcher mat = pat.matcher(texto);
        if (!mat.matches()) {
            texto = "";
        }
        return texto;
    }
    
    public void recorrerTablaHorizontal(JTable tabla) {
        int c, r;
        String g;
        int row, column;
        row = tabla.getRowCount();
        column = tabla.getColumnCount();
        for (int i = 0; i < (row - 1); i++) {
            c = 0;
            for (int j = 2; j < (column - 1); j++) {
                g = (String) tabla.getValueAt(i, j);
                if (g == null || g.equals(" ") || g.equals("")) {
                    r = 0;
                    c = c + r;
                } else {
                    r = Integer.parseInt(g);
                    c = c + r;
                    tabla.setValueAt(c, i, column - 1);
                }
            }
        }
    }
    
    public void recorrerTablaVertical(JTable tabla) {
        int c, r;
        String g;
        int row, column;
        row = tabla.getRowCount();
        column = tabla.getColumnCount();
        for (int i = 2; i < (column - 1); i++) {
            c = 0;
            for (int j = 0; j < (row - 1); j++) {
                g = (String) tabla.getValueAt(j, i);
                if (g == null || g.equals(" ") || g.equals("")) {
                    r = 0;
                    c = c + r;
                } else {
                    r = Integer.parseInt(g);
                    c = c + r;
                    tabla.setValueAt(c, row - 1, i);
                }
            }
        }
    }
    
    public int llenarTScrap(JTable tab) {
        int row, column, t, t2 = 0, ro, r2 = 0;
        String tp, to;
        row = tab.getRowCount();
        column = tab.getColumnCount();
        //////////////// vertical ///////////////////////
        for (int i = 0; i < row - 1; i++) {

            if (tab.getValueAt(i, column - 1) == null
                    || tab.getValueAt(i, column - 1).equals(" ") || tab.getValueAt(i, column - 1).equals("")) {
                t = 0;
            } else {
                //tp=(String) tab.getValueAt(i, column-1);
                t = (int) tab.getValueAt(i, column - 1);
                t2 = t2 + t;
            }
        }
        /////////////////// horizontal ///////////////////
        for (int j = 2; j < column - 1; j++) {
            if (tab.getValueAt(row - 1, j) == null || tab.getValueAt(row - 1, j).equals(" ")
                    || tab.getValueAt(row - 1, j).equals("")) {
                ro = 0;
            } else {
                //to=(String) tab.getValueAt(row-1, j);
                ro = (int) tab.getValueAt(row - 1, j);
                r2 = r2 + ro;
            }
        }
        ////////////////////////////////////////////////
        if (t2 == r2) {
            tab.setValueAt(t2, row - 1, column - 1);
            return t2;
        } else {
            JOptionPane.showMessageDialog(null, "ERROR");
        }
        return t2;
    }
    
    public void limpiar(First_windowRBP first_windowRBP, Second_windowRBP sw, StopView stw) {
        //////////////primer ventana//////////////
        first_windowRBP.linenumber_fw.setText(null);
        first_windowRBP.supervisor_fw.setText(null);
        first_windowRBP.manufacturingorder_fw.setText(null);
        first_windowRBP.MOG_fw.setText(null);
        first_windowRBP.article_fw.setText(null);
        first_windowRBP.drawingnumber_fw.setText(null);
        first_windowRBP.process_fw.setText(null);
        first_windowRBP.partNumber.setText(null);
        /////////////segunda ventana//////////////
        sw.jDateChooserDateSW.setDate(null);
        sw.jTextFieldCodeSW.setText(null);
        sw.jTextFieldNameSW.setText(null);
        sw.jTextFieldStartSW.setText(null);
        sw.jTextFieldEndSW.setText(null);
        sw.jTextFieldTotalSW.setText(null);
        sw.jComboBoxTurn.removeAllItems();
        sw.jComboBoxTurn.addItem("Selecciona");
        sw.jComboBoxTurn.addItem("Turno 1");
        sw.jComboBoxTurn.addItem("Turno 2");
        sw.jComboBoxTurn.addItem("Turno 3");
        //sw.jTextFieldSobraInicial.setText(null);
        sw.jTextFieldRSW1.setText(null);
        sw.jTextFieldFSW1.setText(null);
        sw.jTextFieldCSW1.setText(null);
        sw.jTextFieldCantSW1.setText(null);
        sw.jTextFieldRSW2.setText(null);
        sw.jTextFieldFSW2.setText(null);
        sw.jTextFieldCSW2.setText(null);
        sw.jTextFieldRSW3.setText(null);
        sw.jTextFieldFSW3.setText(null);
        sw.jTextFieldSSW4.setText(null);
        ////////////////////ventan de paro//////////////////
        stw.jTextFieldStartStW.setText(null);
        stw.jComboBoxCatego.removeAllItems();
        /*
        stw.jComboBoxCatego.addItem("Selecciona Categoría");
        stw.jComboBoxCatego.addItem("Dandori");
        stw.jComboBoxCatego.addItem("Ajustes");
        stw.jComboBoxCatego.addItem("Problema de Mantenimiento");
        stw.jComboBoxCatego.addItem("Paro Planeado");*/
        stw.jComboBoxRazon.removeAllItems();
        stw.jComboBoxRazon.addItem("Selecciona...");
        //////////////////////////////////////////////
    }
    
    public String formatoFechaParoLinea(String fecha_inicio) throws ParseException{
        SimpleDateFormat originalFormat = new SimpleDateFormat("dd-MM-yyyy"); 
        // Nuevo formato "año, mes, día" 
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");
        // Parsear la fecha en el formato original 
        Date date = originalFormat.parse(fecha_inicio); 
        // Formatear la fecha en el nuevo formato 
        String fecha = newFormat.format(date);
        return fecha;
    }
    
    public String formatoFecha(Date date) {
        DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        String date_format = dateformat.format(date);
        return date_format;
    }
     
    public String horaActual() throws SQLException {
        String hora = null;
        Statement sen;
        con = conexionMySQL();
        ResultSet res;
        sen = con.createStatement();
        try{
        res = sen.executeQuery("SELECT NOW()");
        if (res.next()) {
            Timestamp timestamp = res.getTimestamp(1);
            Date date = new Date(timestamp.getTime());

            // Obtener la hora con formato:
            DateFormat hourFormat = new SimpleDateFormat("HH:mm");
            hora = hourFormat.format(date);
        }
        res.close();
        sen.close();
        con.close();
        }catch (SQLException ex) {
            Logger.getLogger(SecondWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        return hora;
    }
    
    public String obtenerFechaHora() throws SQLException {
        String fecha = null;
        String hora = null;
        Statement sen;
        Connection con;
        con = conexionMySQL();
        ResultSet res;
        sen = con.createStatement();
        try{
        res = sen.executeQuery("SELECT NOW()");
        if (res.next()) {
            Timestamp timestamp = res.getTimestamp(1);
            Date date = new Date(timestamp.getTime());

            // Obtener la fecha y hora con formato:
            DateFormat hourdateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            fecha = hourdateFormat.format(date);
            
        }
        res.close();
        sen.close();
        con.close();
        }catch (SQLException ex) {
            Logger.getLogger(SecondWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fecha;
       
    }
    
    public String obtenerFecha() throws SQLException {
        String fecha = null;
        Statement sen;
        Connection con;
        con = conexionMySQL();
        ResultSet res;
        sen = con.createStatement();
        try{
        res = sen.executeQuery("SELECT NOW()");
        if (res.next()) {
            Timestamp timestamp = res.getTimestamp(1);
            Date date = new Date(timestamp.getTime());

            // Obtener la fecha y hora con formato:
            DateFormat hourdateFormat = new SimpleDateFormat("yyyy-MM-dd");
            fecha = hourdateFormat.format(date);
            
        }
        res.close();
        sen.close();
        con.close();
        }catch (SQLException ex) {
            Logger.getLogger(SecondWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fecha;
       
    }
    
    public String minutosTotales(String vinicio, String vfinal){
 
            Date dinicio = null, dfinal = null;
            long milis1, milis2, diff;
 
            SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm");
 
            try {
                    // PARSEO STRING A DATE
                    dinicio = sdf.parse(vinicio);
                    dfinal = sdf.parse(vfinal);                    
                   
            } catch (ParseException e) {
 
                    System.out.println("Se ha producido un error en el parseo");
            }
           
            //INSTANCIA DEL CALENDARIO GREGORIANO
            Calendar cinicio = Calendar.getInstance();
            Calendar cfinal = Calendar.getInstance();
 
            //ESTABLECEMOS LA FECHA DEL CALENDARIO CON EL DATE GENERADO ANTERIORMENTE
             cinicio.setTime(dinicio);
             cfinal.setTime(dfinal);
 
             milis1 = cinicio.getTimeInMillis();
             milis2 = cfinal.getTimeInMillis();
             diff = milis2-milis1;

            //calcular la diferencia en segundos
 
            long diffSegundos =  Math.abs (diff / 1000);

            //calcular la diferencia en minutos

            long diffMinutos =  Math.abs (diff / (60 * 1000));


            long restominutos = diffMinutos%60;

            // calcular la diferencia en horas

            long diffHoras =   (diff / (60 * 60 * 1000));

            // calcular la diferencia en dias

            long diffdias = Math.abs ( diff / (24 * 60 * 60 * 1000) );

            String minutos_totales = String.valueOf(diffMinutos);

                   return minutos_totales;
    }
    
     

   
     
    
}
