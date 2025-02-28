/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package paperless.hb.maquinado;

import Controller.ValidarLineaController;
import Controller.CapturaOrdenManufacturaController;
import Controller.OpcionesController;
import Controller.RegistroDASController;
import Controller.RegistroRBPController;
import Model.ValidarLineaModel;
import Model.CapturaOrdenManufacturaModel;
import Model.DBConexion;
import Model.RegistroDASModel;
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
        
        // Vistas
        ValidarLineaView validarLineaView = ValidarLineaView.getInstance();
        CapturaOrdenManufacturaView capturaOrdenManufacturaView = CapturaOrdenManufacturaView.getInstance();
        OpcionesView opcionesView = OpcionesView.getInstance();
        RegistroRBPView registroRBPView = RegistroRBPView.getInstance();
        RegistroDASView registroDASView = RegistroDASView.getInstance(); 
        RegistroParoProcesoView registroParoProcesoView = new RegistroParoProcesoView(); 
        CambioMOGView cambioMOGView = CambioMOGView.getInstance();
        
        validarLineaView.setVisible(true);
        
        // Model
        DBConexion conexion = new DBConexion();
        ValidarLineaModel validarLineaModel = new ValidarLineaModel(conexion);
        CapturaOrdenManufacturaModel capturaOrdenManufacturaModel = new CapturaOrdenManufacturaModel(conexion);
        RegistroDASModel registroDASModel = new RegistroDASModel(conexion);
        RegistroRBPModel registroRBPModel = new RegistroRBPModel(conexion); 
        
        
        // Controller
        ValidarLineaController validarLineaController = new ValidarLineaController(validarLineaView, validarLineaModel, capturaOrdenManufacturaView, conexion);
        CapturaOrdenManufacturaController capturaOrdenManufacturaController = new CapturaOrdenManufacturaController(capturaOrdenManufacturaModel, capturaOrdenManufacturaView);
        OpcionesController opcionesController = new OpcionesController();
        RegistroDASController registroDASController = new RegistroDASController(registroDASModel, registroDASView);
        RegistroRBPController registroRBPController = new RegistroRBPController();
    }
    
}
