/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.RegistroRBPModel;
import Utils.Navegador;
import View.ParoProcesoView;
import View.RegistroDASView;
import View.RegistroRBPView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;
import javax.swing.JButton;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class RegistroRBPController implements ActionListener {
    private static final Logger LOGGER = Logger.getLogger(RegistroRBPController.class.getName());

    private final RegistroRBPModel registroRBPModel = new RegistroRBPModel();
    private final RegistroRBPView registroRBPView;
    private final ParoProcesoView paroProcesoView = ParoProcesoView.getInstance();
    private final RegistroDASView registroDASView = RegistroDASView.getInstance();
    private final Navegador navegador = Navegador.getInstance();
    
    public RegistroRBPController(RegistroRBPView registroRBPView) {
        this.registroRBPView = registroRBPView;
        addListeners();
    }
    
    private void addListeners() {
        registroRBPView.btnDAS.addActionListener(this);
        registroRBPView.btnParoLinea.addActionListener(this);
        registroRBPView.btnCambioMOG.addActionListener(this);
        registroRBPView.btnRegresar.addActionListener(this);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        handleButtonAction((JButton) e.getSource());
    }
    
    private void handleButtonAction(JButton button) {
        switch (button.getName()) {
            case "btnDAS":
                navegador.avanzar(registroDASView, registroRBPView);
                break;
            case "btnParoLinea":
                navegador.avanzar(paroProcesoView, registroRBPView);
                break;
            case "btnCambioMOG":
                navegador.avanzar(registroDASView, registroRBPView);
                break;
            case "btnVerDibujo":
                navegador.avanzar(paroProcesoView, registroRBPView);
                break;
            case "btnRegresar":
                navegador.regresar(registroRBPView);
                break;
            default:
                break;
        }
    }
}
    
    /*private void handleDatosDAS(String codigoSoporte, String codigoInspector, String codigoEmpleado) throws SQLException {
        int DASExistente = registroDASModel.obtenerDASExistente();
        if (DASExistente == 0) {
            registroDASModel.registrarDAS(codigoSoporte, codigoInspector, codigoEmpleado);
        }
    }*/
