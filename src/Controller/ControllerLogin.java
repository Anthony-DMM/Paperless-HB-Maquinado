/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.LoginWindow;
import Model.Metods;
import View.ChoiceWindow;
import View.First_windowRBP;
import View.Login;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

/**
 *
 * @author DMM-ADMIN
 */
public class ControllerLogin implements ActionListener, KeyListener {

    //Vistas
    Login login;
    First_windowRBP firts_window;
    ChoiceWindow choice_window;
    //Metodos
    LoginWindow login_window;
    Metods metods;
    //Variables
    String lineName;
    
    
    public ControllerLogin(Login login, First_windowRBP firts_window, ChoiceWindow choice_window, LoginWindow login_window, Metods metods) {
        this.login = login;
        this.firts_window = firts_window;
        this.choice_window = choice_window;
        this.login_window = login_window;
        this.metods = metods;

        login.txt_user.addActionListener(this);
        login.btn_login.addActionListener(this);
        login.jButtonSalir.addActionListener(this);
        login.txt_user.addKeyListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().getClass().toString().equals("class javax.swing.JButton")) {
            JButton bt = (JButton) e.getSource();
            if (bt.equals(login.btn_login)) {
                String line = login.txt_user.getText();
                if (line.equals("") || line == null) {
                   JOptionPane.showMessageDialog(null, "Debes completar el campo");
                } else {
                    //int li = Integer.parseInt(line);
                    lineName = login_window.get_line(line, choice_window, login, metods);
                    if (lineName == null) {
                        JOptionPane.showMessageDialog(null, "Datos incorrectos CC");
                    } else {
                        choice_window.jLabelProcess.setText(lineName);
                        firts_window.jLabelProc.setText(lineName);
                        if (lineName.equals("MAQUINADO")) {
                            firts_window.jLabelProcess.setText("TH:");
                        }
                        
                    }
                    metods.setLn(lineName);
                    choice_window.jLabelProcess.setText(lineName);
                    firts_window.jLabelProc.setText(lineName);
                }
            }

            if (bt.equals(login.jButtonSalir)) {
                System.exit(0);
            }
        }

       
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
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
