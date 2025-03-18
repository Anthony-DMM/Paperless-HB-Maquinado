/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package paperless.hb.maquinado;

import Controller.ValidarLineaController;
import View.DibujoView;
import View.ParoProcesoManualView;
import View.ValidarLineaView;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class PaperlessHBMaquinado {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ValidarLineaView validarLineaView = ValidarLineaView.getInstance();
        ValidarLineaController validarLineaController = new ValidarLineaController(validarLineaView);
        ParoProcesoManualView manualView = ParoProcesoManualView.getInstance(); 
        manualView.setVisible(true);
    }
}
