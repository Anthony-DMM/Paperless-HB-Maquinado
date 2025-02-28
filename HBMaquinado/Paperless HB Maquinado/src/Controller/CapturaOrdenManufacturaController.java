/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Entities.LineaProduccion;
import Entities.MOG;
import Model.CapturaOrdenManufacturaModel;
import Utils.LimpiarCampos;
import Utils.Navegador;
import View.CapturaOrdenManufacturaView;
import View.OpcionesView;
import View.ValidarLineaView;
import javax.swing.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CapturaOrdenManufacturaController implements ActionListener {
    private static final String EMPTY_FIELD_MESSAGE = "Por favor, complete todos los campos antes de continuar";
    private static final String INVALID_MOG_MESSAGE = "Ingrese una orden de manufactura";
    private static final String INVALID_SUPERVISOR_MESSAGE = "Ingrese un código de supervisor";

    private CapturaOrdenManufacturaModel capturaOrdenManufacturaModel;
    private CapturaOrdenManufacturaView capturaOrdenManufacturaView;
    private ValidarLineaView validarLineaView;
    private OpcionesView opcionesView;

    public CapturaOrdenManufacturaController(CapturaOrdenManufacturaModel capturaOrdenManufacturaModel, CapturaOrdenManufacturaView capturaOrdenManufacturaView) {
        this.capturaOrdenManufacturaModel = capturaOrdenManufacturaModel;
        this.capturaOrdenManufacturaView = CapturaOrdenManufacturaView.getInstance();
        this.validarLineaView = ValidarLineaView.getInstance();
        this.opcionesView = OpcionesView.getInstance();

        addListeners();
    }

    private void addListeners() {
        capturaOrdenManufacturaView.getTxtMogCapturada().addActionListener(this);
        capturaOrdenManufacturaView.getBtnSiguiente().addActionListener(this);
        capturaOrdenManufacturaView.getBtnRegresar().addActionListener(this);

        capturaOrdenManufacturaView.getTxtCodigoSupervisor().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleCodigoSupervisorCapturado((JPasswordField) e.getSource());
                }
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source instanceof JTextField) {
            handleTextFieldAction((JTextField) source);
        } else if (source instanceof JButton) {
            handleButtonAction((JButton) source);
        }
    }

    private void handleTextFieldAction(JTextField textField) {
        if (textField.equals(capturaOrdenManufacturaView.getTxtMogCapturada())) {
            handleMogCapturada();
        }
    }

    private void handleButtonAction(JButton button) {
        if (button.equals(capturaOrdenManufacturaView.getBtnSiguiente())) {
            handleSiguienteButton();
        } else if (button.equals(capturaOrdenManufacturaView.getBtnRegresar())) {
            handleRegresarButton();
        }
    }

    private void handleMogCapturada() {
        String ordenIngresada = capturaOrdenManufacturaView.getTxtMogCapturada().getText();
        if (ordenIngresada.isEmpty()) {
            JOptionPane.showMessageDialog(null, INVALID_MOG_MESSAGE);
        } else {
            try {
                if (capturaOrdenManufacturaModel.obtenerDatosOrden(ordenIngresada)) {
                    MOG datosMOG = MOG.getInstance();
                    LineaProduccion datosLinea = LineaProduccion.getInstance();

                    capturaOrdenManufacturaView.getTxtMog().setText(datosMOG.getMog());
                    capturaOrdenManufacturaView.getTxtModelo().setText(datosMOG.getModelo());
                    capturaOrdenManufacturaView.getTxtDibujo().setText(datosMOG.getNo_dibujo());
                    capturaOrdenManufacturaView.getTxtCantidadPlaneada().setText(Integer.toString(datosMOG.getCantidad_planeada()));
                    capturaOrdenManufacturaView.getTxtParte().setText(datosMOG.getNo_parte());
                    capturaOrdenManufacturaView.getTxtProceso().setText(datosLinea.getProceso());
                } else {
                    LimpiarCampos.limpiarCampo(capturaOrdenManufacturaView.getTxtMogCapturada());
                }
            } catch (SQLException ex) {
                Logger.getLogger(CapturaOrdenManufacturaController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void handleCodigoSupervisorCapturado(JPasswordField passwordField) {
        char[] passwordChars = passwordField.getPassword();
        String codigoIngresado = new String(passwordChars);

        if (codigoIngresado.isEmpty()) {
            JOptionPane.showMessageDialog(null, INVALID_SUPERVISOR_MESSAGE);
        } else {
            try {
                if (capturaOrdenManufacturaModel.validarSupervisor(codigoIngresado)) {
                    LineaProduccion datosLinea = LineaProduccion.getInstance();
                    capturaOrdenManufacturaView.getTxtSupervisorAsignado().setText(datosLinea.getSupervisor());
                } else {
                    LimpiarCampos.limpiarCampo(capturaOrdenManufacturaView.getTxtCodigoSupervisor());
                }
            } catch (Exception ex) {
                Logger.getLogger(CapturaOrdenManufacturaController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void handleSiguienteButton() {
        if (areFieldsEmpty()) {
            JOptionPane.showMessageDialog(null, EMPTY_FIELD_MESSAGE);
        } else {
            Navegador.avanzarSiguienteVentana(capturaOrdenManufacturaView, opcionesView);
        }
    }

    private void handleRegresarButton() {
        Navegador.regresarVentanaAnterior(capturaOrdenManufacturaView, validarLineaView);
    }

    private boolean areFieldsEmpty() {
        char[] passwordChars = capturaOrdenManufacturaView.getTxtCodigoSupervisor().getPassword();
        String codigoSupervisor = new String(passwordChars);

        return codigoSupervisor.isEmpty() ||
               capturaOrdenManufacturaView.getTxtMogCapturada().getText().isEmpty() ||
               capturaOrdenManufacturaView.getTxtSupervisorAsignado().getText().isEmpty() ||
               capturaOrdenManufacturaView.getTxtMog().getText().isEmpty() ||
               capturaOrdenManufacturaView.getTxtModelo().getText().isEmpty() ||
               capturaOrdenManufacturaView.getTxtCantidadPlaneada().getText().isEmpty() ||
               capturaOrdenManufacturaView.getTxtDibujo().getText().isEmpty() ||
               capturaOrdenManufacturaView.getTxtParte().getText().isEmpty() ||
               capturaOrdenManufacturaView.getTxtProceso().getText().isEmpty();
    }
}