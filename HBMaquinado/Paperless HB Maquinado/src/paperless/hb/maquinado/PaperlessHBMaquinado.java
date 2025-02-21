/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package paperless.hb.maquinado;

import Controller.Autenticacion_Controller;
import Controller.Captura_Orden_Manufactura_Controller;
import Model.Autenticacion_Model;
import Model.Captura_Orden_Manufactura_Model;
import Model.DBConexion;
import View.Autenticacion;
import View.Cambio_MOG;
import View.Captura_Orden_Manufactura;
import View.Opciones;
import View.Registro_Paro_Proceso;
import View.Registro_RBP;

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
        Autenticacion autenticacion = new Autenticacion();
        Opciones opciones = new Opciones();
        Captura_Orden_Manufactura capturaOrdenManufactura = new Captura_Orden_Manufactura();
        Registro_RBP registroRBP = new Registro_RBP();
        Cambio_MOG cambioMOG = new Cambio_MOG();
        Registro_Paro_Proceso registroParoProceso = new Registro_Paro_Proceso(); 
        
        autenticacion.setVisible(true);
        
        
        // Model
        DBConexion conexion = new DBConexion();
        Captura_Orden_Manufactura_Model capturaOrdenManufacturaModel = new Captura_Orden_Manufactura_Model(conexion);
        Autenticacion_Model autenticacion_Model = new Autenticacion_Model(conexion);
        
        
        
        // Controller
        Captura_Orden_Manufactura_Controller capturaOrdenManufacturaController = new Captura_Orden_Manufactura_Controller(capturaOrdenManufacturaModel, capturaOrdenManufactura);
        Autenticacion_Controller autenticacion_Controller = new Autenticacion_Controller(autenticacion, autenticacion_Model, capturaOrdenManufactura, conexion);
    }
    
}
