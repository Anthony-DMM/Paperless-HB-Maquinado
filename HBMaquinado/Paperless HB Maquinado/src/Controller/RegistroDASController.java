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
    private static final String INVALID_SUPERVISOR_MESSAGE = "Ingrese un código de supervisor";

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

    /*private void handleTextFieldAction(JTextField textField) {
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
    }*/

    private void handleCodigoSoporteCapturado(JPasswordField passwordField) {
        char[] passwordChars = passwordField.getPassword();
        String codigoIngresado = new String(passwordChars);

        if (codigoIngresado.isEmpty()) {
            JOptionPane.showMessageDialog(null, INVALID_SUPERVISOR_MESSAGE);
        } else {
            try {
                if (registroDASModel.validarSoporteRapido(codigoIngresado)) {
                    DAS datosLinea = DAS.getInstance();
                    System.out.println(datosLinea.getSoporteRapido());
                    registroDASView.txtNombreSoporteRapido.setText(datosLinea.getSoporteRapido());
                } else {
                    LimpiarCampos.limpiarCampo(registroDASView.getTxtCodigoSoporte());
                }
            } catch (Exception ex) {
                Logger.getLogger(RegistroDASController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /*private void handleSiguienteButton() {
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
    }*/
}