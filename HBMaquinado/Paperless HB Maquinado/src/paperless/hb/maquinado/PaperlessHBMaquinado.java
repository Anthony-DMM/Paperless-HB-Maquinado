/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package paperless.hb.maquinado;

import Controller.Autenticacion_Controller;
import Model.Autenticacion_Model;
import Model.DBConexion;
import View.Autenticacion;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class PaperlessHBMaquinado {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Autenticacion autenticacion = new Autenticacion();
        autenticacion.setVisible(true);
        
        
        //Model
        DBConexion conexion = new DBConexion();
        Autenticacion_Model autenticacion_Model = new Autenticacion_Model();
        
        
        
        //Controller
        Autenticacion_Controller autenticacion_Controller = new Autenticacion_Controller(autenticacion, autenticacion_Model, conexion);
    }
    
}
