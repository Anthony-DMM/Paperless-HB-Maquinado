/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Entities.LineaProduccion;
import Model.ValidarLineaModel;
import Model.DBConexion;
import View.ValidarLineaView;
import View.CapturaOrdenManufacturaView;
import View.OpcionesView;
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

public class ValidarLineaController implements ActionListener {
    ValidarLineaView validarLinea;
    ValidarLineaModel autenticacion_model;
    CapturaOrdenManufacturaView capturaLinea;

    public ValidarLineaController(ValidarLineaView validarLinea, ValidarLineaModel autenticacion_model, CapturaOrdenManufacturaView captura_Linea, DBConexion conexion) {
        this.validarLinea = ValidarLineaView.getInstance();
        this.autenticacion_model = autenticacion_model;
        this.capturaLinea = captura_Linea;

        validarLinea.getBtn_ingresar().addActionListener(this);
        validarLinea.getBtn_salir().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == validarLinea.getBtn_ingresar()) {
            String lineaProduccion = validarLinea.getTxt_linea_produccion().getText();
            if (lineaProduccion.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Debe ingresar la línea de producción");
            } else {
                LineaProduccion linea = autenticacion_model.validarLinea(lineaProduccion, "MAQUINADO");
                if (linea == null) {
                    JOptionPane.showMessageDialog(null, "La línea de producción no existe o no pertenece al área de MAQUINADO.");
                } else {
                    capturaLinea.setVisible(true);
                    validarLinea.setVisible(false);
                }
                validarLinea.getTxt_linea_produccion().setText(null);
            }
        } else if (e.getSource() == validarLinea.getBtn_salir()) {
            System.exit(0);
        }
    }
}