/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Interfaces.LineaProduccion;
import Interfaces.MOG;
import Model.Captura_Orden_Manufactura_Model;
import View.Captura_Orden_Manufactura;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class Captura_Orden_Manufactura_Controller implements ActionListener, KeyListener{
    Captura_Orden_Manufactura_Model captura_Linea_Model;
    Captura_Orden_Manufactura capturaMOG;

    public Captura_Orden_Manufactura_Controller(Captura_Orden_Manufactura_Model captura_Linea_Model, Captura_Orden_Manufactura captura_Linea) {
        this.captura_Linea_Model = captura_Linea_Model;
        this.capturaMOG = captura_Linea;
        
        capturaMOG.getTxt_linea_produccion().addActionListener(this);
        capturaMOG.getTxt_linea_produccion().addKeyListener(this);
        capturaMOG.getTxt_mog_capturada().addActionListener(this);
        capturaMOG.getTxt_mog_capturada().addKeyListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().getClass().toString().equals("class javax.swing.JTextField")) {
            JTextField text_field = (JTextField) e.getSource();
            /*if (text_field.equals(capturaMOG.txt_linea_produccion)) {
                String lineaProduccionIngresada = capturaMOG.txt_linea_produccion.getText();
                String supervisorAsignado = captura_Linea_Model.validarLinea(lineaProduccionIngresada, proceso);
                if (lineaProduccionIngresada.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Debe ingresar una línea de producción");
                } else {
                    if (supervisorAsignado == null) {
                        JOptionPane.showMessageDialog(null, "No se ha encontrado la linea de producción");
                    } else {
                        capturaMOG.getTxt_supervisor_asignado().setText(supervisorAsignado);
                    }
                }
            }*/
            if (text_field.equals(capturaMOG.txt_mog_capturada)) {
                String ordenIngresada = capturaMOG.txt_mog_capturada.getText();
                if (ordenIngresada.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Debe ingresar una orden de manufactura");
                } else {
                    try {
                        captura_Linea_Model.obtenerDatosOrden(ordenIngresada);
                        MOG datosMOG = MOG.getInstance();
                        LineaProduccion datosLinea = LineaProduccion.getInstance();

                        capturaMOG.txt_mog.setText(datosMOG.getMog());
                        capturaMOG.txt_modelo.setText(datosMOG.getModelo());
                        capturaMOG.txt_dibujo.setText(datosMOG.getNo_dibujo());
                        capturaMOG.txt_cantidad_planeada.setText(Integer.toString(datosMOG.getCantidad_planeada()));
                        capturaMOG.txt_parte.setText(datosMOG.getNo_parte());
                        capturaMOG.txt_proceso.setText(datosLinea.getProceso());

                    } catch (SQLException ex) {
                        Logger.getLogger(Captura_Orden_Manufactura_Controller.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
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
