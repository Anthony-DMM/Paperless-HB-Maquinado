/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Vista.ReporteBitacoraEmpaque;
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
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author DMM-ADMIN
 */
public class ModelBitacoraEmpaque {

    public void consultaYLlenadoDeTablas(MetodosConnection m, ReporteBitacoraEmpaque reportesVista) {
        Connection connectionDB = m.conexionMySQL();

        String fecha_inicio;
        String fecha_fin;
        String tiempo = null;
        String wc, orden, rodguide = "", scrap="";
        int cant, total = 0;
        String nuevaFecha;
        Date fech;
        Date pro;
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
            dtm.addColumn("Sobrante inicial");
            dtm.addColumn("Sobrante final");
            dtm.addColumn("Piezas procesadas");
            dtm.addColumn("Piezas aprobadas");
            dtm.addColumn("Horas trabajadas");
            dtm.addColumn("Minutos trabajados");
            dtm.addColumn("Sobrante inicial STD+1");
            dtm.addColumn("STD+1");
            dtm.addColumn("Sobrante final STD+1");
            dtm.addColumn("Sobrante inicial STD-1");
            dtm.addColumn("STD-1");
            dtm.addColumn("Sobrante final STD-1");
            dtm.addColumn("RodGuide procesado");
            dtm.addColumn("QTY. SCRAP");
            dtm.addColumn("SCRAP HOJA AZUL");
            dtm.addColumn("VERIFICACIÓN CÁLCULOS");
            dtm.addColumn("VERIFICACIÓN PROCESOS");
            reportesVista.jTableReporte.setModel(dtm);
            reportesVista.jTableReporte.setAutoResizeMode(0);
            for (int i = 0; i < reportesVista.jTableReporte.getColumnCount(); i++) {
                reportesVista.jTableReporte.getColumnModel().getColumn(i).setPreferredWidth(180);
            }
            try {

                CallableStatement cst = connectionDB.prepareCall("{call reporteBitacoraEmpaque(?,?)}");
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
                    data[6] =wc;
                    /*if (wc.equals("TI30")) {
                        data[6] = "30";
                    }
                    if (wc.equals("TI29")) {
                        data[6] = "29";
                    }
                    if (wc.equals("TI28")) {
                        data[6] = "28";
                    }
                    if (wc.equals("TB91")) {
                        data[6] = "91";
                    }
                    if (wc.equals("TB92")) {
                        data[6] = "92";
                    }*/

                    fech = fechaUltima(rst.getString("orden_manufactura"), m);
                    nuevaFecha = dayformat.format(fech);
                    data[7] = nuevaFecha;
                    data[8] = rst.getString("sobrante_inicial");
                    data[9] = rst.getString("sobrante_final");
                    int jj, pzrec ,tootal;
                    pzrec = Integer.parseInt(rst.getString("total_piezas_recibidas"));
                    jj=Integer.parseInt(rst.getString("cambio_mog"));
                    tootal=pzrec-jj;
                    data[10] = rst.getString("total_piezas_recibidas");
                    data[11] = rst.getString("total_piezas_aprobadas");
                    tiempo = totalTiempo(rst.getString("orden_manufactura"), m, rst.getString("mog"), wc);
                    String[] hraDiv = tiempo.split(":");
                    data[12] = hraDiv[0];
                    data[13] = hraDiv[1];
                    data[14] = null;
                    data[15] = null;
                    data[16] = null;
                    data[17] = null;
                    data[18] = null;
                    data[19] = null;
                    orden = validarOrden(rst.getString("orden_manufactura"));
                    if (orden.equals("ASL")) {
                        total = proceRodGuide(rst.getString("orden_manufactura"), m);
                        int aprob, sob;
                        aprob = Integer.parseInt(rst.getString("total_piezas_aprobadas"));
                        sob = Integer.parseInt(rst.getString("sobrante_inicial"));
                        cant = (aprob - sob) + total;
                        rodguide = String.valueOf(cant);
                        int j=0;
                        CallableStatement cst3 = connectionDB.prepareCall("{call traerSoloScrapRG(?)}");
                        cst3.setString(1,rst.getString("orden_manufactura"));
                        ResultSet rst3 = cst3.executeQuery();
                        while(rst3.next()){
                            j=rst3.getInt(1);
                        }
                        int sbush=Integer.parseInt(rst.getString("total_scrap"));
                        int totalS=sbush-j;
                        scrap=String.valueOf(totalS);
                    } else {
                        rodguide = "";
                        scrap=rst.getString("total_scrap");
                    }
                    if (orden.equals("PCK") || orden.equals("ASL")) {
                        String completo = rst.getString("no_parte");
                        nuNe = completo.replace("-", "'");
                        data[1] = nuNe + "'MX";
                    } else {
                        noNuevo = rst.getString("no_parte");
                        nuNe = noNuevo.replace("-", "'");

                        data[1] = nuNe;
                    }
                    data[20] = rodguide;
                    data[21]=scrap;
                    data[22]=null;
                    if(orden.equals("ASL")){
                        data[23]=rst.getString("verificacion");
                        data[24]=rst.getString("verificacion2");
                    }else{
                        data[23]=rst.getString("verificacion");
                        data[24]=rst.getString("verificacion2");
                    }
                    
                    dtm.addRow(data);
                }
                connectionDB.close();
            } catch (SQLException e) {
                System.err.println("Error " + e.getMessage());
            }
        }
    }

    public void consultaYLlenadoDeTablasByMOG(MetodosConnection m, ReporteBitacoraEmpaque reportesVista) {
        Connection connectionDB = m.conexionMySQL();

        String tiempo = null;
        String wc, orden, rodguide = "", scrap = null;
        int cant, total = 0;
        String nuevaFecha;
        Date fech;
        
       
        String fi3;
        
        DateFormat dayformat = new SimpleDateFormat("dd/MM/yyyy");
        
            fi3 = reportesVista.jTextFieldBusquedaMOG.getText();
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
            dtm.addColumn("Sobrante inicial");
            dtm.addColumn("Sobrante final");
            dtm.addColumn("Piezas procesadas");
            dtm.addColumn("Piezas aprobadas");
            dtm.addColumn("Horas trabajadas");
            dtm.addColumn("Minutos trabajados");
            dtm.addColumn("Sobrante inicial STD+1");
            dtm.addColumn("STD+1");
            dtm.addColumn("Sobrante final STD+1");
            dtm.addColumn("Sobrante inicial STD-1");
            dtm.addColumn("STD-1");
            dtm.addColumn("Sobrante final STD-1");
            dtm.addColumn("RodGuide procesado");
            dtm.addColumn("QTY. SCRAP");
            dtm.addColumn("SCRAP HOJA AZUL");
            dtm.addColumn("VERIFICACIÓN CÁLCULOS");
            dtm.addColumn("VERIFICACIÓN PROCESOS");
            reportesVista.jTableReporte.setModel(dtm);
            reportesVista.jTableReporte.setAutoResizeMode(0);
            for (int i = 0; i < reportesVista.jTableReporte.getColumnCount(); i++) {
                reportesVista.jTableReporte.getColumnModel().getColumn(i).setPreferredWidth(180);
            }
            try {

                CallableStatement cst = connectionDB.prepareCall("{call reporteBitacoraEmpaque2(?)}");
                cst.setString(1, "%" + fi3+ "%");
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
                    data[6] = wc;
                    /*
                    if (wc.equals("TI30")) {
                        data[6] = "30";
                    }
                    if (wc.equals("TI29")) {
                        data[6] = "29";
                    }
                    if (wc.equals("TI28")) {
                        data[6] = "28";
                    }
                    if (wc.equals("TB91")) {
                        data[6] = "91";
                    }
                    if (wc.equals("TB92")) {
                        data[6] = "92";
                    }*/

                    fech = fechaUltima(rst.getString("orden_manufactura"), m);
                    nuevaFecha = dayformat.format(fech);
                    data[7] = nuevaFecha;
                    data[8] = rst.getString("sobrante_inicial");
                    data[9] = rst.getString("sobrante_final");
                    int jj, pzrec ,tootal;
                    pzrec = Integer.parseInt(rst.getString("total_piezas_recibidas"));
                    jj=Integer.parseInt(rst.getString("cambio_mog"));
                    tootal=pzrec-jj;
                    data[10] = rst.getString("total_piezas_recibidas");
                    data[11] = rst.getString("total_piezas_aprobadas");
                    tiempo = totalTiempo(rst.getString("orden_manufactura"), m, rst.getString("mog"), wc);
                    String[] hraDiv = tiempo.split(":");
                    data[12] = hraDiv[0];
                    data[13] = hraDiv[1];
                    data[14] = null;
                    data[15] = null;
                    data[16] = null;
                    data[17] = null;
                    data[18] = null;
                    data[19] = null;
                    
                    orden = validarOrden(rst.getString("orden_manufactura"));
                    if (orden.equals("ASL")) {
                        total = proceRodGuide(rst.getString("orden_manufactura"), m);
                        int aprob, sob;
                        aprob = Integer.parseInt(rst.getString("total_piezas_aprobadas"));
                        sob = Integer.parseInt(rst.getString("sobrante_inicial"));
                        cant = (aprob - sob) + total;
                        rodguide = String.valueOf(cant);
                        int j=0;
                        CallableStatement cst3 = connectionDB.prepareCall("{call traerSoloScrapRG(?)}");
                        cst3.setString(1,rst.getString("orden_manufactura"));
                        ResultSet rst3 = cst3.executeQuery();
                        while(rst3.next()){
                            j=rst3.getInt(1);
                        }
                        int sbush=Integer.parseInt(rst.getString("total_scrap"));
                        int totalS=sbush-j;
                        scrap=String.valueOf(totalS);
                    } else {
                        rodguide = "";
                        scrap=rst.getString("total_scrap");
                    }
                    if (orden.equals("PCK") || orden.equals("ASL")) {
                        String completo = rst.getString("no_parte");
                        nuNe = completo.replace("-", "'");
                        data[1] = nuNe + "'MX";
                    } else {
                        noNuevo = rst.getString("no_parte");
                        nuNe = noNuevo.replace("-", "'");

                        data[1] = nuNe;
                    }
                    data[20] = rodguide;
                    data[21]=scrap;
                    data[22]=null;
                    if(orden.equals("ASL")){
                        data[23]=rst.getString("verificacion");
                        data[24]=rst.getString("verificacion2");
                    }else{
                        data[23]=rst.getString("verificacion");
                        data[24]=rst.getString("verificacion2");
                    }
                    dtm.addRow(data);
                }
                connectionDB.close();
            } catch (SQLException e) {
                System.err.println("Error " + e.getMessage());
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

    public int proceRodGuide(String orden, MetodosConnection m) {
        Connection con = m.conexionMySQL();
        int rg = 0;
        try {
            CallableStatement cst = con.prepareCall("{call scrapRg1(?,?)}");
            cst.setString(1, orden);
            cst.registerOutParameter(2, java.sql.Types.INTEGER);
            cst.executeQuery();
            rg = cst.getInt(2);
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(ModelBitacoraEmpaque.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rg;
    }

    public String totalTiempo(String orden, MetodosConnection m, String mog, String linea) {
        Connection con = m.conexionMySQL();
        String total = "";
        String[] data = new String[21];
        int[] horas = new int[21];
        int[] minutos = new int[21];
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

            for (int h = 0; h < 21; h++) {
                if (data[h] == null) {

                } else {
                    horas[h] = traerHoras(data[h]);
                    minutos[h] = traerMinutos(data[h]);
                }

            }

            for (int l = 0; l < 21; l++) {
                horasSuma = horasSuma + horas[l];
            }

            conversion = horasSuma * 60;

            for (int f = 0; f < 21; f++) {
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

    /*
    public int convertirHorasAMinutos(String horaConsulta){
        int totalHoras, totalMinutos;
        
        //int convertHoras=Integer.parseInt(traerHoras(horaConsulta));
        int minutos=traerMinutos(horaConsulta);
        
        //totalMinutos=convertHoras*60;
        return totalMinutos;
    }*/
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

    public void limpiarTabla(ReporteBitacoraEmpaque reportesVista){
        DefaultTableModel dtm = new DefaultTableModel();
        reportesVista.jTableReporte.setModel(dtm);
        reportesVista.jTableReporte.setAutoResizeMode(0);
        
        reportesVista.jDateChooserInicio.setDate(null);
        reportesVista.jDateChooserFin.setDate(null);
        reportesVista.jTextFieldBusquedaMOG.setText(null);
    }
    
    
}
