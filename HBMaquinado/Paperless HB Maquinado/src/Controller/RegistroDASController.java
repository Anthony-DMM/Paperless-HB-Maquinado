/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Entities.DAS;
import Entities.LineaProduccion;
import Entities.MOG;
import Model.CapturaOrdenManufacturaModel;
import Model.RegistroDASModel;
import Utils.FechaHora;
import Utils.LimpiarCampos;
import Utils.Navegador;
import View.CapturaOrdenManufacturaView;
import View.OpcionesView;
import View.RegistroDASView;
import View.ValidarLineaView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class RegistroDASController implements ActionListener, ItemListener {
    private static final String EMPTY_FIELD_MESSAGE = "Por favor, complete todos los campos antes de continuar";
    private static final String INVALID_MOG_MESSAGE = "Ingrese una orden de manufactura";
    private static final String INVALID_SOPORTE_RAPIDO_MESSAGE = "Ingrese un código de soporte rápido";
    private static final String INVALID_INSPECTOR_MESSAGE = "Ingrese un código de inspector";
    private static final String INVALID_OPERADOR_MESSAGE = "Ingrese un número de empleado";

    private RegistroDASModel registroDASModel;
    private RegistroDASView registroDASView;
    private FechaHora fechaHora = new FechaHora();
    DAS datosLinea = DAS.getInstance();

    public RegistroDASController(RegistroDASModel registroDASModel, RegistroDASView registroDASView) {
        this.registroDASModel = registroDASModel;
        this.registroDASView = RegistroDASView.getInstance();

        addListeners();
        registroDASView.cbxOK.addItemListener(this);
        registroDASView.cbxNG.addItemListener(this);
        
        registroDASView.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                cargarDatos();
            }
        });
    }
    
    private void cargarDatos() {
        try {
            MOG datosMOG = MOG.getInstance();
            
            String fechaString = fechaHora.fechaActual("dd-MM-yyyy");
            Date fechaDate = fechaHora.stringToDate(fechaString, "dd-MM-yyyy");
            registroDASView.jdcFecha.setDate(fechaDate);
            registroDASView.txtMOG.setText(datosMOG.getMog());
            registroDASView.txtModelo.setText(datosMOG.getModelo());
            registroDASView.txtSTD.setText(datosMOG.getStd());

            String hora = fechaHora.horaActual();
            registroDASView.txtHora.setText(hora);
        } catch (SQLException | ParseException ex) {
            System.out.println("Error al cargar los datos: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void addListeners() {
        registroDASView.getTxtCodigoSoporte().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleCodigoSoporteCapturado((JPasswordField) e.getSource());
                }
            }
        });
        registroDASView.getTxtCodigoInspector().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleCodigoInspectorCapturado((JPasswordField) e.getSource());
                }
            }
        });
        registroDASView.getTxtNumeroEmpleado().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleNumeroEmpleadoCapturado((JPasswordField) e.getSource());
                }
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source instanceof JTextField) {
            //handleTextFieldAction((JTextField) source);
        } else if (source instanceof JButton) {
            //handleButtonAction((JButton) source);
        }
    }
    
    @Override
    public void itemStateChanged(ItemEvent e) {
       if (e.getSource() == registroDASView.cbxNG){
           if (e.getStateChange() == ItemEvent.SELECTED){
               registroDASView.cbxOK.setEnabled(false);
            }else{
               registroDASView.cbxOK.setEnabled(true); 
           } 
       }else if(e.getSource() == registroDASView.cbxOK){
            if (e.getStateChange() == ItemEvent.SELECTED){
               registroDASView.cbxNG.setEnabled(false);
            }else{
               registroDASView.cbxNG.setEnabled(true); 
           }
       }
    }

    private void handleCodigoSoporteCapturado(JPasswordField passwordField) {
        char[] passwordChars = passwordField.getPassword();
        String codigoSoporteIngresado = new String(passwordChars);

        if (codigoSoporteIngresado.isEmpty()) {
            JOptionPane.showMessageDialog(null, INVALID_SOPORTE_RAPIDO_MESSAGE);
            LimpiarCampos.limpiarCampos(registroDASView.getTxtCodigoSoporte(), registroDASView.getTxtNombreSoporteRapido());
        } else {
            try {
                if (registroDASModel.validarSoporteRapido(codigoSoporteIngresado)) {
                    registroDASView.txtNombreSoporteRapido.setText(datosLinea.getSoporteRapido());
                } else {
                    LimpiarCampos.limpiarCampos(registroDASView.getTxtCodigoSoporte(), registroDASView.getTxtNombreSoporteRapido());
                }
            } catch (Exception ex) {
                Logger.getLogger(RegistroDASController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void handleCodigoInspectorCapturado(JPasswordField passwordField) {
        char[] passwordChars = passwordField.getPassword();
        String codigoInspectorIngresado = new String(passwordChars);

        if (codigoInspectorIngresado.isEmpty()) {
            JOptionPane.showMessageDialog(null, INVALID_INSPECTOR_MESSAGE);
            LimpiarCampos.limpiarCampos(registroDASView.getTxtCodigoInspector(), registroDASView.getTxtNombreInspector());
        } else {
            try {
                if (registroDASModel.validarInspector(codigoInspectorIngresado)) {
                    registroDASView.txtNombreInspector.setText(datosLinea.getInspector());
                } else {
                    LimpiarCampos.limpiarCampos(registroDASView.getTxtCodigoInspector(), registroDASView.getTxtNombreInspector());
                }
            } catch (Exception ex) {
                Logger.getLogger(RegistroDASController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void handleNumeroEmpleadoCapturado(JPasswordField passwordField) {
        char[] passwordChars = passwordField.getPassword();
        String numeroEmpleadoIngresado = new String(passwordChars);

        if (numeroEmpleadoIngresado.isEmpty()) {
            JOptionPane.showMessageDialog(null, INVALID_OPERADOR_MESSAGE);
            LimpiarCampos.limpiarCampos(registroDASView.getTxtNumeroEmpleado(), registroDASView.getTxtNombreEmpleado());
        } else {
            try {
                if (registroDASModel.validarOperador(numeroEmpleadoIngresado)) {
                    registroDASView.txtNombreEmpleado.setText(datosLinea.getEmpleado());
                } else {
                    LimpiarCampos.limpiarCampos(registroDASView.getTxtNumeroEmpleado(), registroDASView.getTxtNombreEmpleado());
                }
            } catch (Exception ex) {
                Logger.getLogger(RegistroDASController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

