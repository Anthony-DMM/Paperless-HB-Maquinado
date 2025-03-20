/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.CambioMOGModel;
import View.CambioMOGView;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class CambioMOGController {
    private final CambioMOGModel cambioMOGModel;
    private final CambioMOGView cambioMOGView;

    public CambioMOGController(CambioMOGView cambioMOGView) {
        this.cambioMOGView = cambioMOGView;
        this.cambioMOGModel = new CambioMOGModel();
    }
}
