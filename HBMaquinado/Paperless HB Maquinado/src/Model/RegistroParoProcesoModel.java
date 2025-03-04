/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Utils.FechaHora;
import java.util.logging.Logger;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class RegistroParoProcesoModel {
    private static final Logger LOGGER = Logger.getLogger(CapturaOrdenManufacturaModel.class.getName());
    private DBConexion conexion;
    FechaHora fechaHora = new FechaHora();
    
    public RegistroParoProcesoModel(DBConexion conexion) {
        this.conexion = conexion;
    }
}
