/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Entities.LineaProduccion;
import Entities.MOG;
import Model.CapturaOrdenManufacturaModel;
import Utils.MostrarMensaje;
import Utils.Navegador;
import Utils.ValidarCampos;
import View.CapturaOrdenManufacturaView;
import View.OpcionesView;
import View.RegistroRBPView;
import View.ValidarLineaView;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JButton;
import javax.swing.JPasswordField;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class CapturaOrdenManufacturaController implements ActionListener, KeyListener{
    CapturaOrdenManufacturaModel capturaOrdenManufacturaModel;
    CapturaOrdenManufacturaView capturaOrdenManufacturaView;
    ValidarLineaView validarLineaView;
    OpcionesView opcionesView;

    public CapturaOrdenManufacturaController(CapturaOrdenManufacturaModel capturaOrdenManufacturaModel, CapturaOrdenManufacturaView capturaOrdenManufacturaView) {
        this.capturaOrdenManufacturaModel = capturaOrdenManufacturaModel;
        this.capturaOrdenManufacturaView = CapturaOrdenManufacturaView.getInstance();
        this.validarLineaView = ValidarLineaView.getInstance();
        this.opcionesView = OpcionesView.getInstance();
        
        capturaOrdenManufacturaView.getTxt_codigo_supervisor().addActionListener(this);
        capturaOrdenManufacturaView.getTxt_codigo_supervisor().addKeyListener(this);
        capturaOrdenManufacturaView.getTxt_mog_capturada().addKeyListener(this);
        capturaOrdenManufacturaView.getTxt_mog_capturada().addActionListener(this);
        capturaOrdenManufacturaView.getTxt_mog_capturada().addKeyListener(this);
        capturaOrdenManufacturaView.getBtn_siguiente().addActionListener(this);
        capturaOrdenManufacturaView.getBtn_regresar().addActionListener(this);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source instanceof JTextField) {
            manejarEventoTextField((JTextField) source);
        } else if (source instanceof JPasswordField) {
            manejarEventoPasswordField((JPasswordField) source);
        } else if (source instanceof JButton) {
            manejarEventoBoton((JButton) source);
        }
    }
    
    private void manejarEventoTextField(JTextField textField) {
        if (textField.equals(capturaOrdenManufacturaView.getTxt_mog_capturada())) {
            String ordenIngresada = textField.getText().trim();
            if (ValidarCampos.esCampoVacio(textField, "Debe ingresar una orden de manufactura")) {
                return;
            }
            procesarOrdenManufactura(ordenIngresada);
        }
    }

    private void manejarEventoPasswordField(JPasswordField passwordField) {
        if (passwordField.equals(capturaOrdenManufacturaView.getTxt_codigo_supervisor())) {
            String codigoIngresado = new String(passwordField.getPassword()).trim();
            if (ValidarCampos.esCampoVacio(passwordField, "Debe ingresar un código de supervisor")) {
                return;
            }
            procesarCodigoSupervisor(codigoIngresado);
        }
    }

    private void manejarEventoBoton(JButton boton) {
        if (boton.equals(capturaOrdenManufacturaView.getBtn_siguiente())) {
            if (validarCampos()) {
                Navegador.avanzarSiguienteVentana(capturaOrdenManufacturaView, opcionesView);
            }
        } else if (boton.equals(capturaOrdenManufacturaView.getBtn_regresar())) {
            Navegador.regresarVentanaAnterior(capturaOrdenManufacturaView, validarLineaView);
        }
    }
    
    private void procesarOrdenManufactura(String ordenIngresada) {
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
            }
        } catch (SQLException ex) {
            Logger.getLogger(CapturaOrdenManufacturaController.class.getName()).log(Level.SEVERE, null, ex);
            MostrarMensaje.mostrarError("Error al obtener los datos de la orden de manufactura.");
        }
    }

    private void procesarCodigoSupervisor(String codigoIngresado) {
        try {
            if (capturaOrdenManufacturaModel.validarSupervisor(codigoIngresado)) {
                LineaProduccion datosLinea = LineaProduccion.getInstance();
                capturaOrdenManufacturaView.getTxt_supervisor_asignado().setText(datosLinea.getSupervisor());
            }
        } catch (Exception ex) {
            Logger.getLogger(CapturaOrdenManufacturaController.class.getName()).log(Level.SEVERE, null, ex);
            MostrarMensaje.mostrarError("Error al validar el código del supervisor.");
        }
    }

    private boolean validarCampos() {
        if (ValidarCampos.esCampoVacio(capturaOrdenManufacturaView.getTxt_codigo_supervisor(), "Código de supervisor vacío") ||
            ValidarCampos.esCampoVacio(capturaOrdenManufacturaView.getTxt_mog_capturada(), "Orden de manufactura vacía") ||
            ValidarCampos.esCampoVacio(capturaOrdenManufacturaView.getTxt_supervisor_asignado(), "Supervisor no asignado") ||
            ValidarCampos.esCampoVacio(capturaOrdenManufacturaView.getTxt_mog(), "MOG vacío") ||
            ValidarCampos.esCampoVacio(capturaOrdenManufacturaView.getTxt_descripcion(), "Modelo vacío") ||
            ValidarCampos.esCampoVacio(capturaOrdenManufacturaView.getTxt_cantidad_planeada(), "Cantidad planeada vacía") ||
            ValidarCampos.esCampoVacio(capturaOrdenManufacturaView.getTxt_dibujo(), "Dibujo vacío") ||
            ValidarCampos.esCampoVacio(capturaOrdenManufacturaView.getTxt_parte(), "Parte vacía") ||
            ValidarCampos.esCampoVacio(capturaOrdenManufacturaView.getTxt_proceso(), "Proceso vacío")) {
            return false;
        }
        return true;
    }

    /*public void actionPerformed(ActionEvent e) {
        if (e.getSource().getClass().toString().equals("class javax.swing.JTextField")) {
            JTextField text_field = (JTextField) e.getSource();
            if (text_field.equals(capturaOrdenManufactura.txt_mog_capturada)) {
                String ordenIngresada = capturaOrdenManufactura.txt_mog_capturada.getText();
                if (ordenIngresada.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Debe ingresar una orden de manufactura");
                } else {
                    try {
                        if (captura_Linea_Model.obtenerDatosOrden(ordenIngresada)) {
                            MOG datosMOG = MOG.getInstance();
                            LineaProduccion datosLinea = LineaProduccion.getInstance();

                            capturaOrdenManufactura.txt_mog.setText(datosMOG.getMog());
                            capturaOrdenManufactura.txt_modelo.setText(datosMOG.getModelo());
                            capturaOrdenManufactura.txt_dibujo.setText(datosMOG.getNo_dibujo());
                            capturaOrdenManufactura.txt_cantidad_planeada.setText(Integer.toString(datosMOG.getCantidad_planeada()));
                            capturaOrdenManufactura.txt_parte.setText(datosMOG.getNo_parte());
                            capturaOrdenManufactura.txt_proceso.setText(datosLinea.getProceso());
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(CapturaOrdenManufacturaController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        } else if (e.getSource().getClass().toString().equals("class javax.swing.JPasswordField")) {
            JPasswordField passwordField = (JPasswordField) e.getSource();
            if (passwordField.equals(capturaOrdenManufactura.txt_codigo_supervisor)) {
                String codigoIngresado = capturaOrdenManufactura.txt_codigo_supervisor.getText();
                if (codigoIngresado.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Debe ingresar un código de supervisor");
                } else {
                    try {
                        if (captura_Linea_Model.validarSupervisor(codigoIngresado)) {
                            LineaProduccion datosLinea = LineaProduccion.getInstance();
                            capturaOrdenManufactura.txt_supervisor_asignado.setText(datosLinea.getSupervisor());
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(CapturaOrdenManufacturaController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        } else if (e.getSource().getClass().toString().equals("class javax.swing.JButton")) {
            JButton button = (JButton) e.getSource();
            if (button.equals(capturaOrdenManufactura.btn_siguiente)){
                if (capturaOrdenManufactura.txt_codigo_supervisor.getText().isEmpty() || capturaOrdenManufactura.txt_mog_capturada.getText().isEmpty()
                        || capturaOrdenManufactura.txt_supervisor_asignado.getText().isEmpty() || capturaOrdenManufactura.txt_mog.getText().isEmpty()
                        || capturaOrdenManufactura.txt_modelo.getText().isEmpty() || capturaOrdenManufactura.txt_cantidad_planeada.getText().isEmpty()
                        || capturaOrdenManufactura.txt_dibujo.getText().isEmpty() || capturaOrdenManufactura.txt_parte.getText().isEmpty()
                        || capturaOrdenManufactura.txt_proceso.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Campos vacíos, favor de completar los datos");
                } else {
                    capturaOrdenManufactura.setVisible(false);
                    opciones.setVisible(true);
                }
            } else if (button.equals(capturaOrdenManufactura.btn_regresar)) {
                validarLinea.setVisible(true);
                capturaOrdenManufactura.setVisible(false);
            }
        }
    }*/

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}