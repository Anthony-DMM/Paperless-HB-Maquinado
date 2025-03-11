/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Interfaces.LineaProduccion;
import Interfaces.MOG;
import Model.ManufacturaModel;
import Utils.LimpiarCampos;
import Utils.MostrarMensaje;
import Utils.Navegador;
import Utils.ValidarCampos;
import View.ManufacturaView;
import View.RegistroRBPView;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ManufacturaController implements ActionListener {
    
    private static final Logger LOGGER = Logger.getLogger(ManufacturaController.class.getName());
    
    private final ManufacturaModel manufacturaModel = new ManufacturaModel();
    private final ManufacturaView manufacturaView;
    private final Navegador navegador = Navegador.getInstance();
    private final RegistroRBPView registroRBPView = RegistroRBPView.getInstance();
    private final RegistroRBPController registroRBPController = new RegistroRBPController(registroRBPView);
    
    public ManufacturaController(ManufacturaView manufacturaView) {
        this.manufacturaView = manufacturaView;
        addListeners();
    }
    
    private void addListeners() {
        manufacturaView.getTxtMogCapturada().addActionListener(this);
        manufacturaView.getBtnSiguiente().addActionListener(this);
        manufacturaView.getBtnRegresar().addActionListener(this);
        
        manufacturaView.getTxtCodigoSupervisor().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleCodigoSupervisorCapturado();
                }
            }
        });
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        
        if (source == manufacturaView.getTxtMogCapturada()) {
            handleMogCapturada();
        } else if (source == manufacturaView.getBtnSiguiente()) {
            handleSiguienteButton();
        } else if (source == manufacturaView.getBtnRegresar()) {
            navegador.regresar(manufacturaView);
        }
    }
    
    private void handleMogCapturada() {
        String ordenIngresada = manufacturaView.getTxtMogCapturada().getText().trim();
        if (ordenIngresada.isEmpty()) {
            MostrarMensaje.mostrarError("Ingrese una orden de manufactura");
            return;
        }

        try {
            if (manufacturaModel.obtenerDatosOrden(ordenIngresada)) {
                llenarCamposMOG();
            } else {
                limpiarCamposMOG();
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al obtener datos de la orden", ex);
        }
    }
    
    private void handleCodigoSupervisorCapturado() {
        String codigoIngresado = new String(manufacturaView.getTxtCodigoSupervisor().getPassword()).trim();

        if (codigoIngresado.isEmpty()) {
            MostrarMensaje.mostrarError("Ingrese un código de supervisor");
            return;
        }

        try {
            if (manufacturaModel.validarSupervisor(codigoIngresado)) {
                manufacturaView.getTxtSupervisorAsignado().setText(LineaProduccion.getInstance().getSupervisor());
                ValidarCampos.activarCampo(manufacturaView.getTxtMogCapturada());
            } else {
                LimpiarCampos.limpiarCampo(manufacturaView.getTxtCodigoSupervisor());
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al validar el código del supervisor", ex);
        }
    }
    
    private void handleSiguienteButton() {
        if (areFieldsEmpty()) {
            MostrarMensaje.mostrarError("Por favor, complete todos los campos antes de continuar");
            return;
        }
        navegador.avanzar(registroRBPView, manufacturaView);
    }
    
    private boolean areFieldsEmpty() {
        return new String(manufacturaView.getTxtCodigoSupervisor().getPassword()).trim().isEmpty()
                || manufacturaView.getTxtMogCapturada().getText().trim().isEmpty()
                || manufacturaView.getTxtSupervisorAsignado().getText().trim().isEmpty()
                || manufacturaView.getTxtMog().getText().trim().isEmpty()
                || manufacturaView.getTxtModelo().getText().trim().isEmpty()
                || manufacturaView.getTxtCantidadPlaneada().getText().trim().isEmpty()
                || manufacturaView.getTxtDibujo().getText().trim().isEmpty()
                || manufacturaView.getTxtParte().getText().trim().isEmpty()
                || manufacturaView.getTxtProceso().getText().trim().isEmpty();
    }

    private void llenarCamposMOG() {
        MOG datosMOG = MOG.getInstance();
        LineaProduccion datosLinea = LineaProduccion.getInstance();

        manufacturaView.getTxtMog().setText(datosMOG.getMog());
        manufacturaView.getTxtModelo().setText(datosMOG.getModelo());
        manufacturaView.getTxtDibujo().setText(datosMOG.getNo_dibujo());
        manufacturaView.getTxtCantidadPlaneada().setText(String.valueOf(datosMOG.getCantidad_planeada()));
        manufacturaView.getTxtParte().setText(datosMOG.getNo_parte());
        manufacturaView.getTxtProceso().setText(datosLinea.getProceso());
    }

    private void limpiarCamposMOG() {
        LimpiarCampos.limpiarCampos(manufacturaView.getTxtMogCapturada(),
                manufacturaView.getTxtMog(),
                manufacturaView.getTxtModelo(),
                manufacturaView.getTxtCantidadPlaneada(),
                manufacturaView.getTxtDibujo(),
                manufacturaView.getTxtParte(),
                manufacturaView.getTxtProceso());
    }
}