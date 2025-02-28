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
public class RegistroDASController implements ActionListener {
    private static final String EMPTY_FIELD_MESSAGE = "Por favor, complete todos los campos antes de continuar";
    private static final String INVALID_MOG_MESSAGE = "Ingrese una orden de manufactura";
    private static final String INVALID_SOPORTE_RAPIDO_MESSAGE = "Ingrese un código de soporte rápido";
    private static final String INVALID_INSPECTOR_MESSAGE = "Ingrese un código de inspector";

    private RegistroDASModel registroDASModel;
    private RegistroDASView registroDASView;
    private FechaHora fechaHora = new FechaHora();

    public RegistroDASController(RegistroDASModel registroDASModel, RegistroDASView registroDASView) {
        this.registroDASModel = registroDASModel;
        this.registroDASView = RegistroDASView.getInstance();

        addListeners();
        
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

    private void handleCodigoSoporteCapturado(JPasswordField passwordField) {
        char[] passwordChars = passwordField.getPassword();
        String codigoSoporteIngresado = new String(passwordChars);

        if (codigoSoporteIngresado.isEmpty()) {
            JOptionPane.showMessageDialog(null, INVALID_SOPORTE_RAPIDO_MESSAGE);
        } else {
            try {
                if (registroDASModel.validarSoporteRapido(codigoSoporteIngresado)) {
                    DAS datosLinea = DAS.getInstance();
                    registroDASView.txtNombreSoporteRapido.setText(datosLinea.getSoporteRapido());
                } else {
                    LimpiarCampos.limpiarCampo(registroDASView.getTxtCodigoSoporte());
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
        } else {
            try {
                if (registroDASModel.validarInspector(codigoInspectorIngresado)) {
                    DAS datosLinea = DAS.getInstance();
                    registroDASView.txtNombreInspector.setText(datosLinea.getInspector());
                } else {
                    LimpiarCampos.limpiarCampo(registroDASView.getTxtCodigoInspector());
                }
            } catch (Exception ex) {
                Logger.getLogger(RegistroDASController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}