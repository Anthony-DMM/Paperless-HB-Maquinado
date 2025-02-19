/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.Captura_Linea_Model;
import View.Captura_Linea;
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
public class Captura_Linea_Controller implements ActionListener, KeyListener{
    String proceso = "MAQUINADO";
    Captura_Linea_Model captura_Linea_Model;
    Captura_Linea captura_Linea;

    public Captura_Linea_Controller(Captura_Linea_Model captura_Linea_Model, Captura_Linea captura_Linea) {
        this.captura_Linea_Model = captura_Linea_Model;
        this.captura_Linea = captura_Linea;
        
        captura_Linea.getTxt_linea_produccion().addActionListener(this);
        captura_Linea.getTxt_linea_produccion().addKeyListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().getClass().toString().equals("class javax.swing.JTextField")) {
            JTextField text_field = (JTextField) e.getSource();
            if (text_field.equals(captura_Linea.txt_linea_produccion)) {
                String lineaProduccionIngresada = captura_Linea.txt_linea_produccion.getText();
                String supervisorAsignado = captura_Linea_Model.validarLinea(lineaProduccionIngresada, proceso);
                if (lineaProduccionIngresada.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Ingresa la línea de producción");
                } else {
                    if (supervisorAsignado == null) {
                        JOptionPane.showMessageDialog(null, "No se ha encontrado la linea de producción");
                    } else {
                        captura_Linea.getTxt_supervisor_asignado().setText(supervisorAsignado);
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
