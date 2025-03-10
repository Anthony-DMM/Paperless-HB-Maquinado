/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Interfaces.LineaProduccion;
import Interfaces.MOG;
import Model.ManufacturaModel;
import Utils.LimpiarCampos;
import Utils.Navegador;
import Utils.ValidarCampos;
import View.ManufacturaView;
import View.RegistroRBPView;
import java.awt.Color;
import javax.swing.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ManufacturaController implements ActionListener {
    
    private final ManufacturaModel manufacturaModel = new ManufacturaModel();
    private final ManufacturaView manufacturaView;
    
    public ManufacturaController(ManufacturaView manufacturaView) {
        this.manufacturaView = manufacturaView;
        addListeners();
    }
    
    private void addListeners() {
        this.manufacturaView.getTxtMogCapturada().addActionListener(this);
        this.manufacturaView.getBtnSiguiente().addActionListener(this);
        this.manufacturaView.getBtnRegresar().addActionListener(this);
        
        this.manufacturaView.getTxtCodigoSupervisor().addKeyListener(new KeyAdapter() {
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
        if (textField.equals(manufacturaView.getTxtMogCapturada())) {
            handleMogCapturada();
        }
    }
    
    private void handleButtonAction(JButton button) {
        if (button.equals(manufacturaView.getBtnSiguiente())) {
            handleSiguienteButton();
        } else if (button.equals(manufacturaView.getBtnRegresar())) {
            handleRegresarButton();
        }
    }
    
    private void handleMogCapturada() {
        String ordenIngresada = manufacturaView.getTxtMogCapturada().getText();
        if (ordenIngresada.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese una orden de manufactura");
        } else {
            try {
                if (manufacturaModel.obtenerDatosOrden(ordenIngresada)) {
                    MOG datosMOG = MOG.getInstance();
                    LineaProduccion datosLinea = LineaProduccion.getInstance();
                    
                    manufacturaView.txtMog.setText(datosMOG.getMog());
                    manufacturaView.txtModelo.setText(datosMOG.getModelo());
                    manufacturaView.txtDibujo.setText(datosMOG.getNo_dibujo());
                    manufacturaView.txtCantidadPlaneada.setText(Integer.toString(datosMOG.getCantidad_planeada()));
                    manufacturaView.txtParte.setText(datosMOG.getNo_parte());
                    manufacturaView.txtProceso.setText(datosLinea.getProceso());
                } else {
                    LimpiarCampos.limpiarCampo(manufacturaView.getTxtMogCapturada());
                    LimpiarCampos.limpiarCampos(manufacturaView.txtMogCapturada,
                            manufacturaView.txtMog, 
                            manufacturaView.txtModelo, 
                            manufacturaView.txtCantidadPlaneada, 
                            manufacturaView.txtDibujo, 
                            manufacturaView.txtParte, 
                            manufacturaView.txtProceso);
                }
            } catch (SQLException ex) {
                Logger.getLogger(ManufacturaController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void handleCodigoSupervisorCapturado(JPasswordField passwordField) {
        char[] passwordChars = passwordField.getPassword();
        String codigoIngresado = new String(passwordChars);
        
        if (codigoIngresado.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese un código de supervisor");
        } else {
            try {
                if (manufacturaModel.validarSupervisor(codigoIngresado)) {
                    LineaProduccion datosLinea = LineaProduccion.getInstance();
                    manufacturaView.getTxtSupervisorAsignado().setText(datosLinea.getSupervisor());
                    ValidarCampos.activarCampo(manufacturaView.txtMogCapturada);
                } else {
                    LimpiarCampos.limpiarCampo(manufacturaView.getTxtCodigoSupervisor());
                }
            } catch (SQLException ex) {
                Logger.getLogger(ManufacturaController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void handleSiguienteButton() {
        if (areFieldsEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos antes de continuar");
        } else {
            RegistroRBPView registroRBPView = RegistroRBPView.getInstance();
            RegistroRBPController registroRBPController = new RegistroRBPController(registroRBPView);
            Navegador.avanzarSiguienteVentana(registroRBPView);
        }
    }
    
    private void handleRegresarButton() {
        Navegador.regresarVentanaAnterior();
    }
    
    private boolean areFieldsEmpty() {
        char[] passwordChars = manufacturaView.getTxtCodigoSupervisor().getPassword();
        String codigoSupervisor = new String(passwordChars);
        
        return codigoSupervisor.isEmpty()
                || manufacturaView.getTxtMogCapturada().getText().isEmpty()
                || manufacturaView.getTxtSupervisorAsignado().getText().isEmpty()
                || manufacturaView.getTxtMog().getText().isEmpty()
                || manufacturaView.getTxtModelo().getText().isEmpty()
                || manufacturaView.getTxtCantidadPlaneada().getText().isEmpty()
                || manufacturaView.getTxtDibujo().getText().isEmpty()
                || manufacturaView.getTxtParte().getText().isEmpty()
                || manufacturaView.getTxtProceso().getText().isEmpty();
    }
}
