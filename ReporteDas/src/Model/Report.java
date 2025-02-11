/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import View.View_report_bg_general;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
 * @author ALEJANDRO LARES
 */
public class Report {

    public void reporteBush(Conection con, View_report_bg_general dm) throws ParseException {
        Connection conne = con.conexionMySQL();
        ResultSet re21;
        String mog, tiemCamStart, tiemCamFin, tiemProStart, tiemProFin, tu, fech, m;
        int turno;

        String fecha_inicio;
        String fecha_fin;

        Date fi = new Date();
        Date ff = new Date();
        DateFormat hourFormat = new SimpleDateFormat("yyyy-MM-dd");

        fi = dm.jDateChooserInicio.getDate();
        ff = dm.jDateChooserFin.getDate();

        if (fi == null || ff == null) {
            JOptionPane.showMessageDialog(null, "Debes de seleccionar alguna fecha para buscar");
        } else {
            fecha_inicio = hourFormat.format(fi);
            fecha_fin = hourFormat.format(ff);

            try {
                DefaultTableModel dtm = new DefaultTableModel();
                dm.jTableReporteDas.setRowHeight(20);
                dtm.addColumn("Line");
                dtm.addColumn("Year");
                dtm.addColumn("Month");
                dtm.addColumn("Day");
                dtm.addColumn("Shift_");
                dtm.addColumn("MOG_");
                dtm.addColumn("Start_Setup");
                dtm.addColumn("Start_Setup");
                dtm.addColumn("Finish_Setup");
                dtm.addColumn("Finish_Setup");
                dtm.addColumn("Start_Production");
                dtm.addColumn("Start_Production");
                dtm.addColumn("Finish_Production");
                dtm.addColumn("Finish_Production");
                dtm.addColumn("Prod. Qty._");
                dtm.addColumn("Good Parts_");
                dtm.addColumn("BG Scrap");
                dtm.addColumn("RG Scrap");
                dm.jTableReporteDas.setModel(dtm);
                dm.jTableReporteDas.setAutoResizeMode(0);
                for (int i = 0; i < dm.jTableReporteDas.getColumnCount(); i++) {
                    dm.jTableReporteDas.getColumnModel().getColumn(i).setPreferredWidth(120);
                }
                CallableStatement cst21 = conne.prepareCall("{call reporteDasBGGeneral2(?,?,?)}");
                cst21.setString(1, fecha_inicio);
                cst21.setString(2, fecha_fin);
                cst21.registerOutParameter(3, java.sql.Types.INTEGER);
                re21 = cst21.executeQuery();
                int resul = cst21.getInt(3);

                if (resul == 0) {
                    JOptionPane.showMessageDialog(null, "No hay registros en DAS, REVISAR CON SUPERVISOR SI SE TRABAJÓ LA MÁQUINA, "
                            + " de lo contrario, NOTIFICAR A EQUIPO PAPERLESS ");
                } else {
                    while (re21.next()) {
                        mog = re21.getString("mog");
                        if (mog.equals("MOG000000")) {

                        } else {
                            Object[] data = new Object[17];
                            data[0] = re21.getString("linea");
                            fech = re21.getString("fecha");
                            String fechaEntera = fech;
                            String[] fechaDividida = fechaEntera.split("-");
                            data[1] = fechaDividida[0];
                            data[2] = fechaDividida[1];
                            data[3] = fechaDividida[2];
                            turno = re21.getInt("turno");
                            if (turno == 1) {
                                tu = "M";
                            } else {
                                tu = "V";
                            }

                            data[4] = tu;
                            mog = re21.getString("mog");
                            m = mog.replace("MOG", "");
                            data[5] = m;

                            tiemCamStart = re21.getString("ini_cm");
                            if (tiemCamStart.equals("") || tiemCamStart == null || tiemCamStart.equals(" ")) {
                                data[6] = "";
                                data[7] = "";
                            } else {
                                System.out.println(mog);
                                System.out.println(tiemCamStart);

                                String horaSetupEntera = tiemCamStart;
                                System.out.println(horaSetupEntera);

                                String[] horaSetupDividida = horaSetupEntera.split(":");
                                System.out.println(horaSetupDividida[0]);
                                data[6] = horaSetupDividida[0];
                                data[7] = horaSetupDividida[1];
                            }

                            tiemCamFin = re21.getString("fin_cm");
                            if (tiemCamFin.equals("") || tiemCamFin == null || tiemCamFin.equals(" ")) {
                                data[8] = "";
                                data[9] = "";
                            } else {
                                String horaSetupEnteraF = tiemCamFin;
                                String[] HoraSetupDivididaF = horaSetupEnteraF.split(":");
                                data[8] = HoraSetupDivididaF[0];
                                data[9] = HoraSetupDivididaF[1];
                            }

                            tiemCamStart = re21.getString("ini_tp");
                            String horaStartEntera = tiemCamStart;
                            String[] HoraStartDividida = horaStartEntera.split(":");
                            data[10] = HoraStartDividida[0];
                            data[11] = HoraStartDividida[1];

                            tiemProFin = re21.getString("fin_tp");
                            String horaStartEnteraF = tiemProFin;
                            String[] HoraStartDivididaF = horaStartEnteraF.split(":");
                            data[12] = HoraStartDivididaF[0];
                            data[13] = HoraStartDivididaF[1];

                            data[14] = re21.getString("pcs_pro_bush");
                            data[15] = re21.getString("pcs_buen_bush");
                            data[16] = re21.getString("scrap_bush");
                            dtm.addRow(data);
                        }
                    }
                }
                conne.close();
            } catch (SQLException ex) {
                Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void reportePlatinado(Conection con, View_report_bg_general dm) throws ParseException {
        Connection conne = con.conexionMySQL();
        ResultSet re21;
        String mog, tiemCamStart, tiemCamFin, tu, fech;
        int turno, razon1, razon2, razon3, razon4, razon5, razon6, razon7, razon8, razon9, scra;

        int idDasPLT;
        int mogg = 0;

        String fecha_inicio;
        String fecha_fin;

        Date fi = new Date();
        Date ff = new Date();
        DateFormat hourFormat = new SimpleDateFormat("yyyy-MM-dd");

        fi = dm.jDateChooserInicio.getDate();
        ff = dm.jDateChooserFin.getDate();

        if (fi == null || ff == null) {
            JOptionPane.showMessageDialog(null, "Debes de seleccionar alguna fecha para buscar");
        } else {
            fecha_inicio = hourFormat.format(fi);
            fecha_fin = hourFormat.format(ff);

            try {
                DefaultTableModel dtm = new DefaultTableModel();
                dm.jTableReporteDas.setRowHeight(20);
                dtm.addColumn("Line");
                dtm.addColumn("Year");
                dtm.addColumn("Month");
                dtm.addColumn("Day");
                dtm.addColumn("Shift_");
                dtm.addColumn("MOG_");
                dtm.addColumn("");
                dtm.addColumn("");
                dtm.addColumn("");
                dtm.addColumn("");
                dtm.addColumn("");
                dtm.addColumn("Start_Production");
                dtm.addColumn("Start_Production");
                dtm.addColumn("Finish_Production");
                dtm.addColumn("Finish_Production");
                dtm.addColumn("Prod. Qty._");
                dtm.addColumn("Good Parts_");
                dtm.addColumn("Scrap");
                //razon1 razon2	razon3	razon4	razon5	razon6	razon7	razon8	razon9;
                dm.jTableReporteDas.setModel(dtm);
                dm.jTableReporteDas.setAutoResizeMode(0);
                for (int i = 0; i < dm.jTableReporteDas.getColumnCount(); i++) {
                    dm.jTableReporteDas.getColumnModel().getColumn(i).setPreferredWidth(120);
                }

                CallableStatement cst21 = conne.prepareCall("{call traerIDDasPLT(?,?)}");
                cst21.setString(1, fecha_inicio);
                cst21.setString(2, fecha_fin);
                re21 = cst21.executeQuery();
                while (re21.next()) {
                    idDasPLT = re21.getInt(1);
                    CallableStatement cst2 = conne.prepareCall("{call traerDatosPLTReporteDas(?)}");
                    cst2.setInt(1, idDasPLT);
                    ResultSet r23 = cst2.executeQuery();
                    while (r23.next()) {
                        mogg = r23.getInt("mog_idmog");
                        CallableStatement c1 = conne.prepareCall("{call traer_piezasPLTDas(?,?)}");
                        c1.setInt(1, mogg);
                        c1.setInt(2, idDasPLT);
                        ResultSet r1 = c1.executeQuery();
                        while (r1.next()) {
                            Object[] data = new Object[18];
                            data[0] = r23.getString("linea");
                            fech = r23.getString("fecha");
                            String fechaEntera = fech;
                            String[] fechaDividida = fechaEntera.split("-");
                            data[1] = fechaDividida[0];
                            data[2] = fechaDividida[1];
                            data[3] = fechaDividida[2];
                            turno = r23.getInt("turno");
                            if (turno == 1) {
                                tu = "M";
                            } else {
                                tu = "V";
                            }

                            data[4] = tu;
                            mog = r23.getString("mog");
                            String m = mog.replace("MOG", "");
                            data[5] = m;
                            data[6] = "";
                            data[7] = "";
                            data[8] = "";
                            data[9] = "";
                            data[10] = "";
                            tiemCamStart = r23.getString("ini_tur");

                            if (tiemCamStart.equals("")) {
                                data[11] = "";
                                data[12] = "";
                            } else {
                                String horaSetupEntera = tiemCamStart;
                                String[] horaSetupDividida = horaSetupEntera.split(":");
                                data[11] = horaSetupDividida[0];
                                data[12] = horaSetupDividida[1];
                            }

                            tiemCamFin = r23.getString("fin_tur");
                            if (tiemCamFin.equals("")) {
                                data[13] = "";
                                data[14] = "";
                            } else {
                                String horaSetupEnteraF = tiemCamFin;
                                String[] HoraSetupDivididaF = horaSetupEnteraF.split(":");
                                data[13] = HoraSetupDivididaF[0];
                                data[14] = HoraSetupDivididaF[1];
                            }

                            razon1 = r23.getInt("razon1");
                            razon2 = r23.getInt("razon2");
                            razon3 = r23.getInt("razon3");
                            razon4 = r23.getInt("razon4");
                            razon5 = r23.getInt("razon5");
                            razon6 = r23.getInt("razon6");
                            razon7 = r23.getInt("razon7");
                            razon8 = r23.getInt("razon8");
                            razon9 = r23.getInt("razon9");
                            scra = razon1 + razon2 + razon3 + razon4 + razon5 + razon6 + razon7 + razon8 + razon9;

                            data[15] = r1.getString("cantidad_piezas_procesadas");
                            data[16] = r1.getString("cantPzaGood");
                            data[17] = scra;
                            dtm.addRow(data);
                        }
                    }
                }
                if (dtm.getRowCount() <= 0) {
                    JOptionPane.showMessageDialog(null, "No hay registros en DAS, REVISAR CON SUPERVISOR SI SE TRABAJÓ LA MÁQUINA, "
                            + " de lo contrario, NOTIFICAR A EQUIPO PAPERLESS ");
                }

                conne.close();
            } catch (SQLException ex) {
                Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void reporteDasPlatinado(Conection con, View_report_bg_general dm){
        Connection conne = con.conexionMySQL();
        ResultSet r23;
        String mog, tiemCamStart, tiemCamFin, tu, fech;
        int turno, razon1, razon2, razon3, razon4, razon5, razon6, razon7, razon8, razon9, scra;

        int idDasPLT;
        int mogg = 0;

        String fecha_inicio;
        String fecha_fin;

        Date fi = new Date();
        Date ff = new Date();
        DateFormat hourFormat = new SimpleDateFormat("yyyy-MM-dd");

        fi = dm.jDateChooserInicio.getDate();
        ff = dm.jDateChooserFin.getDate();

        if (fi == null || ff == null) {
            JOptionPane.showMessageDialog(null, "Debes de seleccionar alguna fecha para buscar");
        } else {
            fecha_inicio = hourFormat.format(fi);
            fecha_fin = hourFormat.format(ff);

            try {
                DefaultTableModel dtm = new DefaultTableModel();
                dm.jTableReporteDas.setRowHeight(20);
                dtm.addColumn("Line");
                dtm.addColumn("Year");
                dtm.addColumn("Month");
                dtm.addColumn("Day");
                dtm.addColumn("Shift_");
                dtm.addColumn("MOG_");
                dtm.addColumn("");
                dtm.addColumn("");
                dtm.addColumn("");
                dtm.addColumn("");
                dtm.addColumn("");
                dtm.addColumn("Start_Production");
                dtm.addColumn("Start_Production");
                dtm.addColumn("Finish_Production");
                dtm.addColumn("Finish_Production");
                dtm.addColumn("Prod. Qty._");
                dtm.addColumn("Good Parts_");
                dtm.addColumn("Scrap");
                //razon1 razon2	razon3	razon4	razon5	razon6	razon7	razon8	razon9;
                dm.jTableReporteDas.setModel(dtm);
                dm.jTableReporteDas.setAutoResizeMode(0);
                for (int i = 0; i < dm.jTableReporteDas.getColumnCount(); i++) {
                    dm.jTableReporteDas.getColumnModel().getColumn(i).setPreferredWidth(120);
                }

                CallableStatement cst21 = conne.prepareCall("{call reporteDasBGPlatinadoFinal(?,?)}");
                cst21.setString(1, fecha_inicio);
                cst21.setString(2, fecha_fin);
                r23 = cst21.executeQuery();
                while (r23.next()) {
                        mogg = r23.getInt("mog_idmog");
                        Object[] data = new Object[18];
                        data[0] = r23.getString("linea");
                        fech = r23.getString("fecha");
                        String fechaEntera = fech;
                        String[] fechaDividida = fechaEntera.split("-");
                        data[1] = fechaDividida[0];
                        data[2] = fechaDividida[1];
                        data[3] = fechaDividida[2];
                        turno = r23.getInt("turno");
                        if (turno == 1) {
                            tu = "M";
                        } else {
                            tu = "V";
                        }

                        data[4] = tu;
                        mog = r23.getString("mog");
                        String m = mog.replace("MOG", "");
                        data[5] = m;
                        data[6] = "";
                        data[7] = "";
                        data[8] = "";
                        data[9] = "";
                        data[10] = "";
                        tiemCamStart = r23.getString("ini_tur");

                        if (tiemCamStart.equals("")) {
                            data[11] = "";
                            data[12] = "";
                        } else {
                            String horaSetupEntera = tiemCamStart;
                            String[] horaSetupDividida = horaSetupEntera.split(":");
                            data[11] = horaSetupDividida[0];
                            data[12] = horaSetupDividida[1];
                        }

                        tiemCamFin = r23.getString("fin_tur");
                        if (tiemCamFin.equals("")) {
                            data[13] = "";
                            data[14] = "";
                        } else {
                            String horaSetupEnteraF = tiemCamFin;
                            String[] HoraSetupDivididaF = horaSetupEnteraF.split(":");
                            data[13] = HoraSetupDivididaF[0];
                            data[14] = HoraSetupDivididaF[1];
                        }

                        razon1 = r23.getInt("razon1");
                        razon2 = r23.getInt("razon2");
                        razon3 = r23.getInt("razon3");
                        razon4 = r23.getInt("razon4");
                        razon5 = r23.getInt("razon5");
                        razon6 = r23.getInt("razon6");
                        razon7 = r23.getInt("razon7");
                        razon8 = r23.getInt("razon8");
                        razon9 = r23.getInt("razon9");
                        scra = razon1 + razon2 + razon3 + razon4 + razon5 + razon6 + razon7 + razon8 + razon9;

                        data[15] = r23.getString("cantidad_piezas_procesadas");
                        data[16] = r23.getString("cantPzaGood");
                        data[17] = scra;
                        dtm.addRow(data);
                }
                if (dtm.getRowCount() <= 0) {
                    JOptionPane.showMessageDialog(null, "No hay registros en DAS, REVISAR CON SUPERVISOR SI SE TRABAJÓ LA MÁQUINA, "
                            + " de lo contrario, NOTIFICAR A EQUIPO PAPERLESS ");
                }

                conne.close();
            } catch (SQLException ex) {
                Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /*public void reporteAssy91(Conection con, View_report_bg_general dm) throws ParseException {
        Connection conne = con.conexionMySQL();
        ResultSet re21;
        String mog, tiemCamStart, tiemCamFin, tiemProStart, tiemProFin, tu, fech, m;
        int turno;
        String fecha_inicio;
        String fecha_fin;

        Date fi = new Date();
        Date ff = new Date();
        DateFormat hourFormat = new SimpleDateFormat("yyyy-MM-dd");

        fi = dm.jDateChooserInicio.getDate();
        ff = dm.jDateChooserFin.getDate();

        if (fi == null || ff == null) {
            JOptionPane.showMessageDialog(null, "Debes de seleccionar alguna fecha para buscar");
        } else {
            fecha_inicio = hourFormat.format(fi);
            fecha_fin = hourFormat.format(ff);
            try {
                DefaultTableModel dtm = new DefaultTableModel();
                dm.jTableReporteDas.setRowHeight(20);
                dtm.addColumn("Line");
                dtm.addColumn("Year");
                dtm.addColumn("Month");
                dtm.addColumn("Day");
                dtm.addColumn("Shift_");
                dtm.addColumn("MOG_");
                dtm.addColumn("Start_Setup");
                dtm.addColumn("Start_Setup");
                dtm.addColumn("Finish_Setup");
                dtm.addColumn("Finish_Setup");
                dtm.addColumn("Start_Production");
                dtm.addColumn("Start_Production");
                dtm.addColumn("Finish_Production");
                dtm.addColumn("Finish_Production");
                dtm.addColumn("Processed Qty.Bushing_");
                dtm.addColumn("Processed Qty.Rod Guides_");
                dtm.addColumn("Good Parts_");
                dtm.addColumn("Scrap BG_");
                dtm.addColumn("Scrap RG_");
                dm.jTableReporteDas.setModel(dtm);
                dm.jTableReporteDas.setAutoResizeMode(0);
                for (int i = 0; i < dm.jTableReporteDas.getColumnCount(); i++) {
                    dm.jTableReporteDas.getColumnModel().getColumn(i).setPreferredWidth(120);
                }
                CallableStatement cst21 = conne.prepareCall("{call reporteDasBGAssy91v2(?,?,?)}");
                cst21.setString(1, fecha_inicio);
                cst21.setString(2, fecha_fin);
                cst21.registerOutParameter(3, java.sql.Types.INTEGER);
                re21 = cst21.executeQuery();
                int resul = cst21.getInt(3);
                if (resul == 0) {
                    JOptionPane.showMessageDialog(null, "No hay registros en DAS, REVISAR CON SUPERVISOR SI SE TRABAJÓ LA MÁQUINA, "
                            + " de lo contrario, NOTIFICAR A EQUIPO PAPERLESS ");
                } else {
                    while (re21.next()) {
                        mog = re21.getString("mog");
                        if (mog.equals("MOG000000")) {

                        } else {
                            Object[] data = new Object[19];
                            data[0] = re21.getString("linea");
                            fech = re21.getString("fecha");
                            String fechaEntera = fech;
                            String[] fechaDividida = fechaEntera.split("-");
                            data[1] = fechaDividida[0];
                            data[2] = fechaDividida[1];
                            data[3] = fechaDividida[2];
                            turno = re21.getInt("turno");
                            if (turno == 1) {
                                tu = "M";
                            } else {
                                tu = "V";
                            }

                            data[4] = tu;
                            mog = re21.getString("mog");
                            m = mog.replace("MOG", "");
                            data[5] = m;

                            tiemCamStart = re21.getString("ini_cm");
                            if (tiemCamStart.equals("")) {
                                data[6] = "";
                                data[7] = "";
                            } else {
                                String horaSetupEntera = tiemCamStart;
                                String[] horaSetupDividida = horaSetupEntera.split(":");
                                data[6] = horaSetupDividida[0];
                                data[7] = horaSetupDividida[1];
                            }

                            tiemCamFin = re21.getString("fin_cm");
                            if (tiemCamFin.equals("")) {
                                data[8] = "";
                                data[9] = "";
                            } else {
                                String horaSetupEnteraF = tiemCamFin;
                                String[] HoraSetupDivididaF = horaSetupEnteraF.split(":");
                                data[8] = HoraSetupDivididaF[0];
                                data[9] = HoraSetupDivididaF[1];
                            }

                            tiemCamStart = re21.getString("ini_tp");
                            String horaStartEntera = tiemCamStart;
                            String[] HoraStartDividida = horaStartEntera.split(":");
                            data[10] = HoraStartDividida[0];
                            data[11] = HoraStartDividida[1];

                            tiemProFin = re21.getString("fin_tp");
                            String horaStartEnteraF = tiemProFin;
                            String[] HoraStartDivididaF = horaStartEnteraF.split(":");
                            data[12] = HoraStartDivididaF[0];
                            data[13] = HoraStartDivididaF[1];
                            data[14] = re21.getString("pcs_pro_bush");
                            data[15] = re21.getString("pcs_pro_rg");
                            data[16] = re21.getString("pcs_buen_bush");
                            data[17] = re21.getString("scrap_bush");
                            data[18] = re21.getString("scrp_rg");
                            dtm.addRow(data);
                        }
                    }
                }
                conne.close();
            } catch (SQLException ex) {
                Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }*/
        public void reporteAssy91(Conection con, View_report_bg_general dm) throws ParseException {
        Connection conne = con.conexionMySQL();
        ResultSet re21;
        String mog, tiemCamStart, tiemCamFin, tiemProStart, tiemProFin, tu, fech, m;
        int turno;
        String fecha_inicio;
        String fecha_fin;

        Date fi = new Date();
        Date ff = new Date();
        DateFormat hourFormat = new SimpleDateFormat("yyyy-MM-dd");

        fi = dm.jDateChooserInicio.getDate();
        ff = dm.jDateChooserFin.getDate();

        if (fi == null || ff == null) {
            JOptionPane.showMessageDialog(null, "Debes de seleccionar alguna fecha para buscar");
        } else {
            fecha_inicio = hourFormat.format(fi);
            fecha_fin = hourFormat.format(ff);
            try {
                DefaultTableModel dtm = new DefaultTableModel();
                dm.jTableReporteDas.setRowHeight(20);
                dtm.addColumn("Line");
                dtm.addColumn("Year");
                dtm.addColumn("Month");
                dtm.addColumn("Day");
                dtm.addColumn("Shift_");
                dtm.addColumn("MOG_");
                dtm.addColumn("Start_Setup");
                dtm.addColumn("Start_Setup");
                dtm.addColumn("Finish_Setup");
                dtm.addColumn("Finish_Setup");
                dtm.addColumn("Start_Production");
                dtm.addColumn("Start_Production");
                dtm.addColumn("Finish_Production");
                dtm.addColumn("Finish_Production");
                dtm.addColumn("Processed Qty.Bushing_");
                dtm.addColumn("Processed Qty.Rod Guides_");
                dtm.addColumn("Processed Qty.Seal Ring_");
                dtm.addColumn("Good Parts_");
                dtm.addColumn("Scrap BG_");
                dtm.addColumn("Scrap RG_");
                dtm.addColumn("Scrap SR_");
                dm.jTableReporteDas.setModel(dtm);
                dm.jTableReporteDas.setAutoResizeMode(0);
                for (int i = 0; i < dm.jTableReporteDas.getColumnCount(); i++) {
                    dm.jTableReporteDas.getColumnModel().getColumn(i).setPreferredWidth(120);
                }
                CallableStatement cst21 = conne.prepareCall("{call reporteDasBGAssy91v3(?,?,?)}");
                cst21.setString(1, fecha_inicio);
                cst21.setString(2, fecha_fin);
                cst21.registerOutParameter(3, java.sql.Types.INTEGER);
                re21 = cst21.executeQuery();
                int resul = cst21.getInt(3);
                if (resul == 0) {
                    JOptionPane.showMessageDialog(null, "No hay registros en DAS, REVISAR CON SUPERVISOR SI SE TRABAJÓ LA MÁQUINA, "
                            + " de lo contrario, NOTIFICAR A EQUIPO PAPERLESS ");
                } else {
                    while (re21.next()) {
                        mog = re21.getString("mog");
                        if (mog.equals("MOG000000")) {

                        } else {
                            Object[] data = new Object[21];
                            data[0] = re21.getString("linea");
                            fech = re21.getString("fecha");
                            String fechaEntera = fech;
                            String[] fechaDividida = fechaEntera.split("-");
                            data[1] = fechaDividida[0];
                            data[2] = fechaDividida[1];
                            data[3] = fechaDividida[2];
                            turno = re21.getInt("turno");
                            if (turno == 1) {
                                tu = "M";
                            } else {
                                tu = "V";
                            }

                            data[4] = tu;
                            mog = re21.getString("mog");
                            m = mog.replace("MOG", "");
                            data[5] = m;

                            tiemCamStart = re21.getString("ini_cm");
                            if (tiemCamStart.equals("")) {
                                data[6] = "";
                                data[7] = "";
                            } else {
                                String horaSetupEntera = tiemCamStart;
                                String[] horaSetupDividida = horaSetupEntera.split(":");
                                data[6] = horaSetupDividida[0];
                                data[7] = horaSetupDividida[1];
                            }

                            tiemCamFin = re21.getString("fin_cm");
                            if (tiemCamFin.equals("")) {
                                data[8] = "";
                                data[9] = "";
                            } else {
                                String horaSetupEnteraF = tiemCamFin;
                                String[] HoraSetupDivididaF = horaSetupEnteraF.split(":");
                                data[8] = HoraSetupDivididaF[0];
                                data[9] = HoraSetupDivididaF[1];
                            }

                            tiemCamStart = re21.getString("ini_tp");
                            String horaStartEntera = tiemCamStart;
                            String[] HoraStartDividida = horaStartEntera.split(":");
                            data[10] = HoraStartDividida[0];
                            data[11] = HoraStartDividida[1];

                            tiemProFin = re21.getString("fin_tp");
                            String horaStartEnteraF = tiemProFin;
                            String[] HoraStartDivididaF = horaStartEnteraF.split(":");
                            data[12] = HoraStartDivididaF[0];
                            data[13] = HoraStartDivididaF[1];
                            data[14] = re21.getString("pcs_pro_bush");
                            data[15] = re21.getString("pcs_pro_rg");
                            data[16] = re21.getString("pcs_pro_sr");
                            data[17] = re21.getString("pcs_buen_bush");
                            data[18] = re21.getString("scrap_bush");
                            data[19] = re21.getString("scrp_rg");
                            data[20] = re21.getString("scrap_sr");
                            dtm.addRow(data);
                        }
                    }
                }
                conne.close();
            } catch (SQLException ex) {
                Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }


    public void reporteAssy92(Conection con, View_report_bg_general dm) throws ParseException {
        Connection conne = con.conexionMySQL();
        ResultSet re21;
        String mog, tiemCamStart, tiemCamFin, tiemProStart, tiemProFin, tu, fech, m;
        int turno;
        String fecha_inicio;
        String fecha_fin;

        Date fi = new Date();
        Date ff = new Date();
        DateFormat hourFormat = new SimpleDateFormat("yyyy-MM-dd");

        fi = dm.jDateChooserInicio.getDate();
        ff = dm.jDateChooserFin.getDate();

        if (fi == null || ff == null) {
            JOptionPane.showMessageDialog(null, "Debes de seleccionar alguna fecha para buscar");
        } else {
            fecha_inicio = hourFormat.format(fi);
            fecha_fin = hourFormat.format(ff);
            try {

                //das_prod_bgproceso.pcs_pro_bush, das_prod_bgproceso.pcs_pro_rg, das_prod_bgproceso.pcs_pro_sr, das_prod_bgproceso.pcs_buen_bush, 
                //das_prod_bgproceso.scrap_bush, das_prod_bgproceso.scrp_rg, das_prod_bgproceso.scrap_sr
                DefaultTableModel dtm = new DefaultTableModel();
                dm.jTableReporteDas.setRowHeight(20);
                dtm.addColumn("Line");
                dtm.addColumn("Year");
                dtm.addColumn("Month");
                dtm.addColumn("Day");
                dtm.addColumn("Shift_");
                dtm.addColumn("MOG_");
                dtm.addColumn("Start_Setup");
                dtm.addColumn("Start_Setup");
                dtm.addColumn("Finish_Setup");
                dtm.addColumn("Finish_Setup");
                dtm.addColumn("Start_Production");
                dtm.addColumn("Start_Production");
                dtm.addColumn("Finish_Production");
                dtm.addColumn("Finish_Production");
                dtm.addColumn("Processed Qty.Bushing_");
                dtm.addColumn("Processed Qty.Rod Guides_");
                dtm.addColumn("Processed Qty. Seal Ring_");
                dtm.addColumn("Good Parts_");
                dtm.addColumn("Scrap BG_");
                dtm.addColumn("Scrap RG_");
                dtm.addColumn("Scrap SR_");
                dm.jTableReporteDas.setModel(dtm);
                dm.jTableReporteDas.setAutoResizeMode(0);
                for (int i = 0; i < dm.jTableReporteDas.getColumnCount(); i++) {
                    dm.jTableReporteDas.getColumnModel().getColumn(i).setPreferredWidth(120);
                }
                CallableStatement cst21 = conne.prepareCall("{call reporteDasBGAssy92v2(?,?,?)}");
                cst21.setString(1, fecha_inicio);
                cst21.setString(2, fecha_fin);
                cst21.registerOutParameter(3, java.sql.Types.INTEGER);
                re21 = cst21.executeQuery();
                int resul = cst21.getInt(3);
                if (resul == 0) {
                    JOptionPane.showMessageDialog(null, "No hay registros en DAS, REVISAR CON SUPERVISOR SI SE TRABAJÓ LA MÁQUINA, "
                            + " de lo contrario, NOTIFICAR A EQUIPO PAPERLESS ");
                } else {
                    while (re21.next()) {
                        mog = re21.getString("mog");
                        if (mog.equals("MOG000000")) {

                        } else {
                            Object[] data = new Object[21];
                            data[0] = re21.getString("linea");
                            fech = re21.getString("fecha");
                            String fechaEntera = fech;
                            String[] fechaDividida = fechaEntera.split("-");
                            data[1] = fechaDividida[0];
                            data[2] = fechaDividida[1];
                            data[3] = fechaDividida[2];
                            turno = re21.getInt("turno");
                            if (turno == 1) {
                                tu = "M";
                            } else {
                                tu = "V";
                            }

                            data[4] = tu;
                            mog = re21.getString("mog");
                            m = mog.replace("MOG", "");
                            data[5] = m;

                            tiemCamStart = re21.getString("ini_cm");
                            if (tiemCamStart.equals("")) {
                                data[6] = "";
                                data[7] = "";
                            } else {
                                String horaSetupEntera = tiemCamStart;
                                String[] horaSetupDividida = horaSetupEntera.split(":");
                                data[6] = horaSetupDividida[0];
                                data[7] = horaSetupDividida[1];
                            }

                            tiemCamFin = re21.getString("fin_cm");
                            if (tiemCamFin.equals("")) {
                                data[8] = "";
                                data[9] = "";
                            } else {
                                String horaSetupEnteraF = tiemCamFin;
                                String[] HoraSetupDivididaF = horaSetupEnteraF.split(":");
                                data[8] = HoraSetupDivididaF[0];
                                data[9] = HoraSetupDivididaF[1];
                            }

                            tiemCamStart = re21.getString("ini_tp");
                            String horaStartEntera = tiemCamStart;
                            String[] HoraStartDividida = horaStartEntera.split(":");
                            data[10] = HoraStartDividida[0];
                            data[11] = HoraStartDividida[1];

                            tiemProFin = re21.getString("fin_tp");
                            String horaStartEnteraF = tiemProFin;
                            String[] HoraStartDivididaF = horaStartEnteraF.split(":");
                            data[12] = HoraStartDivididaF[0];
                            data[13] = HoraStartDivididaF[1];
                            data[14] = re21.getString("pcs_pro_bush");
                            data[15] = re21.getString("pcs_pro_rg");
                            data[16] = re21.getString("pcs_pro_sr");
                            data[17] = re21.getString("pcs_buen_bush");
                            data[18] = re21.getString("scrap_bush");
                            data[19] = re21.getString("scrp_rg");
                            data[20] = re21.getString("scrap_sr");
                            dtm.addRow(data);
                        }

                    }
                }
                conne.close();
            } catch (SQLException ex) {
                Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void reporteEmpaqueMesas(Conection con, View_report_bg_general dm) throws ParseException {
        Connection conne = con.conexionMySQL();
        ResultSet re21;
        String mog, tiemCamStart, tiemCamFin, tu, fech, nopar;
        int turno;

        String fecha_inicio;
        String fecha_fin;

        Date fi = new Date();
        Date ff = new Date();
        DateFormat hourFormat = new SimpleDateFormat("yyyy-MM-dd");

        fi = dm.jDateChooserInicio.getDate();
        ff = dm.jDateChooserFin.getDate();

        if (fi == null || ff == null) {
            JOptionPane.showMessageDialog(null, "Debes de seleccionar alguna fecha para buscar");
        } else {
            fecha_inicio = hourFormat.format(fi);
            fecha_fin = hourFormat.format(ff);

            try {
                DefaultTableModel dtm = new DefaultTableModel();
                dm.jTableReporteDas.setRowHeight(20);

                dtm.addColumn("Line");
                dtm.addColumn("Year");
                dtm.addColumn("Month");
                dtm.addColumn("Day");
                dtm.addColumn("Shift_");
                dtm.addColumn("MOG_");
                dtm.addColumn("No. De Parte");
                dtm.addColumn("Start_Production");
                dtm.addColumn("Start_Production");
                dtm.addColumn("Finish_Production");
                dtm.addColumn("Finish_Production");
                dtm.addColumn("Prod. Qty._");
                dtm.addColumn("Good Parts_");
                dtm.addColumn("Scrap");
                dtm.addColumn("Unbalance_");

                dm.jTableReporteDas.setModel(dtm);
                dm.jTableReporteDas.setAutoResizeMode(0);
                for (int i = 0; i < dm.jTableReporteDas.getColumnCount(); i++) {
                    dm.jTableReporteDas.getColumnModel().getColumn(i).setPreferredWidth(120);
                }
                CallableStatement cst21 = conne.prepareCall("{call reporteDasBGEmpaque2(?,?,?)}");
                cst21.setString(1, fecha_inicio);
                cst21.setString(2, fecha_fin);
                cst21.registerOutParameter(3, java.sql.Types.INTEGER);
                re21 = cst21.executeQuery();
                int resul = cst21.getInt(3);

                if (resul == 0) {
                    JOptionPane.showMessageDialog(null, "No hay registros en DAS, REVISAR CON SUPERVISOR SI SE TRABAJÓ LA MÁQUINA, "
                            + " de lo contrario, NOTIFICAR A EQUIPO PAPERLESS ");
                } else {
                    while (re21.next()) {
                        Object[] data = new Object[19];
                        nopar = re21.getString("no_parte");
                        data[0] = re21.getString("linea");
                        fech = re21.getString("fecha");
                        String fechaEntera = fech;
                        String[] fechaDividida = fechaEntera.split("-");
                        data[1] = fechaDividida[0];
                        data[2] = fechaDividida[1];
                        data[3] = fechaDividida[2];
                        turno = re21.getInt("turno");
                        if (turno == 1) {
                            tu = "M";
                        } else {
                            tu = "V";
                        }

                        data[4] = tu;
                        mog = re21.getString("mog");
                        String m = mog.replace("MOG", "");
                        data[5] = m;
                        data[6] = nopar;
                        tiemCamStart = re21.getString("ini_tu");
                        if (tiemCamStart.equals("")) {
                            data[7] = "";
                            data[8] = "";
                        } else {
                            String horaSetupEntera = tiemCamStart;
                            String[] horaSetupDividida = horaSetupEntera.split(":");
                            data[7] = horaSetupDividida[0];
                            data[8] = horaSetupDividida[1];
                        }

                        tiemCamFin = re21.getString("fin_tu");
                        if (tiemCamFin.equals("")) {
                            data[9] = "";
                            data[10] = "";
                        } else {
                            String horaSetupEnteraF = tiemCamFin;
                            String[] HoraSetupDivididaF = horaSetupEnteraF.split(":");
                            data[9] = HoraSetupDivididaF[0];
                            data[10] = HoraSetupDivididaF[1];
                        }

                        data[11] = re21.getString("cant_proce");
                        data[13] = re21.getString("pza_Sort");
                        data[14] = re21.getString("Sobrante_Final");
                        int goof = Integer.parseInt(re21.getString("pza_buenas"));
                        int sbf = Integer.parseInt(re21.getString("Sobrante_Final"));
                        int total = Integer.parseInt(re21.getString("cant_proce"));
                        int sort = Integer.parseInt(re21.getString("pza_Sort"));

                        int cantidadTotal = sbf + goof + sort;

                        if (cantidadTotal == total) {
                            data[12] = re21.getString("pza_buenas");
                        } else {
                            //goof=goof+sbf;
                            data[12] = (total - sbf - sort);
                        }
                        //data[12] = (goof - sbf);
                        dtm.addRow(data);
                    }
                }
                conne.close();
            } catch (SQLException ex) {
                Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void reportePrensaByMOG(Conection con, View_report_bg_general dm) {
        Connection conne = con.conexionMySQL();
        ResultSet re21 = null, rnuevo;
        String mog, tiemCamStart, tiemCamFin, tiemProStart, tiemProFin, tu, fech, m, material, material2;
        int turno, idDas;
        
        int cont=0;
        String[] horaSetupDividida = new String[2];
        String[] HoraSetupDivididaF = new String[2];
        String mg=dm.jTextFieldMOG.getText();

        if (mg == null || mg.equals("")) {
            JOptionPane.showMessageDialog(null, "Debes colocar la MOG a buscar");
        } else {
            try {
                DefaultTableModel dtm = new DefaultTableModel();
                dm.jTableReporteDas.setRowHeight(20);
                dtm.addColumn("Line");
                dtm.addColumn("Month");
                dtm.addColumn("Day");
                dtm.addColumn("Year");
                dtm.addColumn("Shift_");
                dtm.addColumn("Material Lot_");
                dtm.addColumn("TERMINACION");
                dtm.addColumn("(m) Used");
                dtm.addColumn("MOG_");
                dtm.addColumn("Start_Setup");
                dtm.addColumn("Start_Setup");
                dtm.addColumn("Finish_Setup");
                dtm.addColumn("Finish_Setup");
                dtm.addColumn("Start_Production");
                dtm.addColumn("Start_Production");
                dtm.addColumn("Finish_Production");
                dtm.addColumn("Finish_Production");
                dtm.addColumn("Prod. Qty._");
                dtm.addColumn("Good Parts_");
                dtm.addColumn("Scrap (pcs)_");
                dtm.addColumn("BM (pcs)_");
                dm.jTableReporteDas.setModel(dtm);
                dm.jTableReporteDas.setAutoResizeMode(0);
                for (int i = 0; i < dm.jTableReporteDas.getColumnCount(); i++) {
                    dm.jTableReporteDas.getColumnModel().getColumn(i).setPreferredWidth(120);
                }
                CallableStatement cst21 = conne.prepareCall("{call reporteDasBGPrensabyMOG(?,?)}");
                cst21.setString(1, '%'+mg+'%');
                cst21.registerOutParameter(2, java.sql.Types.INTEGER);
                re21 = cst21.executeQuery();
                int resul = cst21.getInt(2);
                System.err.println(re21);
                int c = 0;

                if (resul == 0) {
                    JOptionPane.showMessageDialog(null, "No hay registros en DAS, REVISAR CON SUPERVISOR SI SE TRABAJÓ LA MÁQUINA, "
                            + " de lo contrario, NOTIFICAR A EQUIPO PAPERLESS");
                } else {

                    while (re21.next()) {

                        mog = re21.getString("mog");
                        if (mog.equals("MOG000000")) {

                        } else {
                            Object[] data = new Object[21];
                            idDas = Integer.parseInt(re21.getString("id_das"));
                            data[0] = re21.getString("linea");
                            fech = re21.getString("fecha");
                            String fechaEntera = fech;
                            String[] fechaDividida = fechaEntera.split("-");
                            data[1] = fechaDividida[1];
                            data[2] = fechaDividida[2];
                            data[3] = fechaDividida[0];
                            turno = re21.getInt("turno");

                            if (turno == 1) {
                                tu = "M";
                            } else {
                                tu = "V";
                            }
                            data[4] = tu;

                            mog = re21.getString("mog");
                            m = mog.replace("MOG", "");
                            data[8] = m;
                            tiemCamStart = re21.getString("ini_cm");
                            if (tiemCamStart.equals("")) {
                                data[9] = "";
                                data[10] = "";
                            } else {
                                String horaSetupEntera = tiemCamStart;
                                horaSetupDividida = horaSetupEntera.split(":");
                                data[9] = horaSetupDividida[0];
                                data[10] = horaSetupDividida[1];
                            }

                            tiemCamFin = re21.getString("fin_cm");
                            if (tiemCamFin.equals("")) {
                                data[11] = "";
                                data[12] = "";
                            } else {
                                String horaSetupEnteraF = tiemCamFin;
                                HoraSetupDivididaF = horaSetupEnteraF.split(":");
                                data[11] = HoraSetupDivididaF[0];
                                data[12] = HoraSetupDivididaF[1];
                            }

                            tiemProStart = re21.getString("ini_tp");
                            String horaStartEntera = tiemProStart;
                            String[] HoraStartDividida = horaStartEntera.split(":");
                            data[13] = HoraStartDividida[0];
                            data[14] = HoraStartDividida[1];
                            tiemProFin = re21.getString("fin_tp");
                            String horaStartEnteraF = tiemProFin;
                            String[] HoraStartDivididaF = horaStartEnteraF.split(":");
                            data[15] = HoraStartDivididaF[0];
                            data[16] = HoraStartDivididaF[1];
                            data[17] = re21.getString("pcs_pro");
                            System.err.println(re21.getString("pcs_pro"));
                            data[18] = re21.getString("pcs_buenas");
                            System.err.println(re21.getString("pcs_buenas"));
                            data[19] = re21.getString("pcs_scrap");
                            System.err.println(re21.getString("pcs_scrap"));
                            data[20] = re21.getString("pcs_bm");
                            System.err.println(re21.getString("pcs_bm"));

                            material = re21.getString("lote_coil");
                            
                            String materialEntero = material;
                            if (materialEntero == null || materialEntero.equals("")) {
                                data[5] = "";
                                data[6] = "";
                                data[7] = "";
                                dtm.addRow(data);
                            } else {
                                if (data[0].equals("TB03B") || data[0].equals("TB02B")) {
                                    
                                    if (re21.getString("metros").equals("0")) {
                                        if (material == null || material.equals("")) {
                                            data[5] = "";
                                            data[6] = "";
                                            data[7] = "";
                                            dtm.addRow(data);
                                        } else {
                                            String materialDividido = materialEntero.substring(0, materialEntero.length() - 3);
                                            int tama = materialEntero.length();
                                            tama = tama - 2;
                                            String materialDividido2 = materialEntero.substring(tama, materialEntero.length());
                                            data[0] = re21.getString("linea");
                                            data[1] = fechaDividida[1];
                                            data[2] = fechaDividida[2];
                                            data[3] = fechaDividida[0];
                                            data[4] = tu;
                                            data[5] = materialDividido;
                                            data[6] = materialDividido2;
                                            data[7] = re21.getString("metros");
                                            data[8] = m;/*
                                            tiemCamStart = re21.getString("ini_cm");
                                            if (tiemCamStart.equals("")) {
                                                data[9] = "";
                                                data[10] = "";
                                            } else {
                                                String horaSetupEntera = tiemCamStart;
                                                horaSetupDividida = horaSetupEntera.split(":");
                                                data[9] = horaSetupDividida[0];
                                                data[10] = horaSetupDividida[1];
                                            }

                                            tiemCamFin = re21.getString("fin_cm");
                                            if (tiemCamFin.equals("")) {
                                                data[11] = "";
                                                data[12] = "";
                                            } else {
                                                String horaSetupEnteraF = tiemCamFin;
                                                HoraSetupDivididaF = horaSetupEnteraF.split(":");
                                                data[11] = HoraSetupDivididaF[0];
                                                data[12] = HoraSetupDivididaF[1];
                                            }/*
                                            data[13] = HoraStartDividida[0];
                                            data[14] = HoraStartDividida[1];
                                            data[15] = HoraStartDivididaF[0];
                                            data[16] = HoraStartDivididaF[1];
                                            data[17] = re21.getString("pcs_pro");
                                            data[18] = re21.getString("pcs_buenas");
                                            data[19] = re21.getString("pcs_scrap");
                                            data[20] = re21.getString("pcs_bm");*/
                                            dtm.addRow(data);
                                        }
                                    } else {
                                        CallableStatement cst34 = conne.prepareCall("{call traerLotesDas(?,?,?,?)}");
                                        cst34.setInt(1, idDas);
                                        cst34.setString(2, mog);
                                        cst34.setString(3, tiemProStart);
                                        cst34.setString(4, tiemProFin);
                                        rnuevo = cst34.executeQuery();
                                        
                                        while (rnuevo.next()) {
                                            material2 = rnuevo.getString("lote_coil");
                                            String materialEntero1 = material2;
                                            if (materialEntero1.equals("")) {
                                                data[5] = "";
                                                data[6] = "";
                                            } else {
                                                if (material.equals(material2)) {
                                                    if(cont==0){
                                                        String materialDividido = materialEntero1.substring(0, materialEntero1.length() - 3);
                                                        int tama = materialEntero1.length();
                                                        tama = tama - 2;
                                                        String materialDividido2 = materialEntero1.substring(tama, materialEntero1.length());
                                                        data[0] = re21.getString("linea");
                                                        data[1] = fechaDividida[1];
                                                        data[2] = fechaDividida[2];
                                                        data[3] = fechaDividida[0];
                                                        data[4] = tu;
                                                        data[5] = materialDividido;
                                                        data[6] = materialDividido2;
                                                        data[7] = rnuevo.getString("metros");
                                                        data[8] = m;
                                                        if (tiemCamStart.equals("")) {
                                                            data[9] = "";
                                                            data[10] = "";
                                                        } else {
                                                            String horaSetupEntera = tiemCamStart;
                                                            horaSetupDividida = horaSetupEntera.split(":");
                                                            data[9] = horaSetupDividida[0];
                                                            data[10] = horaSetupDividida[1];
                                                        }

                                                        tiemCamFin = re21.getString("fin_cm");
                                                        if (tiemCamFin.equals("")) {
                                                            data[11] = "";
                                                            data[12] = "";
                                                        } else {
                                                            String horaSetupEnteraF = tiemCamFin;
                                                            HoraSetupDivididaF = horaSetupEnteraF.split(":");
                                                            data[11] = HoraSetupDivididaF[0];
                                                            data[12] = HoraSetupDivididaF[1];
                                                        }
                                                        data[13] = HoraStartDividida[0];
                                                        data[14] = HoraStartDividida[1];
                                                        data[15] = HoraStartDivididaF[0];
                                                        data[16] = HoraStartDivididaF[1];
                                                        data[17] = re21.getString("pcs_pro");
                                                        data[18] = re21.getString("pcs_buenas");
                                                        data[19] = re21.getString("pcs_scrap");
                                                        data[20] = re21.getString("pcs_bm");

                                                        dtm.addRow(data);
                                                        cont ++;
                                                    }else{
                                                        String materialDividido = materialEntero1.substring(0, materialEntero1.length() - 3);
                                                        int tama = materialEntero1.length();
                                                        tama = tama - 2;
                                                        String materialDividido2 = materialEntero1.substring(tama, materialEntero1.length());
                                                        data[0] = re21.getString("linea");
                                                        data[1] = fechaDividida[1];
                                                        data[2] = fechaDividida[2];
                                                        data[3] = fechaDividida[0];
                                                        data[4] = tu;
                                                        data[5] = materialDividido;
                                                        data[6] = materialDividido2;
                                                        data[7] = rnuevo.getString("metros");
                                                        data[8] = m;
                                                        data[9] = "";
                                                        data[10] = "";
                                                        data[11] = "";
                                                        data[12] = "";
                                                        data[13] = "";
                                                        data[14] = "";
                                                        data[15] = "";
                                                        data[16] = "";
                                                        data[17] = "";
                                                        data[18] = "";
                                                        data[19] = "";
                                                        data[20] = "";
                                                        dtm.addRow(data);
                                                        cont=0;
                                                    }
                                                } else {
                                                    String materialDividido = materialEntero1.substring(0, materialEntero1.length() - 3);
                                                    int tama = materialEntero1.length();
                                                    tama = tama - 2;
                                                    String materialDividido2 = materialEntero1.substring(tama, materialEntero1.length());
                                                    data[0] = re21.getString("linea");
                                                    data[1] = fechaDividida[1];
                                                    data[2] = fechaDividida[2];
                                                    data[3] = fechaDividida[0];
                                                    data[4] = tu;
                                                    data[5] = materialDividido;
                                                    data[6] = materialDividido2;
                                                    data[7] = rnuevo.getString("metros");
                                                    data[8] = m;
                                                    data[9] = "";
                                                    data[10] = "";
                                                    data[11] = "";
                                                    data[12] = "";
                                                    data[13] = "";
                                                    data[14] = "";
                                                    data[15] = "";
                                                    data[16] = "";
                                                    data[17] = "";
                                                    data[18] = "";
                                                    data[19] = "";
                                                    data[20] = "";
                                                    dtm.addRow(data);
                                                }
                                            }
                                        }
                                        cont=0;
                                    }

                                } else {
                                    String materialDividido = materialEntero.substring(0, materialEntero.length() - 3);
                                    int tama = materialEntero.length();
                                    tama = tama - 2;
                                    String materialDividido2 = materialEntero.substring(tama, materialEntero.length());
                                    data[5] = materialDividido;
                                    data[6] = materialDividido2;
                                    data[7] = re21.getString("metros");
                                    dtm.addRow(data);
                                }
                            }
                        }
                    }
                }
                conne.close();
            } catch (SQLException ex) {
                Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void reporteColores(Conection con, View_report_bg_general dm) throws ParseException {
        Connection conne = con.conexionMySQL();
        ResultSet re21;
        String mog, tiemCamStart, tiemCamFin, tu, fech;

        String fecha_inicio;
        String fecha_fin;

        Date fi = new Date();
        Date ff = new Date();
        DateFormat hourFormat = new SimpleDateFormat("yyyy-MM-dd");

        fi = dm.jDateChooserInicio.getDate();
        ff = dm.jDateChooserFin.getDate();

        if (fi == null || ff == null) {
            JOptionPane.showMessageDialog(null, "Debes de seleccionar alguna fecha para buscar");
        } else {
            fecha_inicio = hourFormat.format(fi);
            fecha_fin = hourFormat.format(ff);

            try {
                DefaultTableModel dtm = new DefaultTableModel();
                dm.jTableReporteDas.setRowHeight(20);
                dtm.addColumn("Línea");
                dtm.addColumn("Fecha");
                dm.jTableReporteDas.setModel(dtm);
                CallableStatement cst21 = conne.prepareCall("{call reporteColoresAduana(?,?)}");
                cst21.setString(1, fecha_inicio);
                cst21.setString(2, fecha_fin);
                re21 = cst21.executeQuery();
                while (re21.next()) {
                    Object[] data = new Object[2];
                    data[0] = re21.getString("linea");
                    data[1] = re21.getString("fecha");
                    dtm.addRow(data);
                }
                conne.close();
            } catch (SQLException ex) {
                Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void reporte(JTable tabla) {
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
                Sheet excelHoja = libro.createSheet("DAS");
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
                                h = "0";
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

    public void limpiar(View_report_bg_general dm) {
        dm.buttonGroup1.clearSelection();/*
       dm.jCheckBoxTB92.setSelected(false);
       dm.jCheckBoxTB91.setSelected(false);
       dm.jCheckBoxEmpaque.setSelected(false);
       dm.jCheckBoxTB01.setSelected(false);
       dm.jCheckBoxTB02B.setSelected(false);
       dm.jCheckBoxTB02F.setSelected(false);
       dm.jCheckBoxTB03F.setSelected(true);
       dm.jCheckBoxTB03B.setSelected(false);
       dm.jCheckBoxTB05.setSelected(false);
       dm.jCheckBoxTB06.setSelected(false);
       dm.jCheckBoxTB31.setSelected(false);
       dm.jCheckBoxTB32.setSelected(false);
       dm.jCheckBoxTB51.setSelected(false);
       dm.jCheckBoxTB71.setSelected(false);
       dm.jCheckBoxPlatinado.setSelected(false);*/
        dm.jDateChooserFin.setDate(null);
        dm.jDateChooserInicio.setDate(null);
        DefaultTableModel dtm = new DefaultTableModel();
        dm.jTableReporteDas.setModel(dtm);
        dm.jTableReporteDas.setAutoResizeMode(0);
        
        dm.jComboBoxProce.setSelectedIndex(0);
        dm.jTextFieldMOG.setText(null);
    }
    
    public void reporteBushByMOG(Conection con, View_report_bg_general dm){
        Connection conne = con.conexionMySQL();
        ResultSet re21;
        String mog, tiemCamStart, tiemCamFin, tiemProStart, tiemProFin, tu, fech, m;
        int turno;

        String searchMog=dm.jTextFieldMOG.getText();

        if (searchMog == null || searchMog.equals("")) {
            JOptionPane.showMessageDialog(null, "Debes colocar la MOG a buscar");
        } else {
            
            try {
                DefaultTableModel dtm = new DefaultTableModel();
                dm.jTableReporteDas.setRowHeight(20);
                dtm.addColumn("Line");
                dtm.addColumn("Year");
                dtm.addColumn("Month");
                dtm.addColumn("Day");
                dtm.addColumn("Shift_");
                dtm.addColumn("MOG_");
                dtm.addColumn("Start_Setup");
                dtm.addColumn("Start_Setup");
                dtm.addColumn("Finish_Setup");
                dtm.addColumn("Finish_Setup");
                dtm.addColumn("Start_Production");
                dtm.addColumn("Start_Production");
                dtm.addColumn("Finish_Production");
                dtm.addColumn("Finish_Production");
                dtm.addColumn("Prod. Qty._");
                dtm.addColumn("Good Parts_");
                dtm.addColumn("BG Scrap");
                dtm.addColumn("RG Scrap");
                dm.jTableReporteDas.setModel(dtm);
                dm.jTableReporteDas.setAutoResizeMode(0);
                for (int i = 0; i < dm.jTableReporteDas.getColumnCount(); i++) {
                    dm.jTableReporteDas.getColumnModel().getColumn(i).setPreferredWidth(120);
                }
                CallableStatement cst21 = conne.prepareCall("{call reporteDasBGGeneralbyMOG(?,?)}");
                cst21.setString(1, '%'+searchMog+'%');
                cst21.registerOutParameter(2, java.sql.Types.INTEGER);
                re21 = cst21.executeQuery();
                int resul = cst21.getInt(2);

                if (resul == 0) {
                    JOptionPane.showMessageDialog(null, "No hay registros en DAS, REVISAR CON SUPERVISOR SI SE TRABAJÓ LA MÁQUINA, "
                            + " de lo contrario, NOTIFICAR A EQUIPO PAPERLESS ");
                } else {
                    while (re21.next()) {
                        mog = re21.getString("mog");
                        if (mog.equals("MOG000000")) {

                        } else {
                            Object[] data = new Object[17];
                            data[0] = re21.getString("linea");
                            fech = re21.getString("fecha");
                            String fechaEntera = fech;
                            String[] fechaDividida = fechaEntera.split("-");
                            data[1] = fechaDividida[0];
                            data[2] = fechaDividida[1];
                            data[3] = fechaDividida[2];
                            turno = re21.getInt("turno");
                            if (turno == 1) {
                                tu = "M";
                            } else {
                                tu = "V";
                            }

                            data[4] = tu;
                            mog = re21.getString("mog");
                            m = mog.replace("MOG", "");
                            data[5] = m;

                            tiemCamStart = re21.getString("ini_cm");
                            if (tiemCamStart.equals("") || tiemCamStart == null || tiemCamStart.equals(" ")) {
                                data[6] = "";
                                data[7] = "";
                            } else {
                                System.out.println(mog);
                                System.out.println(tiemCamStart);

                                String horaSetupEntera = tiemCamStart;
                                System.out.println(horaSetupEntera);

                                String[] horaSetupDividida = horaSetupEntera.split(":");
                                System.out.println(horaSetupDividida[0]);
                                data[6] = horaSetupDividida[0];
                                data[7] = horaSetupDividida[1];
                            }

                            tiemCamFin = re21.getString("fin_cm");
                            if (tiemCamFin.equals("") || tiemCamFin == null || tiemCamFin.equals(" ")) {
                                data[8] = "";
                                data[9] = "";
                            } else {
                                String horaSetupEnteraF = tiemCamFin;
                                String[] HoraSetupDivididaF = horaSetupEnteraF.split(":");
                                data[8] = HoraSetupDivididaF[0];
                                data[9] = HoraSetupDivididaF[1];
                            }

                            tiemCamStart = re21.getString("ini_tp");
                            String horaStartEntera = tiemCamStart;
                            String[] HoraStartDividida = horaStartEntera.split(":");
                            data[10] = HoraStartDividida[0];
                            data[11] = HoraStartDividida[1];

                            tiemProFin = re21.getString("fin_tp");
                            String horaStartEnteraF = tiemProFin;
                            String[] HoraStartDivididaF = horaStartEnteraF.split(":");
                            data[12] = HoraStartDivididaF[0];
                            data[13] = HoraStartDivididaF[1];

                            data[14] = re21.getString("pcs_pro_bush");
                            data[15] = re21.getString("pcs_buen_bush");
                            data[16] = re21.getString("scrap_bush");
                            dtm.addRow(data);
                        }
                    }
                }
                conne.close();
            } catch (SQLException ex) {
                Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void reportePlatinadoByMog(Conection con, View_report_bg_general dm){
        Connection conne = con.conexionMySQL();
        ResultSet r23;
        String mog, tiemCamStart, tiemCamFin, tu, fech;
        int turno, razon1, razon2, razon3, razon4, razon5, razon6, razon7, razon8, razon9, scra;

        int idDasPLT;
        int mogg = 0;

        String searchMog=dm.jTextFieldMOG.getText();


            try {
                DefaultTableModel dtm = new DefaultTableModel();
                dm.jTableReporteDas.setRowHeight(20);
                dtm.addColumn("Line");
                dtm.addColumn("Year");
                dtm.addColumn("Month");
                dtm.addColumn("Day");
                dtm.addColumn("Shift_");
                dtm.addColumn("MOG_");
                dtm.addColumn("");
                dtm.addColumn("");
                dtm.addColumn("");
                dtm.addColumn("");
                dtm.addColumn("");
                dtm.addColumn("Start_Production");
                dtm.addColumn("Start_Production");
                dtm.addColumn("Finish_Production");
                dtm.addColumn("Finish_Production");
                dtm.addColumn("Prod. Qty._");
                dtm.addColumn("Good Parts_");
                dtm.addColumn("Scrap");
                //razon1 razon2	razon3	razon4	razon5	razon6	razon7	razon8	razon9;
                dm.jTableReporteDas.setModel(dtm);
                dm.jTableReporteDas.setAutoResizeMode(0);
                for (int i = 0; i < dm.jTableReporteDas.getColumnCount(); i++) {
                    dm.jTableReporteDas.getColumnModel().getColumn(i).setPreferredWidth(120);
                }

                CallableStatement cst21 = conne.prepareCall("{call reporteDasBGPlatinadoFinalbyMog(?)}");
                cst21.setString(1, '%'+searchMog+'%');
                r23 = cst21.executeQuery();
                while (r23.next()) {
                        mogg = r23.getInt("mog_idmog");
                        Object[] data = new Object[18];
                        data[0] = r23.getString("linea");
                        fech = r23.getString("fecha");
                        String fechaEntera = fech;
                        String[] fechaDividida = fechaEntera.split("-");
                        data[1] = fechaDividida[0];
                        data[2] = fechaDividida[1];
                        data[3] = fechaDividida[2];
                        turno = r23.getInt("turno");
                        if (turno == 1) {
                            tu = "M";
                        } else {
                            tu = "V";
                        }

                        data[4] = tu;
                        mog = r23.getString("mog");
                        String m = mog.replace("MOG", "");
                        data[5] = m;
                        data[6] = "";
                        data[7] = "";
                        data[8] = "";
                        data[9] = "";
                        data[10] = "";
                        tiemCamStart = r23.getString("ini_tur");

                        if (tiemCamStart.equals("")) {
                            data[11] = "";
                            data[12] = "";
                        } else {
                            String horaSetupEntera = tiemCamStart;
                            String[] horaSetupDividida = horaSetupEntera.split(":");
                            data[11] = horaSetupDividida[0];
                            data[12] = horaSetupDividida[1];
                        }

                        tiemCamFin = r23.getString("fin_tur");
                        if (tiemCamFin.equals("")) {
                            data[13] = "";
                            data[14] = "";
                        } else {
                            String horaSetupEnteraF = tiemCamFin;
                            String[] HoraSetupDivididaF = horaSetupEnteraF.split(":");
                            data[13] = HoraSetupDivididaF[0];
                            data[14] = HoraSetupDivididaF[1];
                        }

                        razon1 = r23.getInt("razon1");
                        razon2 = r23.getInt("razon2");
                        razon3 = r23.getInt("razon3");
                        razon4 = r23.getInt("razon4");
                        razon5 = r23.getInt("razon5");
                        razon6 = r23.getInt("razon6");
                        razon7 = r23.getInt("razon7");
                        razon8 = r23.getInt("razon8");
                        razon9 = r23.getInt("razon9");
                        scra = razon1 + razon2 + razon3 + razon4 + razon5 + razon6 + razon7 + razon8 + razon9;

                        data[15] = r23.getString("cantidad_piezas_procesadas");
                        data[16] = r23.getString("cantPzaGood");
                        data[17] = scra;
                        dtm.addRow(data);
                }
                if (dtm.getRowCount() <= 0) {
                    JOptionPane.showMessageDialog(null, "No hay registros en DAS, REVISAR CON SUPERVISOR SI SE TRABAJÓ LA MÁQUINA, "
                            + " de lo contrario, NOTIFICAR A EQUIPO PAPERLESS ");
                }

                conne.close();
            } catch (SQLException ex) {
                Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    
    /*public void reporteAssy91ByMOG(Conection con, View_report_bg_general dm){
        Connection conne = con.conexionMySQL();
        ResultSet re21;
        String mog, tiemCamStart, tiemCamFin, tiemProStart, tiemProFin, tu, fech, m;
        int turno;
        String mg=dm.jTextFieldMOG.getText();
        

        if (mg == null || mg.equals("")) {
            JOptionPane.showMessageDialog(null, "Debes colocar la mog a buscar");
        } else {
            try {
                DefaultTableModel dtm = new DefaultTableModel();
                dm.jTableReporteDas.setRowHeight(20);
                dtm.addColumn("Line");
                dtm.addColumn("Year");
                dtm.addColumn("Month");
                dtm.addColumn("Day");
                dtm.addColumn("Shift_");
                dtm.addColumn("MOG_");
                dtm.addColumn("Start_Setup");
                dtm.addColumn("Start_Setup");
                dtm.addColumn("Finish_Setup");
                dtm.addColumn("Finish_Setup");
                dtm.addColumn("Start_Production");
                dtm.addColumn("Start_Production");
                dtm.addColumn("Finish_Production");
                dtm.addColumn("Finish_Production");
                dtm.addColumn("Processed Qty.Bushing_");
                dtm.addColumn("Processed Qty.Rod Guides_");
                dtm.addColumn("Good Parts_");
                dtm.addColumn("Scrap BG_");
                dtm.addColumn("Scrap RG_");
                dm.jTableReporteDas.setModel(dtm);
                dm.jTableReporteDas.setAutoResizeMode(0);
                for (int i = 0; i < dm.jTableReporteDas.getColumnCount(); i++) {
                    dm.jTableReporteDas.getColumnModel().getColumn(i).setPreferredWidth(120);
                }
                CallableStatement cst21 = conne.prepareCall("{call reporteDasBGAssy91byMOG(?,?)}");
                cst21.setString(1, '%'+mg+'%');
                cst21.registerOutParameter(2, java.sql.Types.INTEGER);
                re21 = cst21.executeQuery();
                int resul = cst21.getInt(2);
                if (resul == 0) {
                    JOptionPane.showMessageDialog(null, "No hay registros en DAS, REVISAR CON SUPERVISOR SI SE TRABAJÓ LA MÁQUINA, "
                            + " de lo contrario, NOTIFICAR A EQUIPO PAPERLESS ");
                } else {
                    while (re21.next()) {
                        mog = re21.getString("mog");
                        if (mog.equals("MOG000000")) {

                        } else {
                            Object[] data = new Object[19];
                            data[0] = re21.getString("linea");
                            fech = re21.getString("fecha");
                            String fechaEntera = fech;
                            String[] fechaDividida = fechaEntera.split("-");
                            data[1] = fechaDividida[0];
                            data[2] = fechaDividida[1];
                            data[3] = fechaDividida[2];
                            turno = re21.getInt("turno");
                            if (turno == 1) {
                                tu = "M";
                            } else {
                                tu = "V";
                            }

                            data[4] = tu;
                            mog = re21.getString("mog");
                            m = mog.replace("MOG", "");
                            data[5] = m;

                            tiemCamStart = re21.getString("ini_cm");
                            if (tiemCamStart.equals("")) {
                                data[6] = "";
                                data[7] = "";
                            } else {
                                String horaSetupEntera = tiemCamStart;
                                String[] horaSetupDividida = horaSetupEntera.split(":");
                                data[6] = horaSetupDividida[0];
                                data[7] = horaSetupDividida[1];
                            }

                            tiemCamFin = re21.getString("fin_cm");
                            if (tiemCamFin.equals("")) {
                                data[8] = "";
                                data[9] = "";
                            } else {
                                String horaSetupEnteraF = tiemCamFin;
                                String[] HoraSetupDivididaF = horaSetupEnteraF.split(":");
                                data[8] = HoraSetupDivididaF[0];
                                data[9] = HoraSetupDivididaF[1];
                            }

                            tiemCamStart = re21.getString("ini_tp");
                            String horaStartEntera = tiemCamStart;
                            String[] HoraStartDividida = horaStartEntera.split(":");
                            data[10] = HoraStartDividida[0];
                            data[11] = HoraStartDividida[1];

                            tiemProFin = re21.getString("fin_tp");
                            String horaStartEnteraF = tiemProFin;
                            String[] HoraStartDivididaF = horaStartEnteraF.split(":");
                            data[12] = HoraStartDivididaF[0];
                            data[13] = HoraStartDivididaF[1];
                            data[14] = re21.getString("pcs_pro_bush");
                            data[15] = re21.getString("pcs_pro_rg");
                            data[16] = re21.getString("pcs_buen_bush");
                            data[17] = re21.getString("scrap_bush");
                            data[18] = re21.getString("scrp_rg");
                            dtm.addRow(data);
                        }
                    }
                }
                conne.close();
            } catch (SQLException ex) {
                Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }*/
    public void reporteAssy91ByMOG(Conection con, View_report_bg_general dm){
        Connection conne = con.conexionMySQL();
        ResultSet re21;
        String mog, tiemCamStart, tiemCamFin, tiemProStart, tiemProFin, tu, fech, m;
        int turno;
        String mg=dm.jTextFieldMOG.getText();
        

        if (mg == null || mg.equals("")) {
            JOptionPane.showMessageDialog(null, "Debes colocar la mog a buscar");
        } else {
            try {
                DefaultTableModel dtm = new DefaultTableModel();
                dm.jTableReporteDas.setRowHeight(20);
                dtm.addColumn("Line");
                dtm.addColumn("Year");
                dtm.addColumn("Month");
                dtm.addColumn("Day");
                dtm.addColumn("Shift_");
                dtm.addColumn("MOG_");
                dtm.addColumn("Start_Setup");
                dtm.addColumn("Start_Setup");
                dtm.addColumn("Finish_Setup");
                dtm.addColumn("Finish_Setup");
                dtm.addColumn("Start_Production");
                dtm.addColumn("Start_Production");
                dtm.addColumn("Finish_Production");
                dtm.addColumn("Finish_Production");
                dtm.addColumn("Processed Qty.Bushing_");
                dtm.addColumn("Processed Qty.Rod Guides_");
                dtm.addColumn("Processed Qty.Seal Ring_");
                dtm.addColumn("Good Parts_");
                dtm.addColumn("Scrap BG_");
                dtm.addColumn("Scrap RG_");
                dtm.addColumn("Scrap SR_");
                dm.jTableReporteDas.setModel(dtm);
                dm.jTableReporteDas.setAutoResizeMode(0);
                for (int i = 0; i < dm.jTableReporteDas.getColumnCount(); i++) {
                    dm.jTableReporteDas.getColumnModel().getColumn(i).setPreferredWidth(120);
                }
                CallableStatement cst21 = conne.prepareCall("{call reporteDasBGAssy91byMOGv2(?,?)}");
                cst21.setString(1, '%'+mg+'%');
                cst21.registerOutParameter(2, java.sql.Types.INTEGER);
                re21 = cst21.executeQuery();
                int resul = cst21.getInt(2);
                if (resul == 0) {
                    JOptionPane.showMessageDialog(null, "No hay registros en DAS, REVISAR CON SUPERVISOR SI SE TRABAJÓ LA MÁQUINA, "
                            + " de lo contrario, NOTIFICAR A EQUIPO PAPERLESS ");
                } else {
                    while (re21.next()) {
                        mog = re21.getString("mog");
                        if (mog.equals("MOG000000")) {

                        } else {
                            Object[] data = new Object[21];
                            data[0] = re21.getString("linea");
                            fech = re21.getString("fecha");
                            String fechaEntera = fech;
                            String[] fechaDividida = fechaEntera.split("-");
                            data[1] = fechaDividida[0];
                            data[2] = fechaDividida[1];
                            data[3] = fechaDividida[2];
                            turno = re21.getInt("turno");
                            if (turno == 1) {
                                tu = "M";
                            } else {
                                tu = "V";
                            }

                            data[4] = tu;
                            mog = re21.getString("mog");
                            m = mog.replace("MOG", "");
                            data[5] = m;

                            tiemCamStart = re21.getString("ini_cm");
                            if (tiemCamStart.equals("")) {
                                data[6] = "";
                                data[7] = "";
                            } else {
                                String horaSetupEntera = tiemCamStart;
                                String[] horaSetupDividida = horaSetupEntera.split(":");
                                data[6] = horaSetupDividida[0];
                                data[7] = horaSetupDividida[1];
                            }

                            tiemCamFin = re21.getString("fin_cm");
                            if (tiemCamFin.equals("")) {
                                data[8] = "";
                                data[9] = "";
                            } else {
                                String horaSetupEnteraF = tiemCamFin;
                                String[] HoraSetupDivididaF = horaSetupEnteraF.split(":");
                                data[8] = HoraSetupDivididaF[0];
                                data[9] = HoraSetupDivididaF[1];
                            }

                            tiemCamStart = re21.getString("ini_tp");
                            String horaStartEntera = tiemCamStart;
                            String[] HoraStartDividida = horaStartEntera.split(":");
                            data[10] = HoraStartDividida[0];
                            data[11] = HoraStartDividida[1];

                            tiemProFin = re21.getString("fin_tp");
                            String horaStartEnteraF = tiemProFin;
                            String[] HoraStartDivididaF = horaStartEnteraF.split(":");
                            data[12] = HoraStartDivididaF[0];
                            data[13] = HoraStartDivididaF[1];
                            data[14] = re21.getString("pcs_pro_bush");
                            data[15] = re21.getString("pcs_pro_rg");
                            data[16] = re21.getString("pcs_pro_sr");
                            data[17] = re21.getString("pcs_buen_bush");
                            data[18] = re21.getString("scrap_bush");
                            data[19] = re21.getString("scrp_rg");
                            data[20] = re21.getString("scrap_sr");
                            dtm.addRow(data);
                        }
                    }
                }
                conne.close();
            } catch (SQLException ex) {
                Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void reporteAssy92ByMOG(Conection con, View_report_bg_general dm){
        Connection conne = con.conexionMySQL();
        ResultSet re21;
        String mog, tiemCamStart, tiemCamFin, tiemProStart, tiemProFin, tu, fech, m;
        int turno;
        String mg=dm.jTextFieldMOG.getText();

        if (mg == null || mg.equals("")) {
            JOptionPane.showMessageDialog(null, "Debes colocar la MOG a buscar");
        } else {
            try {
                DefaultTableModel dtm = new DefaultTableModel();
                dm.jTableReporteDas.setRowHeight(20);
                dtm.addColumn("Line");
                dtm.addColumn("Year");
                dtm.addColumn("Month");
                dtm.addColumn("Day");
                dtm.addColumn("Shift_");
                dtm.addColumn("MOG_");
                dtm.addColumn("Start_Setup");
                dtm.addColumn("Start_Setup");
                dtm.addColumn("Finish_Setup");
                dtm.addColumn("Finish_Setup");
                dtm.addColumn("Start_Production");
                dtm.addColumn("Start_Production");
                dtm.addColumn("Finish_Production");
                dtm.addColumn("Finish_Production");
                dtm.addColumn("Processed Qty.Bushing_");
                dtm.addColumn("Processed Qty.Rod Guides_");
                dtm.addColumn("Processed Qty. Seal Ring_");
                dtm.addColumn("Good Parts_");
                dtm.addColumn("Scrap BG_");
                dtm.addColumn("Scrap RG_");
                dtm.addColumn("Scrap SR_");
                dm.jTableReporteDas.setModel(dtm);
                dm.jTableReporteDas.setAutoResizeMode(0);
                for (int i = 0; i < dm.jTableReporteDas.getColumnCount(); i++) {
                    dm.jTableReporteDas.getColumnModel().getColumn(i).setPreferredWidth(120);
                }
                CallableStatement cst21 = conne.prepareCall("{call reporteDasBGAssy92byMOG(?,?)}");
                cst21.setString(1, '%'+mg+'%');
                cst21.registerOutParameter(2, java.sql.Types.INTEGER);
                re21 = cst21.executeQuery();
                int resul = cst21.getInt(2);
                if (resul == 0) {
                    JOptionPane.showMessageDialog(null, "No hay registros en DAS, REVISAR CON SUPERVISOR SI SE TRABAJÓ LA MÁQUINA, "
                            + " de lo contrario, NOTIFICAR A EQUIPO PAPERLESS ");
                } else {
                    while (re21.next()) {
                        mog = re21.getString("mog");
                        if (mog.equals("MOG000000")) {

                        } else {
                            Object[] data = new Object[21];
                            data[0] = re21.getString("linea");
                            fech = re21.getString("fecha");
                            String fechaEntera = fech;
                            String[] fechaDividida = fechaEntera.split("-");
                            data[1] = fechaDividida[0];
                            data[2] = fechaDividida[1];
                            data[3] = fechaDividida[2];
                            turno = re21.getInt("turno");
                            if (turno == 1) {
                                tu = "M";
                            } else {
                                tu = "V";
                            }

                            data[4] = tu;
                            mog = re21.getString("mog");
                            m = mog.replace("MOG", "");
                            data[5] = m;

                            tiemCamStart = re21.getString("ini_cm");
                            if (tiemCamStart.equals("")) {
                                data[6] = "";
                                data[7] = "";
                            } else {
                                String horaSetupEntera = tiemCamStart;
                                String[] horaSetupDividida = horaSetupEntera.split(":");
                                data[6] = horaSetupDividida[0];
                                data[7] = horaSetupDividida[1];
                            }

                            tiemCamFin = re21.getString("fin_cm");
                            if (tiemCamFin.equals("")) {
                                data[8] = "";
                                data[9] = "";
                            } else {
                                String horaSetupEnteraF = tiemCamFin;
                                String[] HoraSetupDivididaF = horaSetupEnteraF.split(":");
                                data[8] = HoraSetupDivididaF[0];
                                data[9] = HoraSetupDivididaF[1];
                            }

                            tiemCamStart = re21.getString("ini_tp");
                            String horaStartEntera = tiemCamStart;
                            String[] HoraStartDividida = horaStartEntera.split(":");
                            data[10] = HoraStartDividida[0];
                            data[11] = HoraStartDividida[1];

                            tiemProFin = re21.getString("fin_tp");
                            String horaStartEnteraF = tiemProFin;
                            String[] HoraStartDivididaF = horaStartEnteraF.split(":");
                            data[12] = HoraStartDivididaF[0];
                            data[13] = HoraStartDivididaF[1];
                            data[14] = re21.getString("pcs_pro_bush");
                            data[15] = re21.getString("pcs_pro_rg");
                            data[16] = re21.getString("pcs_pro_sr");
                            data[17] = re21.getString("pcs_buen_bush");
                            data[18] = re21.getString("scrap_bush");
                            data[19] = re21.getString("scrp_rg");
                            data[20] = re21.getString("scrap_sr");
                            dtm.addRow(data);
                        }

                    }
                }
                conne.close();
            } catch (SQLException ex) {
                Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void reporteEmpaqueMesasByMOG(Conection con, View_report_bg_general dm){
        Connection conne = con.conexionMySQL();
        ResultSet re21;
        String mog, tiemCamStart, tiemCamFin, tu, fech, nopar;
        int turno;

        String mg=dm.jTextFieldMOG.getText();

        if (mg == null || mg.equals("")) {
            JOptionPane.showMessageDialog(null, "Debes colocar la MOG a buscar");
        } else {
            try {
                DefaultTableModel dtm = new DefaultTableModel();
                dm.jTableReporteDas.setRowHeight(20);

                dtm.addColumn("Line");
                dtm.addColumn("Year");
                dtm.addColumn("Month");
                dtm.addColumn("Day");
                dtm.addColumn("Shift_");
                dtm.addColumn("MOG_");
                dtm.addColumn("No. De Parte");
                dtm.addColumn("Start_Production");
                dtm.addColumn("Start_Production");
                dtm.addColumn("Finish_Production");
                dtm.addColumn("Finish_Production");
                dtm.addColumn("Prod. Qty._");
                dtm.addColumn("Good Parts_");
                dtm.addColumn("Scrap");
                dtm.addColumn("Unbalance_");

                dm.jTableReporteDas.setModel(dtm);
                dm.jTableReporteDas.setAutoResizeMode(0);
                for (int i = 0; i < dm.jTableReporteDas.getColumnCount(); i++) {
                    dm.jTableReporteDas.getColumnModel().getColumn(i).setPreferredWidth(120);
                }
                CallableStatement cst21 = conne.prepareCall("{call reporteDASBGEmpaqueByMOG(?,?)}");
                cst21.setString(1,'%'+ mg + '%');
                cst21.registerOutParameter(2, java.sql.Types.INTEGER);
                re21 = cst21.executeQuery();
                int resul = cst21.getInt(2);

                if (resul == 0) {
                    JOptionPane.showMessageDialog(null, "No hay registros en DAS, REVISAR CON SUPERVISOR SI SE TRABAJÓ LA MÁQUINA, "
                            + " de lo contrario, NOTIFICAR A EQUIPO PAPERLESS ");
                } else {
                    while (re21.next()) {
                        Object[] data = new Object[19];
                        nopar = re21.getString("no_parte");
                        data[0] = re21.getString("linea");
                        fech = re21.getString("fecha");
                        String fechaEntera = fech;
                        String[] fechaDividida = fechaEntera.split("-");
                        data[1] = fechaDividida[0];
                        data[2] = fechaDividida[1];
                        data[3] = fechaDividida[2];
                        turno = re21.getInt("turno");
                        if (turno == 1) {
                            tu = "M";
                        } else {
                            tu = "V";
                        }

                        data[4] = tu;
                        mog = re21.getString("mog");
                        String m = mog.replace("MOG", "");
                        data[5] = m;
                        data[6] = nopar;
                        tiemCamStart = re21.getString("ini_tu");
                        if (tiemCamStart.equals("")) {
                            data[7] = "";
                            data[8] = "";
                        } else {
                            String horaSetupEntera = tiemCamStart;
                            String[] horaSetupDividida = horaSetupEntera.split(":");
                            data[7] = horaSetupDividida[0];
                            data[8] = horaSetupDividida[1];
                        }

                        tiemCamFin = re21.getString("fin_tu");
                        if (tiemCamFin.equals("")) {
                            data[9] = "";
                            data[10] = "";
                        } else {
                            String horaSetupEnteraF = tiemCamFin;
                            String[] HoraSetupDivididaF = horaSetupEnteraF.split(":");
                            data[9] = HoraSetupDivididaF[0];
                            data[10] = HoraSetupDivididaF[1];
                        }

                        data[11] = re21.getString("cant_proce");
                        data[13] = re21.getString("pza_Sort");
                        data[14] = re21.getString("Sobrante_Final");
                        int goof = Integer.parseInt(re21.getString("pza_buenas"));
                        int sbf = Integer.parseInt(re21.getString("Sobrante_Final"));
                        int total = Integer.parseInt(re21.getString("cant_proce"));
                        int sort = Integer.parseInt(re21.getString("pza_Sort"));

                        int cantidadTotal = sbf + goof + sort;

                        if (cantidadTotal == total) {
                            data[12] = re21.getString("pza_buenas");
                        } else {
                            //goof=goof+sbf;
                            data[12] = (total - sbf - sort);
                        }
                        //data[12] = (goof - sbf);
                        dtm.addRow(data);
                    }
                }
                conne.close();
            } catch (SQLException ex) {
                Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void reportePrensa(Conection con, View_report_bg_general dm){
         Connection conne = con.conexionMySQL();
        ResultSet re21 = null, rnuevo;
        String mog, tiemCamStart, tiemCamFin, tiemProStart, tiemProFin, tu, fech, m, material, material2;
        int turno, idDas;
        String fecha_inicio;
        String fecha_fin;
        int cont=0;
        String[] horaSetupDividida = new String[2];
        String[] HoraSetupDivididaF = new String[2];
                
        Date fi = new Date();
        Date ff = new Date();
        DateFormat hourFormat = new SimpleDateFormat("yyyy-MM-dd");

        fi = dm.jDateChooserInicio.getDate();
        ff = dm.jDateChooserFin.getDate();

        if (fi == null || ff == null) {
            JOptionPane.showMessageDialog(null, "Debes de seleccionar alguna fecha para buscar");
        } else {
            fecha_inicio = hourFormat.format(fi);
            fecha_fin = hourFormat.format(ff);

            try {
                DefaultTableModel dtm = new DefaultTableModel();
                dm.jTableReporteDas.setRowHeight(20);
                dtm.addColumn("Line");
                dtm.addColumn("Month");
                dtm.addColumn("Day");
                dtm.addColumn("Year");
                dtm.addColumn("Shift_");
                dtm.addColumn("Material Lot_");
                dtm.addColumn("TERMINACION");
                dtm.addColumn("(m) Used");
                dtm.addColumn("MOG_");
                dtm.addColumn("Start_Setup");
                dtm.addColumn("Start_Setup");
                dtm.addColumn("Finish_Setup");
                dtm.addColumn("Finish_Setup");
                dtm.addColumn("Start_Production");
                dtm.addColumn("Start_Production");
                dtm.addColumn("Finish_Production");
                dtm.addColumn("Finish_Production");
                dtm.addColumn("Prod. Qty._");
                dtm.addColumn("Good Parts_");
                dtm.addColumn("Scrap (pcs)_");
                dtm.addColumn("BM (pcs)_");
                dm.jTableReporteDas.setModel(dtm);
                dm.jTableReporteDas.setAutoResizeMode(0);
                for (int i = 0; i < dm.jTableReporteDas.getColumnCount(); i++) {
                    dm.jTableReporteDas.getColumnModel().getColumn(i).setPreferredWidth(120);
                }
                CallableStatement cst21 = conne.prepareCall("{call reporteDasBGPrensaRuth2(?,?,?)}");
                cst21.setString(1, fecha_inicio);
                cst21.setString(2, fecha_fin);
                cst21.registerOutParameter(3, java.sql.Types.INTEGER);
                re21 = cst21.executeQuery();
                int resul = cst21.getInt(3);
                System.err.println(re21);
                int c = 0;

                if (resul == 0) {
                    JOptionPane.showMessageDialog(null, "No hay registros en DAS, REVISAR CON SUPERVISOR SI SE TRABAJÓ LA MÁQUINA, "
                            + " de lo contrario, NOTIFICAR A EQUIPO PAPERLESS");
                } else {

                    while (re21.next()) {

                        mog = re21.getString("mog");
                        if (mog.equals("MOG000000")) {

                        } else {
                            Object[] data = new Object[21];
                            idDas = Integer.parseInt(re21.getString("id_das"));
                            data[0] = re21.getString("linea");
                            fech = re21.getString("fecha");
                            String fechaEntera = fech;
                            String[] fechaDividida = fechaEntera.split("-");
                            data[1] = fechaDividida[1];
                            data[2] = fechaDividida[2];
                            data[3] = fechaDividida[0];
                            turno = re21.getInt("turno");

                            if (turno == 1) {
                                tu = "M";
                            } else {
                                tu = "V";
                            }
                            data[4] = tu;

                            mog = re21.getString("mog");
                            m = mog.replace("MOG", "");
                            data[8] = m;
                            tiemCamStart = re21.getString("ini_cm");
                            if (tiemCamStart.equals("")) {
                                data[9] = "";
                                data[10] = "";
                            } else {
                                String horaSetupEntera = tiemCamStart;
                                horaSetupDividida = horaSetupEntera.split(":");
                                data[9] = horaSetupDividida[0];
                                data[10] = horaSetupDividida[1];
                            }

                            tiemCamFin = re21.getString("fin_cm");
                            if (tiemCamFin.equals("")) {
                                data[11] = "";
                                data[12] = "";
                            } else {
                                String horaSetupEnteraF = tiemCamFin;
                                HoraSetupDivididaF = horaSetupEnteraF.split(":");
                                data[11] = HoraSetupDivididaF[0];
                                data[12] = HoraSetupDivididaF[1];
                            }

                            tiemProStart = re21.getString("ini_tp");
                            String horaStartEntera = tiemProStart;
                            String[] HoraStartDividida = horaStartEntera.split(":");
                            data[13] = HoraStartDividida[0];
                            data[14] = HoraStartDividida[1];
                            tiemProFin = re21.getString("fin_tp");
                            String horaStartEnteraF = tiemProFin;
                            String[] HoraStartDivididaF = horaStartEnteraF.split(":");
                            data[15] = HoraStartDivididaF[0];
                            data[16] = HoraStartDivididaF[1];
                            data[17] = re21.getString("pcs_pro");
                            System.err.println(re21.getString("pcs_pro"));
                            data[18] = re21.getString("pcs_buenas");
                            System.err.println(re21.getString("pcs_buenas"));
                            data[19] = re21.getString("pcs_scrap");
                            System.err.println(re21.getString("pcs_scrap"));
                            data[20] = re21.getString("pcs_bm");
                            System.err.println(re21.getString("pcs_bm"));

                            material = re21.getString("lote_coil");
                            
                            String materialEntero = material;
                            if (materialEntero == null || materialEntero.equals("")) {
                                data[5] = "";
                                data[6] = "";
                                data[7] = "";
                                dtm.addRow(data);
                            } else {
                                if (data[0].equals("TB03B") || data[0].equals("TB02B")) {
                                    
                                    if (re21.getString("metros").equals("0")) {
                                        if (material == null || material.equals("")) {
                                            data[5] = "";
                                            data[6] = "";
                                            data[7] = "";
                                            dtm.addRow(data);
                                        } else {
                                            String materialDividido = materialEntero.substring(0, materialEntero.length() - 3);
                                            int tama = materialEntero.length();
                                            tama = tama - 2;
                                            String materialDividido2 = materialEntero.substring(tama, materialEntero.length());
                                            data[0] = re21.getString("linea");
                                            data[1] = fechaDividida[1];
                                            data[2] = fechaDividida[2];
                                            data[3] = fechaDividida[0];
                                            data[4] = tu;
                                            data[5] = materialDividido;
                                            data[6] = materialDividido2;
                                            data[7] = re21.getString("metros");
                                            data[8] = m;/*
                                            tiemCamStart = re21.getString("ini_cm");
                                            if (tiemCamStart.equals("")) {
                                                data[9] = "";
                                                data[10] = "";
                                            } else {
                                                String horaSetupEntera = tiemCamStart;
                                                horaSetupDividida = horaSetupEntera.split(":");
                                                data[9] = horaSetupDividida[0];
                                                data[10] = horaSetupDividida[1];
                                            }

                                            tiemCamFin = re21.getString("fin_cm");
                                            if (tiemCamFin.equals("")) {
                                                data[11] = "";
                                                data[12] = "";
                                            } else {
                                                String horaSetupEnteraF = tiemCamFin;
                                                HoraSetupDivididaF = horaSetupEnteraF.split(":");
                                                data[11] = HoraSetupDivididaF[0];
                                                data[12] = HoraSetupDivididaF[1];
                                            }/*
                                            data[13] = HoraStartDividida[0];
                                            data[14] = HoraStartDividida[1];
                                            data[15] = HoraStartDivididaF[0];
                                            data[16] = HoraStartDivididaF[1];
                                            data[17] = re21.getString("pcs_pro");
                                            data[18] = re21.getString("pcs_buenas");
                                            data[19] = re21.getString("pcs_scrap");
                                            data[20] = re21.getString("pcs_bm");*/
                                            dtm.addRow(data);
                                        }
                                    } else {
                                        CallableStatement cst34 = conne.prepareCall("{call traerLotesDas(?,?,?,?)}");
                                        cst34.setInt(1, idDas);
                                        cst34.setString(2, mog);
                                        cst34.setString(3, tiemProStart);
                                        cst34.setString(4, tiemProFin);
                                        rnuevo = cst34.executeQuery();
                                        
                                        while (rnuevo.next()) {
                                            material2 = rnuevo.getString("lote_coil");
                                            String materialEntero1 = material2;
                                            if (materialEntero1.equals("")) {
                                                data[5] = "";
                                                data[6] = "";
                                            } else {
                                                if (material.equals(material2)) {
                                                    if(cont==0){
                                                        String materialDividido = materialEntero1.substring(0, materialEntero1.length() - 3);
                                                        int tama = materialEntero1.length();
                                                        tama = tama - 2;
                                                        String materialDividido2 = materialEntero1.substring(tama, materialEntero1.length());
                                                        data[0] = re21.getString("linea");
                                                        data[1] = fechaDividida[1];
                                                        data[2] = fechaDividida[2];
                                                        data[3] = fechaDividida[0];
                                                        data[4] = tu;
                                                        data[5] = materialDividido;
                                                        data[6] = materialDividido2;
                                                        data[7] = rnuevo.getString("metros");
                                                        data[8] = m;
                                                        if (tiemCamStart.equals("")) {
                                                            data[9] = "";
                                                            data[10] = "";
                                                        } else {
                                                            String horaSetupEntera = tiemCamStart;
                                                            horaSetupDividida = horaSetupEntera.split(":");
                                                            data[9] = horaSetupDividida[0];
                                                            data[10] = horaSetupDividida[1];
                                                        }

                                                        tiemCamFin = re21.getString("fin_cm");
                                                        if (tiemCamFin.equals("")) {
                                                            data[11] = "";
                                                            data[12] = "";
                                                        } else {
                                                            String horaSetupEnteraF = tiemCamFin;
                                                            HoraSetupDivididaF = horaSetupEnteraF.split(":");
                                                            data[11] = HoraSetupDivididaF[0];
                                                            data[12] = HoraSetupDivididaF[1];
                                                        }
                                                        data[13] = HoraStartDividida[0];
                                                        data[14] = HoraStartDividida[1];
                                                        data[15] = HoraStartDivididaF[0];
                                                        data[16] = HoraStartDivididaF[1];
                                                        data[17] = re21.getString("pcs_pro");
                                                        data[18] = re21.getString("pcs_buenas");
                                                        data[19] = re21.getString("pcs_scrap");
                                                        data[20] = re21.getString("pcs_bm");

                                                        dtm.addRow(data);
                                                        cont ++;
                                                    }else{
                                                        String materialDividido = materialEntero1.substring(0, materialEntero1.length() - 3);
                                                        int tama = materialEntero1.length();
                                                        tama = tama - 2;
                                                        String materialDividido2 = materialEntero1.substring(tama, materialEntero1.length());
                                                        data[0] = re21.getString("linea");
                                                        data[1] = fechaDividida[1];
                                                        data[2] = fechaDividida[2];
                                                        data[3] = fechaDividida[0];
                                                        data[4] = tu;
                                                        data[5] = materialDividido;
                                                        data[6] = materialDividido2;
                                                        data[7] = rnuevo.getString("metros");
                                                        data[8] = m;
                                                        data[9] = "";
                                                        data[10] = "";
                                                        data[11] = "";
                                                        data[12] = "";
                                                        data[13] = "";
                                                        data[14] = "";
                                                        data[15] = "";
                                                        data[16] = "";
                                                        data[17] = "";
                                                        data[18] = "";
                                                        data[19] = "";
                                                        data[20] = "";
                                                        dtm.addRow(data);
                                                        cont=0;
                                                    }
                                                } else {
                                                    String materialDividido = materialEntero1.substring(0, materialEntero1.length() - 3);
                                                    int tama = materialEntero1.length();
                                                    tama = tama - 2;
                                                    String materialDividido2 = materialEntero1.substring(tama, materialEntero1.length());
                                                    data[0] = re21.getString("linea");
                                                    data[1] = fechaDividida[1];
                                                    data[2] = fechaDividida[2];
                                                    data[3] = fechaDividida[0];
                                                    data[4] = tu;
                                                    data[5] = materialDividido;
                                                    data[6] = materialDividido2;
                                                    data[7] = rnuevo.getString("metros");
                                                    data[8] = m;
                                                    data[9] = "";
                                                    data[10] = "";
                                                    data[11] = "";
                                                    data[12] = "";
                                                    data[13] = "";
                                                    data[14] = "";
                                                    data[15] = "";
                                                    data[16] = "";
                                                    data[17] = "";
                                                    data[18] = "";
                                                    data[19] = "";
                                                    data[20] = "";
                                                    dtm.addRow(data);
                                                }
                                            }
                                        }
                                        cont=0;
                                    }

                                } else {
                                    String materialDividido = materialEntero.substring(0, materialEntero.length() - 3);
                                    int tama = materialEntero.length();
                                    tama = tama - 2;
                                    String materialDividido2 = materialEntero.substring(tama, materialEntero.length());
                                    data[5] = materialDividido;
                                    data[6] = materialDividido2;
                                    data[7] = re21.getString("metros");
                                    dtm.addRow(data);
                                }
                            }
                        }
                    }
                }
                conne.close();
            } catch (SQLException ex) {
                Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void reporteSorting(Conection con, View_report_bg_general dm) {
        Connection conne = con.conexionMySQL();
        ResultSet re21;
        String fecha_inicio;
        String fecha_fin;

        Date fi = new Date();
        Date ff = new Date();
        DateFormat hourFormat = new SimpleDateFormat("yyyy-MM-dd");

        fi = dm.jDateChooserInicio.getDate();
        ff = dm.jDateChooserFin.getDate();
        if (fi == null || ff == null) {
            JOptionPane.showMessageDialog(null, "Debes de seleccionar alguna fecha para buscar");
        } else {
            try {
                fecha_inicio = hourFormat.format(fi);
                fecha_fin = hourFormat.format(ff);
                
                DefaultTableModel dtm = new DefaultTableModel();
                dm.jTableReporteDas.setRowHeight(20);
                dtm.addColumn("MOG");
                dtm.addColumn("Fecha cierre");
                dtm.addColumn("Validado por");
                dtm.addColumn("Qty Scrap");
                dm.jTableReporteDas.setModel(dtm);
                dm.jTableReporteDas.setAutoResizeMode(0);
                for (int i = 0; i < dm.jTableReporteDas.getColumnCount(); i++) {
                    dm.jTableReporteDas.getColumnModel().getColumn(i).setPreferredWidth(120);
                }
                
                CallableStatement cst21 = conne.prepareCall("{call reporteOrdenesSorting(?,?,?)}");
                cst21.setString(1, fecha_inicio);
                cst21.setString(2, fecha_fin);
                cst21.registerOutParameter(3, java.sql.Types.INTEGER);
                re21 = cst21.executeQuery();
                int resul = cst21.getInt(3);
                int c = 0;
                
                if (resul == 0) {
                    JOptionPane.showMessageDialog(null, "No hay ordenes validadas de sorteo, verificar con supervisor "
                            + " de lo contrario, NOTIFICAR A EQUIPO PAPERLESS");
                } else {
                    while(re21.next()){
                        Object[] data = new Object[4];
                        data[0] = re21.getString("mog");
                        int id_m = Integer.parseInt(re21.getString("id_mog"));
                        data[1] = re21.getDate("fecha");
                        data[2] = re21.getString("nombre");

                        CallableStatement cst22 = conne.prepareCall("{call scrapSorting(?)}");
                        cst22.setInt(1, id_m);
                        ResultSet rr=cst22.executeQuery();
                        while(rr.next()){
                            if(rr.getString("qty")==(null)){
                                data[3] = 0;
                            }else{
                                data[3] = rr.getString("qty");
                            }
                            
                        }
                        dtm.addRow(data);
                    }
                }
                conne.close();
            } catch (SQLException ex) {
                Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
