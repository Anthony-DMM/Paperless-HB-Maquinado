/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.RegistroRBPModel;
import Utils.FechaHora;
import Utils.MostrarMensaje;
import Utils.Navegador;
import View.DibujoView;
import View.ParoProcesoView;
import View.RegistroDASView;
import View.RegistroRBPView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class RegistroRBPController implements ActionListener {

    private final RegistroRBPModel registroRBPModel = new RegistroRBPModel();
    private final RegistroRBPView registroRBPView;
    private final RegistroDASView registroDASView = RegistroDASView.getInstance();
    private final RegistroDASController registroDASController = new RegistroDASController(registroDASView);
    private final DibujoView dibujoView = DibujoView.getInstance();
    private final DibujoController dibujoController = DibujoController.getInstance(dibujoView);
    private final Navegador navegador = Navegador.getInstance();
    
    private final FechaHora fechaHora = new FechaHora();
    private LocalTime horaInicio;
    
    private static final Logger LOGGER = Logger.getLogger(ManufacturaController.class.getName());
    
    public RegistroRBPController(RegistroRBPView registroRBPView) {
        this.registroRBPView = registroRBPView;
        addListeners();
    }
    
    private void addListeners() {
        agregarAccion(registroRBPView.btnDAS, AccionBoton.DAS);
        agregarAccion(registroRBPView.btnDibujo, AccionBoton.DIBUJO);
        agregarAccion(registroRBPView.btnParoLinea, AccionBoton.PARO_LINEA);
        agregarAccion(registroRBPView.btnCambioMOG, AccionBoton.CAMBIO_MOG);
        agregarAccion(registroRBPView.btnRegresar, AccionBoton.REGRESAR);
    }

    public enum AccionBoton {
        DAS,
        PARO_LINEA,
        CAMBIO_MOG,
        DIBUJO,
        REGRESAR
    }

    private void handleButtonAction(AccionBoton accion) {
        switch (accion) {
            case DAS:
                navegador.avanzar(registroDASView, registroRBPView);
                break;
            case PARO_LINEA:
                ParoProcesoView paroProcesoView = new ParoProcesoView();
                ParoProcesoController paroProcesoController = new ParoProcesoController(paroProcesoView);
                navegador.avanzar(paroProcesoView, registroRBPView);
                break;
            case CAMBIO_MOG:
                //navegador.avanzar(registroDASView, registroRBPView);
                break;
            case DIBUJO:
                navegador.avanzar(dibujoView, registroRBPView);
                break;
            case REGRESAR:
                navegador.regresar(registroRBPView);
                break;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton botonOrigen = (JButton) e.getSource();
        AccionBoton accion = AccionBoton.valueOf(botonOrigen.getActionCommand());
        handleButtonAction(accion);
    }

    private void inicializarHoraInicio() {
        try {
            horaInicio = LocalTime.now();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al obtener la hora de inicio", e);
            MostrarMensaje.mostrarError("Error al inicializar la hora de inicio.");
        }
    }

    private void agregarAccion(JButton boton, AccionBoton accion) {
        boton.setActionCommand(accion.toString());
        boton.addActionListener(this);
    }
}
    
    /*private void handleDatosDAS(String codigoSoporte, String codigoInspector, String codigoEmpleado) throws SQLException {
        int DASExistente = registroDASModel.obtenerDASExistente();
        if (DASExistente == 0) {
            registroDASModel.registrarDAS(codigoSoporte, codigoInspector, codigoEmpleado);
        }
    }*/
