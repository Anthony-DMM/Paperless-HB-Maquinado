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
        capturaOrdenManufacturaView.getTxt_mog_capturada().addActionListener(this);
        capturaOrdenManufacturaView.getBtn_siguiente().addActionListener(this);
        capturaOrdenManufacturaView.getBtn_regresar().addActionListener(this);

        capturaOrdenManufacturaView.getTxt_codigo_supervisor().addKeyListener(new KeyAdapter() {
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
        if (textField.equals(capturaOrdenManufacturaView.getTxt_mog_capturada())) {
            handleMogCapturada();
        }
    }

    private void handleButtonAction(JButton button) {
        if (button.equals(capturaOrdenManufacturaView.getBtn_siguiente())) {
            handleSiguienteButton();
        } else if (button.equals(capturaOrdenManufacturaView.getBtn_regresar())) {
            handleRegresarButton();
        }
    }

    private void handleMogCapturada() {
        String ordenIngresada = capturaOrdenManufacturaView.getTxt_mog_capturada().getText();
        if (ordenIngresada.isEmpty()) {
            JOptionPane.showMessageDialog(null, INVALID_MOG_MESSAGE);
        } else {
            try {
                if (capturaOrdenManufacturaModel.obtenerDatosOrden(ordenIngresada)) {
                    MOG datosMOG = MOG.getInstance();
                    LineaProduccion datosLinea = LineaProduccion.getInstance();

                    capturaOrdenManufacturaView.getTxt_mog().setText(datosMOG.getMog());
                    capturaOrdenManufacturaView.getTxt_descripcion().setText(datosMOG.getModelo());
                    capturaOrdenManufacturaView.getTxt_dibujo().setText(datosMOG.getNo_dibujo());
                    capturaOrdenManufacturaView.getTxt_cantidad_planeada().setText(Integer.toString(datosMOG.getCantidad_planeada()));
                    capturaOrdenManufacturaView.getTxt_parte().setText(datosMOG.getNo_parte());
                    capturaOrdenManufacturaView.getTxt_proceso().setText(datosLinea.getProceso());
                } else {
                    LimpiarCampos.limpiarCampo(capturaOrdenManufacturaView.getTxt_mog_capturada());
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
                    capturaOrdenManufacturaView.getTxt_supervisor_asignado().setText(datosLinea.getSupervisor());
                } else {
                    LimpiarCampos.limpiarCampo(capturaOrdenManufacturaView.getTxt_codigo_supervisor());
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
        char[] passwordChars = capturaOrdenManufacturaView.getTxt_codigo_supervisor().getPassword();
        String codigoSupervisor = new String(passwordChars);

        return codigoSupervisor.isEmpty() ||
               capturaOrdenManufacturaView.getTxt_mog_capturada().getText().isEmpty() ||
               capturaOrdenManufacturaView.getTxt_supervisor_asignado().getText().isEmpty() ||
               capturaOrdenManufacturaView.getTxt_mog().getText().isEmpty() ||
               capturaOrdenManufacturaView.getTxt_descripcion().getText().isEmpty() ||
               capturaOrdenManufacturaView.getTxt_cantidad_planeada().getText().isEmpty() ||
               capturaOrdenManufacturaView.getTxt_dibujo().getText().isEmpty() ||
               capturaOrdenManufacturaView.getTxt_parte().getText().isEmpty() ||
               capturaOrdenManufacturaView.getTxt_proceso().getText().isEmpty();
    }
}