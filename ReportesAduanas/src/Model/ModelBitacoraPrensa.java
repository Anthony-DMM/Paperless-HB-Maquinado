/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Vista.ReporteBitacoraPrensa;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author DMM-ADMIN
 */
public class ModelBitacoraPrensa {

    public void consultaYLlenadoDeTablas4Lot(MetodosConnection m, ReporteBitacoraPrensa reportesVista) {
        Connection connectionDB = m.conexionMySQL();

        String fecha_inicio;
        String fecha_fin;
        String tiempo = null;
        String wc, orden;
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
            dtm.addColumn("OP1");
            dtm.addColumn("MOG");
            dtm.addColumn("OP2");
            dtm.addColumn("WC");
            dtm.addColumn("Fecha final");
            dtm.addColumn("Horas trabajadas");
            dtm.addColumn("Minutos trabajados");
            dtm.addColumn("Piezas aprobadas");
            for (int i = 1; i < 31; i++) {
                dtm.addColumn("Lote " + i);
                dtm.addColumn("Mts " + i);
                dtm.addColumn("End " + i);
            }
            
            reportesVista.jTableReporte.setModel(dtm);
            reportesVista.jTableReporte.setAutoResizeMode(0);
            for (int i = 0; i < reportesVista.jTableReporte.getColumnCount(); i++) {
                reportesVista.jTableReporte.getColumnModel().getColumn(i).setPreferredWidth(180);
            }
            int cc=0;
            try {

                CallableStatement cst = connectionDB.prepareCall("{call reporteBitacoraPrensa(?,?)}");
                cst.setString(1, fecha_inicio);
                cst.setString(2, fecha_fin);
                ResultSet rst = cst.executeQuery();
                while (rst.next()) {
                    Object[] data = new Object[101];
                    String ord = rst.getString("orden_manufactura");
                    int total = 0;
                    cst=connectionDB.prepareCall("{call contarLotes(?)}");
                    cst.setString(1, ord);
                    ResultSet rst12 = cst.executeQuery();
                    int cont=0;
                    while (rst12.next()){
                        total=rst12.getInt("COUNT(lote_coil.lote_coil)");
                        cont ++;
                    }
                    if(cont>4){
                        data[0] = rst.getString("mog");
                        String noNuevo = rst.getString("no_parte");
                        String nuNe = noNuevo.replace("-", "'");
                        data[1] = nuNe;
                        data[2] = rst.getString("loteTM");
                        data[3] = rst.getString("orden_manufactura");
                        data[4] = rst.getString("mog2");
                        data[5] = rst.getString("po2");
                        wc = rst.getString("linea");
                        if (wc.equals("TB01")) {
                            data[6] = "1";
                        }
                        if (wc.equals("TB02B")) {
                            data[6] = "2";
                        }

                        if (wc.equals("TB03B")) {
                            data[6] = "3";
                        }

                        if (wc.equals("TB05")) {
                            data[6] = "5";
                        }

                        if (wc.equals("TB06")) {
                            data[6] = "6";
                        }

                        fech = fechaUltima(rst.getString("orden_manufactura"), m);
                        nuevaFecha = dayformat.format(fech);
                        data[7] = nuevaFecha;
                        tiempo = totalTiempo(rst.getString("orden_manufactura"), m, rst.getString("mog"), wc);
                        String[] hraDiv = tiempo.split(":");
                        data[8] = hraDiv[0];
                        data[9] = hraDiv[1];
                        data[10] = rst.getString("total_piezas_aprobadas");
                        orden = validarOrden(rst.getString("orden_manufactura"));
                        if (orden.equals("PRS")) {
                            String completo = rst.getString("no_parte");
                            nuNe = completo.replace("-", "'");
                            data[1] = completo + "'TP";
                        }
                        if (orden.equals("BHL")) {
                            String completo = rst.getString("no_parte");
                            nuNe = completo.replace("-", "'");
                            data[1] = nuNe + "'TB";
                        } else {
                            nuNe = rst.getString("no_parte").replace("-", "'");
                            data[1] = nuNe;
                        }
                        dtm.addRow(data);
                        if (cc < reportesVista.jTableReporte.getRowCount()) {

                            cst = connectionDB.prepareCall("{call traerCoils1(?)}");
                            cst.setString(1, ord);
                            ResultSet rst1 = cst.executeQuery();
                            int i = 11;
                            int j = 12;
                            int k = 13;
                            while (rst1.next()) {
                                String bobina, cantidad, finalizado;
                                String lot = rst1.getString("coil");
                                String mtr = rst1.getString("metros");
                                int fin = rst1.getInt("finalizado");

                                if (lot.equals("null")) {
                                    bobina = "";
                                } else {
                                    bobina = lot;
                                }
                                if (mtr.equals("null")) {
                                    cantidad = "";
                                } else {
                                    cantidad = mtr;
                                }
                                if (fin == 1) {
                                    finalizado = "X";
                                } else {
                                    finalizado = "";
                                }
                                reportesVista.jTableReporte.setValueAt(bobina, cc, i);
                                reportesVista.jTableReporte.setValueAt(cantidad, cc, j);
                                reportesVista.jTableReporte.setValueAt(finalizado, cc, k);
                                Object k1, k2;
                                k1 = reportesVista.jTableReporte.getValueAt(cc, i);
                                k2 = reportesVista.jTableReporte.getValueAt(cc, j);
                                System.err.println(k1);
                                System.err.println(k2);
                                i = i + 3;
                                j = j + 3;
                                k = k + 3;
                            }
                            cc++;
                        }
                    }
                }
                connectionDB.close();
            } catch (SQLException e) {
                System.err.println("Error " + e.getMessage());
            }
        }
    }
    
    public void consultaYLlenadoDeTablasNormal(MetodosConnection m, ReporteBitacoraPrensa reportesVista) {
        Connection connectionDB = m.conexionMySQL();

        String fecha_inicio;
        String fecha_fin;
        String tiempo = null;
        String wc, orden;
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
            dtm.addColumn("OP1");
            dtm.addColumn("MOG");
            dtm.addColumn("OP2");
            dtm.addColumn("WC");
            dtm.addColumn("Fecha final");
            dtm.addColumn("Horas trabajadas");
            dtm.addColumn("Minutos trabajados");
            dtm.addColumn("Piezas aprobadas");
            for (int i = 1; i < 5; i++) {
                dtm.addColumn("Lote " + i);
                dtm.addColumn("Mts " + i);
                dtm.addColumn("End " + i);
            }
            dtm.addColumn("PIEZAS PRENSADAS");
            dtm.addColumn("SCRAPS");
            
            reportesVista.jTableReporte.setModel(dtm);
            reportesVista.jTableReporte.setAutoResizeMode(0);
            for (int i = 0; i < reportesVista.jTableReporte.getColumnCount(); i++) {
                reportesVista.jTableReporte.getColumnModel().getColumn(i).setPreferredWidth(180);
            }
            int cc=0;
            try {

                CallableStatement cst = connectionDB.prepareCall("{call reporteBitacoraPrensa(?,?)}");
                cst.setString(1, fecha_inicio);
                cst.setString(2, fecha_fin);
                ResultSet rst = cst.executeQuery();
                while (rst.next()) {
                    Object[] data = new Object[25];
                    String ord = rst.getString("orden_manufactura");
                    int total = 0;
                    cst = connectionDB.prepareCall("{call contarLotes(?)}");
                    cst.setString(1, ord);
                    int cont=0;
                    ResultSet rst12 = cst.executeQuery();
                    while (rst12.next()) {
                        total = rst12.getInt("COUNT(lote_coil.lote_coil)");
                        cont ++;
                    }
                    if (cont <= 4) {
                        data[0] = rst.getString("mog");
                        String noNuevo = rst.getString("no_parte");
                        String nuNe = noNuevo.replace("-", "'");
                        data[1] = nuNe;
                        data[2] = rst.getString("loteTM");
                        data[3] = ord;
                        data[4] = rst.getString("mog2");
                        data[5] = rst.getString("po2");
                        wc = rst.getString("linea");
                        if (wc.equals("TB01")) {
                            data[6] = "1";
                        }
                        if (wc.equals("TB02B")) {
                            data[6] = "2";
                        }

                        if (wc.equals("TB03B")) {
                            data[6] = "3";
                        }

                        if (wc.equals("TB05")) {
                            data[6] = "5";
                        }

                        if (wc.equals("TB06")) {
                            data[6] = "6";
                        }

                        fech = fechaUltima(rst.getString("orden_manufactura"), m);
                        nuevaFecha = dayformat.format(fech);
                        data[7] = nuevaFecha;
                        tiempo = totalTiempo(rst.getString("orden_manufactura"), m, rst.getString("mog"), wc);
                        String[] hraDiv = tiempo.split(":");
                        data[8] = hraDiv[0];
                        data[9] = hraDiv[1];
                        data[10] = rst.getString("total_piezas_aprobadas");
                        orden = validarOrden(rst.getString("orden_manufactura"));
                        if (orden.equals("PRS")) {
                            String completo = rst.getString("no_parte");
                            nuNe = completo.replace("-", "'");
                            data[1] = completo + "'TP";
                        }
                        if (orden.equals("BHL")) {
                            String completo = rst.getString("no_parte");
                            nuNe = completo.replace("-", "'");
                            data[1] = nuNe + "'TB";
                        } else {
                            nuNe = rst.getString("no_parte").replace("-", "'");
                            data[1] = nuNe;
                        }
                        
                        data[23]=Integer.parseInt(rst.getString("totalpiezas_procesadas"));
                        data[24]=Integer.parseInt(rst.getString("total_scrap"));
                        
                        dtm.addRow(data);
                        if (cc < reportesVista.jTableReporte.getRowCount()) {
                            cst = connectionDB.prepareCall("{call traerCoils1(?)}");
                            cst.setString(1, ord);
                            ResultSet rst1 = cst.executeQuery();
                            int i = 11;
                            int j = 12;
                            int k = 13;
                            while (rst1.next()) {
                                String bobina, cantidad, finalizado;
                                String lot = rst1.getString("coil");
                                String mtr = rst1.getString("metros");
                                int fin = rst1.getInt("finalizado");

                                if (lot.equals("null")) {
                                    bobina = "";
                                } else {
                                    bobina = lot;
                                }
                                if (mtr.equals("null")) {
                                    cantidad = "";
                                } else {
                                    cantidad = mtr;
                                }
                                if (fin == 1) {
                                    finalizado = "X";
                                } else {
                                    finalizado = "";
                                }
                                reportesVista.jTableReporte.setValueAt(bobina, cc, i);
                                reportesVista.jTableReporte.setValueAt(cantidad, cc, j);
                                reportesVista.jTableReporte.setValueAt(finalizado, cc, k);
                                Object k1, k2;
                                k1 = reportesVista.jTableReporte.getValueAt(cc, i);
                                k2 = reportesVista.jTableReporte.getValueAt(cc, j);
                                System.err.println(k1);
                                System.err.println(k2);
                                i = i + 3;
                                j = j + 3;
                                k = k + 3;
                            }
                            cc++;
                        }
                    }
                }
                connectionDB.close();
                    
            } catch (SQLException e) {
                System.err.println("Error " + e.getMessage());
            }
        }
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

    public String totalTiempo(String orden, MetodosConnection m, String mog, String linea) {
        Connection con = m.conexionMySQL();
        String total = "";
        String[] data = new String[15];
        int[] horas = new int[15];
        int[] minutos = new int[15];
        int horasSuma = 0, conversion, minutosSuma = 0, totalTotal;
        try {
            CallableStatement cst = con.prepareCall("{call tiempoTotalOrden(?)}");
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
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(ModelBitacoraEmpaque.class.getName()).log(Level.SEVERE, null, ex);
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

    public String validarOrden(String var) {
        String[] parts = var.split("0");
        var = parts[0]; //trae del - hacia atras
        //var= parts[1]; trae el resto del string 
        return var;
    }
    
    public void reportePrensa(JTable tabla) {
        String h;
        JFileChooser file = new JFileChooser();
        file.showSaveDialog(file);
        File guarda = file.getSelectedFile();
        File archivoXLS = new File(guarda + ".xls");
        try {
            if (archivoXLS.exists()) {
                archivoXLS.delete();
            } else {
                archivoXLS.createNewFile();
                Workbook libro = new HSSFWorkbook();
                FileOutputStream archivo = new FileOutputStream(archivoXLS);
                Sheet excelHoja = libro.createSheet("Reporte Bitacora Prensa");
                int a = tabla.getRowCount();
                int b = tabla.getColumnCount();
                for (int f = -1; f < a; f++) {
                    Row fila = excelHoja.createRow(f + 1);
                    for (int c = 0; c < b; c++) {
                        Cell celda = fila.createCell(c);
                        if (f == -1) {
                            celda.setCellValue(tabla.getColumnName(c));
                        } else {
                            Object k = tabla.getValueAt(f, c);
                            if (k == null) {
                                h = "";
                                celda.setCellValue(h);
                            } else {
                                h = String.valueOf(k);
                                celda.setCellValue(h);
                            }
                        }
                    }
                }
                if (file.accept(guarda)) {
                    libro.write(archivo);
                    archivo.close();
                    Desktop.getDesktop().open(archivoXLS);
                } else {
                    JOptionPane.showMessageDialog(null, "No se pudo abrir el archivo, intente de nuevo");
                }
            }
        } catch (Exception e) {
            System.err.println("Error " + e.getMessage());
        }
    }

    public void busquedaMOG(MetodosConnection m, ReporteBitacoraPrensa reportesVista){
        
        Connection connectionDB = m.conexionMySQL();
        String ord;
        ord = reportesVista.jTextFieldBusquedaMOG.getText();
        int total = 0;
       
        String fecha_inicio;
        String fecha_fin;
        String tiempo = null;
        String wc, orden;
        String nuevaFecha;
        Date fech;
        Date fi = new Date();
        Date ff = new Date();
        DateFormat hourFormat = new SimpleDateFormat("yyyy-MM-dd");

        DateFormat dayformat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            CallableStatement cst = connectionDB.prepareCall("{call contarLotes2(?)}");
            String j34="MOG0"+ord;
            cst.setString(1,j34);
            ResultSet rst12 = cst.executeQuery();
            int cont=0;
            while (rst12.next()) {
                total = rst12.getInt("COUNT(lote_coil.lote_coil)");
                cont++;
            }

            if (cont >=5) {
                int cc = 0;
                DefaultTableModel dtm = new DefaultTableModel();
                reportesVista.jTableReporte.setRowHeight(20);
                dtm.addColumn("MOG");
                dtm.addColumn("NoParte MOG");
                dtm.addColumn("Lote");
                dtm.addColumn("OP1");
                dtm.addColumn("MOG");
                dtm.addColumn("OP2");
                dtm.addColumn("WC");
                dtm.addColumn("Fecha final");
                dtm.addColumn("Horas trabajadas");
                dtm.addColumn("Minutos trabajados");
                dtm.addColumn("Piezas aprobadas");
                for (int i = 1; i < 31; i++) {
                    dtm.addColumn("Lote " + i);
                    dtm.addColumn("Mts " + i);
                    dtm.addColumn("End " + i);
                }
                reportesVista.jTableReporte.setModel(dtm);
                reportesVista.jTableReporte.setAutoResizeMode(0);
                for (int i = 0; i < reportesVista.jTableReporte.getColumnCount(); i++) {
                    reportesVista.jTableReporte.getColumnModel().getColumn(i).setPreferredWidth(180);
                }

                cst = connectionDB.prepareCall("{call ReporteBitacoraPrensaFiltro(?)}");
                cst.setString(1, ord);
                ResultSet rst = cst.executeQuery();
                String[] data = new String[101];
                while (rst.next()) {
                    data[0] = rst.getString("mog");
                    String noNuevo = rst.getString("no_parte");
                    String nuNe = noNuevo.replace("-", "'");
                    data[1] = nuNe;
                    data[2] = rst.getString("loteTM");
                    data[3] = rst.getString("orden_manufactura");
                    data[4] = rst.getString("mog2");
                    data[5] = rst.getString("po2");
                    wc = rst.getString("linea");
                    if (wc.equals("TB01")) {
                        data[6] = "1";
                    }
                    if (wc.equals("TB02B")) {
                        data[6] = "2";
                    }

                    if (wc.equals("TB03B")) {
                        data[6] = "3";
                    }

                    if (wc.equals("TB05")) {
                        data[6] = "5";
                    }

                    if (wc.equals("TB06")) {
                        data[6] = "6";
                    }

                    fech = fechaUltima(rst.getString("orden_manufactura"), m);
                    nuevaFecha = dayformat.format(fech);
                    data[7] = nuevaFecha;
                    tiempo = totalTiempo(rst.getString("orden_manufactura"), m, rst.getString("mog"), wc);
                    String[] hraDiv = tiempo.split(":");
                    data[8] = hraDiv[0];
                    data[9] = hraDiv[1];
                    data[10] = rst.getString("total_piezas_aprobadas");
                    orden = validarOrden(rst.getString("orden_manufactura"));
                    if (orden.equals("PRS")) {
                        String completo = rst.getString("no_parte");
                        nuNe = completo.replace("-", "'");
                        data[1] = completo + "'TP";
                    }
                    if (orden.equals("BHL")) {
                        String completo = rst.getString("no_parte");
                        nuNe = completo.replace("-", "'");
                        data[1] = nuNe + "'TB";
                    } else {
                        nuNe = rst.getString("no_parte").replace("-", "'");
                        data[1] = nuNe;
                    }
                    dtm.addRow(data);
                    if (cc < reportesVista.jTableReporte.getRowCount()) {

                        cst = connectionDB.prepareCall("{call traerCoils2(?)}");
                        cst.setString(1, "MOG0"+ord);
                        ResultSet rst1 = cst.executeQuery();
                        int i = 11;
                        int j = 12;
                        int k = 13;
                        while (rst1.next()) {
                            String bobina, cantidad, finalizado;
                            String lot = rst1.getString("coil");
                            String mtr = rst1.getString("metros");
                            int fin = rst1.getInt("finalizado");

                            if (lot.equals("null")) {
                                bobina = "";
                            } else {
                                bobina = lot;
                            }
                            if (mtr.equals("null")) {
                                cantidad = "";
                            } else {
                                cantidad = mtr;
                            }
                            if (fin == 1) {
                                finalizado = "X";
                            } else {
                                finalizado = "";
                            }
                            reportesVista.jTableReporte.setValueAt(bobina, cc, i);
                            reportesVista.jTableReporte.setValueAt(cantidad, cc, j);
                            reportesVista.jTableReporte.setValueAt(finalizado, cc, k);
                            Object k1, k2;
                            k1 = reportesVista.jTableReporte.getValueAt(cc, i);
                            k2 = reportesVista.jTableReporte.getValueAt(cc, j);
                            System.err.println(k1);
                            System.err.println(k2);
                            i = i + 3;
                            j = j + 3;
                            k = k + 3;
                        }
                        cc++;
                    }
                }

            } else {
                if(total<=4){
                    int cc=0;
                    DefaultTableModel dtm = new DefaultTableModel();
                    reportesVista.jTableReporte.setRowHeight(20);
                    dtm.addColumn("MOG");
                    dtm.addColumn("NoParte MOG");
                    dtm.addColumn("Lote");
                    dtm.addColumn("OP1");
                    dtm.addColumn("MOG");
                    dtm.addColumn("OP2");
                    dtm.addColumn("WC");
                    dtm.addColumn("Fecha final");
                    dtm.addColumn("Horas trabajadas");
                    dtm.addColumn("Minutos trabajados");
                    dtm.addColumn("Piezas aprobadas");
                    for (int i = 1; i < 5; i++) {
                        dtm.addColumn("Lote " + i);
                        dtm.addColumn("Mts " + i);
                        dtm.addColumn("End " + i);
                    }
                    dtm.addColumn("PIEZAS PRENSADAS");
                    dtm.addColumn("SCRAPS");
                    
                    reportesVista.jTableReporte.setModel(dtm);
                    reportesVista.jTableReporte.setAutoResizeMode(0);
                    for (int i = 0; i < reportesVista.jTableReporte.getColumnCount(); i++) {
                        reportesVista.jTableReporte.getColumnModel().getColumn(i).setPreferredWidth(180);
                    }
                    
                    CallableStatement cst5 = connectionDB.prepareCall("{call ReporteBitacoraPrensaFiltro(?)}");
                    cst5.setString(1, ord);
                    ResultSet rst13 = cst5.executeQuery();
                    while(rst13.next()){
                        Object[] data2 = new Object[25];
                        data2[0] = rst13.getString("mog");
                        String noNuevo = rst13.getString("no_parte");
                        String nuNe = noNuevo.replace("-", "'");
                        data2[1] = nuNe;
                        data2[2] = rst13.getString("loteTM");
                        data2[3] = rst13.getString("orden_manufactura");
                        data2[4] = rst13.getString("mog2");
                        data2[5] = rst13.getString("po2");
                        wc = rst13.getString("linea");
                        if (wc.equals("TB01")) {
                            data2[6] = "1";
                        }
                        if (wc.equals("TB02B")) {
                            data2[6] = "2";
                        }

                        if (wc.equals("TB03B")) {
                            data2[6] = "3";
                        }

                        if (wc.equals("TB05")) {
                            data2[6] = "5";
                        }

                        if (wc.equals("TB06")) {
                            data2[6] = "6";
                        }

                        fech = fechaUltima(rst13.getString("orden_manufactura"), m);
                        nuevaFecha = dayformat.format(fech);
                        data2[7] = nuevaFecha;
                        tiempo = totalTiempo(rst13.getString("orden_manufactura"), m, rst13.getString("mog"), wc);
                        String[] hraDiv = tiempo.split(":");
                        data2[8] = hraDiv[0];
                        data2[9] = hraDiv[1];
                        data2[10] = rst13.getString("total_piezas_aprobadas");
                        orden = validarOrden(rst13.getString("orden_manufactura"));
                        if (orden.equals("PRS")) {
                            String completo = rst13.getString("no_parte");
                            nuNe = completo.replace("-", "'");
                            data2[1] = completo + "'TP";
                        }
                        if (orden.equals("BHL")) {
                            String completo = rst13.getString("no_parte");
                            nuNe = completo.replace("-", "'");
                            data2[1] = nuNe + "'TB";
                        } else {
                            nuNe = rst13.getString("no_parte").replace("-", "'");
                            data2[1] = nuNe;
                        }
                        
                        data2[23]=Integer.parseInt(rst13.getString("totalpiezas_procesadas"));
                        data2[24]=Integer.parseInt(rst13.getString("total_scrap"));
                        
                        dtm.addRow(data2);
                        if (cc < reportesVista.jTableReporte.getRowCount()) {
                            cst = connectionDB.prepareCall("{call traerCoils2(?)}");
                            cst.setString(1, "MOG0" + ord);
                            ResultSet rst11 = cst.executeQuery();
                            int i = 11;
                            int j = 12;
                            int k = 13;
                            while (rst11.next()) {
                                String bobina, cantidad, finalizado;
                                String lot = rst11.getString("coil");
                                String mtr = rst11.getString("metros");
                                int fin = rst11.getInt("finalizado");

                                if (lot.equals("null")) {
                                    bobina = "";
                                } else {
                                    bobina = lot;
                                }
                                if (mtr.equals("null")) {
                                    cantidad = "";
                                } else {
                                    cantidad = mtr;
                                }
                                if (fin == 1) {
                                    finalizado = "X";
                                } else {
                                    finalizado = "";
                                }
                                reportesVista.jTableReporte.setValueAt(bobina, cc, i);
                                reportesVista.jTableReporte.setValueAt(cantidad, cc, j);
                                reportesVista.jTableReporte.setValueAt(finalizado, cc, k);
                                Object k1, k2;
                                k1 = reportesVista.jTableReporte.getValueAt(cc, i);
                                k2 = reportesVista.jTableReporte.getValueAt(cc, j);
                                System.err.println(k1);
                                System.err.println(k2);
                                i = i + 3;
                                j = j + 3;
                                k = k + 3;
                            }
                            cc++;
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ModelBitacoraPrensa.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void limpiarTabla(ReporteBitacoraPrensa reportesVista){
        DefaultTableModel dtm = new DefaultTableModel();
        reportesVista.jTableReporte.setModel(dtm);
        reportesVista.jTableReporte.setAutoResizeMode(0);
        
        //reportesVista.jCheckBox4lotes.setSelected(false);
       // reportesVista.jCheckBoxOPnormal.setSelected(false);
        reportesVista.buttonGroup1.clearSelection();
        reportesVista.jDateChooserInicio.setDate(null);
        reportesVista.jDateChooserFin.setDate(null);
        reportesVista.jTextFieldBusquedaMOG.setText(null);
    }
}
