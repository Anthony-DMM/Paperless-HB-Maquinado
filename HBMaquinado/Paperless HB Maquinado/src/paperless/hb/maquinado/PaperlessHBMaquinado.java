/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package paperless.hb.maquinado;

import Controller.Autenticacion_Controller;
import Model.Autenticacion_Model;
import Model.DBConexion;
import View.Autenticacion;
import View.Cambio_MOG;
import View.Captura_Linea;
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
        Captura_Linea capturaLinea = new Captura_Linea();
        Registro_RBP registroRBP = new Registro_RBP();
        Cambio_MOG cambioMOG = new Cambio_MOG();
        Registro_Paro_Proceso registroParoProceso = new Registro_Paro_Proceso(); 
        
        capturaLinea.setVisible(true);
        
        
        // Model
        DBConexion conexion = new DBConexion();
        Autenticacion_Model autenticacion_Model = new Autenticacion_Model(conexion);
        
        
        
        // Controller
        Autenticacion_Controller autenticacion_Controller = new Autenticacion_Controller(autenticacion, autenticacion_Model, opciones, conexion);
    }
    
}
