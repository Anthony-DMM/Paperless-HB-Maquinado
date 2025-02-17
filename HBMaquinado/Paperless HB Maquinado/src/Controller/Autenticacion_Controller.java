/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.Autenticacion_Model;
import Model.DBConexion;
import View.Autenticacion;
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
public class Autenticacion_Controller implements ActionListener, KeyListener {
    //Vistas
    Autenticacion autenticacion;
    //First_windowRBP firts_window;
    //ChoiceWindow choice_window;
    //Metodos
    Autenticacion_Model autenticacion_model;
    DBConexion conexion;
    //Metods metods;
    //Variables
    //String lineName;
    String codigo;
    
    
    public Autenticacion_Controller(Autenticacion autenticacion, Autenticacion_Model autenticacion_Model, DBConexion conexion) {
        this.autenticacion = autenticacion;
        //this.firts_window = firts_window;
        //this.choice_window = choice_window;
        this.autenticacion_model = autenticacion_Model;
        this.conexion = conexion;

        autenticacion.txt_codigo_supervisor.addActionListener(this);
        autenticacion.btn_ingresar.addActionListener(this);
        autenticacion.btn_salir.addActionListener(this);
        autenticacion.txt_codigo_supervisor.addKeyListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().getClass().toString().equals("class javax.swing.JButton")) {
            JButton bt = (JButton) e.getSource();
            if (bt.equals(autenticacion.btn_ingresar)) {
                String codigo_supervisor = autenticacion.txt_codigo_supervisor.getText();
                if (codigo_supervisor.equals("") || codigo_supervisor == null) {
                   JOptionPane.showMessageDialog(null, "Debes ingresar el código de supervisor de área");
                } else {
                    codigo = autenticacion_model.validar_supervisor(codigo_supervisor, autenticacion, conexion);
                    if (codigo == null) {
                        JOptionPane.showMessageDialog(null, "Datos incorrectos, favor de verificar");
                    } else {
                        JOptionPane.showMessageDialog(null, "Acceso concedido");
                    }
                    //int li = Integer.parseInt(line);
                    /*lineName = login_window.get_line(line, choice_window, login, metods);
                    if (lineName == null) {
                        JOptionPane.showMessageDialog(null, "Datos incorrectos CC");
                    } else {
                        choice_window.jLabelProcess.setText(lineName);
                        firts_window.jLabelProc.setText(lineName);
                        if (lineName.equals("MAQUINADO")) {
                            firts_window.jLabelProcess.setText("TH:");
                        }
                        
                    }*/
                    //metods.setLn(lineName);
                    //choice_window.jLabelProcess.setText(lineName);
                    //firts_window.jLabelProc.setText(lineName);
                }
            }

            if (bt.equals(autenticacion.btn_salir)) {
                System.exit(0);
            }
        }

       
    }

    /*public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
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
