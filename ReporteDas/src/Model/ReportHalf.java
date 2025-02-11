/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import View.view_report_half;
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
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author DMM-ADMIN
 */
public class ReportHalf {
    
     public void reportePrensa(Conection con, view_report_half dm) throws ParseException {
        Connection conne = con.conexionMySQL2();
        ResultSet re21 = null, rnuevo;
        String mog, tiemCamStart, tiemCamFin, tiemProStart, tiemProFin, tu, fech, m, material;
        int turno, idDas;
        String fecha_inicio;
        String fecha_fin;

        Date fi = new Date();
        Date ff = new Date();
        DateFormat hourFormat = new SimpleDateFormat("yyyy-MM-dd");

        fi = dm.jDateChooserInicio.getDate();
        ff = dm.jDateChooserFin.getDate();

        if (fi == null || ff == null || fi.equals("") || ff.equals("")) {
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
                dtm.addColumn("BM Scrap(kg)_");
                dtm.addColumn("NG Scrap(kg)_");
                dtm.addColumn("BM Scrap(pcs)_");
                dtm.addColumn("Scrap (pcs)_");
                dtm.addColumn("Centro");
                dtm.addColumn("Extremo");
                dtm.addColumn("Sello");
                dm.jTableReporteDas.setModel(dtm);
                dm.jTableReporteDas.setAutoResizeMode(0);
                for (int i = 0; i < dm.jTableReporteDas.getColumnCount(); i++) {
                    dm.jTableReporteDas.getColumnModel().getColumn(i).setPreferredWidth(120);
                }
                CallableStatement cst21 = conne.prepareCall("{call reporteDasPrensa(?,?,?)}");
                cst21.setString(1, fecha_inicio);
                cst21.setString(2, fecha_fin);
                cst21.registerOutParameter(3, java.sql.Types.INTEGER);
                re21 = cst21.executeQuery();
                int resul=cst21.getInt(3);
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
                            Object[] data = new Object[25];
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
                            data[7] = m;
                            tiemCamStart = re21.getString("inicio_cm");
                            if (tiemCamStart==null || tiemCamStart.equals("")) {
                                data[8] = "";
                                data[9] = "";
                            } else {
                                String horaSetupEntera = tiemCamStart;
                                String[] horaSetupDividida = horaSetupEntera.split(":");
                                data[8] = horaSetupDividida[0];
                                data[9] = horaSetupDividida[1];
                            }

                            tiemCamFin = re21.getString("fin_cm");
                            if (tiemCamFin==null || tiemCamFin.equals("")) {
                                data[10] = "";
                                data[11] = "";
                            } else {
                                String horaSetupEnteraF = tiemCamFin;
                                String[] HoraSetupDivididaF = horaSetupEnteraF.split(":");
                                data[10] = HoraSetupDivididaF[0];
                                data[11] = HoraSetupDivididaF[1];
                            }

                            tiemProStart = re21.getString("inicio_tp");
                            String horaStartEntera = tiemProStart;
                            String[] HoraStartDividida = horaStartEntera.split(":");
                            data[12] = HoraStartDividida[0];
                            data[13] = HoraStartDividida[1];
                            tiemProFin = re21.getString("fin_tp");
                            String horaStartEnteraF = tiemProFin;
                            String[] HoraStartDivididaF = horaStartEnteraF.split(":");
                            data[14] = HoraStartDivididaF[0];
                            data[15] = HoraStartDivididaF[1];
                            
                            
                            
                            data[16] = re21.getString("pzasTotales");
                            System.err.println(re21.getString("pzasTotales"));
                            data[17] = re21.getString("pza_ok");
                            System.err.println(re21.getString("pza_ok"));
                            data[18] = re21.getString("bm_kg");
                            System.err.println(re21.getString("bm_kg"));
                            data[19] = re21.getString("ng_kg");
                            System.err.println(re21.getString("ng_kg"));
                            data[20] = re21.getString("pcs_bm");
                            System.err.println(re21.getString("pcs_bm"));
                            data[21] = re21.getString("pcs_ng");
                            System.err.println(re21.getString("pcs_ng"));
                            data[22] = re21.getString("centroEstampado");
                            data[23] = re21.getString("extremo");
                            data[24] = re21.getString("sello");
                            
                            
                            material = re21.getString("lote_coil");
                            
                            String materialEntero = material;
                            if (materialEntero.equals("")) {
                                data[5] = "";
                                data[6] = "";
                            } else {
                                data[5] = materialEntero;
                                data[6] = re21.getString("metros");
                                
                            }
                            dtm.addRow(data);
                        }
                    }
                }
                conne.close();
            } catch (SQLException ex) {
                Logger.getLogger(ReportHalf.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
     
    public void reporteMaquinado(Conection con, view_report_half dm) throws ParseException {
        Connection conne = con.conexionMySQL2();
        ResultSet re21 = null, rnuevo;
        String mog, tiemCamStart, tiemCamFin, tiemProStart, tiemProFin, tu, fech, m, material;
        int turno, idDas;
        String fecha_inicio;
        String fecha_fin;

        Date fi = new Date();
        Date ff = new Date();
        DateFormat hourFormat = new SimpleDateFormat("yyyy-MM-dd");

        fi = dm.jDateChooserInicio.getDate();
        ff = dm.jDateChooserFin.getDate();

        if (fi == null || ff == null || fi.equals("") || ff.equals("")) {
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
                dtm.addColumn("MOG");
                dtm.addColumn("Columna");
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
                dtm.addColumn("Bad Parts_");
                dm.jTableReporteDas.setModel(dtm);
                dm.jTableReporteDas.setAutoResizeMode(0);
                for (int i = 0; i < dm.jTableReporteDas.getColumnCount(); i++) {
                    dm.jTableReporteDas.getColumnModel().getColumn(i).setPreferredWidth(120);
                }
                CallableStatement cst21 = conne.prepareCall("{call reporteDasMaquinado(?,?,?)}");
                cst21.setString(1, fecha_inicio);
                cst21.setString(2, fecha_fin);
                cst21.registerOutParameter(3, java.sql.Types.INTEGER);
                re21 = cst21.executeQuery();
                int resul=cst21.getInt(3);
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
                            Object[] data = new Object[18];
                            idDas = Integer.parseInt(re21.getString("id_das"));
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
                            data[6] = "";
                            tiemCamStart = re21.getString("inicio_cm");
                            if (tiemCamStart==null || tiemCamStart.equals("")) {
                                data[7] = "";
                                data[8] = "";
                            } else {
                                String horaSetupEntera = tiemCamStart;
                                String[] horaSetupDividida = horaSetupEntera.split(":");
                                data[7] = horaSetupDividida[0];
                                data[8] = horaSetupDividida[1];
                            }

                            tiemCamFin = re21.getString("fin_cm");
                            if (tiemCamFin==null || tiemCamFin.equals("")) {
                                data[9] = "";
                                data[10] = "";
                            } else {
                                String horaSetupEnteraF = tiemCamFin;
                                String[] HoraSetupDivididaF = horaSetupEnteraF.split(":");
                                data[9] = HoraSetupDivididaF[0];
                                data[10] = HoraSetupDivididaF[1];
                            }

                            tiemProStart = re21.getString("inicio_tp");
                            String horaStartEntera = tiemProStart;
                            String[] HoraStartDividida = horaStartEntera.split(":");
                            data[11] = HoraStartDividida[0];
                            data[12] = HoraStartDividida[1];
                            tiemProFin = re21.getString("fin_tp");
                            String horaStartEnteraF = tiemProFin;
                            String[] HoraStartDivididaF = horaStartEnteraF.split(":");
                            data[13] = HoraStartDivididaF[0];
                            data[14] = HoraStartDivididaF[1];
                            
                            data[15] = re21.getString("cantidad_piezas_procesadas");
                            System.err.println(re21.getString("cantidad_piezas_procesadas"));
                            data[16] = re21.getString("cantPzaGood");
                            System.err.println(re21.getString("cantPzaGood"));
                            int cantG, cantP, scr;
                            
                            cantG = re21.getInt("cantPzaGood");
                            cantP = re21.getInt("cantidad_piezas_procesadas");
                            
                            scr = cantP-cantG;
                            
                            data[17] = String.valueOf(scr);
                            dtm.addRow(data);
                        }
                    }
                }
                conne.close();
            } catch (SQLException ex) {
                Logger.getLogger(ReportHalf.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void reportePlatinado(Conection con, view_report_half dm) throws ParseException {
        Connection conne = con.conexionMySQL2();
        ResultSet re21 = null, rnuevo;
        String mog, tiemCamStart, tiemCamFin, tiemProStart, tiemProFin, tu, fech, m, material;
        int turno, idDas;
        String fecha_inicio;
        String fecha_fin;

        Date fi = new Date();
        Date ff = new Date();
        DateFormat hourFormat = new SimpleDateFormat("yyyy-MM-dd");

        fi = dm.jDateChooserInicio.getDate();
        ff = dm.jDateChooserFin.getDate();

        if (fi == null || ff == null || fi.equals("") || ff.equals("")) {
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
                dtm.addColumn("MOG");
                dtm.addColumn("Columna");
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
                dtm.addColumn("Bad Parts_");
                dm.jTableReporteDas.setModel(dtm);
                dm.jTableReporteDas.setAutoResizeMode(0);
                for (int i = 0; i < dm.jTableReporteDas.getColumnCount(); i++) {
                    dm.jTableReporteDas.getColumnModel().getColumn(i).setPreferredWidth(120);
                }
                CallableStatement cst21 = conne.prepareCall("{call reporteDasPlatinado(?,?,?)}");
                cst21.setString(1, fecha_inicio);
                cst21.setString(2, fecha_fin);
                cst21.registerOutParameter(3, java.sql.Types.INTEGER);
                re21 = cst21.executeQuery();
                int resul=cst21.getInt(3);
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
                            Object[] data = new Object[18];
                            idDas = Integer.parseInt(re21.getString("id_das"));
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
                            data[6] = "";
                            tiemCamStart = "";
                            if (tiemCamStart==null || tiemCamStart.equals("")) {
                                data[7] = "";
                                data[8] = "";
                            } else {
                                String horaSetupEntera = tiemCamStart;
                                String[] horaSetupDividida = horaSetupEntera.split(":");
                                data[7] = horaSetupDividida[0];
                                data[8] = horaSetupDividida[1];
                            }

                            tiemCamFin = "";
                            if (tiemCamFin==null || tiemCamFin.equals("")) {
                                data[9] = "";
                                data[10] = "";
                            } else {
                                String horaSetupEnteraF = tiemCamFin;
                                String[] HoraSetupDivididaF = horaSetupEnteraF.split(":");
                                data[9] = HoraSetupDivididaF[0];
                                data[10] = HoraSetupDivididaF[1];
                            }

                            tiemProStart = re21.getString("ini_tur");
                            String horaStartEntera = tiemProStart;
                            String[] HoraStartDividida = horaStartEntera.split(":");
                            data[11] = HoraStartDividida[0];
                            data[12] = HoraStartDividida[1];
                            tiemProFin = re21.getString("fin_tur");
                            String horaStartEnteraF = tiemProFin;
                            String[] HoraStartDivididaF = horaStartEnteraF.split(":");
                            data[13] = HoraStartDivididaF[0];
                            data[14] = HoraStartDivididaF[1];
                            
                            data[15] = re21.getString("cantidad_piezas_procesadas");
                            System.err.println(re21.getString("cantidad_piezas_procesadas"));
                            data[16] = re21.getString("cantPzaGood");
                            System.err.println(re21.getString("cantPzaGood"));
                            int cantG, cantP, scr;
                            
                            cantG = re21.getInt("cantPzaGood");
                            cantP = re21.getInt("cantidad_piezas_procesadas");
                            
                            scr = cantP-cantG;
                            
                            data[17] = String.valueOf(scr);
                            dtm.addRow(data);
                        }
                    }
                }
                conne.close();
            } catch (SQLException ex) {
                Logger.getLogger(ReportHalf.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
     
    public void limpiar(view_report_half dm){
       dm.jCheckBoxMaq.setSelected(false);
       dm.jCheckBoxPCK.setSelected(false);
       dm.jCheckBoxPlat.setSelected(false);
       dm.jCheckBoxPrensa.setSelected(false);
       dm.jDateChooserFin.setDate(null);
       dm.jDateChooserInicio.setDate(null);
       DefaultTableModel dtm = new DefaultTableModel();
       dm.jTableReporteDas.setModel(dtm);
       dm.jTableReporteDas.setAutoResizeMode(0);
    }
       
}
