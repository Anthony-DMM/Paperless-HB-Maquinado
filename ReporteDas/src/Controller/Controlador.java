/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Conection;
import Model.Report;
import Model.ReportHalf;
import View.Login;
import View.View_report_bg_general;
import View.view_report_half;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author ALEJANDRO LARES
 */
public class Controlador implements ActionListener{
    
    Login log;
    View_report_bg_general vw_general;
    view_report_half vw_half;
        
    Report report_das;
    ReportHalf report_das_half;
    Conection con;

    public Controlador(Login log, View_report_bg_general vw_general, Report report_das, Conection con, view_report_half vw_half, ReportHalf report_das_half) {
        this.log = log;
        this.vw_general = vw_general;
        this.report_das = report_das;
        this.con = con;
        this.vw_half=vw_half;
        this.report_das_half=report_das_half;
        escuchadores();
        log.setVisible(true);
    }
    
    public void escuchadores(){
        vw_general.jButtonBuscarDas.addActionListener(this);
        vw_general.jButtonExportarDas.addActionListener(this);
        vw_general.jButtonBack_BG.addActionListener(this);
        vw_general.jButtonBuscarDasxMOG.addActionListener(this);
        
        log.jButtonBush.addActionListener(this);
        log.jButtonHalf.addActionListener(this);
        vw_half.jButtonBack_HB.addActionListener(this);
        vw_half.jButtonBuscarDas.addActionListener(this);
        vw_half.jButtonExportarDas.addActionListener(this);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == log.jButtonBush){
            report_das.limpiar(vw_general);
            vw_general.setVisible(true);
        }
        
        if(e.getSource() == log.jButtonHalf){
            vw_half.setVisible(true);
        }
        
        if(e.getSource() == vw_half.jButtonBack_HB){
            log.setVisible(true);
            vw_half.setVisible(false);
            report_das_half.limpiar(vw_half);
        }
        
        if(e.getSource() == vw_half.jButtonBuscarDas){
            try {
                if(vw_half.jCheckBoxPrensa.isSelected()==false && vw_half.jCheckBoxPCK.isSelected()==false && vw_half.jCheckBoxPlat.isSelected()==false && vw_half.jCheckBoxMaq.isSelected()==false){
                    JOptionPane.showMessageDialog(null, "Debes seleccionar algun proceso");
                }else{
                    if(vw_half.jCheckBoxPrensa.isSelected()==true){
                        report_das_half.reportePrensa(con, vw_half);
                    }

                    if(vw_half.jCheckBoxMaq.isSelected()==true){
                        report_das_half.reporteMaquinado(con, vw_half);
                    }

                    if(vw_half.jCheckBoxPlat.isSelected()==true){
                        report_das_half.reportePlatinado(con, vw_half);
                    }

                    if(vw_half.jCheckBoxPCK.isSelected()==true){

                    }
                }
            
            } catch (ParseException ex) {
                Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
        if (e.getSource() == vw_half.jButtonExportarDas) {
                report_das.reporte(vw_half.jTableReporteDas);
        }
        
        
        if(e.getSource() == vw_general.jButtonBack_BG){
            log.setVisible(true);
            vw_general.setVisible(false);
            report_das.limpiar(vw_general);
        }
        
        if (e.getSource() == vw_general.jButtonBuscarDas) {
            if(vw_general.jCheckBoxPrensa.isSelected()==false && 
                    vw_general.jCheckBoxBGgeneral.isSelected()==false &&
                    vw_general.jCheckBoxTB91.isSelected()==false &&
                    vw_general.jCheckBoxTB92.isSelected()==false &&
                    vw_general.jCheckBoxEmpaque.isSelected()==false &&
                    vw_general.jCheckBoxPlatinado.isSelected()==false &&
                    vw_general.jCheckBoxSorting.isSelected()==false){
                JOptionPane.showMessageDialog(null, "No se ha seleccionado linea");
            }else{
                try {
                    if (vw_general.jCheckBoxPrensa.isSelected()) {
                        report_das.reportePrensa(con, vw_general);
                    }
                    if (vw_general.jCheckBoxBGgeneral.isSelected()) {
                        report_das.reporteBush(con, vw_general);
                    }
                    if (vw_general.jCheckBoxPlatinado.isSelected()) {
                        //report_das.reportePlatinado(con, vw_general);
                        report_das.reporteDasPlatinado(con, vw_general);
                    }
                    if (vw_general.jCheckBoxTB91.isSelected()) {
                        report_das.reporteAssy91(con, vw_general);
                    }
                    if (vw_general.jCheckBoxTB92.isSelected()) {
                        report_das.reporteAssy92(con, vw_general);
                    }
                    if (vw_general.jCheckBoxEmpaque.isSelected()) {
                        report_das.reporteEmpaqueMesas(con, vw_general);
                    }
                    if(vw_general.jCheckBoxSorting.isSelected()){
                        report_das.reporteSorting(con, vw_general);
                    }
                } catch (ParseException ex) {
                    Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        if (e.getSource() == vw_general.jButtonExportarDas) {
                report_das.reporte(vw_general.jTableReporteDas);
        }
        
        if(e.getSource() == vw_general.jButtonBuscarDasxMOG){
            int proce=vw_general.jComboBoxProce.getSelectedIndex();
            switch(proce){
                
                case 1:
                    report_das.reportePrensaByMOG(con, vw_general);
                    break;
                case 2:
                    report_das.reporteBushByMOG(con, vw_general);
                    break;
                case 3:
                    report_das.reporteAssy91ByMOG(con, vw_general);
                    break;
                case 4:
                    report_das.reporteAssy92ByMOG(con, vw_general);
                    break;
                case 5:
                    report_das.reporteEmpaqueMesasByMOG(con, vw_general);
                    break;
                case 6:
                    report_das.reportePlatinadoByMog(con, vw_general);
                    break;
                
                default:
                    JOptionPane.showMessageDialog(null, "Debes seleccionar un proceso");
                    break;
            }
        }
    }
}
