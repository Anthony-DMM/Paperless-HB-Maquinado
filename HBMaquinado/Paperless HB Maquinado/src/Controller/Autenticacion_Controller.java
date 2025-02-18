/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.Autenticacion_Model;
import Model.DBConexion;
import View.Autenticacion;
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

public class Autenticacion_Controller implements ActionListener {
    Autenticacion autenticacion;
    Autenticacion_Model autenticacion_model;
    Opciones opciones;

    public Autenticacion_Controller(Autenticacion autenticacion, Autenticacion_Model autenticacion_model, Opciones opciones, DBConexion conexion) {
        this.autenticacion = autenticacion;
        this.autenticacion_model = autenticacion_model;
        this.opciones = opciones;

        autenticacion.getBtn_ingresar().addActionListener(this);
        autenticacion.getBtn_salir().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == autenticacion.getBtn_ingresar()) {
            String codigoSupervisor = autenticacion.getTxt_codigo_supervisor().getText();
            if (codigoSupervisor.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Debes ingresar el código de supervisor de área");
            } else {
                String areaSupervisor = autenticacion_model.validar_supervisor(codigoSupervisor);
                if (areaSupervisor == null) {
                    JOptionPane.showMessageDialog(null, "Datos incorrectos, favor de verificar");
                } else {
                    JOptionPane.showMessageDialog(null, "Acceso concedido");
                    opciones.setVisible(true);
                    autenticacion.setVisible(false);
                }
                autenticacion.getTxt_codigo_supervisor().setText(null);
            }
        } else if (e.getSource() == autenticacion.getBtn_salir()) {
            System.exit(0);
        }
    }
}