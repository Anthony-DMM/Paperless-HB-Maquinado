/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package paperless.hb.maquinado;

import Controller.ValidarLineaController;
import Controller.CapturaOrdenManufacturaController;
import Controller.OpcionesController;
import Model.ValidarLineaModel;
import Model.CapturaOrdenManufacturaModel;
import Model.DBConexion;
import View.ValidarLineaView;
import View.CambioMOGView;
import View.CapturaOrdenManufacturaView;
import View.OpcionesView;
import View.RegistroParoProcesoView;
import View.RegistroRBPView;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class PaperlessHBMaquinado {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        // Vistas
        ValidarLineaView validarLineaView = ValidarLineaView.getInstance();
        OpcionesView opcionesView = OpcionesView.getInstance();
        CapturaOrdenManufacturaView capturaOrdenManufacturaView = CapturaOrdenManufacturaView.getInstance();
        RegistroRBPView registroRBPView = RegistroRBPView.getInstance();
        CambioMOGView cambioMOGView = CambioMOGView.getInstance();
        RegistroParoProcesoView registroParoProcesoView = new RegistroParoProcesoView(); 
        
        validarLineaView.setVisible(true);
        
        
        // Model
        DBConexion conexion = new DBConexion();
        CapturaOrdenManufacturaModel capturaOrdenManufacturaModel = new CapturaOrdenManufacturaModel(conexion);
        ValidarLineaModel validarLineaModel = new ValidarLineaModel(conexion);
        
        
        
        // Controller
        ValidarLineaController validarLineaController = new ValidarLineaController(validarLineaView, validarLineaModel, capturaOrdenManufacturaView, conexion);
        CapturaOrdenManufacturaController capturaOrdenManufacturaController = new CapturaOrdenManufacturaController(capturaOrdenManufacturaModel, capturaOrdenManufacturaView);
        OpcionesController opcionesController = new OpcionesController(); 
    }
    
}
