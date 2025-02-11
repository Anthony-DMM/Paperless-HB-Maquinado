/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Vista.ReporteBitacoraBush;
import Vista.ReporteBitacoraMaquinado;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author DMM-ADMIN
 */
public class ModelBitacoraMaquinado {

    public void consultaYLlenadoDeTablasHBPlatinado(MetodosConnection m, ReporteBitacoraMaquinado reportesVista) {
        Connection connectionDB = m.conexionMySQL2();

        String fecha_inicio;
        String fecha_fin;
        String wc, orden, mm, colu, defe;
        String tiempo = null;
        int cant;
        String nuevaFecha;
        Date fech;

        Date fi = new Date();
        Date ff = new Date();
        DateFormat hourFormat = new SimpleDateFormat("yyyy-MM-dd");

        DateFormat dayformat = new SimpleDateFormat("dd/MM/yyyy");
        fi = reportesVista.jDateChooserInicio.getDate();
        ff = reportesVista.jDateChooserFin.getDate();

        if (fi == null || ff == null) {
            JOptionPane.showMessageDialog(null, "Debes de seleccionar alguna fecha para buscar");
        } else {
            fecha_inicio = hourFormat.format(fi);
            fecha_fin = hourFormat.format(ff);
            DefaultTableModel dtm = new DefaultTableModel();
            reportesVista.jTableReporte.setRowHeight(20);
            dtm.addColumn("MOG");
            dtm.addColumn("NoParte MOG");
            dtm.addColumn("Lote");
            dtm.addColumn("OP");
            dtm.addColumn("MOG");
            dtm.addColumn("OP");
            dtm.addColumn("WC");
            dtm.addColumn("Fecha final");
            dtm.addColumn("");
            dtm.addColumn("");
            dtm.addColumn("Piezas procesadas");
            dtm.addColumn("Piezas aprobadas");
            dtm.addColumn("Horas trabajadas");
            dtm.addColumn("Minutos trabajados");
            dtm.addColumn("");
            dtm.addColumn("");
            dtm.addColumn("");
            dtm.addColumn("");
            dtm.addColumn("");
            dtm.addColumn("");
            dtm.addColumn("");
            dtm.addColumn("QTY. SCRAP");
            dtm.addColumn("SCRAP HOJA AZUL");
            dtm.addColumn("QTY PIEZAS VERIF.");
            reportesVista.jTableReporte.setModel(dtm);
            reportesVista.jTableReporte.setAutoResizeMode(0);
            for (int i = 0; i < reportesVista.jTableReporte.getColumnCount(); i++) {
                reportesVista.jTableReporte.getColumnModel().getColumn(i).setPreferredWidth(180);
            }
            try {

                CallableStatement cst = connectionDB.prepareCall("call reporteBitacoraPlatinado(?,?)");
                cst.setString(1, fecha_inicio);
                cst.setString(2, fecha_fin);
                ResultSet rst = cst.executeQuery();
                while (rst.next()) {
                    String[] data = new String[24];
                    data[0] = rst.getString("mog");
                    String noNuevo = rst.getString("no_parte");
                    String nuNe = noNuevo.replace("-", "'");
                    data[1] = nuNe;
                    data[2] = rst.getString("loteTM");
                    data[3] = rst.getString("orden_manufactura");
                    data[4] = rst.getString("mog2");
                    data[5] = rst.getString("po2");
                    wc = rst.getString("linea");

                    data[6] = wc.replace("TG", "");

                    orden = validarOrden(rst.getString("orden_manufactura"));
                    if (orden.equals("PLT")) {
                        String completo = rst.getString("no_parte");
                        nuNe = completo.replace("-", "'");
                        data[1] = completo + "'TG";
                    }
                    if (orden.equals("HBL")) {
                        String completo = rst.getString("no_parte");
                        nuNe = completo.replace("-", "'");
                        data[1] = completo + "'TH";
                    }
                    if (orden.equals("BFO")) {
                        String completo = rst.getString("no_parte");
                        nuNe = completo.replace("-", "'");
                        data[1] = completo + "'TF";
                    }

                    fech = rst.getDate("fecha");
                    nuevaFecha = dayformat.format(fech);
                    data[7] = nuevaFecha;
                    data[8] = "";
                    data[9] = "";
                    data[10] = rst.getString("totalpiezas_procesadas");
                    data[11] = rst.getString("total_piezas_aprobadas");
                    tiempo = totalTiempo(rst.getString("orden_manufactura"), connectionDB, rst.getString("mog"), wc);
                    String[] hraDiv = tiempo.split(":");
                    data[12] = hraDiv[0];
                    data[13] = hraDiv[1];
                    data[14] = "";
                    data[15] = "";
                    data[16] = "";
                    data[17] = "";
                    data[18] = "";
                    data[19] = "";
                    data[20] = "";
                    data[21] = rst.getString("total_scrap");
                    data[22] = null;
                    data[23] = rst.getString("verificacion");
                    dtm.addRow(data);
                }

            } catch (SQLException e) {
                System.err.println("Error " + e.getMessage());
            }
        }
    }

    public void consultaYLlenadoDeTablasHBMaquinado(MetodosConnection m, ReporteBitacoraMaquinado reportesVista) {
        Connection connectionDB = m.conexionMySQL2();

        String fecha_inicio;
        String fecha_fin;
        String wc, orden, mm, colu, defe;
        String tiempo = null;
        int cant;
        String nuevaFecha;
        Date fech;

        Date fi = new Date();
        Date ff = new Date();
        DateFormat hourFormat = new SimpleDateFormat("yyyy-MM-dd");

        DateFormat dayformat = new SimpleDateFormat("dd/MM/yyyy");
        fi = reportesVista.jDateChooserInicio.getDate();
        ff = reportesVista.jDateChooserFin.getDate();

        if (fi == null || ff == null) {
            JOptionPane.showMessageDialog(null, "Debes de seleccionar alguna fecha para buscar");
        } else {
            fecha_inicio = hourFormat.format(fi);
            fecha_fin = hourFormat.format(ff);
            DefaultTableModel dtm = new DefaultTableModel();
            reportesVista.jTableReporte.setRowHeight(20);
            dtm.addColumn("MOG");
            dtm.addColumn("NoParte MOG");
            dtm.addColumn("Lote");
            dtm.addColumn("OP");
            dtm.addColumn("MOG");
            dtm.addColumn("OP");
            dtm.addColumn("WC");
            dtm.addColumn("Fecha final");
            dtm.addColumn("");
            dtm.addColumn("");
            dtm.addColumn("Piezas procesadas");
            dtm.addColumn("Piezas aprobadas");
            dtm.addColumn("Horas trabajadas");
            dtm.addColumn("Minutos trabajados");
            dtm.addColumn("");
            dtm.addColumn("");
            dtm.addColumn("");
            dtm.addColumn("");
            dtm.addColumn("");
            dtm.addColumn("");
            dtm.addColumn("");
            dtm.addColumn("QTY. SCRAP");
            dtm.addColumn("SCRAP HOJA AZUL");
            dtm.addColumn("QTY VERIF. CALCULOS");
            dtm.addColumn("QTY VERIF. PROCESOS");
            reportesVista.jTableReporte.setModel(dtm);
            reportesVista.jTableReporte.setAutoResizeMode(0);
            for (int i = 0; i < reportesVista.jTableReporte.getColumnCount(); i++) {
                reportesVista.jTableReporte.getColumnModel().getColumn(i).setPreferredWidth(180);
            }
            try {

                CallableStatement cst = connectionDB.prepareCall("call reporteBitacoraMaquinado(?,?)");
                cst.setString(1, fecha_inicio);
                cst.setString(2, fecha_fin);
                ResultSet rst = cst.executeQuery();
                while (rst.next()) {
                    String[] data = new String[25];
                    data[0] = rst.getString("mog");
                    String noNuevo = rst.getString("no_parte");
                    String nuNe = noNuevo.replace("-", "'");
                    data[1] = nuNe;
                    data[2] = rst.getString("loteTM");
                    data[3] = rst.getString("orden_manufactura");
                    data[4] = rst.getString("mog2");
                    data[5] = rst.getString("po2");
                    wc = rst.getString("linea");

                    data[6] = wc.replace("TH", "");

                    orden = validarOrden(rst.getString("orden_manufactura"));
                    if (orden.equals("PLT")) {
                        String completo = rst.getString("no_parte");
                        nuNe = completo.replace("-", "'");
                        data[1] = completo + "'TG";
                    }
                    if (orden.equals("HBL")) {
                        String completo = rst.getString("no_parte");
                        nuNe = completo.replace("-", "'");
                        data[1] = completo + "'TH";
                    }
                    if (orden.equals("BFO")) {
                        String completo = rst.getString("no_parte");
                        nuNe = completo.replace("-", "'");
                        data[1] = completo + "'TF";
                    }

                    fech = rst.getDate("fecha");
                    nuevaFecha = dayformat.format(fech);
                    data[7] = nuevaFecha;
                    data[8] = "";
                    data[9] = "";
                    data[10] = rst.getString("total_piezas_recibidas");
                    data[11] = rst.getString("total_piezas_aprobadas");
                    tiempo = totalTiempo(rst.getString("orden_manufactura"), connectionDB, rst.getString("mog"), wc);
                    String[] hraDiv = tiempo.split(":");
                    data[12] = hraDiv[0];
                    data[13] = hraDiv[1];
                    data[14] = "";
                    data[15] = "";
                    data[16] = "";
                    data[17] = "";
                    data[18] = "";
                    data[19] = "";
                    data[20] = "";
                    data[21] = rst.getString("total_scrap");
                    data[22] = null;
                    data[23] = rst.getString("verificacion");
                    data[24] = rst.getString("verificacion2");
                    dtm.addRow(data);
                }

            } catch (SQLException e) {
                System.err.println("Error " + e.getMessage());
            }
        }
    }

    public void consultaYLlenadoDeTablas(MetodosConnection m, ReporteBitacoraMaquinado reportesVista) {
        Connection connectionDB = m.conexionMySQL();

        String fecha_inicio;
        String fecha_fin;
        String wc, orden, mm, colu, defe;
        String tiempo = null;
        int cant;
        String nuevaFecha;
        Date fech;

        Date fi = new Date();
        Date ff = new Date();
        DateFormat hourFormat = new SimpleDateFormat("yyyy-MM-dd");

        DateFormat dayformat = new SimpleDateFormat("dd/MM/yyyy");
        fi = reportesVista.jDateChooserInicio.getDate();
        ff = reportesVista.jDateChooserFin.getDate();

        if (fi == null || ff == null) {
            JOptionPane.showMessageDialog(null, "Debes de seleccionar alguna fecha para buscar");
        } else {
            fecha_inicio = hourFormat.format(fi);
            fecha_fin = hourFormat.format(ff);
            DefaultTableModel dtm = new DefaultTableModel();
            reportesVista.jTableReporte.setRowHeight(20);
            dtm.addColumn("MOG");
            dtm.addColumn("NoParte MOG");
            dtm.addColumn("Lote");
            dtm.addColumn("OP");
            dtm.addColumn("MOG");
            dtm.addColumn("OP");
            dtm.addColumn("WC");
            dtm.addColumn("Fecha final");
            dtm.addColumn("");
            dtm.addColumn("");
            dtm.addColumn("Piezas procesadas");
            dtm.addColumn("Piezas aprobadas");
            dtm.addColumn("Horas trabajadas");
            dtm.addColumn("Minutos trabajados");
            dtm.addColumn("");
            dtm.addColumn("");
            dtm.addColumn("");
            dtm.addColumn("");
            dtm.addColumn("");
            dtm.addColumn("");
            dtm.addColumn("");
            dtm.addColumn("QTY. SCRAP");
            dtm.addColumn("SCRAP HOJA AZUL");
            dtm.addColumn("QTY PIEZAS VERIF.");
            reportesVista.jTableReporte.setModel(dtm);
            reportesVista.jTableReporte.setAutoResizeMode(0);
            for (int i = 0; i < reportesVista.jTableReporte.getColumnCount(); i++) {
                reportesVista.jTableReporte.getColumnModel().getColumn(i).setPreferredWidth(180);
            }
            try {

                CallableStatement cst = connectionDB.prepareCall("call reporteBitacoraMaquinado(?,?)");
                cst.setString(1, fecha_inicio);
                cst.setString(2, fecha_fin);
                ResultSet rst = cst.executeQuery();
                while (rst.next()) {
                    String[] data = new String[24];
                    data[0] = rst.getString("mog");
                    String noNuevo = rst.getString("no_parte");
                    String nuNe = noNuevo.replace("-", "'");
                    data[1] = nuNe;
                    data[2] = rst.getString("loteTM");
                    data[3] = rst.getString("orden_manufactura");
                    data[4] = rst.getString("mog2");
                    data[5] = rst.getString("po2");
                    wc = rst.getString("linea");
                    if (wc.equals("TG03")) {
                        data[6] = "3";
                    }
                    if (wc.equals("TG02")) {
                        data[6] = "2";
                    }
                    if (wc.equals("TG01")) {
                        data[6] = "1";
                    }
                    if (wc.equals("TGP01")) {
                        data[6] = "P1";
                    }
                    if (wc.equals("TB02F")) {
                        data[6] = "2";
                    }
                    if (wc.equals("TB03F")) {
                        data[6] = "3";
                    }
                    orden = validarOrden(rst.getString("orden_manufactura"));
                    if (orden.equals("PLT")) {
                        String completo = rst.getString("no_parte");
                        nuNe = completo.replace("-", "'");
                        data[1] = completo + "'TG";
                    }
                    if (orden.equals("HBL")) {
                        String completo = rst.getString("no_parte");
                        nuNe = completo.replace("-", "'");
                        data[1] = completo + "'TH";
                    }
                    if (orden.equals("BFO")) {
                        String completo = rst.getString("no_parte");
                        nuNe = completo.replace("-", "'");
                        data[1] = completo + "'TF";
                    }

                    fech = fechaUltima(rst.getString("orden_manufactura"), m);
                    nuevaFecha = dayformat.format(fech);
                    data[7] = nuevaFecha;
                    data[8] = "";
                    data[9] = "";
                    data[10] = rst.getString("totalpiezas_procesadas");
                    data[11] = rst.getString("total_piezas_aprobadas");
                    tiempo = totalTiempo(rst.getString("orden_manufactura"), connectionDB, rst.getString("mog"), wc);
                    String[] hraDiv = tiempo.split(":");
                    data[12] = hraDiv[0];
                    data[13] = hraDiv[1];
                    data[14] = "";
                    data[15] = "";
                    data[16] = "";
                    data[17] = "";
                    data[18] = "";
                    data[19] = "";
                    data[20] = "";
                    data[21] = rst.getString("total_scrap");
                    data[22] = null;
                    data[23] = rst.getString("verificacion");
                    dtm.addRow(data);
                }

            } catch (SQLException e) {
                System.err.println("Error " + e.getMessage());
            }
        }
    }

    public void consultaYLlenadoDeTablasNuevo(MetodosConnection m, ReporteBitacoraBush reportesVista) {
        Connection connectionDB = m.conexionMySQL();

        String fecha_inicio;
        String fecha_fin;
        String wc, orden, mm, colu, defe;
        String tiempo = null;
        int cant;
        String nuevaFecha;
        Date fech;

        Date fi = new Date();
        Date ff = new Date();
        DateFormat hourFormat = new SimpleDateFormat("yyyy-MM-dd");

        DateFormat dayformat = new SimpleDateFormat("dd/MM/yyyy");
        fi = reportesVista.jDateChooserInicio.getDate();
        ff = reportesVista.jDateChooserFin.getDate();

        if (fi == null || ff == null) {
            JOptionPane.showMessageDialog(null, "Debes de seleccionar alguna fecha para buscar");
        } else {
            fecha_inicio = hourFormat.format(fi);
            fecha_fin = hourFormat.format(ff);
            DefaultTableModel dtm = new DefaultTableModel();
            reportesVista.jTableReporte.setRowHeight(20);
            dtm.addColumn("MOG");
            dtm.addColumn("NoParte MOG");
            dtm.addColumn("Lote");
            dtm.addColumn("OP");
            dtm.addColumn("MOG");
            dtm.addColumn("OP");
            dtm.addColumn("WC");
            dtm.addColumn("Fecha final");
            dtm.addColumn("");
            dtm.addColumn("");
            dtm.addColumn("Piezas procesadas");
            dtm.addColumn("Piezas aprobadas");
            dtm.addColumn("Horas trabajadas");
            dtm.addColumn("Minutos trabajados");
            dtm.addColumn("");
            dtm.addColumn("");
            dtm.addColumn("");
            dtm.addColumn("");
            dtm.addColumn("");
            dtm.addColumn("");
            dtm.addColumn("");
            dtm.addColumn("QTY. SCRAP");
            dtm.addColumn("SCRAP HOJA AZUL");
            dtm.addColumn("QTY PIEZAS VERIF.");
            reportesVista.jTableReporte.setModel(dtm);
            reportesVista.jTableReporte.setAutoResizeMode(0);
            for (int i = 0; i < reportesVista.jTableReporte.getColumnCount(); i++) {
                reportesVista.jTableReporte.getColumnModel().getColumn(i).setPreferredWidth(180);
            }
            try {

                CallableStatement cst = connectionDB.prepareCall("call reporteBitacoraMaquinadoBushruthp(?,?)");
                cst.setString(1, fecha_inicio);
                cst.setString(2, fecha_fin);
                ResultSet rst = cst.executeQuery();
                while (rst.next()) {
                    String[] data = new String[24];
                    data[0] = rst.getString("mog");
                    String noNuevo = rst.getString("no_parte");
                    String nuNe = noNuevo.replace("-", "'");
                    data[1] = nuNe;
                    data[2] = rst.getString("loteTM");
                    data[3] = rst.getString("orden_manufactura");
                    data[4] = rst.getString("mog2");
                    data[5] = rst.getString("po2");
                    wc = rst.getString("linea");
                    if (wc.equals("TG03")) {
                        data[6] = "3";
                    }
                    if (wc.equals("TG02")) {
                        data[6] = "2";
                    }
                    if (wc.equals("TG01")) {
                        data[6] = "1";
                    }
                    if (wc.equals("TGP01")) {
                        data[6] = "P1";
                    }
                    if (wc.equals("TB31")) {
                        data[6] = "31";
                    }
                    if (wc.equals("TB32")) {
                        data[6] = "32";
                    }
                    if (wc.equals("TB71")) {
                        data[6] = "71";
                    }
                    if (wc.equals("TB51")) {
                        data[6] = "51";
                    }
                    orden = validarOrden(rst.getString("orden_manufactura"));
                    if (orden.equals("PLT")) {
                        String completo = rst.getString("no_parte");
                        nuNe = completo.replace("-", "'");
                        data[1] = completo + "'TG";
                    }
                    if (orden.equals("HBL")) {
                        String completo = rst.getString("no_parte");
                        nuNe = completo.replace("-", "'");
                        data[1] = completo + "'TH";
                    }
                    if (orden.equals("BCO")) {
                        String completo = rst.getString("no_parte");
                        nuNe = completo.replace("-", "'");
                        data[1] = completo + "'TCO";
                    }
                    if (orden.equals("BCH")) {
                        String completo = rst.getString("no_parte");
                        nuNe = completo.replace("-", "'");
                        data[1] = completo + "'TCH";
                    }
                    if (orden.equals("BGR")) {
                        String completo = rst.getString("no_parte");
                        nuNe = completo.replace("-", "'");
                        data[1] = completo + "'TGR";
                    }
                    fech = fechaUltima(rst.getString("orden_manufactura"), m);
                    nuevaFecha = dayformat.format(fech);
                    data[7] = nuevaFecha;
                    data[8] = "";
                    data[9] = "";
                    data[10] = rst.getString("totalpiezas_procesadas");
                    data[11] = rst.getString("total_piezas_aprobadas");
                    String jidj = rst.getString("orden_manufactura");
                    /*if (orden.equals("BCO") && rst.getString("loteTM") != null) {
                        if (rst.getString("orden_manufactura").equals("BCO001767")) {
                            int val = 0;
                            val = validarProduccioncontinua(m, rst.getString("orden_manufactura"));
                            if (val == 1) {
                                tiempo = tiempoRealCoining(rst.getString("orden_manufactura"), rst.getString("mog"), m);
                            } else {
                                tiempo = totalTiempo(rst.getString("orden_manufactura"), connectionDB, rst.getString("mog"), wc);
                            }
                        } else {
                            int val = 0;
                            val = validarProduccioncontinua(m, rst.getString("orden_manufactura"));
                            if (val == 1) {
                                tiempo = tiempoRealCoining(rst.getString("orden_manufactura"), rst.getString("mog"), m);
                            } else {
                                tiempo = totalTiempo(rst.getString("orden_manufactura"), connectionDB, rst.getString("mog"), wc);
                            }
                        }
                    } else {
                        tiempo = totalTiempo(rst.getString("orden_manufactura"), connectionDB, rst.getString("mog"), wc);
                    }*/

                    tiempo = totalTiempo(rst.getString("orden_manufactura"), connectionDB, rst.getString("mog"), wc);
                    String[] hraDiv = tiempo.split(":");
                    data[12] = hraDiv[0];
                    data[13] = hraDiv[1];
                    data[14] = "";
                    data[15] = "";
                    data[16] = "";
                    data[17] = "";
                    data[18] = "";
                    data[19] = "";
                    data[20] = "";
                    data[21] = rst.getString("total_scrap");
                    data[22] = null;
                    data[23] = rst.getString("verificacion");
                    dtm.addRow(data);
                }

            } catch (SQLException e) {
                System.err.println("Error " + e.getMessage());
            }
        }
    }
    
    public void consultaYLlenadoDeTablasByMOGHB(MetodosConnection m, ReporteBitacoraMaquinado reportesVista) {
        Connection connectionDB = m.conexionMySQL2();

        String fecha_inicio;
        String fecha_fin,orden1;
        String wc, orden, mm, colu, defe;
        String tiempo = null;
        int cant;
        String nuevaFecha;
        Date fech;

        DateFormat dayformat = new SimpleDateFormat("dd/MM/yyyy");

        String fi3;
        fi3 = reportesVista.jTextFieldBusquedaMOG.getText();
        DefaultTableModel dtm = new DefaultTableModel();
        reportesVista.jTableReporte.setRowHeight(20);
        dtm.addColumn("MOG");
        dtm.addColumn("NoParte MOG");
        dtm.addColumn("Lote");
        dtm.addColumn("OP");
        dtm.addColumn("MOG");
        dtm.addColumn("OP");
        dtm.addColumn("WC");
        dtm.addColumn("Fecha final");
        dtm.addColumn("");
        dtm.addColumn("");
        dtm.addColumn("Piezas procesadas");
        dtm.addColumn("Piezas aprobadas");
        dtm.addColumn("Horas trabajadas");
        dtm.addColumn("Minutos trabajados");
        dtm.addColumn("");
        dtm.addColumn("");
        dtm.addColumn("");
        dtm.addColumn("");
        dtm.addColumn("");
        dtm.addColumn("");
        dtm.addColumn("");
        dtm.addColumn("QTY. SCRAP");
        dtm.addColumn("SCRAP HOJA AZUL");
        dtm.addColumn("QTY VERIF. CALCULOS");
        dtm.addColumn("QTY VERIF. PROCESOS");
        reportesVista.jTableReporte.setModel(dtm);
        reportesVista.jTableReporte.setAutoResizeMode(0);
        for (int i = 0; i < reportesVista.jTableReporte.getColumnCount(); i++) {
            reportesVista.jTableReporte.getColumnModel().getColumn(i).setPreferredWidth(180);
        }
        try {

            CallableStatement cst = connectionDB.prepareCall("call reporteBitacoraMaquinado2(?)");
            cst.setString(1, "%" + fi3 + "%");
            ResultSet rst = cst.executeQuery();
            while (rst.next()) {
                String[] data = new String[25];
                data[0] = rst.getString("mog");
                String noNuevo = rst.getString("no_parte");
                String nuNe = noNuevo.replace("-", "'");
                data[1] = nuNe;
                data[2] = rst.getString("loteTM");
                data[3] = rst.getString("orden_manufactura");
                data[4] = rst.getString("mog2");
                data[5] = rst.getString("po2");
                wc = rst.getString("linea");
                orden1=rst.getString("orden_manufactura");
                
                
                if(orden1.contains("PLT")){
                    data[6] = wc.replace("TG", "");
                }else{
                    data[6] = wc.replace("TH", "");
                }
                
                orden = validarOrden(rst.getString("orden_manufactura"));
                if (orden.equals("PLT")) {
                    String completo = rst.getString("no_parte");
                    nuNe = completo.replace("-", "'");
                    data[1] = completo + "'TG";
                }
                if (orden.equals("HBL")) {
                    String completo = rst.getString("no_parte");
                    nuNe = completo.replace("-", "'");
                    data[1] = completo + "'TH";
                }
                if (orden.equals("BFO")) {
                    String completo = rst.getString("no_parte");
                    nuNe = completo.replace("-", "'");
                    data[1] = completo + "'TF";
                }
                fech = fechaUltima2(rst.getString("orden_manufactura"), m);
                nuevaFecha = dayformat.format(fech);
                data[7] = nuevaFecha;
                data[8] = "";
                data[9] = "";
                data[10] = rst.getString("total_piezas_recibidas");
                data[11] = rst.getString("total_piezas_aprobadas");

                tiempo = totalTiempo(rst.getString("orden_manufactura"), connectionDB, rst.getString("mog"), wc);

                String[] hraDiv = tiempo.split(":");
                data[12] = hraDiv[0];
                data[13] = hraDiv[1];
                data[14] = "";
                data[15] = "";
                data[16] = "";
                data[17] = "";
                data[18] = "";
                data[19] = "";
                data[20] = "";
                data[21] = rst.getString("total_scrap");
                data[22] = null;
                data[23] = rst.getString("verificacion");
                data[24] = rst.getString("verificacion2");
                dtm.addRow(data);
            }

        } catch (SQLException e) {
            System.err.println("Error " + e.getMessage());
        }
    }

    public void consultaYLlenadoDeTablasByMOG(MetodosConnection m, ReporteBitacoraMaquinado reportesVista) {
        Connection connectionDB = m.conexionMySQL();

        String fecha_inicio;
        String fecha_fin;
        String wc, orden, mm, colu, defe;
        String tiempo = null;
        int cant;
        String nuevaFecha;
        Date fech;

        DateFormat dayformat = new SimpleDateFormat("dd/MM/yyyy");

        String fi3;
        fi3 = reportesVista.jTextFieldBusquedaMOG.getText();
        DefaultTableModel dtm = new DefaultTableModel();
        reportesVista.jTableReporte.setRowHeight(20);
        dtm.addColumn("MOG");
        dtm.addColumn("NoParte MOG");
        dtm.addColumn("Lote");
        dtm.addColumn("OP");
        dtm.addColumn("MOG");
        dtm.addColumn("OP");
        dtm.addColumn("WC");
        dtm.addColumn("Fecha final");
        dtm.addColumn("");
        dtm.addColumn("");
        dtm.addColumn("Piezas procesadas");
        dtm.addColumn("Piezas aprobadas");
        dtm.addColumn("Horas trabajadas");
        dtm.addColumn("Minutos trabajados");
        dtm.addColumn("");
        dtm.addColumn("");
        dtm.addColumn("");
        dtm.addColumn("");
        dtm.addColumn("");
        dtm.addColumn("");
        dtm.addColumn("");
        dtm.addColumn("QTY. SCRAP");
        dtm.addColumn("SCRAP HOJA AZUL");
        dtm.addColumn("QTY PIEZAS VERIF.");
        reportesVista.jTableReporte.setModel(dtm);
        reportesVista.jTableReporte.setAutoResizeMode(0);
        for (int i = 0; i < reportesVista.jTableReporte.getColumnCount(); i++) {
            reportesVista.jTableReporte.getColumnModel().getColumn(i).setPreferredWidth(180);
        }
        try {

            CallableStatement cst = connectionDB.prepareCall("call reporteBitacoraMaquinado2(?)");
            cst.setString(1, "%" + fi3 + "%");
            ResultSet rst = cst.executeQuery();
            while (rst.next()) {
                String[] data = new String[24];
                data[0] = rst.getString("mog");
                String noNuevo = rst.getString("no_parte");
                String nuNe = noNuevo.replace("-", "'");
                data[1] = nuNe;
                data[2] = rst.getString("loteTM");
                data[3] = rst.getString("orden_manufactura");
                data[4] = rst.getString("mog2");
                data[5] = rst.getString("po2");
                wc = rst.getString("linea");
                if (wc.equals("TG03")) {
                    data[6] = "3";
                }
                if (wc.equals("TG02")) {
                    data[6] = "2";
                }
                if (wc.equals("TG01")) {
                    data[6] = "1";
                }
                if (wc.equals("TGP01")) {
                    data[6] = "P1";
                }
                if (wc.equals("TB02F")) {
                    data[6] = "2";
                }
                if (wc.equals("TB03F")) {
                    data[6] = "3";
                }

                orden = validarOrden(rst.getString("orden_manufactura"));
                if (orden.equals("PLT")) {
                    String completo = rst.getString("no_parte");
                    nuNe = completo.replace("-", "'");
                    data[1] = completo + "'TG";
                }
                if (orden.equals("HBL")) {
                    String completo = rst.getString("no_parte");
                    nuNe = completo.replace("-", "'");
                    data[1] = completo + "'TH";
                }
                if (orden.equals("BFO")) {
                    String completo = rst.getString("no_parte");
                    nuNe = completo.replace("-", "'");
                    data[1] = completo + "'TF";
                }
                fech = fechaUltima(rst.getString("orden_manufactura"), m);
                nuevaFecha = dayformat.format(fech);
                data[7] = nuevaFecha;
                data[8] = "";
                data[9] = "";
                data[10] = rst.getString("totalpiezas_procesadas");
                data[11] = rst.getString("total_piezas_aprobadas");

                tiempo = totalTiempo(rst.getString("orden_manufactura"), connectionDB, rst.getString("mog"), wc);

                String[] hraDiv = tiempo.split(":");
                data[12] = hraDiv[0];
                data[13] = hraDiv[1];
                data[14] = "";
                data[15] = "";
                data[16] = "";
                data[17] = "";
                data[18] = "";
                data[19] = "";
                data[20] = "";
                data[21] = rst.getString("total_scrap");
                data[22] = null;
                data[23] = rst.getString("verificacion");
                dtm.addRow(data);
            }

        } catch (SQLException e) {
            System.err.println("Error " + e.getMessage());
        }
    }

    public Date fechaUltima2(String orden, MetodosConnection m) {
        Connection con = m.conexionMySQL2();
        Date rg = null;
        try {
            CallableStatement cst = con.prepareCall("{call traerUltimaFecha(?,?)}");
            cst.setString(1, orden);
            cst.registerOutParameter(2, java.sql.Types.DATE);
            cst.executeQuery();
            rg = cst.getDate(2);
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(ModelBitacoraEmpaque.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rg;
    }
    
    public void consultaYLlenadoDeTablasByMOGnuevo(MetodosConnection m, ReporteBitacoraBush reportesVista) {
        Connection connectionDB = m.conexionMySQL();

        String fecha_inicio;
        String fecha_fin;
        String wc, orden, mm, colu, defe;
        String tiempo = null;
        int cant;
        String nuevaFecha;
        Date fech;

        DateFormat dayformat = new SimpleDateFormat("dd/MM/yyyy");

        String fi3;
        fi3 = reportesVista.jTextFieldBusquedaMOG.getText();
        DefaultTableModel dtm = new DefaultTableModel();
        reportesVista.jTableReporte.setRowHeight(20);
        dtm.addColumn("MOG");
        dtm.addColumn("NoParte MOG");
        dtm.addColumn("Lote");
        dtm.addColumn("OP");
        dtm.addColumn("MOG");
        dtm.addColumn("OP");
        dtm.addColumn("WC");
        dtm.addColumn("Fecha final");
        dtm.addColumn("");
        dtm.addColumn("");
        dtm.addColumn("Piezas procesadas");
        dtm.addColumn("Piezas aprobadas");
        dtm.addColumn("Horas trabajadas");
        dtm.addColumn("Minutos trabajados");
        dtm.addColumn("");
        dtm.addColumn("");
        dtm.addColumn("");
        dtm.addColumn("");
        dtm.addColumn("");
        dtm.addColumn("");
        dtm.addColumn("");
        dtm.addColumn("QTY. SCRAP");
        dtm.addColumn("SCRAP HOJA AZUL");
        dtm.addColumn("QTY PIEZAS VERIF.");
        reportesVista.jTableReporte.setModel(dtm);
        reportesVista.jTableReporte.setAutoResizeMode(0);
        for (int i = 0; i < reportesVista.jTableReporte.getColumnCount(); i++) {
            reportesVista.jTableReporte.getColumnModel().getColumn(i).setPreferredWidth(180);
        }
        try {

            CallableStatement cst = connectionDB.prepareCall("call reporteBitacoraMaquinadoBushruthp2(?)");
            cst.setString(1, "%" + fi3 + "%");
            ResultSet rst = cst.executeQuery();
            while (rst.next()) {
                String[] data = new String[24];
                data[0] = rst.getString("mog");
                String noNuevo = rst.getString("no_parte");
                String nuNe = noNuevo.replace("-", "'");
                data[1] = nuNe;
                data[2] = rst.getString("loteTM");
                data[3] = rst.getString("orden_manufactura");
                data[4] = rst.getString("mog2");
                data[5] = rst.getString("po2");
                wc = rst.getString("linea");
                if (wc.equals("TG03")) {
                    data[6] = "3";
                }
                if (wc.equals("TG02")) {
                    data[6] = "2";
                }
                if (wc.equals("TG01")) {
                    data[6] = "1";
                }
                if (wc.equals("TGP01")) {
                    data[6] = "P1";
                }
                if (wc.equals("TB31")) {
                    data[6] = "31";
                }
                if (wc.equals("TB32")) {
                    data[6] = "32";
                }
                if (wc.equals("TB71")) {
                    data[6] = "71";
                }
                if (wc.equals("TB51")) {
                    data[6] = "51";
                }
                orden = validarOrden(rst.getString("orden_manufactura"));
                if (orden.equals("PLT")) {
                    String completo = rst.getString("no_parte");
                    nuNe = completo.replace("-", "'");
                    data[1] = completo + "'TG";
                }
                if (orden.equals("HBL")) {
                    String completo = rst.getString("no_parte");
                    nuNe = completo.replace("-", "'");
                    data[1] = completo + "'TH";
                }
                if (orden.equals("BCO")) {
                    String completo = rst.getString("no_parte");
                    nuNe = completo.replace("-", "'");
                    data[1] = completo + "'TCO";
                }
                if (orden.equals("BCH")) {
                    String completo = rst.getString("no_parte");
                    nuNe = completo.replace("-", "'");
                    data[1] = completo + "'TCH";
                }
                if (orden.equals("BGR")) {
                    String completo = rst.getString("no_parte");
                    nuNe = completo.replace("-", "'");
                    data[1] = completo + "'TGR";
                }
                fech = fechaUltima(rst.getString("orden_manufactura"), m);
                nuevaFecha = dayformat.format(fech);
                data[7] = nuevaFecha;
                data[8] = "";
                data[9] = "";
                data[10] = rst.getString("totalpiezas_procesadas");
                data[11] = rst.getString("total_piezas_aprobadas");

                /*if (orden.equals("BCO")) {
                    if (rst.getString("orden_manufactura").equals("BCO001775")) {
                        int val = 0;
                        val = validarProduccioncontinua(m, rst.getString("orden_manufactura"));
                        if (val == 1) {
                            tiempo = tiempoRealCoining(rst.getString("orden_manufactura"), rst.getString("mog"), m);
                        } else {
                            tiempo = totalTiempo(rst.getString("orden_manufactura"), connectionDB, rst.getString("mog"), wc);
                        }
                    } else {
                        int val = 0;
                        val = validarProduccioncontinua(m, rst.getString("orden_manufactura"));
                        if (val == 1) {
                            tiempo = tiempoRealCoining(rst.getString("orden_manufactura"), rst.getString("mog"), m);
                        } else {
                            tiempo = totalTiempo(rst.getString("orden_manufactura"), connectionDB, rst.getString("mog"), wc);
                        }
                    }
                } else {
                    tiempo = totalTiempo(rst.getString("orden_manufactura"), connectionDB, rst.getString("mog"), wc);
                }*/
                
                tiempo = totalTiempo(rst.getString("orden_manufactura"), connectionDB, rst.getString("mog"), wc);

                String[] hraDiv = tiempo.split(":");
                data[12] = hraDiv[0];
                data[13] = hraDiv[1];
                data[14] = "";
                data[15] = "";
                data[16] = "";
                data[17] = "";
                data[18] = "";
                data[19] = "";
                data[20] = "";
                data[21] = rst.getString("total_scrap");
                data[22] = null;
                data[23] = rst.getString("verificacion");
                dtm.addRow(data);
            }

        } catch (SQLException e) {
            System.err.println("Error " + e.getMessage());
        }

    }

    public String ultimoProceso(MetodosConnection m, ReporteBitacoraBush reportesVista) {
        String last = null;
        Connection connectionDB = m.conexionMySQL();
        String fi3, ultim;

        fi3 = reportesVista.jTextFieldBusquedaMOG.getText();
        String[] data = new String[5];
        try {
            CallableStatement cst = connectionDB.prepareCall("call traerProcesos(?)");
            cst.setString(1, fi3);
            ResultSet rst = cst.executeQuery();
            while (rst.next()) {
                data[0] = rst.getString("no_parte");
                data[1] = rst.getString("forming");
                data[2] = rst.getString("coining");
                data[3] = rst.getString("chamfer");
                data[4] = rst.getString("grinding");
            }

            if (data[4].equals("1")) {
                last = "GRINDING";
            } else {
                if (data[3].equals("1")) {
                    last = "CHAMFER";
                } else {
                    if (data[2].equals("1")) {
                        last = "COINING";
                    } else {
                        last = "FORMING";
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error " + e.getMessage());
        }
        return last;
    }

    public String ultimoProceso2(MetodosConnection m, String mg) {
        String last = null;
        Connection connectionDB = m.conexionMySQL();
        String ultim;

        String[] data = new String[5];
        try {
            CallableStatement cst = connectionDB.prepareCall("call traerProcesos2(?)");
            cst.setString(1, mg);
            ResultSet rst = cst.executeQuery();
            while (rst.next()) {
                data[0] = rst.getString("no_parte");
                data[1] = rst.getString("forming");
                data[2] = rst.getString("coining");
                data[3] = rst.getString("chamfer");
                data[4] = rst.getString("grinding");
            }

            if (data[4].equals("1")) {
                last = "GRINDING";
            } else {
                if (data[3].equals("1")) {
                    last = "CHAMFER";
                } else {
                    if (data[2].equals("1")) {
                        last = "COINING";
                    } else {
                        last = "FORMING";
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error " + e.getMessage());
        }
        return last;
    }

    public String tiemposASumar(MetodosConnection m, ReporteBitacoraMaquinado reportesVista) {
        Connection connectionDB = m.conexionMySQL();
        String fi3 = reportesVista.jTextFieldBusquedaMOG.getText();
        String[] data = new String[20];
        int[] horas = new int[20];
        int[] minutos = new int[20];
        int horasSuma = 0, conversion, minutosSuma = 0, totalTotal;
        int idPo;
        int c = 0;
        String total = "";
        try {
            CallableStatement cst = connectionDB.prepareCall("call traerIdsOP(?)");
            cst.setString(1, fi3);
            ResultSet rst = cst.executeQuery();
            while (rst.next()) {
                idPo = rst.getInt("id_registro_rbp");

                CallableStatement cst1 = connectionDB.prepareCall("call traerTiempos(?)");
                cst1.setInt(1, idPo);
                ResultSet rst1 = cst1.executeQuery();
                while (rst1.next()) {
                    data[c] = rst1.getString("tiempito");
                    System.out.println(data[c]);
                    c++;
                }
            }

            for (int h = 0; h < 20; h++) {
                if (data[h] == null) {

                } else {
                    horas[h] = traerHoras(data[h]);
                    minutos[h] = traerMinutos(data[h]);
                }

            }

            for (int l = 0; l < 20; l++) {
                horasSuma = horasSuma + horas[l];
            }

            conversion = horasSuma * 60;

            for (int f = 0; f < 20; f++) {
                minutosSuma = minutosSuma + minutos[f];
            }

            totalTotal = conversion + minutosSuma;

            total = formatearMinutosAHoraMinuto(totalTotal);

        } catch (SQLException e) {
            System.err.println("Error " + e.getMessage());
        }
        return total;
    }

    public String tiemposASumarBush(MetodosConnection m, ReporteBitacoraBush reportesVista) {
        Connection connectionDB = m.conexionMySQL();
        String fi3 = reportesVista.jTextFieldBusquedaMOG.getText();
        String[] data = new String[20];
        int[] horas = new int[20];
        int[] minutos = new int[20];
        int horasSuma = 0, conversion, minutosSuma = 0, totalTotal;
        int idPo;
        int c = 0;
        String total = "";
        try {
            CallableStatement cst = connectionDB.prepareCall("call traerIdsOP2(?)");
            cst.setString(1, fi3);
            ResultSet rst = cst.executeQuery();
            while (rst.next()) {
                idPo = rst.getInt("id_registro_rbp");

                CallableStatement cst1 = connectionDB.prepareCall("call traerTiempos(?)");
                cst1.setInt(1, idPo);
                ResultSet rst1 = cst1.executeQuery();
                while (rst1.next()) {
                    data[c] = rst1.getString("tiempito");
                    System.out.println(data[c]);
                    c++;
                }
            }

            for (int h = 0; h < 20; h++) {
                if (data[h] == null) {

                } else {
                    horas[h] = traerHoras(data[h]);
                    minutos[h] = traerMinutos(data[h]);
                }

            }

            for (int l = 0; l < 20; l++) {
                horasSuma = horasSuma + horas[l];
            }

            conversion = horasSuma * 60;

            for (int f = 0; f < 20; f++) {
                minutosSuma = minutosSuma + minutos[f];
            }

            totalTotal = conversion + minutosSuma;

            total = formatearMinutosAHoraMinuto(totalTotal);

        } catch (SQLException e) {
            System.err.println("Error " + e.getMessage());
        }
        return total;
    }

    public String tiemposASumarBush2(MetodosConnection m, String mg) {
        Connection connectionDB = m.conexionMySQL();
        //String fi3 = reportesVista.jTextFieldBusquedaMOG.getText();
        String[] data = new String[20];
        int[] horas = new int[20];
        int[] minutos = new int[20];
        int horasSuma = 0, conversion, minutosSuma = 0, totalTotal;
        int idPo;
        int c = 0;
        String total = "";
        try {
            CallableStatement cst = connectionDB.prepareCall("call traerIdsOP3(?)");
            cst.setString(1, mg);
            ResultSet rst = cst.executeQuery();
            while (rst.next()) {
                idPo = rst.getInt("id_registro_rbp");

                CallableStatement cst1 = connectionDB.prepareCall("call traerTiempos(?)");
                cst1.setInt(1, idPo);
                ResultSet rst1 = cst1.executeQuery();
                while (rst1.next()) {
                    data[c] = rst1.getString("tiempito");
                    System.out.println(data[c]);
                    c++;
                }
            }

            for (int h = 0; h < 20; h++) {
                if (data[h] == null) {

                } else {
                    horas[h] = traerHoras(data[h]);
                    minutos[h] = traerMinutos(data[h]);
                }

            }

            for (int l = 0; l < 20; l++) {
                horasSuma = horasSuma + horas[l];
            }

            conversion = horasSuma * 60;

            for (int f = 0; f < 20; f++) {
                minutosSuma = minutosSuma + minutos[f];
            }

            totalTotal = conversion + minutosSuma;

            total = formatearMinutosAHoraMinuto(totalTotal);

        } catch (SQLException e) {
            System.err.println("Error " + e.getMessage());
        }
        return total;
    }

    public void consultaBushByMOG(MetodosConnection m, ReporteBitacoraBush reportesVista) {
        Connection connectionDB = m.conexionMySQL();
        String wc;

        String fi3, ultim, tiempo;

        int totalScrap = 0;

        DateFormat dayformat = new SimpleDateFormat("dd/MM/yyyy");

        fi3 = reportesVista.jTextFieldBusquedaMOG.getText();
        DefaultTableModel dtm = new DefaultTableModel();
        reportesVista.jTableReporte.setRowHeight(20);
        dtm.addColumn("MOG");
        dtm.addColumn("NoParte MOG");
        dtm.addColumn("Lote");
        dtm.addColumn("OP");
        dtm.addColumn("MOG");
        dtm.addColumn("OP");
        dtm.addColumn("WC");
        dtm.addColumn("Fecha final");
        dtm.addColumn("");
        dtm.addColumn("");
        dtm.addColumn("Piezas procesadas");
        dtm.addColumn("Piezas aprobadas");
        dtm.addColumn("Horas trabajadas");
        dtm.addColumn("Minutos trabajados");
        dtm.addColumn("");
        dtm.addColumn("");
        dtm.addColumn("");
        dtm.addColumn("");
        dtm.addColumn("");
        dtm.addColumn("");
        dtm.addColumn("");
        dtm.addColumn("QTY. SCRAP");
        dtm.addColumn("SCRAP HOJA AZUL");
        dtm.addColumn("QTY PIEZAS VERIF.");
        reportesVista.jTableReporte.setModel(dtm);
        reportesVista.jTableReporte.setAutoResizeMode(0);
        for (int i = 0; i < reportesVista.jTableReporte.getColumnCount(); i++) {
            reportesVista.jTableReporte.getColumnModel().getColumn(i).setPreferredWidth(160);
        }
        try {

            ultim = ultimoProceso(m, reportesVista);

            CallableStatement cst2 = connectionDB.prepareCall("call valdiacionBushReport(?,?,?)");
            cst2.setString(1, ultim);
            cst2.setString(2, fi3);
            cst2.registerOutParameter(3, java.sql.Types.INTEGER);
            cst2.executeQuery();
            int valida = cst2.getInt("val");

            if (valida == 1) {
                JOptionPane.showMessageDialog(null, "Aún no han liberado todos los procesos de la orden");
            } else {
                CallableStatement cst = connectionDB.prepareCall("call predatos(?,?,?,?,?,?,?,?,?,?)");
                cst.setString(1, fi3);
                cst.registerOutParameter(2, java.sql.Types.VARCHAR);
                cst.registerOutParameter(3, java.sql.Types.VARCHAR);
                cst.registerOutParameter(4, java.sql.Types.VARCHAR);
                cst.registerOutParameter(5, java.sql.Types.VARCHAR);
                cst.registerOutParameter(6, java.sql.Types.VARCHAR);
                cst.registerOutParameter(7, java.sql.Types.VARCHAR);
                cst.registerOutParameter(8, java.sql.Types.VARCHAR);
                cst.setString(9, ultim);
                cst.registerOutParameter(10, java.sql.Types.DATE);
                cst.executeQuery();
                String[] data = new String[24];
                String mmg = cst.getString("mg");
                data[0] = mmg;
                String noNuevo = cst.getString("num_part");
                String nuNe;
                if (noNuevo.contains("-")) {
                    nuNe = noNuevo.replace("-", "'");
                } else {
                    nuNe = cst.getString("num_part");
                }

                data[1] = nuNe + "'TF";
                data[2] = cst.getString("lot");
                data[3] = cst.getString("orden");
                data[4] = cst.getString("mg");
                data[5] = cst.getString("orden");
                wc = cst.getString("WC");
                if (wc.equals("TB02F")) {
                    data[6] = "2";
                }
                if (wc.equals("TB03F")) {
                    data[6] = "3";
                }

                String nuevaFecha = dayformat.format(cst.getDate("fech"));
                data[7] = nuevaFecha;
                data[8] = "";
                data[9] = "";
                data[10] = cst.getString("piezaspro");
                data[11] = cst.getString("piezasgood");

                tiempo = tiemposASumarBush(m, reportesVista);

                String[] hraDiv = tiempo.split(":");
                data[12] = hraDiv[0];
                data[13] = hraDiv[1];
                data[14] = "";
                data[15] = "";
                data[16] = "";
                data[17] = "";
                data[18] = "";
                data[19] = "";
                data[20] = "";

                CallableStatement cc = connectionDB.prepareCall("call TraerScrapTotalBushReport(?)");
                cc.setString(1, mmg);
                ResultSet r = cc.executeQuery();
                while (r.next()) {
                    int idReg = r.getInt(1);
                    CallableStatement cc1 = connectionDB.prepareCall("call sumaScrapReportBush(?,?)");
                    cc1.setInt(1, idReg);
                    cc1.registerOutParameter(2, java.sql.Types.INTEGER);
                    cc1.execute();
                    int tt = cc1.getInt("tot");
                    totalScrap = totalScrap + tt;
                }
                data[21] = String.valueOf(totalScrap);
                data[22] = "";
                data[23] = "";
                dtm.addRow(data);
            }

        } catch (SQLException e) {
            System.err.println("Error " + e.getMessage());
        }
    }

    public void consultaBushByFechas(MetodosConnection m, ReporteBitacoraBush reportesVista) {
        Connection connectionDB = m.conexionMySQL();
        String wc;
        String fecha_inicio;
        String fecha_fin;
        String fi3 = null, ultim;

        int totalScrap = 0;

        Date fi = new Date();
        Date ff = new Date();
        DateFormat hourFormat = new SimpleDateFormat("yyyy-MM-dd");

        DateFormat dayformat = new SimpleDateFormat("dd/MM/yyyy");
        fi = reportesVista.jDateChooserInicio.getDate();
        ff = reportesVista.jDateChooserFin.getDate();

        if (fi == null || ff == null) {
            JOptionPane.showMessageDialog(null, "Debes de seleccionar alguna fecha para buscar");
        } else {

            //fi3 = reportesVista.jTextFieldBusquedaMOG.getText();
            fecha_inicio = hourFormat.format(fi);
            fecha_fin = hourFormat.format(ff);
            DefaultTableModel dtm = new DefaultTableModel();
            reportesVista.jTableReporte.setRowHeight(20);
            dtm.addColumn("MOG");
            dtm.addColumn("NoParte MOG");
            dtm.addColumn("Lote");
            dtm.addColumn("OP");
            dtm.addColumn("MOG");
            dtm.addColumn("OP");
            dtm.addColumn("WC");
            dtm.addColumn("Fecha final");
            dtm.addColumn("");
            dtm.addColumn("");
            dtm.addColumn("Piezas procesadas");
            dtm.addColumn("Piezas aprobadas");
            dtm.addColumn("Horas trabajadas");
            dtm.addColumn("Minutos trabajados");
            dtm.addColumn("");
            dtm.addColumn("");
            dtm.addColumn("");
            dtm.addColumn("");
            dtm.addColumn("");
            dtm.addColumn("");
            dtm.addColumn("");
            dtm.addColumn("QTY. SCRAP");
            dtm.addColumn("SCRAP HOJA AZUL");
            dtm.addColumn("QTY PIEZAS VERIF.");
            reportesVista.jTableReporte.setModel(dtm);
            reportesVista.jTableReporte.setAutoResizeMode(0);
            for (int i = 0; i < reportesVista.jTableReporte.getColumnCount(); i++) {
                reportesVista.jTableReporte.getColumnModel().getColumn(i).setPreferredWidth(160);
            }
            try {

                CallableStatement cstF = connectionDB.prepareCall("call reporteBitacoraBushMOG(?,?)");
                cstF.setString(1, fecha_inicio);
                cstF.setString(2, fecha_fin);
                ResultSet rstF = cstF.executeQuery();
                while (rstF.next()) {
                    fi3 = rstF.getString("mog");

                    ultim = ultimoProceso2(m, fi3);

                    CallableStatement cst2 = connectionDB.prepareCall("call valdiacionBushReport2(?,?,?)");
                    cst2.setString(1, ultim);
                    cst2.setString(2, fi3);
                    cst2.registerOutParameter(3, java.sql.Types.INTEGER);
                    cst2.executeQuery();
                    int valida = cst2.getInt("val");

                    if (valida == 1) {
                        //JOptionPane.showMessageDialog(null, "Aún no han liberado todos los procesos de la orden");
                    } else {
                        CallableStatement cst = connectionDB.prepareCall("call predatos2(?,?,?,?,?,?,?,?,?,?)");
                        cst.setString(1, fi3);
                        cst.registerOutParameter(2, java.sql.Types.VARCHAR);
                        cst.registerOutParameter(3, java.sql.Types.VARCHAR);
                        cst.registerOutParameter(4, java.sql.Types.VARCHAR);
                        cst.registerOutParameter(5, java.sql.Types.VARCHAR);
                        cst.registerOutParameter(6, java.sql.Types.VARCHAR);
                        cst.registerOutParameter(7, java.sql.Types.VARCHAR);
                        cst.registerOutParameter(8, java.sql.Types.VARCHAR);
                        cst.setString(9, ultim);
                        cst.registerOutParameter(10, java.sql.Types.DATE);
                        cst.executeQuery();
                        String[] data = new String[24];
                        String mmg = cst.getString("mg");
                        data[0] = mmg;
                        String noNuevo = cst.getString("num_part");
                        String nuNe;
                        if (noNuevo.contains("-")) {
                            nuNe = noNuevo.replace("-", "'");
                        } else {
                            nuNe = cst.getString("num_part");
                        }

                        data[1] = nuNe + "'TF";
                        data[2] = cst.getString("lot");
                        data[3] = cst.getString("orden");
                        data[4] = cst.getString("mg");
                        data[5] = cst.getString("orden");
                        wc = cst.getString("WC");
                        if (wc.equals("TB02F")) {
                            data[6] = "2";
                        }
                        if (wc.equals("TB03F")) {
                            data[6] = "3";
                        }

                        String nuevaFecha = dayformat.format(cst.getDate("fech"));
                        data[7] = nuevaFecha;
                        data[8] = "";
                        data[9] = "";
                        data[10] = cst.getString("piezaspro");
                        data[11] = cst.getString("piezasgood");
                        String tiempo = tiemposASumarBush2(m, fi3);
                        String[] hraDiv = tiempo.split(":");
                        data[12] = hraDiv[0];
                        data[13] = hraDiv[1];
                        data[14] = "";
                        data[15] = "";
                        data[16] = "";
                        data[17] = "";
                        data[18] = "";
                        data[19] = "";
                        data[20] = "";

                        CallableStatement cc = connectionDB.prepareCall("call TraerScrapTotalBushReport(?)");
                        cc.setString(1, mmg);
                        ResultSet r = cc.executeQuery();
                        while (r.next()) {
                            int idReg = r.getInt(1);
                            CallableStatement cc1 = connectionDB.prepareCall("call sumaScrapReportBush(?,?)");
                            cc1.setInt(1, idReg);
                            cc1.registerOutParameter(2, java.sql.Types.INTEGER);
                            cc1.execute();
                            int tt = cc1.getInt("tot");
                            totalScrap = totalScrap + tt;
                        }
                        data[21] = String.valueOf(totalScrap);
                        data[22] = "";
                        data[23] = "";
                        dtm.addRow(data);
                    }
                }

            } catch (SQLException e) {
                System.err.println("Error " + e.getMessage());
            }
        }
    }

    public String totalTiempo(String orden, Connection con, String mog, String linea) {
        String total = "";
        String[] data = new String[15];
        int[] horas = new int[15];
        int[] minutos = new int[15];
        int horasSuma = 0, conversion, minutosSuma = 0, totalTotal;
        try {
            CallableStatement cst = con.prepareCall("call tiempoTotalOrden(?)");
            cst.setString(1, orden);
            ResultSet r = cst.executeQuery();
            int i = 0;
            /*
            CallableStatement cst1 = con.prepareCall("call minutosParosLinea(?,?,?)");
            cst1.setString(1, linea);
            cst1.setString(2, mog);
            cst1.registerOutParameter(3, java.sql.Types.INTEGER);
            cst1.executeQuery();
            int retornoMinutos=cst1.getInt(3);
             */
            while (r.next()) {
                data[i] = r.getString("horas_trabajadas");
                System.out.println(data[i]);
                i++;
            }

            for (int h = 0; h < 15; h++) {
                if (data[h] == null) {

                } else {
                    horas[h] = traerHoras(data[h]);
                    minutos[h] = traerMinutos(data[h]);
                }

            }

            for (int l = 0; l < 15; l++) {
                horasSuma = horasSuma + horas[l];
            }

            conversion = horasSuma * 60;

            for (int f = 0; f < 15; f++) {
                minutosSuma = minutosSuma + minutos[f];
            }

            totalTotal = conversion + minutosSuma;

            total = formatearMinutosAHoraMinuto(totalTotal);

        } catch (SQLException ex) {
            Logger.getLogger(ModelBitacoraMaquinado.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total;
    }

    public String formatearMinutosAHoraMinuto(int minutos) {
        String formato = "%02d:%02d";
        long horasReales = TimeUnit.MINUTES.toHours(minutos);
        long minutosReales = TimeUnit.MINUTES.toMinutes(minutos) - TimeUnit.HOURS.toMinutes(TimeUnit.MINUTES.toHours(minutos));
        return String.format(formato, horasReales, minutosReales);
    }

    public int traerMinutos(String cadena) {
        StringBuilder sb = new StringBuilder();
        String p = null;
        int t = 0;
        if (cadena == null || cadena.equals("")) {
            return t;
        } else {
            for (int i = 0; i < cadena.length(); i++) {
                char letra = cadena.charAt(i);
                if (letra == ':') {
                    for (int o = i + 1; o < cadena.length(); o++) {
                        sb.append(cadena.charAt(o));
                    }
                    p = sb.toString();

                } else {

                }
            }
            t = Integer.valueOf(p);
            return t;
        }
    }

    public int traerHoras(String var) {
        String[] parts = var.split(":");
        var = parts[0]; //trae del - hacia atras
        //var= parts[1]; trae el resto del string 
        int var1 = Integer.parseInt(var);
        return var1;
    }

    public int convertirMinutosAHoras(int minutos) {
        int totalHoras;
        totalHoras = minutos / 60;
        return totalHoras;
    }

    public String medir(String cadena) {
        StringBuilder sb = new StringBuilder();
        String p = null;
        for (int i = 0; i < cadena.length(); i++) {
            char letra = cadena.charAt(i);
            if (letra == '0') {
                for (int o = i + 1; o < cadena.length(); o++) {
                    sb.append(cadena.charAt(o));
                }
                p = sb.toString();
            } else {

            }
        }
        //int t = Integer.valueOf(p);
        return p;
    }

    public String validarOrden(String var) {
        String[] parts = var.split("0");
        var = parts[0]; //trae del - hacia atras
        //var= parts[1]; trae el resto del string 
        return var;
    }

    public Date fechaUltima(String orden, MetodosConnection m) {
        Connection con = m.conexionMySQL();
        Date rg = null;
        try {
            CallableStatement cst = con.prepareCall("{call traerUltimaFecha(?,?)}");
            cst.setString(1, orden);
            cst.registerOutParameter(2, java.sql.Types.DATE);
            cst.executeQuery();
            rg = cst.getDate(2);
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(ModelBitacoraEmpaque.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rg;
    }

    public void limpiarTablaMachining(ReporteBitacoraMaquinado reportesVista) {
        DefaultTableModel dtm = new DefaultTableModel();
        reportesVista.jTableReporte.setModel(dtm);
        reportesVista.jTableReporte.setAutoResizeMode(0);
        reportesVista.jDateChooserInicio.setDate(null);
        reportesVista.jDateChooserFin.setDate(null);
        reportesVista.jTextFieldBusquedaMOG.setText(null);
    }

    public void limpiarTablaBushing(ReporteBitacoraBush reportesVista) {
        DefaultTableModel dtm = new DefaultTableModel();
        reportesVista.jTableReporte.setModel(dtm);
        reportesVista.jTableReporte.setAutoResizeMode(0);
        reportesVista.jTextFieldBusquedaMOG.setText(null);
    }

    public int validarProduccioncontinua(MetodosConnection m, String order) {
        int res = 0;
        Connection con = m.conexionMySQL();
        String horaInC, horaFinF, horaInF;
        Date fechaInC, fechaFinF, fechaInF;
        try {

            CallableStatement cst = con.prepareCall("call validarArranqueBush(?,?,?,?,?,?,?)");
            cst.setString(1, order);
            cst.registerOutParameter(2, java.sql.Types.VARCHAR);
            cst.registerOutParameter(3, java.sql.Types.VARCHAR);
            cst.registerOutParameter(4, java.sql.Types.DATE);
            cst.registerOutParameter(5, java.sql.Types.DATE);
            cst.registerOutParameter(6, java.sql.Types.DATE);
            cst.registerOutParameter(7, java.sql.Types.VARCHAR);
            cst.execute();
            horaFinF = cst.getString(2);
            horaInC = cst.getString(3);
            fechaFinF = cst.getDate(4);
            fechaInC = cst.getDate(5);
            fechaInF = cst.getDate(6);
            horaInF = cst.getString(7);

            ////hora forming antes que hora coinign y fecha forming antes que fecha coining///
            if (horaFinF.compareTo(horaInC) < 0 && fechaFinF.compareTo(fechaInC) < 0) {
                res = 0;
            }
            ////hora forming antes que hora coinign y fecha forming despues que fecha coining///
            if (horaFinF.compareTo(horaInC) < 0 && fechaFinF.compareTo(fechaInC) > 0) {
                res = 0;
            }
            ////hora forming antes que hora coinign y fecha forming igual que fecha coining///
            if (horaFinF.compareTo(horaInC) < 0 && fechaFinF.compareTo(fechaInC) == 0) {
                res = 0;
            }
            ////hora forming igual que hora coinign y fecha forming igual que fecha coining///
            if (horaFinF.compareTo(horaInC) == 0 && fechaFinF.compareTo(fechaInC) == 0) {
                res = 0;
            }
            ////hora forming igual que hora coinign y fecha forming antes que fecha coining///
            if (horaFinF.compareTo(horaInC) == 0 && fechaFinF.compareTo(fechaInC) < 0) {
                res = 0;
            }
            ////hora forming igual que hora coinign y fecha forming despues que fecha coining///
            if (horaFinF.compareTo(horaInC) == 0 && fechaFinF.compareTo(fechaInC) > 0) {
                res = 1;
            }
            ////hora forming despues que hora coinign y fecha forming despues que fecha coining///
            if (horaFinF.compareTo(horaInC) > 0 && fechaFinF.compareTo(fechaInC) > 0) {
                res = 1;
            }
            ////hora forming despues que hora coinign y fecha forming antes que fecha coining///
            if (horaFinF.compareTo(horaInC) > 0 && fechaFinF.compareTo(fechaInC) < 0) {
                res = 0;
            }
            ////hora forming despues que hora coinign y fecha forming igual que fecha coining///
            if (horaFinF.compareTo(horaInC) > 0 && fechaFinF.compareTo(fechaInC) == 0) {
                res = 1;
            }

        } catch (SQLException ex) {
            Logger.getLogger(ModelBitacoraMaquinado.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

    public String tiempoRealCoining(String orden, String mg, MetodosConnection m) {
        String valor = "";
        Connection con = m.conexionMySQL();
        String tiempoF, tiempoC, TotalTiempo;
        String fechaCierreF = null, fechaCierreC = null, horaCierreForming = null;
        String comp;

        try {
            CallableStatement cst1 = con.prepareCall("call validarFechasCierre(?,?,?,?)");
            cst1.setString(1, orden);
            cst1.registerOutParameter(2, java.sql.Types.VARCHAR);
            cst1.registerOutParameter(3, java.sql.Types.VARCHAR);
            cst1.registerOutParameter(4, java.sql.Types.VARCHAR);
            cst1.execute();
            fechaCierreF = cst1.getString(2);
            fechaCierreC = cst1.getString(3);
            horaCierreForming = cst1.getString(4);

        } catch (SQLException ex) {
            Logger.getLogger(ModelBitacoraMaquinado.class.getName()).log(Level.SEVERE, null, ex);
        }

        comp = DiferenciaFechas1(fechaCierreF, fechaCierreC);

        if (Integer.parseInt(comp) >= 1440) {
            System.err.println("prueba");
            String total = "";
            try {
                String[] data1 = new String[8];
                String[] data2 = new String[8];
                String[] data = new String[8];
                int[] horas = new int[8];
                int[] minutos = new int[8];
                int valordeID = 0;
                int horasSuma = 0, conversion, minutosSuma = 0, totalTotal;
                CallableStatement cst = con.prepareCall("call traerHoraIniForming(?)");
                cst.setString(1, orden);
                ResultSet r = cst.executeQuery();
                int i = 0;
                while (r.next()) {
                    data[i] = r.getString("horas_trabajadas");
                    data1[i] = r.getString("hora_inicio");
                    data2[i] = r.getString("fecha");
                }

                for (int r1 = 0; r1 < 8; r1++) {
                    String pruebaTiempo = data1[r1];
                    String pruebaFecha = data2[r1];
                    ////hora coining despues que hora forming y fecha coining despues que fecha forming ///
                    if (pruebaTiempo.compareTo(horaCierreForming) > 0 && pruebaFecha.compareTo(fechaCierreF) > 0) {
                        valordeID = r1;
                        break;
                    }
                }

                for (int h = valordeID; h < 8; h++) {
                    if (data[h] == null) {

                    } else {
                        horas[h] = traerHoras(data[h]);
                        minutos[h] = traerMinutos(data[h]);
                    }
                }

                for (int l = 0; l < 8; l++) {
                    horasSuma = horasSuma + horas[l];
                }

                conversion = horasSuma * 60;

                for (int f = 0; f < 8; f++) {
                    minutosSuma = minutosSuma + minutos[f];
                }

                totalTotal = conversion + minutosSuma;

                total = formatearMinutosAHoraMinuto(totalTotal);

            } catch (SQLException ex) {
                Logger.getLogger(ModelBitacoraMaquinado.class.getName()).log(Level.SEVERE, null, ex);
            }
            valor = total;

        } else {
            try {
                CallableStatement cst = con.prepareCall("call tiempoCerradoCoiyFor(?,?,?,?)");
                cst.setString(1, mg);
                cst.setString(2, orden);
                cst.registerOutParameter(3, java.sql.Types.VARCHAR);
                cst.registerOutParameter(4, java.sql.Types.VARCHAR);
                cst.execute();
                tiempoF = cst.getString(4);
                tiempoC = cst.getString(3);
                valor = DiferenciaFechas(tiempoF, tiempoC);
                if (valor.equals("00:00")) {
                    valor = "00:01";
                }
            } catch (SQLException ex) {
                Logger.getLogger(ModelBitacoraMaquinado.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return valor;
    }

    public String DiferenciaFechas(String vinicio, String vfinal) {

        Date dinicio = null, dfinal = null;
        long milis1, milis2, diff;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

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

        diff = milis2 - milis1;

        // calcular la diferencia en segundos
        long diffSegundos = Math.abs(diff / 1000);

        // calcular la diferencia en minutos
        long diffMinutos = Math.abs(diff / (60 * 1000));

        long restominutos = diffMinutos % 60;

        // calcular la diferencia en horas
        long diffHoras = (diff / (60 * 60 * 1000));

        // calcular la diferencia en dias
        long diffdias = Math.abs(diff / (24 * 60 * 60 * 1000));

        /*
     System.out.println("En segundos: " + diffSegundos + " segundos.");
 
     System.out.println("En minutos: " + diffMinutos + " minutos.");
 
     System.out.println("En horas: " + diffHoras + " horas.");
 
     System.out.println("En dias: " + diffdias + " dias.");
         */
        //String devolver = String.valueOf(diffHoras + "H " + restominutos + "m ");
        System.out.println("En minutos: " + diffMinutos + " minutos.");
        String devolver = String.valueOf(diffMinutos);
        devolver = convertir(Integer.valueOf(devolver));

        return devolver;
    }

    public String DiferenciaFechas1(String vinicio, String vfinal) {

        Date dinicio = null, dfinal = null;
        long milis1, milis2, diff;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

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

        diff = milis2 - milis1;

        // calcular la diferencia en segundos
        long diffSegundos = Math.abs(diff / 1000);

        // calcular la diferencia en minutos
        long diffMinutos = Math.abs(diff / (60 * 1000));

        long restominutos = diffMinutos % 60;

        // calcular la diferencia en horas
        long diffHoras = (diff / (60 * 60 * 1000));

        // calcular la diferencia en dias
        long diffdias = Math.abs(diff / (24 * 60 * 60 * 1000));

        /*
     System.out.println("En segundos: " + diffSegundos + " segundos.");
 
     System.out.println("En minutos: " + diffMinutos + " minutos.");
 
     System.out.println("En horas: " + diffHoras + " horas.");
 
     System.out.println("En dias: " + diffdias + " dias.");
         */
        //String devolver = String.valueOf(diffHoras + "H " + restominutos + "m ");
        System.out.println("En minutos: " + diffMinutos + " minutos.");
        String devolver = String.valueOf(diffMinutos);
        //devolver=convertir(Integer.valueOf(devolver));  

        return devolver;
    }

    public String convertir(int t) {
        int hours = t / 60; //since both are ints, you get an int
        int minutes = t % 60;
        String y = String.format("%02d:%02d", hours, minutes);
        return y;
    }

}
