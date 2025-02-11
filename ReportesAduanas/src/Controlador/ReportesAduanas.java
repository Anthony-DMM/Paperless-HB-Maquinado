/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Model.MetodosConnection;
import Model.ModelBitacoraEmpaque;
import Model.ModelBitacoraHBPrensa;
import Model.ModelBitacoraMaquinado;
import Model.ModelBitacoraPrensa;
import Vista.ElegirReporte;
import Vista.ElegirReporteLogin;
import Vista.ReporteBitacoraBush;
import Vista.ReporteBitacoraEmpaque;
import Vista.ReporteBitacoraMaquinado;
import Vista.ReporteBitacoraPrensa;
import Vista.ReporteBitacoraPrensaHB;

/**
 *
 * @author DMM-ADMIN
 */
public class ReportesAduanas {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        //ServerSocket SERVER_SOCKET = new ServerSocket(1334);
        ElegirReporte vistaElegirReporte = new ElegirReporte();
        MetodosConnection m = new MetodosConnection();
        ModelBitacoraEmpaque mdl_pck = new ModelBitacoraEmpaque();
        ReporteBitacoraEmpaque vw_pck = new ReporteBitacoraEmpaque();
        ModelBitacoraPrensa mdl_prs = new ModelBitacoraPrensa();
        ReporteBitacoraPrensa vw_prs = new ReporteBitacoraPrensa();
        ModelBitacoraMaquinado mdl_maq = new ModelBitacoraMaquinado();
        ReporteBitacoraMaquinado vw_maq = new ReporteBitacoraMaquinado();
        ReporteBitacoraBush vw_bush = new ReporteBitacoraBush();
        ElegirReporteLogin vistaElegirReporteLogin = new ElegirReporteLogin();
        ReporteBitacoraPrensaHB vw_pressHB = new ReporteBitacoraPrensaHB();
        ModelBitacoraHBPrensa mdl_prs_hb = new ModelBitacoraHBPrensa();
        PrincipalController pc = new PrincipalController(vistaElegirReporte, m, mdl_pck, vw_pck, mdl_prs, vw_prs, mdl_maq, vw_maq, vw_bush, vistaElegirReporteLogin, vw_pressHB, mdl_prs_hb);
    }
}
