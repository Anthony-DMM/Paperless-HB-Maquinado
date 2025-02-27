/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.RegistroDASModel;
import Utils.FechaHora;
import View.RegistroDASView;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class RegistroDASController {
    private RegistroDASModel registroDASModel;
    private RegistroDASView registroDASView;
    FechaHora fechaHora = new FechaHora();

    public RegistroDASController(RegistroDASModel registroDASModel, RegistroDASView registroDASView) throws SQLException, ParseException {
        this.registroDASModel = registroDASModel;
        this.registroDASView = registroDASView;
        
        String fechaString = fechaHora.fechaActual("dd-MM-yyyy");
        Date fechaDate = fechaHora.stringToDate(fechaString, "dd-MM-yyyy");
        
        registroDASView.jdcFecha.setDate(fechaDate);
    }
}
