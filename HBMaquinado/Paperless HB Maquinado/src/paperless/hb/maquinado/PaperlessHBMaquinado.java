/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package paperless.hb.maquinado;

import Controller.ValidarLineaController;
import Controller.CapturaOrdenManufacturaController;
import Controller.OpcionesController;
import Controller.RegistroDASController;
import Controller.RegistroParoProcesoController;
import Controller.RegistroRBPController;
import Model.ValidarLineaModel;
import Model.CapturaOrdenManufacturaModel;
import Model.DBConexion;
import Model.RegistroDASModel;
import Model.RegistroParoProcesoModel;
import Model.RegistroRBPModel;
import View.ValidarLineaView;
import View.CambioMOGView;
import View.CapturaOrdenManufacturaView;
import View.OpcionesView;
import View.RegistroDASView;
import View.RegistroParoProcesoView;
import View.RegistroRBPView;
import java.sql.SQLException;
import java.text.ParseException;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class PaperlessHBMaquinado {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException, ParseException {
        ValidarLineaView validarLineaView = ValidarLineaView.getInstance();
        ValidarLineaController validarLineaController = new ValidarLineaController(validarLineaView);
        validarLineaView.setVisible(true);
    }
    
}
