/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.RegistroParoProcesoModel;
import Utils.FechaHora;
import View.RegistroParoProcesoView;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class RegistroParoProcesoController {
    private RegistroParoProcesoModel registroParoProcesoModel;
    private RegistroParoProcesoView registroParoProcesoView;
    private FechaHora fechaHora = new FechaHora();

    public RegistroParoProcesoController(RegistroParoProcesoModel registroParoProcesoModel, RegistroParoProcesoView registroParoProcesoView) {
        this.registroParoProcesoModel = registroParoProcesoModel;
        this.registroParoProcesoView = RegistroParoProcesoView.getInstance();
    }
}
