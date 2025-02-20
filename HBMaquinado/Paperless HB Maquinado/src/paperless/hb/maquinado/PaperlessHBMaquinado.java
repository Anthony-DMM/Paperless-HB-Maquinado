/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package paperless.hb.maquinado;

import Controller.Autenticacion_Controller;
import Controller.Captura_MOG_Controller;
import Model.Autenticacion_Model;
import Model.Captura_MOG_Model;
import Model.DBConexion;
import View.Autenticacion;
import View.Cambio_MOG;
import View.Captura_MOG;
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
        Captura_MOG capturaLinea = new Captura_MOG();
        Registro_RBP registroRBP = new Registro_RBP();
        Cambio_MOG cambioMOG = new Cambio_MOG();
        Registro_Paro_Proceso registroParoProceso = new Registro_Paro_Proceso(); 
        
        autenticacion.setVisible(true);
        
        
        // Model
        DBConexion conexion = new DBConexion();
        Captura_MOG_Model captura_Linea_Model = new Captura_MOG_Model(conexion);
        Autenticacion_Model autenticacion_Model = new Autenticacion_Model(conexion);
        
        
        
        // Controller
        Captura_MOG_Controller captura_Linea_Controller = new Captura_MOG_Controller(captura_Linea_Model, capturaLinea);
        Autenticacion_Controller autenticacion_Controller = new Autenticacion_Controller(autenticacion, autenticacion_Model, capturaLinea, conexion);
    }
    
}
