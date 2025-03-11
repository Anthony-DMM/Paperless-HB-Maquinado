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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.swing.JButton;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class RegistroRBPController implements ActionListener {
    private static final Logger LOGGER = Logger.getLogger(RegistroRBPController.class.getName());
    private final Map<JButton, Runnable> buttonActions = new HashMap<>();

    private final RegistroRBPModel registroRBPModel = new RegistroRBPModel();
    private final RegistroRBPView registroRBPView;
    private final RegistroDASView registroDASView = RegistroDASView.getInstance();
    private final RegistroDASController registroDASController = new RegistroDASController(registroDASView);
    private final Navegador navegador = Navegador.getInstance();
    
    public RegistroRBPController(RegistroRBPView registroRBPView) {
        this.registroRBPView = registroRBPView;
        addListeners();
    }
    
    private void addListeners() {
        registroRBPView.btnDAS.addActionListener(this);
        registroRBPView.btnDibujo.addActionListener(this);
        registroRBPView.btnParoLinea.addActionListener(this);
        registroRBPView.btnCambioMOG.addActionListener(this);
        registroRBPView.btnRegresar.addActionListener(this);
    }
    
    private void handleButtonAction(JButton button) {
        if (button == registroRBPView.btnDAS) {
            navegador.avanzar(registroDASView, registroRBPView);
        } else if (button == registroRBPView.btnParoLinea) {
            ParoProcesoView paroProcesoView = new ParoProcesoView();
            ParoProcesoController paroProcesoController = new ParoProcesoController(paroProcesoView);
            navegador.avanzar(paroProcesoView, registroRBPView);
        } else if (button == registroRBPView.btnCambioMOG) {
            navegador.avanzar(registroDASView, registroRBPView);
        } else if (button == registroRBPView.btnDibujo) {
            //navegador.avanzar(paroProcesoView, registroRBPView);
        } else if (button == registroRBPView.btnRegresar) {
            navegador.regresar(registroRBPView);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        handleButtonAction((JButton) e.getSource());
    }
}
    
    /*private void handleDatosDAS(String codigoSoporte, String codigoInspector, String codigoEmpleado) throws SQLException {
        int DASExistente = registroDASModel.obtenerDASExistente();
        if (DASExistente == 0) {
            registroDASModel.registrarDAS(codigoSoporte, codigoInspector, codigoEmpleado);
        }
    }*/
