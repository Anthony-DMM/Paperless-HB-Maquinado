/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Interfaces.LineaProduccion;
import Model.Validar_Linea_Model;
import Model.DBConexion;
import View.Validar_Linea;
import View.Captura_Orden_Manufactura;
import View.Opciones;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 *
 * @author ANTHONY-MARTINEZ
 */

public class Validar_Linea_Controller implements ActionListener {
    Validar_Linea autenticacion;
    Validar_Linea_Model autenticacion_model;
    Captura_Orden_Manufactura capturaLinea;

    public Validar_Linea_Controller(Validar_Linea autenticacion, Validar_Linea_Model autenticacion_model, Captura_Orden_Manufactura captura_Linea, DBConexion conexion) {
        this.autenticacion = autenticacion;
        this.autenticacion_model = autenticacion_model;
        this.capturaLinea = captura_Linea;

        autenticacion.getBtn_ingresar().addActionListener(this);
        autenticacion.getBtn_salir().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == autenticacion.getBtn_ingresar()) {
            String lineaProduccion = autenticacion.getTxt_linea_produccion().getText();
            if (lineaProduccion.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Debe ingresar la línea de producción");
            } else {
                LineaProduccion linea = autenticacion_model.validarLinea(lineaProduccion, "MAQUINADO");
                if (linea == null) {
                    JOptionPane.showMessageDialog(null, "La línea de producción no existe o no pertenece al área de MAQUINADO.");
                } else {
                    capturaLinea.setVisible(true);
                    autenticacion.setVisible(false);
                }
                autenticacion.getTxt_linea_produccion().setText(null);
            }
        } else if (e.getSource() == autenticacion.getBtn_salir()) {
            System.exit(0);
        }
    }
}