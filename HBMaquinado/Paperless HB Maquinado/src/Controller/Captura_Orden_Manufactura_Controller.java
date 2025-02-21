/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Interfaces.LineaProduccion;
import Interfaces.MOG;
import Model.Captura_Orden_Manufactura_Model;
import View.Captura_Orden_Manufactura;
import View.Validar_Linea;
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

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class Captura_Orden_Manufactura_Controller implements ActionListener, KeyListener{
    Captura_Orden_Manufactura_Model captura_Linea_Model;
    Captura_Orden_Manufactura capturaOrdenManufactura;
    Validar_Linea validarLinea;

    public Captura_Orden_Manufactura_Controller(Captura_Orden_Manufactura_Model captura_Linea_Model, Captura_Orden_Manufactura captura_Linea) {
        this.captura_Linea_Model = captura_Linea_Model;
        this.capturaOrdenManufactura = Captura_Orden_Manufactura.getInstance();
        this.validarLinea = Validar_Linea.getInstance();
        
        capturaOrdenManufactura.getTxt_linea_produccion().addActionListener(this);
        capturaOrdenManufactura.getTxt_linea_produccion().addKeyListener(this);
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
                        captura_Linea_Model.obtenerDatosOrden(ordenIngresada);
                        MOG datosMOG = MOG.getInstance();
                        LineaProduccion datosLinea = LineaProduccion.getInstance();

                        capturaOrdenManufactura.txt_mog.setText(datosMOG.getMog());
                        capturaOrdenManufactura.txt_modelo.setText(datosMOG.getModelo());
                        capturaOrdenManufactura.txt_dibujo.setText(datosMOG.getNo_dibujo());
                        capturaOrdenManufactura.txt_cantidad_planeada.setText(Integer.toString(datosMOG.getCantidad_planeada()));
                        capturaOrdenManufactura.txt_parte.setText(datosMOG.getNo_parte());
                        capturaOrdenManufactura.txt_proceso.setText(datosLinea.getProceso());

                    } catch (SQLException ex) {
                        Logger.getLogger(Captura_Orden_Manufactura_Controller.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        else if (e.getSource().getClass().toString().equals("class javax.swing.JButton")) {
            JButton button = (JButton) e.getSource();
            if (button.equals(capturaOrdenManufactura.btn_siguiente)){
                
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
