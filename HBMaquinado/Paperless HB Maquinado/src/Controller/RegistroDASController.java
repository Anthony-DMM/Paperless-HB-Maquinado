/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Entities.DAS;
import Entities.LineaProduccion;
import Entities.MOG;
import Model.RegistroDASModel;
import Utils.FechaHora;
import Utils.LimpiarCampos;
import View.RegistroDASView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
    private RegistroDASModel registroDASModel;
    private RegistroDASView registroDASView;
    private FechaHora fechaHora = new FechaHora();
    
    private static final String EMPTY_FIELD_MESSAGE = "Por favor, complete todos los campos antes de continuar";
    private static final String INVALID_SOPORTE_RAPIDO_MESSAGE = "Ingrese un código de supervisor válido";

    public RegistroDASController(RegistroDASModel registroDASModel, RegistroDASView registroDASView) {
        this.registroDASModel = registroDASModel;
        this.registroDASView = registroDASView;
        
        registroDASView.txtCodigoSoporte.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleCodigoSupervisorCapturado((JPasswordField) e.getSource());
                }
            }
        });
        
        
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
        if (textField.equals(registroDASView.txtMOG.getText())) {
            //handleMogCapturada();
        }
    }

    private void handleButtonAction(JButton button) {
        /*if (button.equals(capturaOrdenManufacturaView.getBtn_siguiente())) {
            //handleSiguienteButton();
        } else if (button.equals(capturaOrdenManufacturaView.getBtn_regresar())) {
            //handleRegresarButton();
        }*/
    }
    
    private void handleCodigoSupervisorCapturado(JPasswordField passwordField) {
        char[] passwordChars = passwordField.getPassword();
        String codigoSoporteRapidoIngresado = new String(passwordChars);

        if (codigoSoporteRapidoIngresado.isEmpty()) {
            JOptionPane.showMessageDialog(null, INVALID_SOPORTE_RAPIDO_MESSAGE);
        } else {
            try {
                if (registroDASModel.validarSoporteRapido(codigoSoporteRapidoIngresado)) {
                    DAS datosDAS = DAS.getInstance();
                    registroDASView.getTxtCodigoSoporte().setText(datosDAS.getSoporteRapido());
                } else {
                    LimpiarCampos.limpiarCampo(registroDASView.getTxtCodigoSoporte());
                }
            } catch (Exception ex) {
                Logger.getLogger(CapturaOrdenManufacturaController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}