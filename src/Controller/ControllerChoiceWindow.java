/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.FirstWindow;
import View.ChoiceWindow;
import View.First_windowRBP;
import View.Login;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

/**
 *
 * @author DMM-ADMIN
 */
public class ControllerChoiceWindow implements ActionListener {
    //Class
    ControllerLogin controller_login;
   
    //View
    
    ChoiceWindow choice_window;
    First_windowRBP first_window;
    Login login;
    
    //Modelo
    FirstWindow first_window_model;
    //Variables
    int b;

    public ControllerChoiceWindow(ControllerLogin controller_login, ChoiceWindow choice_window, First_windowRBP first_window, Login login, FirstWindow first_window_model) {
        this.controller_login = controller_login;
        this.choice_window = choice_window;
        this.first_window = first_window;
        this.login = login;
        this.first_window_model = first_window_model;
        
        escuchadores();
    }
    
    
    
    public void escuchadores(){
        
        choice_window.btn_getout_cw.addActionListener(this);
        choice_window.btn_register_cw.addActionListener(this);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().getClass().toString().equals("class javax.swing.JButton")) {
            JButton bt = (JButton) e.getSource();
            if (bt.equals(choice_window.btn_register_cw)) {
      
                
                first_window.setVisible(true);
                first_window.linenumber_fw.requestFocus();
                choice_window.setVisible(false);
            }

            if (bt.equals(choice_window.btn_getout_cw)) {
                choice_window.setVisible(false);
                login.setVisible(true);
                
                
            }
        }
    }

}
