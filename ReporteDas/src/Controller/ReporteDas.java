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
import java.io.IOException;
import java.net.ServerSocket;
import javax.swing.JOptionPane;

/**
 *
 * @author ALEJANDRO LARES
 */
public class ReporteDas {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
            //ServerSocket SERVER_SOCKET = new ServerSocket(1334);
            Login log = new Login();
            View_report_bg_general vw_general = new View_report_bg_general();
            Report report_das = new Report();
            Conection con = new Conection();
            view_report_half vw_half = new view_report_half();
            ReportHalf report_das_half = new ReportHalf();
            Controlador cnt = new Controlador(log, vw_general, report_das, con,vw_half, report_das_half);
        
    }

}
