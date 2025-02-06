/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import View.DASRegisterTHMaquinado;
import View.First_windowRBP;
import View.Second_windowRBP;
import java.awt.Color;
import java.sql.CallableStatement;
import java.sql.Connection;
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
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;

/**
 *
 * @author DMM-ADMIN
 */
public class SecondWindow {
    String grupo;
    public void traerPiezasAnteriores(Metods m, String mog, Second_windowRBP sw) {
        int cant = 0, t;
        Connection con;
        con = m.conexionMySQL();
        try {
            CallableStatement cst = con.prepareCall("{call traerPiezas(?,?)}");
            cst.setString(1, mog);
            cst.registerOutParameter(2, java.sql.Types.INTEGER);
            ResultSet r = cst.executeQuery();
            t = cst.getInt(2);
            if (t != 0) {
                while (r.next()) {
                    cant = r.getInt("total_piezas_aprobadas");
                }
                sw.jTextFieldTPiecesSW.setText(String.valueOf(cant));
            } else {
                sw.jTextFieldTPiecesSW.setText("0");
            }
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(LoginWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    

    public void traerPiezasAnterioresBushUltimaVista(Metods m, String mog, JTextField txt) {
        int cant = 0, t;
        Connection con;
        con = m.conexionMySQL();
        try {
            CallableStatement cst = con.prepareCall("{call traerPiezas(?,?)}");
            cst.setString(1, mog);
            cst.registerOutParameter(2, java.sql.Types.INTEGER);
            ResultSet r = cst.executeQuery();
            t = cst.getInt(2);
            if (t != 0) {
                while (r.next()) {
                    cant = r.getInt("total_piezas_aprobadas");
                }
                txt.setText(String.valueOf(cant));
            } else {
                txt.setText("0");
            }
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(LoginWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public Date sumarRestarDiasFecha(Date fecha, int dias) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha); // Configuramos la fecha que se recibe
        calendar.add(Calendar.DAY_OF_YEAR, dias);  // numero de días a añadir, o restar en caso de días<0
        return calendar.getTime(); // Devuelve el objeto Date con los nuevos días añadidos
    }

    
    public void maxPrensa(int cantP, int cantpro) {
        double max;

        max = cantP * 1.5;

        if (max < cantpro) {
            JOptionPane.showMessageDialog(null, "La cantidad de piezas procesadas excede el valor permitido.\n Revise los datos ");
        } else {

        }
    }

    public int validarMaterial(JTextField j, Metods m, String lot) {
        Connection con, cone;
        con = m.oracle();
        cone = m.conexionMySQL();
        Statement sen;
        ResultSet res;
        int val = 0;
        String no_parte = "";
        String orden = j.getText();

        String lotERP = null;

        try {
            sen = con.createStatement();
            res = sen.executeQuery("SELECT T$PDNO, TRIM(T$SITM) FROM BAANLN.tticst001500 WHERE T$PDNO ='" + lot + "'");

            while (res.next()) {
                lotERP = res.getString(2);
            }
            if (lotERP.equals(orden)) {
                val = 1;
            } else {
                val = 0;
            }

            if (val == 0) {
                Statement stm = cone.createStatement();
                ResultSet rst = stm.executeQuery("SELECT * FROM `mog` WHERE id_mog=(SELECT registro_rbp.mog_id_mog FROM registro_rbp WHERE registro_rbp.orden_manufactura='" + lot + "')");
                while (rst.next()) {
                    no_parte = rst.getString("no_parte");
                }

                Statement stm1 = cone.createStatement();
                ResultSet rst1 = stm1.executeQuery("SELECT * FROM `bom_doble` WHERE id_numPart=(SELECT catalogo_no_parte.id_no_parte FROM catalogo_no_parte WHERE catalogo_no_parte.no_parte ='" + no_parte + "')");
                while (rst1.next()) {
                    String bom = rst1.getString("descripcionBOM");
                    if (bom.equals(orden)) {
                        val = 1;
                    }
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(SecondWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        return val;
    }
    
    public void getOperator(String numero_empleado, Second_windowRBP second_window_rbp, Metods metods) {
        String nombre_operador = "";
        Connection con;
        con = metods.conexionMySQL();
        try {
            CallableStatement cst = con.prepareCall("{call buscarOperador(?,?)}");
            cst.setString(1, numero_empleado);
            cst.registerOutParameter(2, java.sql.Types.VARCHAR);
            cst.executeQuery();
            nombre_operador=cst.getString(2);

            if (nombre_operador == null) {
                JOptionPane.showMessageDialog(null, "Datos incorrectos");
                second_window_rbp.jTextFieldCodeSW.setText("");
                second_window_rbp.jTextFieldCodeSW.requestFocus();
            } else {
                second_window_rbp.jTextFieldNameSW.setText(nombre_operador);
                UIManager UI = new UIManager();
                UI.put("OptionPane.background", Color.WHITE);
                UI.put("Panel.background", Color.WHITE);
                JOptionPane.showMessageDialog(null, "<html><font color=\"black\"> RECUERDA LLENAR LA HOJA DE ACTIVIDAD DIARIA (DAS) </font></html>", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                second_window_rbp.jTextFieldRSW1.requestFocus();
            }
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(LoginWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String horaActual(Metods metods) throws SQLException {
        String hora = null;
        Statement sen;
        Connection con;
        con = metods.conexionMySQL();
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

    public void agregarFechaHora(Metods metods, Second_windowRBP swrbp, DASRegisterTHMaquinado das_register_maquinado) throws SQLException {
        String fecha = null;
        String hora = null;
        Statement sen;
        Connection con;
        con = metods.conexionMySQL();
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

            // Obtener la fecha con formato:
            DateFormat hourdateFormat = new SimpleDateFormat("dd-MM-yyyy");
            fecha = hourdateFormat.format(date);
            
            // Agregar la hora a la ventana de llenado
            swrbp.jTextFieldStartSW.setText(hora);
            // Agregar la fecha a la ventada de llenado
            swrbp.jDateChooserDateSW.setDate(new Date());
            // Agregar la fecha a la ventana DAS
            das_register_maquinado.jLabelFechaDas.setText(fecha);
        }
        res.close();
        sen.close();
        con.close();
        }catch (SQLException ex) {
            Logger.getLogger(SecondWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void traerGrupoAsignado(Metods metods, String linea_ingresada, DASRegisterTHMaquinado das_register_maquinado){
        Connection con;
        con = metods.conexionMySQL();
        try {
            CallableStatement cst = con.prepareCall("{call traerGrupoLinea(?,?)}");
            cst.setString(1, linea_ingresada);
            cst.registerOutParameter(2, java.sql.Types.VARCHAR);
            cst.executeQuery();
            grupo=cst.getString(2);

            if (grupo == null) {
                JOptionPane.showMessageDialog(null, "Datos incorrectos");
            } else {
                das_register_maquinado.jLabelGrupo.setText(grupo);
            }
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(SecondWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String getGrupoEncontrado() {
        return grupo;
    }

}
