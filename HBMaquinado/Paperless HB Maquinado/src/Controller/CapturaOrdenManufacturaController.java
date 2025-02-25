/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Entities.LineaProduccion;
import Entities.MOG;
import Model.CapturaOrdenManufacturaModel;
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
    CapturaOrdenManufacturaModel captura_Linea_Model;
    CapturaOrdenManufacturaView capturaOrdenManufactura;
    ValidarLineaView validarLinea;
    OpcionesView opciones;

    public CapturaOrdenManufacturaController(CapturaOrdenManufacturaModel captura_Linea_Model, CapturaOrdenManufacturaView captura_Linea) {
        this.captura_Linea_Model = captura_Linea_Model;
        this.capturaOrdenManufactura = CapturaOrdenManufacturaView.getInstance();
        this.validarLinea = ValidarLineaView.getInstance();
        this.opciones = OpcionesView.getInstance();
        
        capturaOrdenManufactura.getTxt_codigo_supervisor().addActionListener(this);
        capturaOrdenManufactura.getTxt_codigo_supervisor().addKeyListener(this);
        capturaOrdenManufactura.getTxt_mog_capturada().addKeyListener(this);
        capturaOrdenManufactura.getTxt_mog_capturada().addActionListener(this);
        capturaOrdenManufactura.getTxt_mog_capturada().addKeyListener(this);
        capturaOrdenManufactura.getBtn_siguiente().addActionListener(this);
        capturaOrdenManufactura.getBtn_regresar().addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
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
    }

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
